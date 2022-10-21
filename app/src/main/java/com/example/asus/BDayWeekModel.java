package com.example.asus;


//Класс пмодели дней недели
public class BDayWeekModel {
    private int id; // Идентификатор дня
    private boolean fixDay=false; //Признак, используется этот день или нет

    public int getId() {
        return id;
    }

    //Задание используется или нет, с переводо из целого - для базы данных
    BDayWeekModel(int i, int f) {
        id=i;
        if(f==1)fixDay=true;
        else fixDay=false;
    };
    BDayWeekModel(){id=-1;fixDay=false;};
    public void setId(int id) {
        this.id = id;
    }


    public boolean isFixDay() {
        return fixDay;
    }

    //Перевод признака в целое - нужно для базы данных
    public int isFixDayI() {
        if (fixDay)
            return 1;
        else
            return 0;
    }

    public void setFixDay(boolean fixDay) {
        this.fixDay = fixDay;
    }
}
