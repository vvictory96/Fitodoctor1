package com.example.asus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IzbranActivity extends AppCompatActivity {

    private List<Flower> FLOWERS; //список растений
    //Адаптер для показа списка
    IzbranActivity.IzbranAdapter adapter;


    private class IzbranHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        //Поля элемента списка
        int idF;//Код растения
        TextView mNameFlowerView; //Поле названия
        ImageView mIcoView; // Картинка
        ImageButton BtnDelMy; //Кнопка удаления из списка

        IzbranHolder(View itemView) {
            super(itemView);

            idF=0;

            mNameFlowerView = (TextView) itemView.findViewById(R.id.textView6);
            mIcoView = (ImageView) itemView.findViewById(R.id.icon);
            BtnDelMy = (ImageButton)itemView.findViewById(R.id.imageButtonDelMy);
            //Установка обработчика нажатий
            itemView.setOnClickListener(this);
            //Установка обработчика нажатия на кнопку удаления
            BtnDelMy.setOnClickListener(this);


        }

        //Нажатие на элемент списка
        @Override
        public void onClick(View v) {
            //Если нажато на кнопке удаления
            if(v==BtnDelMy)
            {
                //Открытие базы на запись
                DatabaseHelper mDBHelper = new DatabaseHelper(IzbranActivity.this);
                SQLiteDatabase mDb;
                mDb = mDBHelper.getWritableDatabase();

                //Выполнение запроса на снятие отметки "моё растение"
                mDb.execSQL("UPDATE FLOWERS SET MY_FLOWER=0 WHERE _ID_FLOWERS="+idF);

                //Закрытие базы
                mDb.close();

                //Вызов обновления данных на активности
                UpdateFitos();

            }else {
                //Если нажато не на кнопке, то
                //Вызов активности растения
                Intent intent = new Intent(IzbranActivity.this, FlowerActivity.class);
                //Передача кода растения в активность
                intent.putExtra(FlowerActivity.KOD_FLOWER, idF);
                //Запуск активности
                startActivity(intent);
            }
        }
    }


    //Адаптер для списка
    private class IzbranAdapter extends RecyclerView.Adapter<IzbranActivity.IzbranHolder>   {
        //Список всех растений
        private List<Flower> mFlowers;

        IzbranAdapter(List<Flower> flrs) {
            mFlowers = flrs;
        }
        @Override
        public IzbranActivity.IzbranHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater
                    .inflate(R.layout.izbran_item, parent, false);

            return new IzbranActivity.IzbranHolder(view);
        }
        @Override
        public void onBindViewHolder(IzbranActivity.IzbranHolder holder, int position) {
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
        setContentView(R.layout.activity_izbran);

        //подключаем toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar Ab = getSupportActionBar();
            if (Ab != null) Ab.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.icons8_home);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }

        //Создание списка
        FLOWERS = new ArrayList<Flower>();

        //создаем адаптер
        adapter = new IzbranActivity.IzbranAdapter(FLOWERS);
        RecyclerView listView = (RecyclerView) findViewById(R.id.my_recycle_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        //Передача адаптера в список.
        listView.setAdapter(adapter);

        //Вызов обновления данных на активности
        UpdateFitos();
    }

    //Процедура обновления данных на активности
     void UpdateFitos()
     {
        //Подключение к базе данных
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        SQLiteDatabase mDb;
        mDb = mDBHelper.getReadableDatabase();

        //отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT _ID_FLOWERS,NAME_FLOWERS,PIC_NAME FROM FLOWERS WHERE MY_FLOWER=1",
                null);
        //Определение номеров полей в запросе
        int clID = cursor.getColumnIndex("_ID_FLOWERS");
        int clNm = cursor.getColumnIndex("NAME_FLOWERS");
        int clPic = cursor.getColumnIndex("PIC_NAME");

        //Очистка списка от прежних значений
         FLOWERS.clear();

        //пробегаем по все растениям
        while (cursor.moveToNext()) {
            //заполняем растение
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

        //Уведомить адаптер об изменении данных
        if(adapter!= null)
            adapter.notifyDataSetChanged();

    }


}
