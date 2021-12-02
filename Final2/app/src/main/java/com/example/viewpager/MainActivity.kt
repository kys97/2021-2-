package com.example.viewpager

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.widget.Toast
import com.example.viewpager.databinding.ActivityMainBinding
import com.example.viewpager.login.Login
import com.google.android.material.tabs.TabLayoutMediator
import java.util.jar.Manifest

class MainActivity : BaseActivity() {
	val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		requirePermissions(arrayOf(android.Manifest.permission.READ_CALL_LOG), 998)
		requirePermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 997)
		requirePermissions(arrayOf(android.Manifest.permission.CALL_PHONE), 996)
		requirePermissions(arrayOf(android.Manifest.permission.SEND_SMS), 995)
		requirePermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 994)

		val data = getSharedPreferences("login_data", MODE_PRIVATE)
		val data_input = data.edit()
		val test_id = data.getString("LoginID","")
		Log.e("로그인 아이디","${test_id}")

		val intent = Intent(this, Login::class.java)
		binding.btnPatient.setOnClickListener {
			data_input.putString("LoginMode", "Patient")
			data_input.commit()
			startActivity(intent)
		}
		binding.btnProtector.setOnClickListener {
			data_input.putString("LoginMode", "Protector")
			data_input.commit()
			startActivity(intent)
		}
	}

	override fun permissionGranted(requestCode: Int) {

	}

	override fun permissionDenied(requestCode: Int) {
		Toast.makeText(this, "외부저장소승인이필요함", Toast.LENGTH_SHORT).show()
		finish()
	}
}

