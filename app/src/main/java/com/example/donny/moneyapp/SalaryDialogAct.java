package com.example.donny.moneyapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Long.valueOf;

public class SalaryDialogAct extends Activity {

    SharedPreferences prefs = null;

    public static long salary;
    EditText userSalary;
    @Override
    public void onCreate(@Nullable Bundle savedInstantService) {
        prefs = getSharedPreferences("com.donny.MoneyApp", MODE_PRIVATE);
        super.onCreate(savedInstantService);
        setContentView(R.layout.salary_dialog);
        

        final EditText userSalary = (EditText) findViewById(R.id.salary);
        final EditText cash = findViewById(R.id.inCash);
        final EditText card = findViewById(R.id.inCard);

        Button ok = (Button) findViewById(R.id.okk);



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((userSalary.getText().toString().length()==0) ||
                        (card.getText().toString().length() == 0) ||
                        (cash.getText().toString().length() == 0) ||
                        (userSalary.getText().toString().length()>5) ||
                        (card.getText().toString().length() >5) ||
                        (cash.getText().toString().length() > 5))  {
                    Toast.makeText(SalaryDialogAct.this, "Введите правильные значения, пожалуйста!",Toast.LENGTH_SHORT ).show();

                }
                else {



                Main_Activity.salary = valueOf(userSalary.getText().toString());
                Main_Activity.card = valueOf(card.getText().toString());
                Main_Activity.cash = valueOf(cash.getText().toString());
                //Main_Activity.balance = Main_Activity.card + Main_Activity.cash;
                SharedPreferences.Editor editor = prefs.edit();


                editor.putFloat("savedCash", Main_Activity.cash).apply();
                editor.putFloat("savedCard", Main_Activity.card).apply();
                editor.putFloat("savedSalary", Main_Activity.salary).apply();
                prefs.edit().putBoolean("firstrun", false).apply();
                Intent intent = new Intent(SalaryDialogAct.this,Main_Activity.class);
                startActivity(intent);
                }

            }
        });

    }

}
