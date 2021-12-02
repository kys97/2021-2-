package com.example.viewpager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomCode extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String input_code = "", code = "";
    boolean check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_protector_code);

        EditText edt_randomcode = findViewById(R.id.edt_addprotectorcode_patient_code);
        Button btn_next = findViewById(R.id.btn_addprotectorcode_next);


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_randomcode.getText().toString().length() == 6){
                    db.collection("Patient").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                boolean ck = true;

                                for(QueryDocumentSnapshot document : task.getResult()){
                                    code = document.getData().get("code").toString();
                                    input_code = edt_randomcode.getText().toString();

                                    if(input_code.equals(code)) {
                                        ck = false;

                                        //새 코드 입력
                                        String new_code = setRandomCode();
                                        String patient_id = document.getId();
                                        Map<String,Object> update_code = new HashMap<String, Object>();
                                        update_code.put("code", new_code);
                                        update_code.put("name",document.getData().get("name"));
                                        update_code.put("gender",document.getData().get("gender"));
                                        update_code.put("id",document.getData().get("id"));
                                        update_code.put("pw",document.getData().get("pw"));
                                        update_code.put("phone",document.getData().get("phone"));
                                        db.collection("Patient").document(patient_id).set(update_code);

                                        Intent intent = new Intent(getApplicationContext(),Protector_addPatient.class);
                                        intent.putExtra("patient_id",patient_id);
                                        startActivity(intent);
                                        break;
                                    }
                                }

                                if(ck)
                                    Toast.makeText(getApplicationContext(),"코드가 일치하는 환자가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else
                    Toast.makeText(getApplicationContext(),"코드 6자리를 모두 입력해주세요.", Toast.LENGTH_LONG).show();
            }
        });

    }

    public String setRandomCode(){
        input_code = "";
        check = false;

        do{
            for(int i = 0; i < 6; i++){
                Random rand = new Random();
                input_code += Integer.toString(rand.nextInt(10));
            }

            db.collection("Patient").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    check = true;
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            if(document.getData().get("code").toString().matches(input_code)) {
                                check = false;
                                break;
                            }
                        }
                    }
                }
            });
        }while (check);

        return input_code;
    }
}
