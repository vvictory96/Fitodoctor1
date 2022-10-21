package com.example.asus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;




public class NewTaskFragment  extends Fragment {

    public static final int WITHOUD_DEADLINE = 1000;//Коичество дней, если выбрано без ограничения
    public static final int START_DATE = 0;

    public ScheduleView scheduleView; //Элемента задания плана
    public EditText target; //Элемента ввода названия задачи
    public Button ButtonAdd; //Кнопка добавить задачу

    //Используемые цвета
    int blueColor;
    int grayColor;

    private BDTaskModel basedatTaskModel;
    private List<BDTaskHistoryModel> taskHistoryModels;
    public TaskController taskController;

    public Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        setHasOptionsMenu(true);

        //Начальная инициализация
        scheduleView=(ScheduleView)view.findViewById(R.id.schletudeView);
        target=(EditText)view.findViewById(R.id.target);
        ButtonAdd=(Button)view.findViewById(R.id.buttonadd);
        ButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTask();
            }
        });
        blueColor=R.color.colorPrimary;
        grayColor=R.color.dark_gray_text;
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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Создание списка задач
        taskController = new TaskController(context);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //инициализация переменных
        taskHistoryModels = new ArrayList<BDTaskHistoryModel>();
        basedatTaskModel = new BDTaskModel();
    }

    private void AddTask()
    {
        //Считывание длительности задачи
        int deadlineValue = WITHOUD_DEADLINE;     //неограниченная длительность
        if(!TextUtils.isEmpty(scheduleView.getDeadline()))
            deadlineValue = Integer.valueOf(scheduleView.getDeadline());
        //считывание других данных о задаче в переменную
        basedatTaskModel.setTitle(target.getText().toString());
        basedatTaskModel.setDateStart(getDate(START_DATE));
        basedatTaskModel.setDateFinish(getDate(deadlineValue));
        basedatTaskModel.setCountDays(deadlineValue);
        basedatTaskModel.setCountRepeats(scheduleView.getRepeatValue());

        //Запрос списка выбранных и не выбранных дней недели
        List<Boolean> boo = scheduleView.getFixDaysList();
        ArrayList<BDayWeekModel> dayWeekModels = new ArrayList<>();
        for(int k = 0; k < boo.size(); k++) {
            BDayWeekModel booleanModel = new BDayWeekModel();
            booleanModel.setId(k);
            booleanModel.setFixDay(boo.get(k));
            dayWeekModels.add(booleanModel);
        }
        //занесение дней выполнения в задачу
        basedatTaskModel.setFixDaysList(dayWeekModels);

        //формирование истории задачи
        Calendar calendar = Calendar.getInstance();
        for(int i = 0; i <deadlineValue; i++) {
            BDTaskHistoryModel baseTaskHistoryModel = new BDTaskHistoryModel();
            baseTaskHistoryModel.setId(i); //задание порядкового номера дня
            //формирование даты
            baseTaskHistoryModel.setDate(calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DATE));
            //добавление даты в историю
            taskHistoryModels.add(baseTaskHistoryModel);
            //сдвиг даты на следующую
            calendar.add(Calendar.DATE, 1);
        }
        //занесение истории в задачу
        basedatTaskModel.setBaseTaskHistoryModels(taskHistoryModels);

        if (basedatTaskModel.getFixDaysList().size() > 0)
            new InsertDataAboutTask().execute();
        else
            Toast.makeText(context, getString(R.string.task_new_target_toast_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Реакция на команду создания задачи
            case R.id.add:
                AddTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Вычисление даты со сдвигом на daysCount дней
    public String getDate(int daysCount) {
        Time time = new Time();
        Calendar now = Calendar.getInstance();
        //Начальная дата - текущая. нечего добавлять
        if(daysCount != START_DATE) {
            //добавить дни
            now.add(Calendar.DATE, daysCount);
        }
        time.set(now.get(Calendar.DATE), now.get(Calendar.MONTH), now.get(Calendar.YEAR));
        return time.format("%d %m %Y");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Класс ачинхронного выполнения записи новой задачи в базу.
    private class InsertDataAboutTask extends AsyncTask<Void, Void, Void> {

        private TaskController taskController;

        @Override
        protected Void doInBackground(Void... params) {
            taskController = new TaskController(context);
            taskController.addTask(basedatTaskModel);
            return null;
        }

        @Override
        protected void onPreExecute() { }


        //Когда задача выполнится - закрытие окна
        @Override
        protected void onPostExecute(Void sum) {
            Intent intent = new Intent();
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    }
}