package com.example.asus;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CalendarDelTaskActivity extends AppCompatActivity {

    //Массив для хранения данных об отметках
    class TaskDel{
        int idT;
        String NameT;
        boolean DelT;
        TaskDel (int i, String T, boolean b){idT=i;NameT=T; DelT=b;};
    }
//    TaskDel[] AllTsks;
    List<TaskDel> TASKS; //список задач

    //Элемента списка
    private class TaskDelHolder extends RecyclerView.ViewHolder  {
        int idF; //идентификатор
        TextView mNameFlowerView; //Название
        CheckBox ChB; //Чекбокс

        TaskDelHolder(View itemView) {
            super(itemView);

            idF=0;

            mNameFlowerView = (TextView)
                    itemView.findViewById(R.id.textView6);
            ChB = (CheckBox)
                    itemView.findViewById(R.id.checksimp);
            //Задание обрботчика нажатия на чекбокс

            ChB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=-1;
                    for (int i = 0; i < TASKS.size(); i++) {
                        if (TASKS.get(i).idT==idF) {
                            pos=i;
                            break;
                        }
                    }


                    if(pos>=0) {
                        if (ChB.isChecked()) {
                            TASKS.get(pos).DelT = true;
                        } else {
                            TASKS.get(pos).DelT = false;
                        }
                    }
                }
            });

        }
    }


    //Адаптер списка
    private class TaskDelAdapter extends RecyclerView.Adapter<CalendarDelTaskActivity.TaskDelHolder>   {
        private List<TaskDel> mTasks; //Список
        TaskDelAdapter(List<TaskDel> flrs) {
            mTasks = flrs;
        }
        @Override
        public CalendarDelTaskActivity.TaskDelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater
                    .inflate(R.layout.taskdel_item, parent, false);

            return new CalendarDelTaskActivity.TaskDelHolder(view);
        }
        @Override
        public void onBindViewHolder(CalendarDelTaskActivity.TaskDelHolder holder, int position) {
            //Вывод данных о симптоме в элемент списка
            TaskDel nmTask = mTasks.get(position);
            holder.mNameFlowerView.setText(nmTask.NameT);
            holder.idF=nmTask.idT;
            holder.ChB.setChecked(nmTask.DelT);
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fordel);

        setTitle(getString(R.string.del_tasks));
        //подключаем toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar Ab = getSupportActionBar();
            if (Ab != null) Ab.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.icons8_home);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        //подключение к базе
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        SQLiteDatabase mDb;
        //Открытие базы на чтение
        mDb = mDBHelper.getReadableDatabase();

        TASKS = new ArrayList<TaskDel>();

        //отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT _ID,NAME_TASK FROM TASKSLIST ",
                null);
        //Определение номеров столбцов с данными
        int clID = cursor.getColumnIndex("_ID");
        int clNm = cursor.getColumnIndex("NAME_TASK");

        //пробегаем по все симптомы
        while (cursor.moveToNext()) {
            //считывание данных из запроса
            String NmTask = cursor.getString(clNm);
            int id = cursor.getInt(clID);
            //закидываем симптом в список
            TaskDel NbdT=new TaskDel(id,NmTask,false);
            TASKS.add(NbdT);
        }
        //Закрытие запроса и базы
        cursor.close();
        mDb.close();

        /*
        //Создание массива отметок симптомов
        AllTsks= new TaskDel[TASKS.size()];
        //инициализация
        for(int i=0; i<TASKS.size(); i++) {
            TaskDel T1=TASKS.get(i);
            AllTsks[i] = new TaskDel(T1.getId(), T1. getTitle(), false);
        }
        */


        //создаем адаптер
        CalendarDelTaskActivity.TaskDelAdapter adapter = new CalendarDelTaskActivity.TaskDelAdapter(TASKS);
        RecyclerView listView = (RecyclerView) findViewById(R.id.my_recycle_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);


        //Задание обработки кнопки поиска
        Button button1 = (Button) findViewById(R.id.button_diagnos);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Формирование данных об отмеченных симптомах
                String ValP = "";
                int kolUsl = 0; //Счётчик отметок
                for (int i = 0; i < TASKS.size(); i++) {
                    if (TASKS.get(i).DelT) {
                        if (!ValP.isEmpty()) ValP += ","; //первой запятой нет
                        ValP += TASKS.get(i).idT;
                        kolUsl++;  //Подсчёт отметок
                    }
                }
                if (kolUsl > 0) {
                    /*
                    //Если есть отметки, то открыть активность результата поиска
                    Intent intent = new Intent(CalendarDelTaskActivity.this, TreatmentActivity.class);
                    startActivity(intent);
                    */

                    Intent intent = new Intent();
                    //Передать данные об отметках
                    intent.putExtra(CalendarFragment.DEL_TASKS, ValP);
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                } else
                    //Если ничего не отмечено, то уведомить об этом пользователя
                    Snackbar.make(v, R.string.Not_USL, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

            }
        });

    }
}
