package com.example.asus;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;


//Класс адаптера календаря
public class CalendarAdapter extends BaseAdapter {

    //Типы запси в таблице
    private static final int TYPE_HEAD = 0;  //Заголовок
    private static final int TYPE_ITEM = 1;  //Запись с данными
    private static final String DATE_TEMPLATE = "MMMM \nyyyy";

    private TaskController taskController;
    private OnClickCallback onClickCallback;
    private DateFormat dateFormatter = new DateFormat();

    private int datePosition = 0; //смещение относительно текущей даты

    private Context context;
    private Calendar calendar;

    //Список задач
    private ArrayList<BDTaskModel> listTasks = new ArrayList<>();
    //Список видимых дат
    private ArrayList<CalendarModel> getDatesInView;

    public CalendarAdapter(Context context) {
        this.context = context;
        taskController = new TaskController(context);
    }

    //Уставнока списка задач
    public void setItem(ArrayList<BDTaskModel> articleResultModels) {
        this.listTasks = articleResultModels;
        //Вызов обновления экрана
        notifyDataSetChanged();
    }

    //Уставнока списка дат
    public void setCalendarDated(ArrayList<CalendarModel> calendarDated) {
        this.getDatesInView = calendarDated;
        //Вызов обновления экрана
        notifyDataSetChanged();
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        //Вызов обновления экрана
        notifyDataSetChanged();
    }

    //Запрос типа записи при формировании таблицы
    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_HEAD;
        else
            return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }


    //Запрос количества задач
    @Override
    public int getCount() {
        return listTasks.size() + 1;
    }

    //Запрос 1 задачи по индексу
    @Override
    public BDTaskModel getItem(int position) {
        if(position == 0) return null;
        return (listTasks.size() >= position) ? listTasks.get(position - 1) : new BDTaskModel();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //Формирование таблицы
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0.1f);
        TableRow tableRow = new TableRow(context);
        tableRow.setOrientation(TableLayout.HORIZONTAL);

        //Формирование зависит от типа записи - 1 запись это заголовок
        int type = getItemViewType(position);
        switch(type) {
            case TYPE_HEAD:
                //Формирование заголовка
                //Задание шаблона для элемента заголовка
                convertView = inflater.inflate(R.layout.item_calendar_days, parent, false);
                HeaderHolder headHolder = new HeaderHolder(convertView);

                headHolder.currentMonth.setFirstCupText(dateFormatter.format(DATE_TEMPLATE, calendar.getTime()));

                //Формирование заголовка на 7 дней
                for(int i = 0; i < 7; i++) {
                    //Создание и формирвоание элемента заголовка
                    CalendarDaysView daysView = new CalendarDaysView(context);
                    //Задание числа
                    daysView.setDate(String.valueOf(getDatesInView.get(i).getDay()));
                    //Задание дня недели
                    daysView.setDayOfWeek(getDatesInView.get(i).getDayOfWeek());
                    //Добавление элемента в строку заголовка
                    tableRow.addView(daysView, rowParams);
                }
                //Добавление строки заголовка в элемент на экране
                headHolder.daysRow.addView(tableRow);

                //Задание реакции обработки нажатия на кнопу следующей недели
                headHolder.nextMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePosition += 7;
                        onClickCallback.onNextClick();
                    }
                });
                //Задание реакции обработки нажатия на кнопу предыдущей недели
                headHolder.prevMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (datePosition > 0)
                            datePosition -= 7;
                        onClickCallback.onPrevClick();
                    }
                });
                //Задание реакции обработки нажатия на текущую дату
                headHolder.currentMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickCallback.onDateClick(datePosition);
                    }
                });
                break;
            case TYPE_ITEM:
                //Формирование записи, строки о задаче
                //Задание шаблона для элемента задачи
                convertView = inflater.inflate(R.layout.item_calendar_progress, parent, false);
                ItemHolder itemHolder = new ItemHolder(convertView);

                //Запрос задачи
                final BDTaskModel model = getItem(position);
                //Установка названия задачи
                itemHolder.title.setText(model.getTitle());
                //Установка длительности задачи
                itemHolder.days.setText(String.format(context.getString(R.string.task_details_times_day), model.getCountRepeats(), model.getCountDays()));

                //Формирование массива прогресса задачи по дням
                for(int numOfDaysInc = 0; numOfDaysInc < 7; numOfDaysInc++) {
                    //Создание элемента прогресса на 1 день
                    CalendarProgressView progressView = new CalendarProgressView(context);
                    //Проверка - делается ли задача в эот день недели?
                    if( model.getFixDaysList().get(numOfDaysInc).isFixDay()) {
                        //Если да, то формирование рабочего элемента, если нет, то просто "заглушка"
                        String DateFlPos=getFullDate(numOfDaysInc);
                        //Поиск даты в истории задачи
                        int IdD=model.FindDateInHistory(DateFlPos);
                        if(IdD>=0)
                        {
                            //Если дата найдена в истории задачи
                            setRepeatsInfo(progressView.getText(), model, IdD, model.getFixDaysList().get(numOfDaysInc).isFixDay());
                            boolean bTod= DateFlPos.equals(getCurrentDate());
                            onProgressClick(progressView.getProgressView(), model, IdD, model.getFixDaysList().get(numOfDaysInc).isFixDay(), position,bTod);
                        }
                    }
                    tableRow.addView(progressView, rowParams);
                }
                itemHolder.progressRow.addView(tableRow);
                break;
        }
        return convertView;
    }

    //Настройка элемента прогресса
    private void onProgressClick(ProgressView progressView, final BDTaskModel model, final int daysInc, boolean isFixed, final int position, boolean isToday) {
        if(isFixed) {
            //Установка значения прогресса
            progressView.setValue(model.getBaseTaskHistoryModels().get(daysInc).getProgress());
            //Установка максимального значения
            progressView.setProgressMaximum(model.getCountRepeats());

            progressView.SetTaskIDay(model.getId(),daysInc);
            if(isToday) {
                //Если день - сегодня, то задать обработчик начатия
                progressView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Поиск текущего значения прогресса и увеличение на 1
                        int progress = model.getBaseTaskHistoryModels().get(daysInc).getProgress() + 1;
                        //Проверка - достигнут ли максимум повторов?
                        //Установка нового значения прогресса
                        if (progress <= model.getCountRepeats())
                            setProgress(progress, position, daysInc, model.getId());
                        else
                            setProgress(0, position, daysInc, model.getId());
                        //Установка нового значения прогресса в истории
                        model.getBaseTaskHistoryModels().get(daysInc).setProgress(progress);
                        //Запустить обновление экрана
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

    //Процедура отправки нового знначения прогресса в историю
    private void setProgress(int progress, int position, int daysInc,  int idTsk) {
            taskController.updateTaskProgress(idTsk, daysInc, progress);
    }

    //вывод текста со значением  повторов и прогресса
    private void setRepeatsInfo(TextView repeatText, BDTaskModel model, int i, boolean isFixed) {
        if(isFixed) {
            if (!TextUtils.isEmpty(model.getBaseTaskHistoryModels().get(i).getDate())) {
                repeatText.setVisibility(View.VISIBLE);
                String TxtProgr= model.getBaseTaskHistoryModels().get(i).getProgress() + "/" + model.getCountRepeats();
                repeatText.setText(TxtProgr);
            }
        }
    }


    //Функция формирования даты
    private String getFullDate(int position) {
        int year = getDatesInView.get(position).getYear();
        int month = getDatesInView.get(position).getMonth();
        int day = getDatesInView.get(position).getDay();
        return year + " " + month + " " + day;
    }

    //Функция формирования строки текущей даты
    private String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) + " " + cal.get(Calendar.MONTH) + " " + cal.get(Calendar.DATE);
    }

    //Установка обработчика нажатий на кнопке перехода на следующую неделю
    public void setOnClickListener(OnClickCallback onNextMonthClickListener) {
        this.onClickCallback = onNextMonthClickListener;
    }


    public interface OnClickCallback {
        void onNextClick();
        void onPrevClick();
        void onDateClick(int dayCount);
    }
}