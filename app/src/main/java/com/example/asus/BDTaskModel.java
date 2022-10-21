package com.example.asus;


import java.util.ArrayList;
import java.util.List;

//Класс задачи
public class BDTaskModel {

    private int id;     //идентификатор задачи
    private String title;  //Название задачи
    private int countDays; //Количество дней
    private int countRepeats;  //Количество повторов в день
    private String dateStart;  // Дата начала
    private String dateFinish; //Дата окончания
    private List<BDayWeekModel> fixDaysList;   //Список дней недели, когда надо выполнять задачу
    private List<BDTaskHistoryModel> baseTaskHistoryModels = new ArrayList<BDTaskHistoryModel>();  //Истори я выполнения задачи

    public BDTaskModel(int idt, String ttl, int cnD, int cnR, String DtS, String DtFn) {
        id = idt;
        title = ttl;
        countDays = cnD;
        countRepeats = cnR;
        dateStart = DtS;
        dateFinish = DtFn;
    }

    public BDTaskModel() {
        id = 0;
        title = "";
        countDays = 0;
        countRepeats = 0;
        dateStart = "";
        dateFinish = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }


    public List<BDTaskHistoryModel> getBaseTaskHistoryModels() {
        return baseTaskHistoryModels;
    }

    public void setBaseTaskHistoryModels(List<BDTaskHistoryModel> baseTaskHistoryModels) {
        this.baseTaskHistoryModels = baseTaskHistoryModels;
    }


    public int getCountDays() {
        return countDays;
    }

    public void setCountDays(int countDays) {
        this.countDays = countDays;
    }

    public int getCountRepeats() {
        return countRepeats;
    }

    public void setCountRepeats(int countRepeats) {
        this.countRepeats = countRepeats;
    }


    public List<BDayWeekModel> getFixDaysList() {
        return fixDaysList;
    }

    public void setFixDaysList(List<BDayWeekModel> fixDaysList) {
        this.fixDaysList = fixDaysList;
    }

    //Функция поиска даты в истории задачи
    public int FindDateInHistory(String dateFind)
    {
        //Перебор всей истории
        for(int i=0; i<baseTaskHistoryModels.size();i++)
        {
            BDTaskHistoryModel H1=baseTaskHistoryModels.get(i);
            if(H1!=null)
            {
                //Извлечение даты
                String dt2=H1.getDate();
                if (dateFind.equals(dt2.trim())) {
                    //Если совпало, то возврат идентификатора записи истории и незачем искать далее
                    return H1.getId();
                }
            }
        }
        //Если не найдено, то вернуть -1
        return -1;
    }


}