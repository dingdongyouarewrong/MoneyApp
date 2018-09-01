package com.example.donny.moneyapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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

public class Spending extends AppCompatActivity {

    public boolean isTouched = true;
    public EditText spendSum,spendName;
    SharedPreferences prefs = null;
    DBHelper dbHelper;
    SQLiteDatabase database;
    long currentWeek, currentMonth, currentDate, currentDay;
    AutoCompleteTextView  spendCategory;


    String[] categories = {"пищевые продукты", "одежда", "обувь", "аксессуары","косметика", "бытовая химия", "техника, электроника", "товары для дома", "спорт и отдых", "подарки", "другое"};
    @Override
    public void onCreate(@Nullable final Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.direction_of_spending);

        prefs = getSharedPreferences("com.donny.MoneyApp", MODE_PRIVATE);
        prefs.contains("com.donny.MoneyApp");

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        checktime();




        spendName = findViewById(R.id.spendName);
        spendSum = findViewById(R.id.spendSum);
        spendCategory = findViewById(R.id.spendCategory);
        Button spendEnd = findViewById(R.id.spendEnd);
        spendCategory.setThreshold(0);
        //String[] words =getResources().getStringArray(R.array.auto_complete);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.textview, categories);
        spendCategory.setAdapter(adapter);




        spendEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((spendName.getText().toString().length() == 0) || (spendSum.getText().toString().length() == 0)
                        || (spendCategory.getText().toString().length() == 0) || (spendName.getText().toString().length() >20) || (spendSum.getText().toString().length() >20)
                        || (spendCategory.getText().toString().length() >20)) {
                    Toast.makeText(Spending.this,"Введите верные значения",Toast.LENGTH_SHORT ).show();
                }
                else {
                    showCardCashDialog();
                }
            }
        });


    }


    public void showCardCashDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Spending.this);
        builder.setMessage("чем вы расплатились?")
                .setCancelable(true)
                .setPositiveButton("картой", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        card-=valueOf(spendSum.getText().toString());
                        putThings();
                        prefs.edit().putFloat("savedCash", Main_Activity.card).apply();
                        Intent intent = new Intent(Spending.this,Main_Activity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("наличными", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Main_Activity.cash-=valueOf(spendSum.getText().toString());
                        putThings();
                        prefs.edit().putFloat("savedCash", Main_Activity.cash).apply();
                        Intent intent = new Intent(Spending.this,Main_Activity.class);
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

    protected void putThings() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME,spendName.getText().toString());
        contentValues.put(KEY_SUM, valueOf(spendSum.getText().toString()));
        contentValues.put(KEY_CATEGORY, spendCategory.getText().toString());
        contentValues.put(KEY_WEEK, currentWeek);
        contentValues.put(KEY_MONTH, currentMonth);
        contentValues.put(KEY_DAY, currentDay);
        contentValues.put(KEY_DATE, currentDate);
        database.insert(DBHelper.TABLE_NAME, null, contentValues);

    }


}
