package com.example.donny.moneyapp;



import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.donny.moneyapp.DBHelper.KEY_DATE;
import static com.example.donny.moneyapp.DBHelper.KEY_DAY;
import static com.example.donny.moneyapp.DBHelper.KEY_MONTH;
import static com.example.donny.moneyapp.DBHelper.KEY_NAME;
import static com.example.donny.moneyapp.DBHelper.KEY_SUM;
import static com.example.donny.moneyapp.DBHelper.KEY_WEEK;
import static com.example.donny.moneyapp.settings.daysPlus;
import static java.lang.Long.valueOf;



public class Main_Activity extends AppCompatActivity {

    int currentWeek;
    int currentMonth;
    int currentDay;
    int currentHour;
    int currentMin;
    int currentSec;
    Long currentDate;
    DBHelper dbHelper;
    SQLiteDatabase database;
    boolean isStartTimer = true;
    NotificationManager nm;

    SharedPreferences prefs = null;
    SharedPreferences.Editor editor;
    static float salary, cash, card;
    float balance;
    static int dateOfSalary;
    TextView timer, before;
    LinearLayout.LayoutParams cashParams, cardParams;
    long timeLeftMillis;
    int days;
    boolean notify;
    //int plusDays;
    String dollarsText,rublesText,grivensText, euroText;

    @SuppressLint("SetTextI18n")
    @Override


    public void onCreate(@Nullable Bundle savedInstantService) {
        super.onCreate(savedInstantService);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        nm = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Toast.makeText(Main_Activity.this,valueOf((long) salary).toString(),Toast.LENGTH_LONG ).show();//потом пригодится

        Typeface typefaceMain = ResourcesCompat.getFont(Main_Activity.this, R.font.rob);
        Typeface typefaceBeforeSalary = ResourcesCompat.getFont(Main_Activity.this, R.font.avenir);

        prefs = getSharedPreferences("com.donny.MoneyApp", MODE_PRIVATE);
        prefs.contains("com.donny.MoneyApp");
        SharedPreferences.Editor editor = prefs.edit();


        checkFirstRun();
        checktime();

        //checktime();

         dollarsText = getResources().getString(R.string.Dollars);
         rublesText = getResources().getString(R.string.Rubles);
         grivensText = getResources().getString(R.string.Grivens);
         euroText = getResources().getString(R.string.Euro);

        int currency = prefs.getInt("currency",1);

        cash = prefs.getFloat("savedCash",0);
        prefs.edit().putFloat("savedCash", cash).apply();
        card = prefs.getFloat("savedCard",0);
        prefs.edit().putFloat("savedCard", card).apply();

        notify = prefs.getBoolean("notify",true);

        balance = cash+card;

        Button toShop = findViewById(R.id.inShop);
        Button earnMoneyButton = findViewById(R.id.getMoneyButton);
        Button spendMoneyButton = findViewById(R.id.spendMoneyButton);
        Button atmButton = findViewById(R.id.ATM);
        Button settingsButton = findViewById(R.id.settings);
        TextView cashtext = findViewById(R.id.cash);
        TextView cardtext = findViewById(R.id.card);
        TextView cashnumber = findViewById(R.id.cashnumber);
        TextView cardnumber = findViewById(R.id.cardnumber);
        View cashline = findViewById(R.id.redline);
        View cardline = findViewById(R.id.blueline);
        TextView currencyText = findViewById(R.id.currency);
        TextView userBalance = findViewById(R.id.balance);
        Button statisticsButton = findViewById(R.id.statisticsButton);
        timer = findViewById(R.id.beforeSalaryLeft);
        before = findViewById(R.id.beforeSalary);
        StartTimer();


        if (notify) {
            showNotification();
        }

        int cypher = (int)balance % 100;


        String buf = currencyWord(currency, cypher);
        currencyText.setText(buf);


        cashtext.setTypeface(typefaceMain);
        cardtext.setTypeface(typefaceMain);
        currencyText.setTypeface(typefaceMain);

        userBalance.setTypeface(typefaceMain);
        before.setTypeface(typefaceBeforeSalary);
        timer.setTypeface(typefaceBeforeSalary);

        userBalance.setText(Float.toString(balance));
        cardnumber.setText(Float.toString(cash));//веса работают наоборот
        cashnumber.setText(Float.toString(card));//веса работают наоборот
        userBalance.setText(Float.toString(balance));



        cashParams = (LinearLayout.LayoutParams) cashline.getLayoutParams();
        cardParams = (LinearLayout.LayoutParams) cardline.getLayoutParams();
        cashParams.weight = cash;
        cardParams.weight = card;


        toShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this,inShop.class);
                startActivity(intent);
            }
        });

        earnMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Earning.class);
                startActivity(intent);
            }
        });


        spendMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Spending.class);
                startActivity(intent);
            }
        });

        atmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showATMDialog();
            }
        });

        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this,daysstat.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, settings.class);
                startActivity(intent);
            }
        });
    }

    public void showATMDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main_Activity.this);
        builder.setMessage("Что вы сделали?:")
                .setCancelable(true)
                .setPositiveButton("я снял деньги", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Main_Activity.this,ATMDialog.class);
                        intent.putExtra("cashCard", false);

                        startActivity(intent);

                    }
                })
                .setNegativeButton("я положил деньги на счет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Main_Activity.this,ATMDialog.class);
                        intent.putExtra("cashCard", true);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void checktime() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat timeFormat = new SimpleDateFormat("w");
        currentWeek = Integer.valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("M");
        currentMonth = Integer.valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("d");
        currentDay = Integer.valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("M");
        currentMonth = Integer.valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("H");
        currentHour = Integer.valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("s");
        currentSec = Integer.valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("m");
        currentMin = Integer.valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("yy");
        long year = (valueOf(timeFormat.format(calendar.getTime())));
        currentDate = valueOf(timeFormat.format(calendar.getTime()));
        int days;
        if ((currentMonth==0) && (currentDay==0) && (currentHour==0) && (currentMin==0)) {
            this.deleteDatabase(DBHelper.DATABASE_NAME);
        }

        switch (currentMonth) {

            case 1: days = 31;
                break;

            case 3: days = 31;
                break;

            case 5: days = 31;
                break;

            case 7: days = 31;
                break;

            case 8: days = 31;
                break;

            case 10: days = 31;
                break;

            case 2: if ((year==2024) || (year==2044)) {
                days = 29;
            }
            else {
                days = 29;
            }
                break;

            default: days=30;
        }


        int i=0;
        i+=1;

        if (isStartTimer){
            StartTimer();
        } else {
            timeLeftMillis = 86400000 * days;
        }

        Toast.makeText(Main_Activity.this,"дней "+String.valueOf(days)+" дата "+String.valueOf(currentDate),Toast.LENGTH_LONG ).show();//потом пригодится
        timeLeftMillis = ((days - currentDate)*86400000) + (86400000 - (currentHour*3600000 + 60000*currentMin + 1000*currentSec));
        timeLeftMillis+= daysPlus*86400000;
    }

    protected void checkFirstRun() {
        if (prefs.getBoolean("firstrun", true)) {//если первый запуск - веселимся с окном
            Intent intent = new Intent(Main_Activity.this,startOneTime.class);
            startActivity(intent);
        }
    }


    public void StartTimer() {
        CountDownTimer mCountDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long untilFinished) {
                isStartTimer = true;
                prefs.edit().putLong("msLeft", timeLeftMillis).apply();
                timeLeftMillis = untilFinished;

                updateCountDownText();
            }

            @Override
            public void onFinish() {
                isStartTimer = false;
                //putValue();


                //startActivity(intent);
                checktime();
            }
        }.start();
    }

    private void updateCountDownText() {

        int days = (int) (timeLeftMillis / (60000*60*24));
        int hours = (int) (timeLeftMillis / 3600000)  % 24;
        int minutes = (int) (timeLeftMillis / 60000) % 60;
        int seconds = (int) (timeLeftMillis / 1000) % 60;
        String timeLeftFormat = String.format(Locale.getDefault(), "%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
        timer.setText((valueOf(days).toString()).concat(" д. ").concat(valueOf(hours).toString()).concat(" ч. ").concat(valueOf(minutes).toString()).concat(" мин. ").concat(valueOf(seconds).toString().concat(" сек.")));


    }

    public void showNotification() {
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), inShop.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Я в магазине")
                .setAutoCancel(true)
                .setOngoing(true)
                .setContentText("Нажмите, если вы сейчас в магазине");

        Notification notification = builder.build();
        nm.notify(228, notification);
    }

    public String currencyWord(int currency, int cypher) {


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

    public void putValue() {
        ContentValues contentValues = new ContentValues();

        database.beginTransaction();
        try {

            contentValues.put(KEY_NAME, "зарплата");
            contentValues.put(KEY_SUM, salary);
            contentValues.put(KEY_WEEK, currentWeek);
            contentValues.put(KEY_MONTH, currentMonth);
            contentValues.put(KEY_DAY, currentDay);
            contentValues.put(KEY_DATE, currentDate);

            database.insert(DBHelper.TABLE_NAME2, null, contentValues);
            database.setTransactionSuccessful();

        } finally {
            database.endTransaction();

        }
        database.close();

//                }

    }

}
