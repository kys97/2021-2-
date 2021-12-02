package com.example.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viewpager.login.Login;

public class Loading extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        intent = new Intent(getApplicationContext(), MainActivity.class);
        SharedPreferences data = getSharedPreferences("login_data", Activity.MODE_PRIVATE);
        Boolean login_check = data.getBoolean("LoginCheck",false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //SharedPreferences 사용
                if(login_check) {
                    String login_mode = data.getString("LoginMode","");
                    if(login_mode.matches("Patient"))
                        intent = new Intent(getApplicationContext(), Patient.class);
                    else if(login_mode.matches("Protector"))
                        intent = new Intent(getApplicationContext(), CheckRoleActivity.class);
                }

                startActivity(intent);
            }
        }, 2000);
    }
}
