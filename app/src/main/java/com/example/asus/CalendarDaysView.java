package com.example.asus;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

//Класс для показа дат и дней недель в заголовке
public class CalendarDaysView extends LinearLayout {

    TextView date;    //Число
    TextView dayOfWeek;    //День недели

    //инициализация

    public CalendarDaysView(Context context) {
        super(context);
        init(context);
    }

    public CalendarDaysView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarDaysView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //Задание вида
        inflate(context, R.layout.view_calendar_days_item, this);
        //Установка ориентации
        setOrientation(HORIZONTAL);
        date=(TextView)this.findViewById(R.id.date);
        dayOfWeek=(TextView)this.findViewById(R.id.dayOfWeek);
        setup();
    }

    private void setup() {

    }

    //Установка даты
    public void setDate(String date) {
        this.date.setText(date);
    }

    //Установка дня недели
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek.setText(dayOfWeek);
    }
}
