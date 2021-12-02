package com.example.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viewpager.login.Login;
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

public class Protector_addPatient extends AppCompatActivity {

    final private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String Protector_PK, Patient_PK;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_protector);

        EditText edt_protector_name = findViewById(R.id.edt_addprotector_protector_name);
        EditText edt_patient_name = findViewById(R.id.edt_addprotector_patient_name);
        Button btn_complete = findViewById(R.id.btn_addprotector_complete);

        Intent intent = getIntent();
        Patient_PK = intent.getStringExtra("patient_id");

        SharedPreferences data = getSharedPreferences("login_data", Activity.MODE_PRIVATE);
        Protector_PK = data.getString("PrimaryKey","");

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> relatoin = new HashMap<>();
                relatoin.put("Protector_PK",Protector_PK);
                relatoin.put("Protector_Name",edt_protector_name.getText().toString());
                relatoin.put("Patient_PK",Patient_PK);
                relatoin.put("Patient_Name",edt_patient_name.getText().toString());
                db.collection("Relation").add(relatoin).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), edt_patient_name.getText().toString() + "환자가 추가되었습니다. ",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), CheckRoleActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }
}
