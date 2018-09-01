package com.example.donny.moneyapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


import static com.example.donny.moneyapp.DBHelper.KEY_CATEGORY;
import static com.example.donny.moneyapp.DBHelper.KEY_DAY;
import static com.example.donny.moneyapp.DBHelper.KEY_NAME;
import static com.example.donny.moneyapp.DBHelper.KEY_SUM;
import static java.lang.Long.valueOf;

public class daysstat extends AppCompatActivity {

    DBHelper dbHelper;
    double spentThisDaySum = 0;
    double earnThisDaySum = 0;
    TextView spentThisDayView, earnThisDay, stat;
    double[] days = new double[7];

    long currentMonth, currentDay, currentWeek;

    float day;
    long currentDate;
    double balance;
    TextView currencyText;


    class StatOne {
        String name;
        String category;
        double sum;

        StatOne(String name, String category, double sum) {
            this.name = name;
            this.category = category;
            this.sum = sum;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstantService) {
        super.onCreate(savedInstantService);
        setContentView(R.layout.daysstat);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        checktime();

        spentThisDayView = findViewById(R.id.spentThisDay);
        earnThisDay = findViewById(R.id.earnThisDay);
        stat = findViewById(R.id.daystat);
        currencyText = findViewById(R.id.currencyDay);

        Toast.makeText(daysstat.this,valueOf((long) currentDate).toString(),Toast.LENGTH_LONG ).show();//потом пригодится



        Button weekstat = findViewById(R.id.weekStat);
        weekstat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(daysstat.this, Statistics.class);
                startActivity(intent);
            }
        });

        int textColor = getResources().getColor(R.color.greenText);


        String queryDays = "SELECT * FROM spending WHERE date="+currentDate+";";



        Cursor dayCursor = database.rawQuery(queryDays, null);
        database.beginTransaction();
        dayCursor.moveToFirst();

        ItemsAdapter adapter = new ItemsAdapter();
        ListView list = findViewById(R.id.statListView);
        list.setAdapter(adapter);


        try {


            for (int i=0;i<dayCursor.getCount();i+=1) {

                int index = dayCursor.getColumnIndex(KEY_SUM);
                spentThisDaySum += dayCursor.getDouble(index);

                dayCursor.moveToNext();
            }
            dayCursor.close();

            database.setTransactionSuccessful();
            spentThisDayView.setText(String.valueOf(spentThisDaySum));
        }

        catch (Exception e) {

            spentThisDayView.setText("0");
        }

        finally {

            database.endTransaction();
        }

        String queryEDays = "SELECT * FROM earning WHERE date="+currentDate+";";



        dayCursor = database.rawQuery(queryEDays, null);

        database.beginTransaction();
        dayCursor.moveToFirst();


        try {


            for (int i=0;i<dayCursor.getCount();i+=1) {

                int index = dayCursor.getColumnIndex(KEY_SUM);
                earnThisDaySum += dayCursor.getDouble(index);

                dayCursor.moveToNext();
            }

            dayCursor.close();

            database.setTransactionSuccessful();

            earnThisDay.setText(String.valueOf(earnThisDaySum));
        }

        catch (Exception e) {

            earnThisDay.setText("0");
        }

        finally {

            database.endTransaction();
        }
        balance = earnThisDaySum-spentThisDaySum;
        stat.setText(String.valueOf(balance));
        if (balance>0) {
            stat.setTextColor(textColor);
        }

        String query = "SELECT * FROM spending WHERE date="+currentDate+";";
        Cursor itemCursor = database.rawQuery(query, null);
        itemCursor.moveToFirst();
        for (int i=0;i<itemCursor.getCount();i+=1) {
            int index = itemCursor.getColumnIndex(KEY_SUM);
            double itemSum = itemCursor.getDouble(index);
            index = itemCursor.getColumnIndex(KEY_NAME);
            String itemName = (String.valueOf(itemCursor.getString(index)));
            index = itemCursor.getColumnIndex(KEY_CATEGORY);
            String itemCategory = itemCursor.getString(index);
            StatOne d = new StatOne(itemName, itemCategory,itemSum);
            itemCursor.moveToNext();

            adapter.add(d);


        }
        itemCursor.close();

        if (balance<0) {
            balance*=-1;
        }
        int cypher = (int)balance % 100;

        String buf = currencyWord(settings.value, cypher, balance);
        currencyText.setText(buf);

    }

    private class ItemsAdapter extends ArrayAdapter<StatOne> {

        public ItemsAdapter() {
            super(daysstat.this, R.layout.spending_element);
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final View view = getLayoutInflater().inflate(R.layout.spending_element, null);
            StatOne stOne = getItem(position);
            ((TextView)view.findViewById(R.id.sp_name)).setText(stOne.name);
            ((TextView)view.findViewById(R.id.sp_sum)).setText(String.valueOf(stOne.sum));
            ((TextView)view.findViewById(R.id.sp_category)).setText(stOne.category);
            return view;
        }



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

    public static String currencyWord(int currency, int cypher, double balance) {


        String currencyText = null;


        if (currency==1) {
            if ((cypher<20) && (cypher > 10)) {
                currencyText = "Рублей";
            }
            else {
                cypher = (int)balance % 10;
                if (cypher == 1) {
                    currencyText = "Рубль";
                } else if ((cypher > 1) && (cypher < 5)) {
                    currencyText = "Рубля";
                } else if ((cypher == 0) || (cypher >= 5)) {
                    currencyText= ("Рублей");
                }

            }
            settings.value = 1;

        }
        if (currency==2) {
            if ((cypher<20) && (cypher > 10)){
                currencyText = "Долларов";
            }
            else {
                cypher = (int)balance % 10;

                if (cypher == 1) {
                    currencyText = "Доллар";
                } else if ((cypher > 1) && (cypher < 5)) {
                    currencyText = "Доллара";
                } else if ((cypher >= 5) || (cypher == 0 )) {
                    currencyText = "Долларов";
                }

            }
            settings.value = 2;

        }
        if ((currency==3)) {
            currencyText = "Евро";
            settings.value = 3;
        }
        if ((currency==4)) {
            if ((cypher<20) && (cypher >= 10)) {
                currencyText = "Гривен";
            }
            else {
                cypher = (int)balance % 10;
                if (cypher == 1) {
                    currencyText = "Гривна";
                } else if ((cypher > 1) && (cypher < 5)) {
                    currencyText = "Гривны";
                } else if ((cypher >= 5) || (cypher == 0 )) {
                    currencyText = "Гривен";

                }
            }
            settings.value = 4;
        }




        return currencyText;
    }

}

