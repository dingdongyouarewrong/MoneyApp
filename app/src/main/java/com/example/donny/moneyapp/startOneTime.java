package com.example.donny.moneyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class startOneTime extends Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceService) {
        super.onCreate(savedInstanceService);
        setContentView(R.layout.startfirst);
        Button letsstart  = findViewById(R.id.st);

        letsstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(startOneTime.this,SalaryDialogAct.class);
                startActivity(intent);
                finish();

            }
        });

    }



}
