package com.sevenpeakssoftware.fott.screens.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.sevenpeakssoftware.fott.R;
import com.sevenpeakssoftware.fott.screens.feeds.feeds.FeedsActivity;

import java.util.concurrent.TimeUnit;

public class SplashScreenActivity extends AppCompatActivity {

    private static long SPLASH_TIME_OUT = TimeUnit.SECONDS.toMillis(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                ActivityCompat.startActivity(SplashScreenActivity.this, new Intent(SplashScreenActivity.this,
                        FeedsActivity.class), null);
                overridePendingTransition(0, R.anim.fadeout);
            }
        }, SPLASH_TIME_OUT);
    }
}