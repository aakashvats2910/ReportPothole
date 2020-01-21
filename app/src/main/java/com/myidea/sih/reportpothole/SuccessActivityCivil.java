package com.myidea.sih.reportpothole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SuccessActivityCivil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_civil);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SuccessActivityCivil.this, CivilInsideActivity.class));
                    }
                },
                2000
        );
    }
}
