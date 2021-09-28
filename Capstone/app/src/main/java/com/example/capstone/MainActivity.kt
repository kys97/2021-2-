package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.capstone.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
	val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		val intent = Intent(this, CheckRoleActivity::class.java)
		thread(start = true) {
			Thread.sleep(3000L)
			runOnUiThread {
				startActivity(intent)
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == RESULT_OK) {
			val message = data?.getStringExtra("userType")
			val intent = Intent(this, WhichActivity::class.java)
			intent.putExtra("userType", message)
			startActivity(intent)
		}
	}
}