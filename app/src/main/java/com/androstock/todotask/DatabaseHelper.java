package com.androstock.todotask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    static String name = "TodoTask";
    static int version=1;


    String sqlCreateUserTable="CREATE TABLE  if not exists `UserInfo` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`FirstName`\tTEXT,\n" +
            "\t`LastName`\tTEXT,\n" +
            "\t`Email`\tTEXT,\n" +
            "\t`Password`\tTEXT,\n" +
            "\t`ConfirmPassword`\tTEXT\n" +
            ")";


    public DatabaseHelper(Context context) {


        super(context, name, null, version);

        getWritableDatabase().execSQL(sqlCreateUserTable);
    }



    public  void insertUser(ContentValues contentValues)
    {
        getWritableDatabase().insert("UserInfo","",contentValues);
    }


    public boolean isLoginSuccessfulFromemail(String email, String password)
    {
        String sql="select count(*) from UserInfo where Email='"+email+"' and ConfirmPassword='"+password+"'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);

        long l = statement.simpleQueryForLong();

        statement.close();

        if(l==1)
        {
            return  true;
        }
        else
        {
            return  false;
        }

    }

    public boolean isUsernamesame(String username)
    {
        String sql="select count(*) from UserInfo where Username='"+username+"'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);

        long l = statement.simpleQueryForLong();

        statement.close();

        if(l==1)
        {
            return  true;
        }
        else
        {
            return  false;
        }

    }

    public boolean isEmailsame(String email)
    {
        String sql="select count(*) from UserInfo where Email='"+email+"'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);

        long l = statement.simpleQueryForLong();

        statement.close();

        if(l==1)
        {
            return  true;
        }
        else
        {
            return  false;
        }

    }
    public void updateUser(String id, ContentValues contentValues)
    {
        getWritableDatabase().update("UserInfo",contentValues,"id="+id,null);
        //  getWritableDatabase().update("info",contentValues,"id=?",new String []{id});
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




}

