package com.example.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.viewpager.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Info extends Fragment {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences data;
    private ListView listView;
    private ArrayList<String> patientNameList = new ArrayList<String>();
    private ArrayList<String> patientPKList = new ArrayList<String>();

    private TextView tv_protector_name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_b, container, false);
        tv_protector_name = root.findViewById(R.id.info_tv_username);
        listView = root.findViewById(R.id.lv_protector_patient_list);
        Button logout = root.findViewById(R.id.info_btn_logout);
        Button add_patient = root.findViewById(R.id.info_btn_addpatient);
        data = getActivity().getSharedPreferences("login_data",getActivity().MODE_PRIVATE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice,patientNameList);
        listView.setAdapter(adapter);
        db.collection("Protector").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String protector_pk = data.getString("PrimaryKey","");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getId().equals(protector_pk)){
                            tv_protector_name.setText(document.getData().get("name").toString() + "보호자님");
                        }
                    }
                }
            }
        });
        db.collection("Relation").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String protector_pk = data.getString("PrimaryKey","");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getData().get("Protector_PK").toString().equals(protector_pk)){
                            patientNameList.add(document.getData().get("Patient_Name").toString());
                            patientPKList.add(document.getData().get("Patient_PK").toString());
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        }
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor data_input = data.edit();
                data_input.putString("RelationPatient",patientPKList.get(position));
                data_input.commit();
                Log.e("선택 환자",data.getString("RelationPatient",""));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //앱내에 자동로그인 기록 남기기
                SharedPreferences data = getActivity().getSharedPreferences("login_data", Activity.MODE_PRIVATE);
                SharedPreferences.Editor data_input = data.edit();

                data_input.putBoolean("LoginCheck", false);
                data_input.putString("LoginID","");
                data_input.putString("LoginPW","");

                data_input.commit();

                Intent intent = new Intent(root.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(),RandomCode.class);
                Log.e("화면 넘기기","");
                startActivity(intent);
            }
        });

        return root;
    }
}
