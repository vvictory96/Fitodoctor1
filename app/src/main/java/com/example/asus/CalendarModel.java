package com.example.asus;


import android.support.annotation.NonNull;


//класс календаря
public class CalendarModel implements Comparable<CalendarModel> {

    private String dayOfWeek; //День недели
    private Integer day;     //Число
    private Integer month;   //Месяц
    private Integer year;    //Год


    //Запрос дня недели
    String getDayOfWeek() {
        return dayOfWeek;
    }

    //Установка дня недели
    void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    //Запрос числа
    int getDay() {
        return day;
    }

    //Установка числа
    void setDay(int date) {
        this.day = date;
    }

    //Сравнение чисел
    @Override
    public int compareTo(@NonNull CalendarModel another) {
        return day.compareTo(another.day);
    }

    //Запрос года
    Integer getYear() {
        return year;
    }

    //Установка года
    void setYear(Integer year) {
        this.year = year;
    }

    //Запрос месяца
    Integer getMonth() {
        return month;
    }

    //Установка месяца
    void setMonth(Integer month) {
        this.month = month;
    }
}