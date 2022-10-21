package com.example.asus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PoiskActivity extends AppCompatActivity {

    //Массив для хранения данных об отметках симптомов
    int CheckSIMPTOMS[];


    //Элемента списка симптомов
    private class SimptomHolder extends RecyclerView.ViewHolder  {
        int idF; //идентификатор симптома
        TextView mNameFlowerView; //Название симптома
        ImageView mIcoView; //Картинка симптома
        CheckBox ChB; //Чекбокс

        SimptomHolder(View itemView) {
            super(itemView);

            idF=0;

            mNameFlowerView = (TextView)
                    itemView.findViewById(R.id.textView6);
            mIcoView = (ImageView)
                    itemView.findViewById(R.id.icon);
            ChB = (CheckBox)
                    itemView.findViewById(R.id.checksimp);
            //Задание обрботчика нажатия на чекбокс

            ChB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ChB.isChecked()) {
                        CheckSIMPTOMS[idF - 1] = 1;
                    } else {
                        CheckSIMPTOMS[idF - 1] = 0;
                    }
                }
            });

        }
    }


    //Адаптер списка симптомов
    private class SimptomAdapter extends RecyclerView.Adapter<PoiskActivity.SimptomHolder>   {
        private List<Flower> mFlowers; //Список симптомов (данные идентичны данным о растении)
        SimptomAdapter(List<Flower> flrs) {
            mFlowers = flrs;
        }
        @Override
        public PoiskActivity.SimptomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater
                    .inflate(R.layout.poisk_item, parent, false);

            return new PoiskActivity.SimptomHolder(view);
        }
        @Override
        public void onBindViewHolder(PoiskActivity.SimptomHolder holder, int position) {
            //Вывод данных о симптоме в элемент списка
            Flower nmflr = mFlowers.get(position);
            holder.mNameFlowerView.setText(nmflr.NameFl);
            holder.mIcoView.setImageResource(nmflr.KodImg);
            holder.idF=nmflr.ID;

            if(CheckSIMPTOMS[holder.idF - 1]==1)
                holder.ChB.setChecked(true);
            else
            holder.ChB.setChecked(false);
        }

        @Override
        public int getItemCount() {
            return mFlowers.size();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poisk);

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

        //подключение к базе
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        SQLiteDatabase mDb;
        //Открытие базы на чтение
        mDb = mDBHelper.getReadableDatabase();

        List<Flower> SIMPTOMS; //список симптомов
        SIMPTOMS = new ArrayList<Flower>();

        //отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT _ID_DISEASE,SYMPTOMS,PIC_NAME FROM DISEASES ",
                null);
        //Определение номеров столбцов с данными
        int clID = cursor.getColumnIndex("_ID_DISEASE");
        int clNm = cursor.getColumnIndex("SYMPTOMS");
        int clPic = cursor.getColumnIndex("PIC_NAME");

        //пробегаем по все симптомы
        while (cursor.moveToNext()) {
            //считывание данных из запроса
            String NmFlrs = cursor.getString(clNm);
            int PicI = cursor.getInt(clPic);
            int id = cursor.getInt(clID);
            //закидываем симптом в список
            SIMPTOMS.add(new Flower(id, NmFlrs, PicI));
        }
        //Закрытие запроса и базы
        cursor.close();
        mDb.close();


        //создаем адаптер
        PoiskActivity.SimptomAdapter adapter = new PoiskActivity.SimptomAdapter(SIMPTOMS);
        RecyclerView listView = (RecyclerView) findViewById(R.id.my_recycle_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);

        //Создание массива отметок симптомов
        CheckSIMPTOMS = new int[SIMPTOMS.size()];
        //инициализация
        for(int i=0; i<SIMPTOMS.size(); i++)CheckSIMPTOMS[i]=0;

        //Задание обработки кнопки поиска
        Button button1 = (Button) findViewById(R.id.button_diagnos);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Формирование данных об отмеченных симптомах
                String ValP = "";
                int kolUsl = 0; //Счётчик отметок
                for (int i = 0; i < CheckSIMPTOMS.length; i++) {
                    if (CheckSIMPTOMS[i] == 1) {
                        if (!ValP.isEmpty()) ValP += ","; //первой запятой нет
                        ValP += (i + 1);
                        kolUsl++;  //Подсчёт отметок
                    }
                }
                if (kolUsl > 0) {
                    //Если есть отметки, то открыть активность результата поиска
                    Intent intent = new Intent(PoiskActivity.this, TreatmentActivity.class);
                    //Передать данные об отметках
                    intent.putExtra(TreatmentActivity.KOD_SYMPT, ValP);
                    startActivity(intent);
                } else
                    //Если ничего не отмечено, то уведомить об этом пользователя
                    Snackbar.make(v, R.string.Not_USL, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

            }
        });

    }
}
