package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone.databinding.ActivityCheckRoleBinding

class CheckRoleActivity : AppCompatActivity() {
	val binding by lazy { ActivityCheckRoleBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		val intent = Intent(this, CheckRoleActivity::class.java)
		lateinit var userType: String
		binding.btnPatient.setOnClickListener {
			intent.putExtra("userType", "Patient")
			setResult(RESULT_OK, intent)
			finish()
		}
		binding.btnProtector.setOnClickListener {
			intent.putExtra("userType", "Protector")
			setResult(RESULT_OK, intent)
			finish()
		}
	}
}