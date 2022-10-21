package com.example.asus;

//Класс истории задачи
public class BDTaskHistoryModel {

    private int id = 0;      //Идентификтор дня - порядковый номер
    private String date = ""; //Дата
    private int progress = 0;  //Значение прогресса

    public int getId() {
        return id;
    }
    BDTaskHistoryModel(){};
    BDTaskHistoryModel(int i, String nm, int p){id=i;progress=p;date=nm;};

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}