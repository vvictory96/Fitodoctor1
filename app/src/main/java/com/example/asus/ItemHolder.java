package com.example.asus;

import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;



//Класс строки прогресса задачи
public class ItemHolder {
    public TextView title;
    public TextView days;

    public TableLayout progressRow;

    public ItemHolder(View view){
        //Название задачи
        title=(TextView)view.findViewById(R.id.title);
        //Длительность задачи в днях
        days=(TextView)view.findViewById(R.id.days);
        //Элемент прогресса задачи
        progressRow=(TableLayout)view.findViewById(R.id.progressRow);
    }
}
