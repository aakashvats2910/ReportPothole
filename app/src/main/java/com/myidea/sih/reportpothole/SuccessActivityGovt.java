package com.myidea.sih.reportpothole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SuccessActivityGovt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_govt);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SuccessActivityGovt.this, GovtInsideActivity.class));
                    }
                },
                2000
        );
    }
}
