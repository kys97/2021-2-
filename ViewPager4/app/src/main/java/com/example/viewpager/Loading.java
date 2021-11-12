package com.example.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viewpager.login.Login;

public class Loading extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        SharedPreferences data = getSharedPreferences("login_data", Activity.MODE_PRIVATE);
        Boolean login_check = data.getBoolean("LoginCheck",false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //SharedPreferences 사용
                Intent intent;
                if(login_check) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                else {
                    intent = new Intent(getApplicationContext(), Login.class);
                }

                startActivity(intent);
            }
        }, 2000);
    }
}
