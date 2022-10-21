package com.example.asus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

//Класс элемента настройки плана задачи
public class ScheduleView extends LinearLayout implements View.OnClickListener {

    //Переменные элементов
    WeeksView weeksView;
    Spinner repeatSpinner;
    EditText deadLine;
    TextView withoutDeadLine;
    TextView txtSrok;
    TextView txtDays;

    //Цвета
    int labelColor;
    int grayColor;
    int primDarkColor;

    //Вспомогательные переменные
    private boolean withoutDeadline=false; //Признак выбора без ограничения
    private int repeatValue; //Количество повторов

    private Context context;

    public ScheduleView(Context context) {
        super(context);
        init(context);
    }

    public ScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.view_new_target_shletude, this);
        setOrientation(VERTICAL);

        //Поиск элементов
        weeksView=(WeeksView)this.findViewById(R.id.weeksView);
        repeatSpinner=(Spinner)this.findViewById(R.id.repeatSpinner);
        deadLine=(EditText)this.findViewById(R.id.deadLine);
        withoutDeadLine=(TextView)this.findViewById(R.id.withoutDeadLine);
        txtSrok=(TextView)this.findViewById(R.id.txtSrok);
        txtDays=(TextView)this.findViewById(R.id.txtDney);

        //Установка цветов
        labelColor=context.getResources().getColor(R.color.primary_text);
        grayColor=context.getResources().getColor(R.color.day_not_sel);
        primDarkColor=context.getResources().getColor(R.color.primary_dark);
        setupViews();
    }

    private void setupViews() {
        withoutDeadLine.setOnClickListener(this);

        //Взять начальное значение
        repeatValue = Integer.valueOf(getResources().getStringArray(R.array.task_repeats)[0]);
        setRepeatSpinnerAdapter();
    }

    //Установка адаптера для спинера количества повторов
    public void setRepeatSpinnerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.task_repeats));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(adapter);
        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Реакция на выбор количества повторов
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //считать выбранное значение
                repeatValue = Integer.valueOf(getResources().getStringArray(R.array.task_repeats)[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
    }

    //Обработка нажатия на без ограничения
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.withoutDeadLine:
                onWithoutDeadlineChecked(view);
                break;
        }
    }

    //выбрано без ограничения
    public void onWithoutDeadlineChecked(View v) {
        //проверка состояния выбора отметки
        if(v.getId() == R.id.withoutDeadLine) {
            txtSrok.setEnabled(withoutDeadline);
            txtDays.setEnabled(withoutDeadline);
            deadLine.setEnabled(withoutDeadline);
            if(!withoutDeadline) {
                //Если не выбрано, то отключить поле
                ((TextView) v).setText(R.string.task_new_target_setdeadline);
                txtSrok.setTextColor(grayColor);
                txtDays.setTextColor(grayColor);
                deadLine.setTextColor(grayColor);
            } else {
                //Если выбрано, то включить поле
                ((TextView) v).setText(R.string.task_new_target_without_deadline);
                txtSrok.setTextColor(labelColor);
                txtDays.setTextColor(labelColor);
                deadLine.setTextColor(primDarkColor);
            }
            withoutDeadline = !withoutDeadline;
        }
    }

    //запрос продолжительности
    public String getDeadline() {
        return deadLine.getText().toString();
    }

    //запрос повторов
    public int getRepeatValue() {
        return repeatValue;
    }

    //запрос выбранных дней
    public List<Boolean> getFixDaysList() {
        return weeksView.getFixDaysList();
    }

    public boolean isWithoutDeadline() {
        return withoutDeadline;
    }
}