package com.example.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.viewpager.login.Login;

public class Info extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_b, container, false);

        Button logout = root.findViewById(R.id.info_btn_logout);
        Button add_patient = root.findViewById(R.id.info_btn_addpatient);
        RadioGroup patients = root.findViewById(R.id.info_rg_patientlist);

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

                Intent intent = new Intent(root.getContext(), Login.class);
                startActivity(intent);
            }
        });

        add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
    }
}
