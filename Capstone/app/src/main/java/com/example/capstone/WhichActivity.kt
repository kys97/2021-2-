package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone.databinding.ActivityMainBinding
import com.example.capstone.databinding.ActivityWhichBinding

class WhichActivity : AppCompatActivity() {
	val binding by lazy { ActivityWhichBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		binding.Title.text = intent.getStringExtra("userType") + "의 메인 페이지 입니다."

		setFragment()
	}

	fun setFragment() {
		val listFragment: ListFragment = ListFragment()
		val transaction = supportFragmentManager.beginTransaction()
		transaction.add(R.id.frameLayout, listFragment)
		transaction.commit()
	}
}