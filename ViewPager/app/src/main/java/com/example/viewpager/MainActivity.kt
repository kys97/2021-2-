package com.example.viewpager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.viewpager.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
	val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
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