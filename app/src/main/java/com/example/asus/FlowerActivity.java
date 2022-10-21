package com.example.asus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class FlowerActivity extends AppCompatActivity {

    int IDfl; //Идентификатор растения
    int MyFl; //Признак Моё растение

    //Имя передаваемой переменной
    public static String KOD_FLOWER="kod_flower";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);

        //подключаем toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
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

        //Определение реакции на нажатие кнопки "Симптомы"
        Button button1 = (Button) findViewById(R.id.buttonsimp);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Вызов активности поиска
                Intent intent = new Intent(FlowerActivity.this, PoiskActivity.class);
                startActivity(intent);
            }
        });
        //Определение реакции на нажатие кнопки "сердечко"
        Button button2 = (Button) findViewById(R.id.buttonheart);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int KodStr=R.string.AddToMy;
                //Определение удалить или добавить в мои растения
                if(MyFl==0)MyFl=1;
                else {MyFl=0; KodStr=R.string.DelFromMy;}

                //Подключение к базе данных
                DatabaseHelper mDBHelper = new DatabaseHelper(FlowerActivity.this);
                SQLiteDatabase mDb;
                //Открытие базы на запись
                mDb = mDBHelper.getWritableDatabase();

                //изменение признака "моё растение"
                mDb.execSQL("UPDATE FLOWERS SET MY_FLOWER="+MyFl+
                        " WHERE _ID_FLOWERS="+IDfl);

                //Закрытие базы
                mDb.close();
                //Вывод сообщения пользователю.
                Snackbar.make(v, KodStr, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        IDfl=0;
        //Получение переданного кода растения
        Intent intnt=getIntent();
        //Если код не передан, то нечего и делать
        if(intnt!=null) {
            Bundle ExtraB= intnt.getExtras();
            if(ExtraB!=null)
                IDfl = (int)ExtraB.get(KOD_FLOWER);


            DatabaseHelper mDBHelper = new DatabaseHelper(this);
            SQLiteDatabase mDb;
            mDb = mDBHelper.getReadableDatabase();

            //отправляем запрос в БД
            Cursor cursor = mDb.rawQuery("SELECT * FROM FLOWERS WHERE _ID_FLOWERS="+IDfl, null);
            int clNm = cursor.getColumnIndex("NAME_FLOWERS");
            int clPic = cursor.getColumnIndex("PIC_NAME");
            int clOpis = cursor.getColumnIndex("DESCRIPTION_FLOWERS");
            int clMyfl = cursor.getColumnIndex("MY_FLOWER");

            MyFl=0;
            //пробегаем по все растениям
            if (cursor.moveToNext()) {
                //считываем данные из запроса
                String NmFlrs = cursor.getString(clNm);
                String DescrFlrs = cursor.getString(clOpis);
                int PicI = cursor.getInt(clPic);
                MyFl = cursor.getInt(clMyfl);

                //Установка названия растения в заголовок активности
                setTitle(NmFlrs);

                //Вывод данных на активность
                TextView mOpisFlowerView = (TextView)findViewById(R.id.textOpisView);
                if(mOpisFlowerView!=null)mOpisFlowerView.setText(DescrFlrs);

                //Показ картинки
                ImageView mIcoView = (ImageView) findViewById(R.id.PictView);
                if(mIcoView!=null)mIcoView.setImageResource(PicI);

            }
            //Закрытие курсора и базы
            cursor.close();
            mDb.close();

        }

    }

}
