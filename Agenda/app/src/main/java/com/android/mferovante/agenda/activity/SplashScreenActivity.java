package com.android.mferovante.agenda.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.mferovante.agenda.R;
import com.android.mferovante.agenda.creative.CreatorCandidatos;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //CreatorCandidatos creatorCandidatos = new CreatorCandidatos(getApplicationContext());
        //creatorCandidatos.creation(200);
        Handler handle = new Handler();

        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);


    }
}
