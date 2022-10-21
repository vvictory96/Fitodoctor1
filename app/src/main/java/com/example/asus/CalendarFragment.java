package com.example.asus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;



public class CalendarFragment extends Fragment implements CalendarAdapter.OnClickCallback {

    //Код для результата вызова активности
    public static final int REQUEST_RESULT = 256;
    public static final int DELETE_RESULT = 257;

    public static String DEL_TASKS="deltasks";


    public Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        listView=(ListView)view.findViewById(R.id.listView);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onAttach(Context contxt) {
        super.onAttach(contxt);
        context = contxt;
    }

    //Список задач на экране
    ListView listView;

    //Список задач из базы данных
    public TaskController taskController;
    public CalendarAdapter calendarAdapter;
    public Calendar current;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Создание списка задач. Он же посредник для работы с БД
        taskController = new TaskController(context);
        current = Calendar.getInstance();
        current.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarAdapter = new CalendarAdapter(context);
        //Запрос задач из базы
        calendarAdapter.setItem(taskController.getAllTasks());
        calendarAdapter.setCalendarDated(showDatesInView());
        calendarAdapter.setCalendar(current);
        calendarAdapter.setOnClickListener(this);
        listView.setAdapter(calendarAdapter);
    }

    public String getDayOfWeek(Calendar calendar) {
        return new SimpleDateFormat("EE", Locale.getDefault()).format(calendar.getTime());
    }

    //Обновление данных
    public void refreshAdapter() {
        calendarAdapter.setCalendar(current);
        calendarAdapter.setCalendarDated(showDatesInView());
        listView.setAdapter(calendarAdapter);
    }

    //Формирование списка для вывода на экран
    public ArrayList<CalendarModel> showDatesInView() {
        ArrayList<CalendarModel> dates = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            CalendarModel model = new CalendarModel();
            model.setDay(current.get(Calendar.DATE));
            model.setMonth(current.get(Calendar.MONTH));
            model.setYear(current.get(Calendar.YEAR));
            model.setDayOfWeek(getDayOfWeek(current));
            current.add(Calendar.DATE, 1);
            dates.add(model);
        }
        return dates;
    }

    //Переход на следующую неделю
    @Override
    public void onNextClick() {
        current.add(Calendar.DATE, 0);
        refreshAdapter();
    }

    //Переход на предыдущую неделю
    @Override
    public void onPrevClick() {
        current.add(Calendar.DATE, -14);
        refreshAdapter();
    }

    @Override
    public void onDateClick(int dayCount) { }

    //Создание меню
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Обработка нажатия меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                //Нажата кнопка добавить
                //Вызов активности добавления новой задачи
                startActivityForResult(new Intent(context, NewTaskActivity.class), REQUEST_RESULT);
                return true;
            case R.id.del:
                //Нажата кнопка удалить
                //Вызов активности добавления новой задачи
                startActivityForResult(new Intent(context, CalendarDelTaskActivity.class), DELETE_RESULT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Обработка возврата из активности добавления задачи
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==DELETE_RESULT) {
            if(data!=null) {
                Bundle ExtraB = data.getExtras();
                if (ExtraB != null) {
                    String UslDetTsk = ExtraB.getString(DEL_TASKS);
                    if(UslDetTsk!=null) {
                        taskController.DelTasks(UslDetTsk);
                    }
                }
            }
        }
        //Установка даты
        calendarAdapter.setCalendar(current);
        //Загрузка данных из базы и установка их
        calendarAdapter.setItem(taskController.getAllTasks());
        //Передача данных в список на экране
        listView.setAdapter(calendarAdapter);

    }
}