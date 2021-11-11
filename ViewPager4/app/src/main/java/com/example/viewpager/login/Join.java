package com.example.viewpager.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viewpager.R;

public class Join extends AppCompatActivity {

    String gender = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        EditText edt_name = findViewById(R.id.edt_join_name);
        RadioGroup rg_gender = findViewById(R.id.rg_join_gender);
        EditText edt_id = findViewById(R.id.edt_join_id);
        EditText edt_pw = findViewById(R.id.edt_join_pw);
        EditText edt_pw_check = findViewById(R.id.edt_join_pw_check);
        EditText edt_phone = findViewById(R.id.edt_join_phone);
        DatePicker dt_birth = findViewById(R.id.dp_join_birth);
        Button btn_join = findViewById(R.id.btn_join_join);

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_join_gender_man: gender = "남성"; break;
                    case R.id.rb_join_gender_woman: gender = "여성"; break;
                }
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_name.getText().toString().matches(""))
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                else if(gender.matches(""))
                    Toast.makeText(getApplicationContext(), "성별을 선택해주세요.",Toast.LENGTH_SHORT).show();
                else if(!edt_pw.getText().toString().matches(edt_pw_check.getText().toString()))
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                else{
                    //핸드폰 번호 인증 - 파이어베이스 연동

                    //서버 저장 - 파이어베이스 연동
                }
            }
        });
    }
}
