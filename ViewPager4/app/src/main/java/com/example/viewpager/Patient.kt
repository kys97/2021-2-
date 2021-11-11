package com.example.viewpager

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import com.example.viewpager.databinding.ActivityPatientBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class Patient : AppCompatActivity() {
    private val locationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding by lazy { ActivityPatientBinding.inflate(layoutInflater)}

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        var number:String = "01071375461"

        binding.emergency.setOnClickListener{
            //위에 number 변수에 보호자 번호를 넣거나 번호를 지정해서 넣을 수 있도록함.

            var tel = "tel:"+number
            val intent = Intent(Intent.ACTION_CALL, Uri.parse(tel))
            Log.d("Call누구한테","${tel}")
            Toast.makeText(this, "전화연결", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        binding.send1.setOnClickListener{
            var text = binding.send1.text.toString()
            Log.d("text내용","${text}")

            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number,null,text,null,null)
            Toast.makeText(this, "전송완료", Toast.LENGTH_SHORT).show()


        }
        binding.send2.setOnClickListener{
            var text = binding.send2.text.toString()
            Log.d("text내용","${text}")

            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number,null,text,null,null)
            Toast.makeText(this, "전송완료", Toast.LENGTH_SHORT).show()


        }
        binding.send3.setOnClickListener{
            var text = binding.send3.text.toString()
            Log.d("text내용","${text}")

            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number,null,text,null,null)
            Toast.makeText(this, "전송완료", Toast.LENGTH_SHORT).show()


        }


        binding.wd.setOnClickListener {
            Log.d("check", "이거")
            Toast.makeText(baseContext, "출력", Toast.LENGTH_SHORT).show()
            val callLogUri = CallLog.Calls.CONTENT_URI
            var proj = arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE)
            baseContext?.run {
                val cursor = contentResolver.query(callLogUri, proj, null, null, null)

                val data = mutableListOf<Memo>()
                val db = Firebase.firestore
                while (cursor?.moveToNext() == true) {
                    var index = cursor.getColumnIndex((proj[0]))
                    val number = cursor.getString(index)
                    index = cursor.getColumnIndex((proj[1]))
                    val type = cursor.getInt(index)// 1: 수신, 2:발신, 3:부재중
                    var typeKo: String = "수신"
                    if (type == 1) {
                        typeKo = "수신"
                    } else if (type == 2)
                        typeKo = "발신"
                    else if (type == 3)
                        typeKo = "부재중"

                    index = cursor.getColumnIndex((proj[2]))
                    val date = cursor.getLong(index)
                    val formatter = SimpleDateFormat("yy-MM-dd HH:mm")
                    val dateString = formatter.format(Date(date))
                    Log.d("CallLog", "${number}")
                    Log.d("CallLog", "${type}")
                    Log.d("CallLog", "${typeKo}")
                    Log.d("CallLog", "${dateString}")
                    val call = Memo(number, typeKo, dateString)
                    val map = hashMapOf("number" to number, "type" to typeKo, "date" to dateString)
                    db.collection("patient")
                        .add(map)
                        .addOnSuccessListener { documentReference ->
                            Log.d(
                                ContentValues.TAG,
                                "DocumentSnapshot added with ID: ${documentReference.id}"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                        }

                    data.add(call)
                }

            }



        }
    }
}