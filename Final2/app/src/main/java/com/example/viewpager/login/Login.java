package com.example.viewpager.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viewpager.CheckRoleActivity;
import com.example.viewpager.MainActivity;
import com.example.viewpager.Patient;
import com.example.viewpager.R;
import com.example.viewpager.RandomCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Timer;

public class Login extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences data;
    boolean idCheck = false;
    boolean pwCheck = false;
    String pk, id, pw;
    EditText edt_id, edt_pw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edt_id = findViewById(R.id.edt_login_id);
        edt_pw = findViewById(R.id.edt_login_pw);
        Button join = findViewById(R.id.btn_login_join);
        Button login = findViewById(R.id.btn_login_login);

        data = getSharedPreferences("login_data", Activity.MODE_PRIVATE);

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
                edt_id.setText("");
                edt_pw.setText("");

                Intent intent = new Intent(getApplicationContext(), Join.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idCheck = false;
                pwCheck = false;
                id = edt_id.getText().toString();
                pw = edt_pw.getText().toString();

                if(id.matches(""))
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.",Toast.LENGTH_SHORT).show();
                else if(pw.matches(""))
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                else{
                    String databaseName = data.getString("LoginMode","");

                    //서버연결 후 아이디 확인
                    String login_mode = data.getString("LoginMode","");
                    db.collection(login_mode).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    if(document.getData().get("id").toString().matches(id)) {
                                        idCheck = true;
                                        if(document.getData().get("pw").toString().matches(pw)) {
                                            pwCheck = true;

                                            //앱내에 자동로그인 기록 남기기
                                            SharedPreferences.Editor data_input = data.edit();
                                            data_input.putBoolean("LoginCheck", true);
                                            data_input.putString("LoginID",id);
                                            data_input.putString("LoginPW",pw);

                                            db.collection(login_mode).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for(QueryDocumentSnapshot document : task.getResult()){
                                                            if(document.getData().get("id").toString().matches(id)){
                                                                pk = document.getId();
                                                                break;
                                                            }
                                                        }

                                                        if(login_mode.matches("Patient")) {
                                                            data_input.putString("PrimaryKey",pk);
                                                            data_input.commit();
                                                            Intent intent = new Intent(getApplicationContext(), Patient.class);
                                                            startActivity(intent);
                                                        }
                                                        else if(login_mode.matches("Protector")) {
                                                            data_input.putString("PrimaryKey",pk);
                                                            data_input.commit();
                                                            Intent intent = new Intent(getApplicationContext(), CheckRoleActivity.class);
                                                            startActivity(intent);
                                                        }
                                                        else
                                                            Toast.makeText(getApplicationContext(),"로그인 오류", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                            if(!idCheck || !pwCheck){
                                                if (!idCheck) {
                                                    Log.e("로그인",String.valueOf(pwCheck));
                                                    Toast.makeText(getApplicationContext(),"존재하지 않는 아이디 입니다.",Toast.LENGTH_LONG).show();
                                                    edt_id.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                                                }else if(!pwCheck){
                                                    Toast.makeText(getApplicationContext(),"존재하지 않는 비밀번호 입니다.",Toast.LENGTH_LONG).show();
                                                    edt_pw.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
