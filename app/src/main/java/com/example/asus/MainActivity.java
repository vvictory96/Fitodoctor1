package com.example.asus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    //объявляем toolbar
   private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //подключаем toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar !=null) {
            setSupportActionBar(toolbar);

        }

        //переход по нажатию кнопки в каталог Все растения
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
                startActivity(intent);
            }
        });


        //переход по нажатию кнопки в каталог Календарь
        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                Intent intent = new Intent(MainActivity.this, CalendarActivity2.class);
                startActivity(intent);
            }
        });

        //переход по нажатию кнопки в каталог Мои растения
        Button button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent (MainActivity.this, IzbranActivity.class);
                startActivity(intent);
            }
        });

        //переход по нажатию кнопки в каталог Поиск
        Button button4 = (Button)findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, PoiskCatalogActivity.class);
                startActivity(intent);
            }
        });

        //переход по нажатию кнопки в активити О приложении
        Button button5 = (Button)findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
