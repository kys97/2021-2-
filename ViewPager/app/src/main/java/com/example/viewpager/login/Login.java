package com.example.viewpager.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viewpager.MainActivity;
import com.example.viewpager.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditText edt_id = findViewById(R.id.edt_login_id);
        EditText edt_pw = findViewById(R.id.edt_login_pw);
        Button join = findViewById(R.id.btn_login_join);
        Button login = findViewById(R.id.btn_login_login);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Join.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edt_id.getText().toString();
                String pw = edt_pw.getText().toString();

                if(id.matches(""))
                    Toast.makeText(getApplicationContext(), "ID를 입력해주세요.",Toast.LENGTH_SHORT).show();
                else{
                    //서버연결 후 아이디 확인
                    //비번 확인
                    //로그인가능하면
                }
                //앱내에 자동로그인 기록 남기기
                SharedPreferences data = getSharedPreferences("login_data", Activity.MODE_PRIVATE);
                SharedPreferences.Editor data_input = data.edit();

                data_input.putBoolean("LoginCheck", true);
                data_input.putString("LoginID",id);
                data_input.putString("LoginPW",pw);

                data_input.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
