package com.example.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.viewpager.databinding.ActivityCheckRoleBinding
import com.google.android.material.tabs.TabLayoutMediator

class CheckRoleActivity : BaseActivity() {
    val binding by lazy { ActivityCheckRoleBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {

        requirePermissions(arrayOf(android.Manifest.permission.READ_CALL_LOG), 998)
        requirePermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 997)
        requirePermissions(arrayOf(android.Manifest.permission.CALL_PHONE), 996)
        requirePermissions(arrayOf(android.Manifest.permission.SEND_SMS), 995)
        requirePermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 994)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fragmentList = listOf(FragmentA(), Info(), FragmentC())



        val data = getSharedPreferences("login_data", MODE_PRIVATE)
        //id는 환자의 pk이다.
        val id = data.getString("PrimaryKey", "")
        Log.e("로그인 아이디","${id}")
        Toast.makeText(this, "${id}아이디출력", Toast.LENGTH_SHORT).show()

        var bundle = Bundle()
        bundle.putString("id",id)
        val fragmentC = FragmentC()
        val fragmentB = Info()
        val fragmentA = FragmentA()
        fragmentC.arguments =bundle
        fragmentB.arguments = bundle
        fragmentA.arguments =bundle








        val adapter = FragmentAdapter(this)
        adapter.fragmentList = fragmentList
        adapter.id = id.toString()
        binding.viewPager.adapter = adapter


        //tap
        val tabTitles = listOf<String>("위치", "정보", "통화기록")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        val message = intent.getStringExtra("userType")
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    override fun permissionGranted(requestCode: Int) {

    }

    override fun permissionDenied(requestCode: Int) {
        Toast.makeText(this, "외부저장소승인이필요함", Toast.LENGTH_SHORT).show()
        finish()
    }
}