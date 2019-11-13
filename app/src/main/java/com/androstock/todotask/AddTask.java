package com.androstock.todotask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    TaskDBHelper mydb;
    DatePickerDialog dpd;
    TimePickerDialog tpd;
    int startYear = 0, startMonth = 0, startDay = 0;
    int startHour = 0, startMinute = 0;
    String dateFinal;
    String nameFinal;
    String timeFinal;
    int priority;
    String statusfinal;

    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    public static final String Status_done = "Done";
    public static final String Status_notdone = "Not Done";


    Intent intent;
    Boolean isUpdate;
    String id;

    RadioGroup mRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add_new);

        mydb = new TaskDBHelper(getApplicationContext());
        intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);

        Log.d("tagg","value"+isUpdate);



        dateFinal = todayDateString();
        timeFinal = todayTimeString();
        Date your_date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(your_date);
        startYear = cal.get(Calendar.YEAR);
        startMonth = cal.get(Calendar.MONTH);
        startDay = cal.get(Calendar.DAY_OF_MONTH);

        startHour = cal.get(Calendar.HOUR_OF_DAY);
        startMinute = cal.get(Calendar.MINUTE);

      //  Date today = Calendar.getInstance().getTime();

        if (isUpdate) {
            init_update();
        }
    }


    public void init_update() {
        id = intent.getStringExtra("id");

        TextView toolbar_task_add_title = (TextView) findViewById(R.id.toolbar_task_add_title);
        EditText task_name = (EditText) findViewById(R.id.task_name);
        EditText task_date = (EditText) findViewById(R.id.task_date);
        EditText task_time = (EditText) findViewById(R.id.notify_time);
        ImageView imageView = (ImageView) findViewById(R.id.waste);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroupstatus);


        imageView.setVisibility(View.VISIBLE);
        toolbar_task_add_title.setText("Update");
        Cursor task = mydb.getDataSpecific(id);
        if (task != null) {
            task.moveToFirst();

            task_name.setText(task.getString(1).toString());


            Calendar cal = Function.Epoch2Calender(task.getString(2).toString());
            startYear = cal.get(Calendar.YEAR);
            startMonth = cal.get(Calendar.MONTH);
            startDay = cal.get(Calendar.DAY_OF_MONTH);

            Calendar cal1 = Function.Epoch2Calender(task.getString(3).toString());
            startHour = cal1.get(Calendar.HOUR_OF_DAY);
            startMinute = cal1.get(Calendar.MINUTE);


            task_date.setText(Function.Epoch2DateString(task.getString(2).toString(), "dd/MM/yyyy"));
            task_time.setText(Function.Epoch2TimeString(task.getString(3).toString(), "hh:mm a"));

        }

    }

    public String todayDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());

        return dateFormat.toString();

    }

    public String todayTimeString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "hh:mm a", Locale.getDefault());

        return dateFormat.toString();

    }



    public void closeAddTask(View v) {
        finish();
    }


    public void doneAddTask(View v) {
        int errorStep = 0;
        EditText task_name = (EditText) findViewById(R.id.task_name);
        EditText task_date = (EditText) findViewById(R.id.task_date);
        EditText task_time = (EditText) findViewById(R.id.notify_time);


        priority = getPriorityFromViews();
        nameFinal = task_name.getText().toString();
        dateFinal = task_date.getText().toString();
        timeFinal = task_time.getText().toString();
        if (getStatusFromViews() == 1)
        {
            statusfinal = "Done";
        }
        else
        {
            statusfinal = "Not done";
        }


  /* Checking */
        if (nameFinal.trim().length() < 1) {
            errorStep++;
            task_name.setError("Provide a task name.");
        }

        if (dateFinal.trim().length() < 4) {
            errorStep++;
            task_date.setError("Provide a specific date");
        }

        if (timeFinal.trim().length() < 4) {
            errorStep++;
            task_date.setError("Provide a specific time");
        }



        if (errorStep == 0) {
            if (isUpdate) {
                mydb.updateContact(id, nameFinal, dateFinal, timeFinal, priority, statusfinal);
                Toast.makeText(getApplicationContext(), "Task Updated.", Toast.LENGTH_SHORT).show();
            } else {
                mydb.insertContact(nameFinal, dateFinal, timeFinal, priority, statusfinal);
                Toast.makeText(getApplicationContext(), "Task Added.", Toast.LENGTH_SHORT).show();
            }

            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("startDatepickerdialog");
        tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("startTimepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
        if (tpd != null) tpd.setOnTimeSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        startYear = year;
        startMonth = monthOfYear;
        startDay = dayOfMonth;
        int monthAddOne = startMonth + 1;
        String date = (startDay < 10 ? "0" + startDay : "" + startDay) + "/" +
                (monthAddOne < 10 ? "0" + monthAddOne : "" + monthAddOne) + "/" +
                startYear;
        EditText task_date = (EditText) findViewById(R.id.task_date);
        task_date.setText(date);
    }








    public void showStartDatePicker(View v) {
        dpd = DatePickerDialog.newInstance(AddTask.this, startYear, startMonth, startDay);
        dpd.setOnDateSetListener(this);
        dpd.show(getFragmentManager(), "startDatepickerdialog");
    }

    public void showStartTimePicker(View view) {
        tpd = TimePickerDialog.newInstance(AddTask.this, startHour, startMinute, false);
        tpd.setOnTimeSetListener(this);
        tpd.show(getFragmentManager(),"startTimepickerdialog");

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        String AM_PM = " AM";
        String mm_precede = "";
        if (hourOfDay >= 12) {
            AM_PM = " PM";
            if (hourOfDay >=13 && hourOfDay < 24) {
                hourOfDay -= 12;
            }
            else {
                hourOfDay = 12;
            }
        } else if (hourOfDay == 0) {
            hourOfDay = 12;
        }
        if (minute < 10) {
            mm_precede = "0";
        }
        String time = hourOfDay + ":" + mm_precede + minute + AM_PM;
        EditText task_time = (EditText) findViewById(R.id.notify_time);
        task_time.setText(time);
    }

    public void deleteButton(View view) {
        new AlertDialog.Builder(AddTask.this)
                .setTitle("Are we want to delete task!!!")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        TaskDBHelper taskDBHelper = new TaskDBHelper(AddTask.this);
                        taskDBHelper.deleteUser(id);
                        Toast.makeText(AddTask.this, "Task deleted", Toast.LENGTH_SHORT).show();
                        finish();


                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */
    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }

    public int getStatusFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroupstatus)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButtondone:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButtonnotdone:
                priority = PRIORITY_LOW;
        }
        return priority;
    }
    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }



}