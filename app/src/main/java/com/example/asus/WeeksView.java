package com.example.asus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


//Список дней недели
public class WeeksView extends LinearLayout implements View.OnClickListener {

    //Список названий дней недели
    TextView monBtn;
    TextView thuBtn;
    TextView wedBtn;
    TextView thurBtn;
    TextView frBtn;
    TextView sutBtn;
    TextView sunBtn;

    //Используемые цвета
    int blueColor;
    int grayColor;

    //Состояния кнопок - выбора дней недели
    private boolean monday=false;
    private boolean tuesday=false;
    private boolean wednesday=false;
    private boolean thursday=false;
    private boolean friday=false;
    private boolean saturday=false;
    private boolean sunday=false;
    //Список состояния выбора дней недели
    private List<Boolean> fixDaysList;

    public WeeksView(Context context) {
        super(context);
        init(context);
    }

    public WeeksView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeeksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_tasks_weeks, this);
        setOrientation(VERTICAL);

        //Настройка цветов
        blueColor=context.getResources().getColor(R.color.day_sel);
        grayColor=context.getResources().getColor(R.color.day_not_sel);

        //Поиск элементов списка
        monBtn=(TextView)this.findViewById(R.id.monBtn);
        monBtn.setTextColor(grayColor);
        thuBtn=(TextView)this.findViewById(R.id.thuBtn);
        thuBtn.setTextColor(grayColor);
        wedBtn=(TextView)this.findViewById(R.id.wedBtn);
        wedBtn.setTextColor(grayColor);
        thurBtn=(TextView)this.findViewById(R.id.thurBtn);
        thurBtn.setTextColor(grayColor);
        frBtn=(TextView)this.findViewById(R.id.frBtn);
        frBtn.setTextColor(grayColor);
        sutBtn=(TextView)this.findViewById(R.id.surthBtn);
        sutBtn.setTextColor(grayColor);
        sunBtn=(TextView)this.findViewById(R.id.sunBtn);
        sunBtn.setTextColor(grayColor);

        setupViews();
    }

    private void setupViews() {
        //Задания обработчиков нажатия на элементы
        monBtn.setOnClickListener(this);
        thuBtn.setOnClickListener(this);
        wedBtn.setOnClickListener(this);
        thurBtn.setOnClickListener(this);
        frBtn.setOnClickListener(this);
        sutBtn.setOnClickListener(this);
        sunBtn.setOnClickListener(this);

        fixDaysList = new ArrayList<>(7);
    }

    @Override
    public void onClick(View view) {
        //Анализ, на какой день недели нажали
        switch(view.getId()) {
            case R.id.monBtn:
                monday = selectDayOfWeek(view, monday);
                break;
            case R.id.thuBtn:
                tuesday = selectDayOfWeek(view, tuesday);
                break;
            case R.id.wedBtn:
                wednesday = selectDayOfWeek(view, wednesday);
                break;
            case R.id.thurBtn:
                thursday = selectDayOfWeek(view, thursday);
                break;
            case R.id.frBtn:
                friday = selectDayOfWeek(view, friday);
                break;
            case R.id.surthBtn:
                saturday = selectDayOfWeek(view, saturday);
                break;
            case R.id.sunBtn:
                sunday = selectDayOfWeek(view, sunday);
                break;
        }
    }


    //Процедура внешних изменений элемента
    private boolean selectDayOfWeek(View v, boolean day) {
        if(!day) {
            ((TextView) v).setTextColor(blueColor);
            day = true;
        } else {
            ((TextView) v).setTextColor(grayColor);
            day = false;
        }
        return day;
    }

    public List<Boolean> getFixDaysList() {
        fixDaysList.clear();
        fixDaysList.add(0 ,monday);
        fixDaysList.add(1, tuesday);
        fixDaysList.add(2, wednesday);
        fixDaysList.add(3, thursday);
        fixDaysList.add(4, friday);
        fixDaysList.add(5, saturday);
        fixDaysList.add(6, sunday);
        return fixDaysList;
    }
}