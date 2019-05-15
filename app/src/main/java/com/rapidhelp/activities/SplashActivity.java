package com.rapidhelp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rapidhelp.utilities.Constants;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences=getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);

        if(sharedPreferences.getBoolean(Constants.IS_LOGGED_IN,false)){
            intent=new Intent(SplashActivity.this,MainActivity.class);
        }else {
            intent=new Intent(SplashActivity.this,LoginActivity.class);
        }


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
                // overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        }, 2000);
    }
}
