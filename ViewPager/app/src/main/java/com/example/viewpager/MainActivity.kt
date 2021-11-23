package com.example.viewpager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.viewpager.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity() {
	val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
	override fun permissionGranted(requestCode: Int) {

	}

	override fun permissionDenied(requestCode: Int) {
		Toast.makeText(this, "외부저장소승인이필요함", Toast.LENGTH_SHORT).show()
		finish()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		requirePermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 997)
		val intent = Intent(this, CheckRoleActivity::class.java)
		binding.btnPatient.setOnClickListener {
			intent.putExtra("userType", "patient")
			startActivity(intent)
		}
		binding.btnProtector.setOnClickListener {
			intent.putExtra("userType", "protector")
			startActivity(intent)
		}
	}
}