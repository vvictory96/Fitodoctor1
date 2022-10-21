package com.example.asus;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CatalogActivity extends AppCompatActivity {


    private class CatalogHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
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
            Intent intent = new  Intent (CatalogActivity.this, FlowerActivity.class);
            //Передача кода растения в активность
            intent.putExtra(FlowerActivity.KOD_FLOWER, idF);
            //Запуск активности
            startActivity(intent);
        }
    }


    //Адаптер для каталога
    private class CatalogAdapter extends RecyclerView.Adapter<CatalogHolder>   {
        //Список всех растений
        private List<Flower> mFlowers;

        CatalogAdapter(List<Flower> flrs) {
            mFlowers = flrs;
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
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

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
                    Intent intent = new Intent(CatalogActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

        }


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

        //пробегаем по все растениям
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
        CatalogAdapter adapter = new CatalogAdapter(FLOWERS);
        RecyclerView listView = (RecyclerView)findViewById(R.id.my_recycle_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        //Передача адаптера в список.
        listView.setAdapter(adapter);
    }
}
