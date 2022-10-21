package com.example.asus;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

//Класс контроллера задач и посредник по работе с базой
public class TaskController {

    private Context context;

    public TaskController(Context context) {
        this.context = context;
    }

    public void addTask(BDTaskModel model) {

        //Подключение к базе данных
        DatabaseHelper mDBHelper = new DatabaseHelper(context);
        SQLiteDatabase mDb;
        //База нужна для записи
        mDb = mDBHelper.getWritableDatabase();


        //Формирование идентификатора задачи
        //Запрос максимального значения из уже имеющихся
        String query = "SELECT MAX(_ID) AS MX FROM TASKSLIST";
        Cursor cursor = mDb.rawQuery(query, null);

        int id = 0;
        if (cursor.moveToFirst())
        {
            do
            {
                id = cursor.getInt(0);
            } while(cursor.moveToNext());
        }
        //Увеличение максимального имеющегося на 1
        id++;
        //Закрыть курсор
        cursor.close();


        //Внесение записи о новом задании
        //Формирование строки запроса
        String addTask = "INSERT INTO TASKSLIST(_ID,NAME_TASK,COUNT_DAYS,COUNT_REPEATS, " +
                " DATE_START,DATE_FINISH) VALUES("+ id+
                ",\""+model.getTitle()+"\","+model.getCountDays()+","+model.getCountRepeats()+
                ",\""+model.getDateStart()+"\",\""+model.getDateFinish()+"\")";
        mDb.execSQL(addTask);

        //внесение записей о днях выполнения задачи
        String addDaysTask = "INSERT INTO TASKSDAYS(_ID_TSK,ID_DAY,DAY_SEL) " +
                " VALUES("+ id+",";
        List<BDayWeekModel> Days= model.getFixDaysList();
        for(int i=0; i< Days.size();i++)
        {
            //Занесение дней выполнения задачи
            mDb.execSQL(addDaysTask+i+","+Days.get(i).isFixDayI()+")");
        }

        //формирование записей об истории выполнения задачи
        String addHistoryTask = "INSERT INTO TASKHISTORY(_ID_TSK,PROGRESS,DAY_NUM,DAY_PROGR) " +
                " VALUES("+ id+",0,";
        List<BDTaskHistoryModel> Hists= model.getBaseTaskHistoryModels();
        for(int i=0; i< Hists.size();i++)
        {
            BDTaskHistoryModel hs1=Hists.get(i);
            if(hs1!=null) {
                //Занесение записи об истории выполнения задачи
                mDb.execSQL(addHistoryTask  + hs1.getId() + ",\"" + hs1.getDate() + "\")");
            }
        }

        //Закрыть БД
        mDb.close();
    }

    //Запрос всех задач из базы
    public ArrayList<BDTaskModel> getAllTasks() {
        //массив задач
        ArrayList<BDTaskModel> bsdatTaskModels = new ArrayList<>();

        //Подключение к базе данных
        DatabaseHelper mDBHelper = new DatabaseHelper(context);
        SQLiteDatabase mDb;
        //База нужна для чтения
        mDb = mDBHelper.getReadableDatabase();

        //отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT * FROM TASKSLIST ",
                null);
        //Определение номеров полей в запросе
        int clID = cursor.getColumnIndex("_ID");
        int clNm = cursor.getColumnIndex("NAME_TASK");
        int clCntDay = cursor.getColumnIndex("COUNT_DAYS");
        int clCntRept = cursor.getColumnIndex("COUNT_REPEATS");
        int clDatStrt = cursor.getColumnIndex("DATE_START");
        int clDatFin = cursor.getColumnIndex("DATE_FINISH");

        //пробегаем по всем задачам
        while (cursor.moveToNext()) {
            //считывание данных из запроса
            int id = cursor.getInt(clID);
            String NmTsk = cursor.getString(clNm);
            int CnDy = cursor.getInt(clCntDay);
            int CnRep = cursor.getInt(clCntRept);
            String DtS = cursor.getString(clDatStrt);
            String DtF = cursor.getString(clDatFin);
            //закидываем задачу в список задач
            bsdatTaskModels.add(new BDTaskModel(id, NmTsk, CnDy,CnRep,DtS,DtF));
        }
        //Закрыть курсор
        cursor.close();

        //Запрос дней для задач
        String GetDaysT="SELECT ID_DAY,DAY_SEL FROM TASKSDAYS WHERE _ID_TSK=";
        String GetHistoryT="SELECT DAY_NUM,DAY_PROGR,PROGRESS FROM TASKHISTORY WHERE _ID_TSK=";
        //пробегаем по всем задачам
        for (int i = 0; i < bsdatTaskModels.size(); i++) {
            BDTaskModel T1=bsdatTaskModels.get(i);
            if(T1!=null)
            {
                //Запрос дней для задачи
                Cursor cursor2 = mDb.rawQuery(GetDaysT+T1.getId(),null);
                List<BDayWeekModel> fixDaysList=new ArrayList<BDayWeekModel>();
                //пробегаем по всем дням задачи
                while (cursor2.moveToNext()) {
                    //считывание данных из запроса
                    int idD = cursor2.getInt(0);
                    int slD = cursor2.getInt(1);
                    fixDaysList.add(idD, new BDayWeekModel(idD,slD));
                }
                T1.setFixDaysList(fixDaysList);
                //Закрыть курсор
                cursor2.close();

                //Запрос дней для задачи
                cursor2 = mDb.rawQuery(GetHistoryT+T1.getId(),null);
                List<BDTaskHistoryModel> HistDaysList=new ArrayList<BDTaskHistoryModel>();
                //пробегаем по всем дням задачи
                while (cursor2.moveToNext()) {
                    //считывание данных из запроса
                    int idD = cursor2.getInt(0);
                    String datH = cursor2.getString(1);
                    int prog = cursor2.getInt(2);
                    HistDaysList.add(idD, new BDTaskHistoryModel(idD,datH,prog));
                }
                T1.setBaseTaskHistoryModels(HistDaysList);
                //Закрыть курсор
                cursor2.close();
            }
        }
        //Закрыть БД
        mDb.close();

        return bsdatTaskModels;
    }

    public void updateTaskProgress(int idTsk, int idD, int progress) {

        //Подключение к базе данных
        DatabaseHelper mDBHelper = new DatabaseHelper(context);
        SQLiteDatabase mDb;
        //База нужна для записи
        mDb = mDBHelper.getWritableDatabase();


        //Условие для запросов. При поиске и обновлении
        String AskWhere= " WHERE _ID_TSK ="+idTsk +" AND DAY_NUM="+idD;
        //Запрос имеющегося значения прогресса
        String query = "SELECT PROGRESS FROM TASKHISTORY "+AskWhere;
        Cursor cursor = mDb.rawQuery(query, null);

        int Progr = 0;
        if (cursor.moveToFirst())
        {
            do
            {
                Progr = cursor.getInt(0);
            } while(cursor.moveToNext());
        }
        //Увеличение прогресса на 1
        Progr++;
        //Закрыть курсор
        cursor.close();

        //формирование запроса на изменение прогресса
        String updateProgressTask = "UPDATE TASKHISTORY SET PROGRESS= "+Progr+AskWhere;
        //выполнение запроса
        mDb.execSQL(updateProgressTask);


        //Закрыть БД
        mDb.close();
    }

    //Очистка задач в базе
    public void clearDatabase() {
        //Подключение к базе данных
        DatabaseHelper mDBHelper = new DatabaseHelper(context);
        SQLiteDatabase mDb;
        //База нужна для записи
        mDb = mDBHelper.getWritableDatabase();
        //отправляем запрос в БД
        mDb.execSQL("DELETE FROM TASKSLIST ");
        mDb.execSQL("DELETE FROM TASKSDAYS ");
        mDb.execSQL("DELETE FROM TASKHISTORY ");
        //Закрыть БД
        mDb.close();
    }

    public void DelTasks(String Tw)
    {
        if(Tw.isEmpty())return;
        //Подключение к базе данных
        DatabaseHelper mDBHelper = new DatabaseHelper(context);
        SQLiteDatabase mDb;
        //База нужна для записи
        mDb = mDBHelper.getWritableDatabase();
        //отправляем запрос в БД
        mDb.execSQL("DELETE FROM TASKSLIST WHERE _ID IN("+Tw+")");
        mDb.execSQL("DELETE FROM TASKSDAYS WHERE _ID_TSK IN("+Tw+")");
        mDb.execSQL("DELETE FROM TASKHISTORY WHERE _ID_TSK IN("+Tw+")");
        //Закрыть БД
        mDb.close();
    }

}
