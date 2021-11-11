package com.example.viewpager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.widget.Toast
import com.example.viewpager.databinding.ActivityMainBinding
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


		val intent = Intent(this, CheckRoleActivity::class.java)
		binding.btnPatient.setOnClickListener {
			val intent = Intent(this, Patient::class.java)
			intent.putExtra("userType", "patient")
			startActivity(intent)
		}
		binding.btnProtector.setOnClickListener {
			intent.putExtra("userType", "protector")
			startActivity(intent)
		}
	}

	override fun permissionGranted(requestCode: Int) {
		val callLogUri = CallLog.Calls.CONTENT_URI
		var proj = arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE)
		val cursor = contentResolver.query(callLogUri, proj, null, null, null)

		val data = mutableListOf<Memo>()

		while (cursor?.moveToNext() == true) {
			var index = cursor.getColumnIndex((proj[0]))
			val number = cursor.getString(index)
			index = cursor.getColumnIndex((proj[1]))
			val type = cursor.getString(index)
			index = cursor.getColumnIndex((proj[2]))
			val date = cursor.getString(index)


			Log.d("CallLog", "${number}")
			Log.d("CallLog", "${type}")
			Log.d("CallLog", "${date}")
			val call = Memo(number, type, date)

			data.add(call)
		}

	}

	override fun permissionDenied(requestCode: Int) {
		Toast.makeText(this, "외부저장소승인이필요함", Toast.LENGTH_SHORT).show()
		finish()
	}
}

