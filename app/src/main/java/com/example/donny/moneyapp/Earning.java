package com.example.donny.moneyapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.donny.moneyapp.DBHelper.KEY_CATEGORY;
import static com.example.donny.moneyapp.DBHelper.KEY_DATE;
import static com.example.donny.moneyapp.DBHelper.KEY_DAY;
import static com.example.donny.moneyapp.DBHelper.KEY_MONTH;
import static com.example.donny.moneyapp.DBHelper.KEY_NAME;
import static com.example.donny.moneyapp.DBHelper.KEY_SUM;
import static com.example.donny.moneyapp.DBHelper.KEY_WEEK;
import static com.example.donny.moneyapp.Main_Activity.card;
import static java.lang.Long.valueOf;

public class Earning extends AppCompatActivity {

    EditText earnSum, earnName;
    SharedPreferences prefs = null;
    DBHelper dbHelper;
    SQLiteDatabase database;
    long currentWeek, currentMonth, currentDay, currentDate;


    @Override
    public void onCreate(@Nullable final Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.direction_of_earning);

        prefs = getSharedPreferences("com.donny.MoneyApp", MODE_PRIVATE);
        prefs.contains("com.donny.MoneyApp");

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        checktime();

        earnName = findViewById(R.id.earnName);
        earnSum = findViewById(R.id.earnSum);
        Button earnEnd = findViewById(R.id.earnEnd);





        earnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((earnName.getText().toString().length() == 0) || (earnSum.getText().toString().length() == 0)
                        || (earnName.getText().toString().length() >20) || (earnSum.getText().toString().length() >20)) {
                    Toast.makeText(Earning.this,"Введите верные значения",Toast.LENGTH_SHORT ).show();
                }
                else {
                    showCardCashDialog();
                }

            }
        });


    }


    public void showCardCashDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Earning.this);
        builder.setMessage("В какой форме деньги?")
                .setCancelable(true)
                .setPositiveButton("безналичные", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        card+=Float.parseFloat(earnSum.getText().toString());

                        ContentValues contentValues = new ContentValues();

                        database.beginTransaction();
                        try {

                            contentValues.put(KEY_NAME, earnName.getText().toString());
                            contentValues.put(KEY_SUM, earnSum.getText().toString());
                            contentValues.put(KEY_WEEK, currentWeek);
                            contentValues.put(KEY_MONTH, currentMonth);
                            contentValues.put(KEY_DAY, currentDay);
                            contentValues.put(KEY_DATE, currentDate);


                            database.insert(DBHelper.TABLE_NAME2, null, contentValues);

                            database.setTransactionSuccessful();
                        } finally {

                            database.endTransaction();
                        }

                        prefs.edit().putFloat("savedCash", Main_Activity.card).apply();
                        Intent intent = new Intent(Earning.this,Main_Activity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("наличные", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Main_Activity.cash+=Float.parseFloat(earnSum.getText().toString());

                        ContentValues contentValues = new ContentValues();

                        database.beginTransaction();
                        try {

                            contentValues.put(KEY_NAME, earnName.getText().toString());
                            contentValues.put(KEY_SUM, earnSum.getText().toString());
                            contentValues.put(KEY_WEEK, currentWeek);
                            contentValues.put(KEY_MONTH, currentMonth);
                            contentValues.put(KEY_DAY, currentDay);
                            contentValues.put(KEY_DATE, currentDate);

                            database.insert(DBHelper.TABLE_NAME2, null, contentValues);
                            database.setTransactionSuccessful();

                        } finally {
                            database.endTransaction();

                        }

                        prefs.edit().putFloat("savedCash", Main_Activity.cash).apply();
                        Intent intent = new Intent(Earning.this,Main_Activity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void checktime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("w");
        currentWeek = valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("M");
        currentMonth = valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("D");
        currentDate = valueOf(timeFormat.format(calendar.getTime()));

        currentDay = valueOf(calendar.get(Calendar.DAY_OF_WEEK))-1;

    }




}

