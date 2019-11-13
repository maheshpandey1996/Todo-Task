package com.androstock.todotask;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class TaskDBHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME ="todotask";
    public static final String CONTACTS_TABLE_NAME = "todo";


    public TaskDBHelper(Context context)
    {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub



        db.execSQL(
                "CREATE TABLE "+CONTACTS_TABLE_NAME +
                        "(id INTEGER PRIMARY KEY, task TEXT, dateStr INTEGER,timeStr INTEGER, priority INTEGER, status TEXT)"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE_NAME);
        onCreate(db);
    }



    private long getDate(String day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        try {
        date = dateFormat.parse(day);
        } catch (ParseException e) {}
        return date.getTime();
    }

    private long getTime(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "hh:mm a");
        Date date = new Date();
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {}
        return date.getTime();
    }



    public boolean insertContact  (String task, String dateStr, String timeStr,int pri,String status)
    {
        Date date;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("dateStr", getDate(dateStr));
        contentValues.put("timeStr",getTime(timeStr));
        contentValues.put("priority",pri);
        contentValues.put("status",status);
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateContact (String id, String task, String dateStr, String timeStr, int pri, String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("task", task);
        contentValues.put("dateStr", getDate(dateStr));
        contentValues.put("timeStr", getTime(timeStr));
        contentValues.put("priority", pri);
        contentValues.put("status",status);


        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[] { id } );
        return true;
    }


    public void deleteUser(String id){
        getWritableDatabase().delete(CONTACTS_TABLE_NAME,"id="+id,null);
    }




    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+" order by id desc", null);
        return res;

    }

    public Cursor getDataSpecific(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+" WHERE id = '"+id+"' order by priority asc", null);
        return res;

    }

    public Cursor getDataToday(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', 'localtime') order by priority asc", null);
        return res;

    }


    public Cursor getDataTomorrow(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime')  order by priority asc", null);
        return res;

    }


    public Cursor getDataUpcoming(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by priority asc", null);
        return res;

    }



}
