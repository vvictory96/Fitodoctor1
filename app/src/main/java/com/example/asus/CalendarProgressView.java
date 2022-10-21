package com.example.asus;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;


//Вспомогательный класс для элемента прогресса
public class CalendarProgressView extends LinearLayout {

    //Элмент показа прогресса
    ProgressView progresBar;
    //Текст
    TextView text;

    public CalendarProgressView(Context context) {
        super(context);
        init(context);
    }

    public CalendarProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_calendar_progress_item, this);
        setOrientation(HORIZONTAL);
        progresBar=(ProgressView)this.findViewById(R.id.progresBar);
        text=(TextView)this.findViewById(R.id.text);
        setup();
    }

    private void setup() {

    }

    public ProgressView getProgressView() {
        return progresBar;
    }

    public TextView getText() {
        return text;
    }
}
