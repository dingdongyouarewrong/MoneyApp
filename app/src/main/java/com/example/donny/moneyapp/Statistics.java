package com.example.donny.moneyapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;


import static com.example.donny.moneyapp.DBHelper.KEY_CATEGORY;
import static com.example.donny.moneyapp.DBHelper.KEY_MONTH;
import static com.example.donny.moneyapp.DBHelper.KEY_NAME;
import static com.example.donny.moneyapp.DBHelper.KEY_SUM;
import static com.example.donny.moneyapp.DBHelper.KEY_WEEK;
import static com.example.donny.moneyapp.DBHelper.TABLE_NAME;
import static java.lang.String.valueOf;

public class Statistics extends Activity {

    long currentWeek, currentMonth;
    DBHelper dbHelper;
    double spentThisMonthSum = 0;
    double spentThisWeekSum = 0;
    double spentThisDaySum = 0;
    double[] days = new double[7];


    @Override
    protected void onCreate(@Nullable Bundle savedInstantService) {
        super.onCreate(savedInstantService);
        setContentView(R.layout.statistics);
        checktime();
        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getReadableDatabase();


        TextView spentThisWeek = findViewById(R.id.spending_week_sum);
        TextView spentThisMonth = findViewById(R.id.spending_month_sum);
        TextView elementName = findViewById(R.id.element_name);
        TextView elementSum = findViewById(R.id.element_sum);
        TextView elementCategory = findViewById(R.id.element_category);
        TextView elementSumEarn = findViewById(R.id.element_getSum);
        TextView elementNameEarn = findViewById(R.id.element_getName);
        TextView earnThisWeek = findViewById(R.id.weekGetSum);
        TextView earnThisMonth = findViewById(R.id.mothGetSum);







////////////////////////////////////////////////////////////////////////////////////



        String queryMonth = "SELECT * FROM spending WHERE month=" + currentMonth+";";

        String queryWeek = "SELECT * FROM spending WHERE week=" + currentWeek+";";

        try {
        database.beginTransaction();
        Cursor monthCursor = database.rawQuery(queryMonth, null);
        monthCursor.moveToFirst();

            for (int i=0;i<monthCursor.getCount();i+=1) {
                int index = monthCursor.getColumnIndex(KEY_SUM);
                spentThisMonthSum += monthCursor.getFloat(index);
                monthCursor.moveToNext();
            }
            monthCursor.close();
            spentThisMonth.setText(String.valueOf(spentThisMonthSum));
            database.setTransactionSuccessful();
        }

        catch (Exception e) {
            spentThisMonth.setText("0");
        }

        finally {
            database.endTransaction();
        }

        try {
            database.beginTransaction();
            Cursor weekCursor = database.rawQuery(queryWeek, null);
            weekCursor.moveToFirst();

            for (int i=0;i<weekCursor.getCount();i+=1) {
                int index = weekCursor.getColumnIndex(KEY_SUM);
                spentThisWeekSum += weekCursor.getFloat(index);
                weekCursor.moveToNext();
            }
            weekCursor.close();
            spentThisWeek.setText(String.valueOf(spentThisWeekSum));
            database.setTransactionSuccessful();

        }
        catch (Exception e) {
            spentThisWeek.setText("0");
        }
        finally {
            database.endTransaction();
        }

        String queryElement = "SELECT * FROM spending;";
        try {
            database.beginTransaction();
            Cursor cursorElement = database.rawQuery(queryElement, null);
            cursorElement.moveToLast();
            int index = cursorElement.getColumnIndex(KEY_NAME);
            elementName.setText(String.valueOf(cursorElement.getString(index)));
            index = cursorElement.getColumnIndex(KEY_SUM);
            elementSum.setText(String.valueOf(cursorElement.getDouble(index)));
            index = cursorElement.getColumnIndex(KEY_CATEGORY);
            elementCategory.setText(String.valueOf(cursorElement.getString(index)));
            database.setTransactionSuccessful();

        }
        catch (Exception e) {
            elementName.setText("Не найдено");
            elementSum.setText("0");
            elementCategory.setText("Не найдено");
        }
        finally {
            database.endTransaction();

        }

        //////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////
        String queryEarningMonth = "SELECT * FROM earning WHERE month=" + currentMonth+";";

        String queryEarningWeek = "SELECT * FROM earning WHERE week=" + currentWeek+";";


        try {
            database.beginTransaction();
            Cursor monthCursor = database.rawQuery(queryEarningMonth, null);
            monthCursor.moveToFirst();

            for (int i=0;i<monthCursor.getCount();i+=1) {
                int index = monthCursor.getColumnIndex(KEY_SUM);
                spentThisMonthSum += monthCursor.getDouble(index);
                monthCursor.moveToNext();
            }
            monthCursor.close();
            earnThisMonth.setText(String.valueOf(spentThisMonthSum));
            database.setTransactionSuccessful();
        }

        catch (Exception e) {
            spentThisMonth.setText("0");
        }

        finally {
            database.endTransaction();
        }

        try {
            database.beginTransaction();
            Cursor weekCursor = database.rawQuery(queryEarningWeek, null);
            weekCursor.moveToFirst();

            for (int i=0;i<weekCursor.getCount();i+=1) {
                int index = weekCursor.getColumnIndex(KEY_SUM);
                spentThisWeekSum += weekCursor.getDouble(index);
                weekCursor.moveToNext();
            }
            weekCursor.close();
            earnThisWeek.setText(String.valueOf(spentThisWeekSum));
            database.setTransactionSuccessful();

        }
        catch (Exception e) {
            earnThisWeek.setText("0");
        }
        finally {
            database.endTransaction();
        }

        String queryEarn = "SELECT * FROM earning;";
        try {
            database.beginTransaction();
            Cursor cursorElement = database.rawQuery(queryEarn, null);
            cursorElement.moveToLast();
            int index = cursorElement.getColumnIndex(KEY_NAME);
            elementNameEarn.setText(String.valueOf(cursorElement.getString(index)));
            index = cursorElement.getColumnIndex(KEY_SUM);
            elementSumEarn.setText(String.valueOf(cursorElement.getDouble(index)));
            database.setTransactionSuccessful();

        }
        catch (Exception e) {
            elementNameEarn.setText("Не найдено");
            elementSumEarn.setText("0");
        }
        finally {
            database.endTransaction();

        }



            //Toast.makeText(Statistics.this,valueOf(spentThisMonthSum).toString(),Toast.LENGTH_LONG ).show();

    }

    protected void checktime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("w");
        currentWeek = Long.valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("M");
        currentMonth = Long.valueOf(timeFormat.format(calendar.getTime()));


    }

    public void onClickEarn(View view) {
        Intent intent = new Intent(Statistics.this,earnList.class);
        startActivity(intent);
    }

    public void onClickSpent(View view) {
        Intent intent = new Intent(Statistics.this,spendList.class);
        startActivity(intent);
    }


}
