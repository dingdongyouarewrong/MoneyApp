package com.example.donny.moneyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Long.valueOf;

public class ATMDialog extends AppCompatActivity{



    public boolean noncash;
    SharedPreferences prefs = null;


    @Override
    public void onCreate(@Nullable final Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.tocard);
        prefs = getSharedPreferences("com.donny.MoneyApp", MODE_PRIVATE);
        prefs.contains("com.donny.MoneyApp");

        Intent intent = getIntent();
        noncash = intent.getBooleanExtra("cashCard",false);

        final EditText sumUser = findViewById(R.id.cashtocard);
        TextView description = findViewById(R.id.descript);
        Button confirmButton = findViewById(R.id.confirm);

        if (noncash) {
            description.setText("Сколько денег вы положили на счет");
        }
        else {
            description.setText("Сколько денег вы сняли");
        }




        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.getBooleanExtra("cashCard",false);
                if ((sumUser.getText().toString().length() == 0) || (sumUser.getText().toString().length() == 0)
                        || (sumUser.getText().toString().length() >20) || (sumUser.getText().toString().length() >20)) {

                    Toast.makeText(ATMDialog.this,"Введите верное значение",Toast.LENGTH_SHORT ).show();
                }
                else {

                    if (noncash) { //если тру - положили деньги

                        Main_Activity.card+=Float.parseFloat(sumUser.getText().toString());
                        Main_Activity.cash-=Float.parseFloat(sumUser.getText().toString());
                        prefs.edit().putFloat("savedCash", Main_Activity.cash).apply();
                        prefs.edit().putFloat("savedCard", Main_Activity.card).apply();
                        Intent intentStart = new Intent(ATMDialog.this,Main_Activity.class);
                        startActivity(intentStart);

                    }

                    else { // иначе сняли

                        Main_Activity.card -= Float.parseFloat(sumUser.getText().toString());
                        Main_Activity.cash += Float.parseFloat(sumUser.getText().toString());
                        prefs.edit().putFloat("savedCash", Main_Activity.cash).apply();
                        prefs.edit().putFloat("savedCard", Main_Activity.card).apply();
                        Intent intentStart = new Intent(ATMDialog.this, Main_Activity.class);
                        startActivity(intentStart);
                        finish();


                    }
                }

            }
        });


    }



}

