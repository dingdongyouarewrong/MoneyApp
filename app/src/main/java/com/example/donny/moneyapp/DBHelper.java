package com.example.donny.moneyapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "moneyDB";
    public static final String TABLE_NAME = "spending";
    public static final String TABLE_NAME2 = "earning";
    public static final String TABLE_NAME3 = "savings";





    public static final String KEY_ID = "_id";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_SUM = "sum";
    public static final String KEY_NAME = "name";
    public static final String KEY_MONTH = "month";
    public static final String KEY_WEEK = "week";
    public static final String KEY_DAY = "day";
    public static final String KEY_DATE = "date";
    public static final String KEY_CURRENCY = "currency";





    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists spending ( _id integer primary key,name text, category text, sum real, month integer, week integer, day integer, date integer);" );
        db.execSQL("create table if not exists earning ( _id integer primary key,name text, sum real, month integer, week integer, day integer, date integer);" );

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS spending");
        db.execSQL("DROP TABLE IF EXISTS earning");


        onCreate(db);
    }
}
