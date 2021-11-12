package com.example.viewpager.login;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viewpager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Join extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String gender = "";
    private int year, month, day;
    boolean check = true;

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

        edt_pw_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edt_pw.getText().toString().matches(edt_pw_check.getText().toString()))
                    edt_pw_check.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                else
                    edt_pw_check.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edt_phone.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        dt_birth.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                year = i; month = i1+1; day = i2;
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
                    db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                check = true;
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    if(document.getData().get("id").toString().matches(edt_id.getText().toString())){
                                        Toast.makeText(getApplicationContext(),"중복되는 아이디 입니다.",Toast.LENGTH_SHORT).show();
                                        edt_id.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                                        edt_id.requestFocus();
                                        check = false;
                                        break;
                                    }
                                    else if(document.getData().get("phone").toString().matches(edt_phone.getText().toString().substring(1))){
                                        Toast.makeText(getApplicationContext(),"중복되는 전화번호 입니다.",Toast.LENGTH_SHORT).show();
                                        edt_phone.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                                        edt_phone.requestFocus();
                                        check = false;
                                        break;
                                    }
                                }
                                if(check){
                                    Log.e("왜 실행되는거죠?",String.valueOf(check));
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name",edt_name.getText().toString());
                                    user.put("gender",gender);
                                    user.put("id",edt_id.getText().toString());
                                    user.put("pw",edt_pw.getText().toString());
                                    user.put("phone",edt_phone.getText().toString());
                                    user.put("birth_year",year);
                                    user.put("birth_month",month);
                                    user.put("birth_day",day);
                                    db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Intent intent = new Intent(getApplicationContext(), Login.class);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("에러",e.toString());
                                        }
                                    });
                                }
                            }
                        }
                    });


                }
            }
        });
    }
}
