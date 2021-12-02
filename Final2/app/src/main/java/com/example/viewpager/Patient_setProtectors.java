package com.example.viewpager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class Patient_setProtectors extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<RelationData> RelationList;
    private RelationAdapter relationAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String relation_id, patient_pk, patient_name, protector_pk, protector_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_patient);

        TextView relation_code = findViewById(R.id.tv_patient_code);
        recyclerView = findViewById(R.id.rv_addprotector);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        RelationList = new ArrayList<>();
        relationAdapter = new RelationAdapter(RelationList);
        recyclerView.setAdapter(relationAdapter);

        SharedPreferences data = getSharedPreferences("login_data", Activity.MODE_PRIVATE);
        patient_pk = data.getString("PrimaryKey", "");
        Log.e("로그인 아이디",patient_pk);

        //랜덤 코드 가져오기
        db.collection("Patient").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()) {

                        Log.e("환자 아이디",document.getId());
                        Log.e("환자 코드",document.getData().get("code").toString());
                        if (document.getId().matches(patient_pk)) {
                            Log.e("내부 로그인 아이디",patient_pk);
                            Log.e("내부 환자 아이디",document.getId());
                            Log.e("내부 환자 코드",document.getData().get("code").toString());
                            relation_code.setText(document.getData().get("code").toString());
                        }
                    }
                }
            }
        });

        //보호자 리스트 가져오기
        db.collection("Relation").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        if(document.getData().get("Patient_PK").toString().matches(patient_pk)){
                            relation_id = document.getId();
                            patient_name = document.getData().get("Patient_Name").toString();
                            protector_pk = document.getData().get("Protector_PK").toString();
                            protector_name = document.getData().get("Protector_Name").toString();
                            RelationData listData = new RelationData(relation_id, patient_pk, patient_name, protector_pk, protector_name);
                            RelationList.add(listData);
                            relationAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }
}
