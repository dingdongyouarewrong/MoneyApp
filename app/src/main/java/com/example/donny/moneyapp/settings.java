package com.example.donny.moneyapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;


public class settings extends AppCompatActivity {

    public static int value;
    public static int daysPlus;
    Switch switchNotify;
    SharedPreferences prefs = null;
    RadioGroup group;
    RadioButton radio;
    String dollarsText, euroText, rublesText, grivensText;
    boolean notify;
    NotificationManager nm;

   String[] numbers = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
    @Override
    public void onCreate(@Nullable Bundle SavedInstantService ) {
        super.onCreate(SavedInstantService);
        setContentView(R.layout.settings);

        prefs = getSharedPreferences("com.donny.MoneyApp", MODE_PRIVATE);
        prefs.contains("com.donny.MoneyApp");

        nm = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        dollarsText = getResources().getString(R.string.Dollars);
        rublesText = getResources().getString(R.string.Rubles);
        grivensText = getResources().getString(R.string.Grivens);
        euroText = getResources().getString(R.string.Euro);

        switchNotify = findViewById(R.id.switchNotify);
        RadioButton rubles = findViewById(R.id.radioRuble);
        RadioButton euro = findViewById(R.id.radioEuro);
        RadioButton dollars = findViewById(R.id.radioDollars);
        RadioButton grivens = findViewById(R.id.radioGrivens);
        group = findViewById(R.id.radiogroup);

        final Spinner day = findViewById(R.id.spinner);
        final TextView dayText = findViewById(R.id.day);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.textview, numbers);
        dataAdapter.setDropDownViewResource(R.layout.textview);
        day.setAdapter(dataAdapter);

        day.setSelection(prefs.getInt("plusDaysNow",0));

        notify = prefs.getBoolean("notify",true);

        if (notify)
        switchNotify.toggle();

        switchNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (notify) {
                    prefs.edit().putBoolean("notify", false).apply();
                    notify=!notify;
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (manager != null) {
                        manager.cancel(228);
                    }

                }
                else {
                    prefs.edit().putBoolean("notify", true).apply();
                    notify=!notify;
                    showNotification();
                }
            }
        });

        final int daysMinus = prefs.getInt("minusDays",0);
        day.setSelection(daysMinus);//setText(String.valueOf(daysMinus+1));


        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {




                SharedPreferences.Editor editor = prefs.edit();

                daysPlus = position;
//                Toast.makeText(settings.this,String.valueOf(daysPlus) ,Toast.LENGTH_LONG ).show();//потом пригодится


                editor.putInt("minusDays", daysPlus).apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        dollars.setText(dollarsText);
        rubles.setText(rublesText);
        grivens.setText(grivensText);
        euro.setText(euroText);



        switch (value) {
            case 1:rubles.setChecked(true);break;
            case 2:dollars.setChecked(true);break;
            case 3:euro.setChecked(true);break;
            case 4:grivens.setChecked(true);break;

        }




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



    public void onClickRadio(View view) {
        int radioId = group.getCheckedRadioButtonId();
        radio = findViewById(radioId);
        SharedPreferences.Editor editor = prefs.edit();

        switch (radioId) {
            case R.id.radioRuble: editor.putInt("currency",1).apply();break;
            case R.id.radioDollars: editor.putInt("currency",2).apply();break;
            case R.id.radioEuro: editor.putInt("currency", 3).apply();break;
            case R.id.radioGrivens: editor.putInt("currency", 4).apply();break;

        }


    }
}
