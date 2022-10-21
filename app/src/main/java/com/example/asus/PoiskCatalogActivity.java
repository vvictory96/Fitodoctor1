package com.example.asus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PoiskCatalogActivity extends AppCompatActivity {

    private class CatalogHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Поля элемента списка
        int idF;//Код растения
        TextView mNameFlowerView; //Поле названия
        ImageView mIcoView; // Картинка
		
        CatalogHolder(View itemView) {
            super(itemView);

            idF=0;

            mNameFlowerView = (TextView)
                    itemView.findViewById(R.id.textView6);
            mIcoView = (ImageView)
                    itemView.findViewById(R.id.icon);
            //Установка обработчика нажатий
            itemView.setOnClickListener(this);
        }

        //Нажатие на элемент списка
        @Override
        public void onClick(View v) {
            //Вызов активности растения
            Intent intent = new  Intent (PoiskCatalogActivity.this, FlowerActivity.class);
            //Передача кода растения в активность
            intent.putExtra(FlowerActivity.KOD_FLOWER, idF);
            //Запуск активности
            startActivity(intent);
        }
    }


    //Адаптер для каталога
    private class CatalogAdapter extends RecyclerView.Adapter<CatalogHolder> {
        //Список всех растений
        private List<Flower> mFlowers;
        private List<Flower> mCleanCopyFlower;//вставила

        //конструктор
        CatalogAdapter(List<Flower> flrs) {
            mFlowers = flrs;
            mCleanCopyFlower = mFlowers;
        }
        @Override
        public CatalogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater
                    .inflate(R.layout.adapter_item, parent, false);

            return new CatalogHolder(view);
        }
        @Override
        public void onBindViewHolder(CatalogHolder holder, int position) {
            //Заполнение элемента списка данными
            Flower nmflr = mFlowers.get(position);
            holder.mNameFlowerView.setText(nmflr.NameFl);
            holder.mIcoView.setImageResource(nmflr.KodImg);
            holder.idF=nmflr.ID;
        }

        @Override
        public int getItemCount() {
            return mFlowers.size();
        }


        //в методе filter() мы перебираем все пункты списка и, если какой-то пункт содержит искомый текст
        //то мы его добавляем в новый список mFlower


        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            mFlowers = new ArrayList<>();
             //mFlowers = new List<Flower>();
            if (charText.length() == 0){
                //mCleanCopyFlower у нас содержит неизменную и неотфильтрованную (полную) копию данных списка
                mFlowers.addAll(mCleanCopyFlower);
            } else {
                for (Flower flower : mCleanCopyFlower ) {
                    //мы перебираем все пункты списка и если какой-то пункт содержит искомый текст
                    //то мы его добавляем в новый список mFlower
                    if (flower.NameFl.toLowerCase(Locale.getDefault()).contains(charText.toLowerCase())) {
                        mFlowers.add(flower);
                    }
                }
            }
            //метод notifyDataSetChanget() позволяет обновить список на экране после фильтрации
            notifyDataSetChanged();
        }

    }


    //объявляем toolbar
    private Toolbar toolbar;

    //объявляем поиск searchview
    private SearchView searchView;
	private CatalogAdapter adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_poisk_catalog);


        //подключаем toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar Ab=getSupportActionBar();
            if(Ab!=null)Ab.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.icons8_home);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PoiskCatalogActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

        }

        //подключаем searchview
        searchView = (SearchView)findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
				if(adapter!=null)
                adapter.filter(text);
                return false;
            }
        });


        //подготовительные действия

        //Подключение к базе данных
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        SQLiteDatabase mDb;
        //База нужна для чтения
        mDb = mDBHelper.getReadableDatabase();

        //Массив цветов
        List<Flower> FLOWERS = new ArrayList<Flower>();

        //отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT _ID_FLOWERS,NAME_FLOWERS,PIC_NAME FROM FLOWERS ",
                null);
        //Определение номеров полей в запросе
        int clID = cursor.getColumnIndex("_ID_FLOWERS");
        int clNm = cursor.getColumnIndex("NAME_FLOWERS");
        int clPic = cursor.getColumnIndex("PIC_NAME");

        //пробегаем по всем растениям
        while (cursor.moveToNext()) {
            //считывание данных из запроса
            String NmFlrs = cursor.getString(clNm);
            int PicI = cursor.getInt(clPic);
            int id = cursor.getInt(clID);
            //закидываем растение в список растений
            FLOWERS.add(new Flower(id, NmFlrs, PicI));
        }
        //Закрыть курсор
        cursor.close();
        //Закрыть БД
        mDb.close();

        //создаем адаптер
        adapter = new CatalogAdapter(FLOWERS);
        RecyclerView listView = (RecyclerView)findViewById(R.id.my_recycle_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        //Передача адаптера в список.
        listView.setAdapter(adapter);
    }
}