package com.example.asus;

import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;



//Класс заголовка таблицы задач, число-день недели
public class HeaderHolder {
    public CapFirstLetterTextView currentMonth;
    public ImageView prevMonth;  //Кнопка на следующий месяц
    public ImageView nextMonth;  //Кнопка на предыдущий месяц

    public TableLayout daysRow;   //строка с элементами вью

    //используемые цвета
    public int primaryColor;
    public int primaryDarcColor;
    public int colorGrayLight;
    public int backgroundColor;

    public HeaderHolder(View view){
        primaryColor=R.color.colorPrimary;
        primaryDarcColor=R.color.colorPrimaryDark;
        colorGrayLight=R.color.colorGrayLight;
        backgroundColor=R.color.enter_button_color;

        prevMonth=(ImageView)view.findViewById(R.id.prevMonth);
        nextMonth=(ImageView)view.findViewById(R.id.nextMonth);
        currentMonth=(CapFirstLetterTextView)view.findViewById(R.id.currentMonth);

        daysRow=(TableLayout)view.findViewById(R.id.daysRow);
    }
}
