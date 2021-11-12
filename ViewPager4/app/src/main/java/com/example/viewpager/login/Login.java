package com.example.viewpager.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viewpager.MainActivity;
import com.example.viewpager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean idCheck = false;
    boolean pwCheck = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditText edt_id = findViewById(R.id.edt_login_id);
        EditText edt_pw = findViewById(R.id.edt_login_pw);
        Button join = findViewById(R.id.btn_login_join);
        Button login = findViewById(R.id.btn_login_login);

        edt_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edt_id.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        edt_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edt_pw.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

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
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.",Toast.LENGTH_SHORT).show();
                else if(pw.matches(""))
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                else{
                    //서버연결 후 아이디 확인
                    db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    if(document.getData().get("id").toString().matches(id)) {
                                        idCheck = true;
                                        if(document.getData().get("pw").toString().matches(pw)) {
                                            pwCheck = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    });


                    if(!idCheck || !pwCheck){
                        if (!idCheck) {
                            Toast.makeText(getApplicationContext(),"존재하지 않는 아이디 입니다.",Toast.LENGTH_LONG).show();
                            edt_id.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        }else if(!pwCheck){
                            Toast.makeText(getApplicationContext(),"존재하지 않는 비밀번호 입니다.",Toast.LENGTH_LONG).show();
                            edt_pw.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        }
                    }
                    else{
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
                }

            }
        });
    }
}
