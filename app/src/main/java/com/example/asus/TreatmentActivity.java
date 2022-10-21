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

public class TreatmentActivity extends AppCompatActivity {


    //Код для передачи данных о выбранных симптомах
    public static String KOD_SYMPT="kod_sympt";


    //Данные о болезни в одном элементе
    private class DeseasHolder extends RecyclerView.ViewHolder  {

        TextView mNameDView; //Название болезни
        ImageView mIcoView; //Картинка
        TextView mSymptView; //Симптом
        TextView mDesrptView; //Описание
        TextView mTreatView; //Лечение

        DeseasHolder(View itemView) {
            super(itemView);
            mNameDView = (TextView)
                    itemView.findViewById(R.id.textView6);
            mIcoView = (ImageView)
                    itemView.findViewById(R.id.icon);
            mSymptView = (TextView)
                    itemView.findViewById(R.id.textViewSympt);
            mDesrptView = (TextView)
                    itemView.findViewById(R.id.textViewDescript);
            mTreatView = (TextView)
                    itemView.findViewById(R.id.textViewTreatment);
        }
    }

    //Создание адаптера
    private class DeseasAdapter extends RecyclerView.Adapter<TreatmentActivity.DeseasHolder>   {
        private List<Disease> mDiseases; //Список
        DeseasAdapter(List<Disease> flrs) {
            mDiseases = flrs;
        }
        @Override
        public TreatmentActivity.DeseasHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater
                    .inflate(R.layout.disease_item, parent, false);

            return new TreatmentActivity.DeseasHolder(view);
        }
        @Override
        public void onBindViewHolder(TreatmentActivity.DeseasHolder holder, int position) {
            //Заполнение элемента списка данными
            Disease nmflr = mDiseases.get(position);
            holder.mNameDView.setText(nmflr.NameD);
            holder.mSymptView.setText(nmflr.SymptD);
            holder.mDesrptView.setText(nmflr.DesrD);
            holder.mTreatView.setText(nmflr.TreatD);
            holder.mIcoView.setImageResource(nmflr.idPict);
        }

        @Override
        public int getItemCount() {
            return mDiseases.size();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);

        //подключаем toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        if (toolbar !=null) {
            setSupportActionBar(toolbar);
            ActionBar Ab=getSupportActionBar();
            if(Ab!=null)Ab.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.icons8_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        //Извлечение переданных данных о выбранных симптомах
        Intent intnt=getIntent();
        String UslSympt="";
        if(intnt!=null) {
            Bundle ExtraB = intnt.getExtras();
            if (ExtraB != null) {
                UslSympt = ExtraB.getString(KOD_SYMPT);
                //Формирование условия поиска по симптомах
                UslSympt=" WHERE _ID_DISEASE IN ("+UslSympt+")";
            }
        }

        //подготовительные действия
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        SQLiteDatabase mDb;
        mDb = mDBHelper.getReadableDatabase();

        List<Disease> DESEASES; //список
        DESEASES = new ArrayList<Disease>();

        //отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT * FROM DISEASES "+UslSympt,
                null);
        //Получение номеров столбцов с данными
        int clID = cursor.getColumnIndex("_ID_DISEASE");
        int clNm = cursor.getColumnIndex("NAME_DISEASE");
        int clPic = cursor.getColumnIndex("PIC_NAME");
        int clSm = cursor.getColumnIndex("SYMPTOMS");
        int clDscr = cursor.getColumnIndex("DESCRIPTION_DISEASE");
        int clTrtm = cursor.getColumnIndex("TREATMENT");


        //пробегаем по все растениям
        while (cursor.moveToNext()) {

            //считывание данных из запроса
            String NmDes = cursor.getString(clNm);
            int PicI = cursor.getInt(clPic);
            int id = cursor.getInt(clID);
            String SymDes = cursor.getString(clSm);
            String DescrDes = cursor.getString(clDscr);
            String TreatDes = cursor.getString(clTrtm);
            //закидываем в список
            DESEASES.add(new Disease(id, PicI, NmDes, SymDes,DescrDes,TreatDes));
        }
        //Закрытие запроса и базы
        cursor.close();
        mDb.close();


        //создаем адаптер
        TreatmentActivity.DeseasAdapter adapter = new TreatmentActivity.DeseasAdapter(DESEASES);
        RecyclerView listView = (RecyclerView)findViewById(R.id.my_recycle_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);


        //Задание обработчика кнопка ОК
        ImageButton button1 = (ImageButton) findViewById(R.id.imageButtonOK);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Вызов активности всех растений
                Intent intent = new Intent(TreatmentActivity.this, CatalogActivity.class);
                startActivity(intent);

            }
        });

    }
}
