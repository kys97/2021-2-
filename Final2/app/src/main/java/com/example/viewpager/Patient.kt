package com.example.viewpager
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.viewpager.databinding.ActivityPatientBinding
import com.example.viewpager.login.Login
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer

class Patient : BaseActivity() {
    private val locationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding by lazy { ActivityPatientBinding.inflate(layoutInflater)}
        requirePermissions(arrayOf(android.Manifest.permission.READ_CALL_LOG), 998)
        requirePermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 997)
        requirePermissions(arrayOf(android.Manifest.permission.CALL_PHONE), 996)
        requirePermissions(arrayOf(android.Manifest.permission.SEND_SMS), 995)
        requirePermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 994)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //var id = intent.getStringExtra("id")
        val data = getSharedPreferences("login_data", MODE_PRIVATE)

        //id는 환자의 pk이다.
        val id = data.getString("PrimaryKey", "")
        Log.e("로그인 아이디","${id}")
        Toast.makeText(this, "${id}아이디출력", Toast.LENGTH_SHORT).show()


        val db = Firebase.firestore
        var number:String = "01071375461"

        //환자 id 기반 relation 탐색해서 연결된 보호자 아이디를 가져온다.
        var protector_id = "No id"

        db.collection("Relation")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val patient_id = document.data.get("Patient_PK") as String
                    if (patient_id == id) {
                        protector_id = document.data.get("Protector_PK") as String
                        db.collection("Protector")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    Log.d("good", "잘하구이써")
                                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")

                                    //Protector collection 탐색.
                                    if (document.id == protector_id) {
                                        number = document.data.get("phone") as String
                                        Log.d("보호자 번호 가져옴", "${number}")
                                        //보호자의 폰번호를 가져와서 저장.
                                    }
                                }
                            }
                    }
                    Log.d("보호자 아이디 가져옴1", "${protector_id}")

                }
            }









        //디버깅


        db.collection("Patient")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == id) { //아이디가 동일  자신의 영역
                        timer(period=10000){
                            runOnUiThread{

                            }
                            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            val getLongitude = location?.longitude
                            val getLatitude = location?.latitude
                            Log.d("위치불러옴", "${getLatitude}")
                            Log.d("위치불러옴", "${getLongitude}")
                            //일단 버튼 누르면 가도록구현, 위치바뀌거나 일정시간 지나면 위치업데이트되도록은 만듬.
                            //일정시간마다 서버에 위치 어캐쏨?
                            val locationstate = hashMapOf(
                                "위도"  to "${getLatitude}",
                                "경도" to "${getLongitude}"
                            )
                            db.collection("Patient").document(document.id).set(locationstate, SetOptions.merge())
                                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
                            //위치정보 끝

                            val callLogUri = CallLog.Calls.CONTENT_URI
                            var proj = arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE)
                            baseContext?.run {
                                val cursor = contentResolver.query(callLogUri, proj, null, null, null)

                                val data = mutableListOf<Memo>()
                                val db = Firebase.firestore
                                var cnt =0
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
                                    val map = hashMapOf(
                                        "number" to number,
                                        "type" to typeKo,
                                        "date" to dateString
                                    )

                                    // collection이 id인곳에 저장함. 불러올때도 그러도록함
                                    db.collection(document.id).document("${cnt}")
                                        .set(map, SetOptions.merge())
                                        .addOnSuccessListener {
                                            Log.d(
                                                ContentValues.TAG,
                                                "DocumentSnapshot successfully written!"
                                            )
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(
                                                ContentValues.TAG,
                                                "Error writing document",
                                                e
                                            )
                                        }
                                    cnt = cnt+1
                                }
                            }
                            //전화정보 끝


                        }
                        //주기적으로 위치정보와 전화정보를 전송하도록함.
                    }

                }
            }


        binding.patientLogout.setOnClickListener {
            val data = getSharedPreferences("login_data", MODE_PRIVATE)
            val data_input = data.edit()
            data_input.putString("LoginPW","")
            data_input.putString("LoginID","")
            data_input.putString("LoginCheck","")
            data_input.putString("PrimaryKey","")

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.fbPatientProtector.setOnClickListener{
            val intent = Intent(this, Patient_setProtectors::class.java)
            startActivity(intent)
        }

        binding.emergency.setOnClickListener{
            //위에 number 변수에 보호자 번호를 넣거나 번호를 지정해서 넣을 수 있도록함.

            var tel = "tel:"+number
            val intent1 = Intent(Intent.ACTION_CALL, Uri.parse(tel))
            Log.d("Call누구한테","${tel}")
            Toast.makeText(this, "전화연결", Toast.LENGTH_SHORT).show()
            startActivity(intent1)
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
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                location?.let {
                    val position = LatLng(it.latitude, it.longitude)
                    Log.e("lat and long", "${position.latitude} and ${position.longitude}")
                }

            }
            override fun onLocationChanged(locations: MutableList<Location>) {
                super.onLocationChanged(locations)
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                super.onStatusChanged(provider, status, extras)
            }

        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            10000,
            1f,
            locationListener
        )

        fun getAddress(position: LatLng){
            val geoCoder = Geocoder(this@Patient, Locale.getDefault())
            val address =
                geoCoder.getFromLocation(position.latitude, position.longitude, 1).first()
                    .getAddressLine(0)

            Log.e("Address", address)
        }
    }

    fun getcalllog(){
        Log.d("check", "이거")
        Toast.makeText(baseContext, "출력", Toast.LENGTH_SHORT).show()
        val callLogUri = CallLog.Calls.CONTENT_URI
        var proj = arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE)
        baseContext?.run {
            val cursor = contentResolver.query(callLogUri, proj, null, null, null)

            val data = mutableListOf<Memo>()
            val db = Firebase.firestore
            var cnt =0
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

                db.collection("patient").document("${cnt}").set(map, SetOptions.merge())
                    .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

                data.add(call)
            }

        }
    }
    override fun permissionGranted(requestCode: Int) {

    }

    override fun permissionDenied(requestCode: Int) {
        Toast.makeText(this, "외부저장소승인이필요함", Toast.LENGTH_SHORT).show()
        finish()
    }
}