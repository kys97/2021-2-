package com.example.viewpager

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.viewpager.databinding.FragmentBBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentB.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentB : Fragment() {
	// TODO: Rename and change types of parameters
	private var param1: String? = null
	private var param2: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			param1 = it.getString(ARG_PARAM1)
			param2 = it.getString(ARG_PARAM2)
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val binding = FragmentBBinding.inflate(inflater,container,false)

		/*context?.run{
			val cursor = contentResolver.query(callLogUri, proj, null, null, null)
			while(cursor?.moveToNext()==true){
				val number = cursor?.getString(0)
				val type = cursor?.getString(1)
				val date = cursor?.getString(2)
				Log.d("CallLog", "${number}")
				Log.d("CallLog", "${type}")
				Log.d("CallLog", "${date}")


			}
		}*/





		/*binding.wd.setOnClickListener {
			Log.d("check", "이거")
			Toast.makeText(context, "출력", Toast.LENGTH_SHORT).show()
			val callLogUri = CallLog.Calls.CONTENT_URI
			var proj = arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE)
			context?.run {
				val cursor = contentResolver.query(callLogUri, proj, null, null, null)

				val data = mutableListOf<Memo>()
				val db = Firebase.firestore
				while (cursor?.moveToNext() == true) {
					var index = cursor.getColumnIndex((proj[0]))
					val number = cursor.getString(index)
					index = cursor.getColumnIndex((proj[1]))
					val type = cursor.getInt(index)// 1: 수신, 2:발신, 3:부재중
					var typeKo:String = "수신"
					if (type == 1){
						typeKo = "수신"
					}
					else if (type ==2)
						typeKo ="발신"
					else if (type ==3)
						typeKo ="부재중"

					index = cursor.getColumnIndex((proj[2]))
					val date = cursor.getLong(index)
					val formatter = SimpleDateFormat("yy-MM-dd HH:mm")
					val dateString = formatter.format(Date(date))
					Log.d("CallLog", "${number}")
					Log.d("CallLog", "${type}")
					Log.d("CallLog", "${typeKo}")
					Log.d("CallLog", "${dateString}")
					val call = Memo(number,typeKo,dateString)
					val map = hashMapOf("number" to number, "type" to typeKo, "date" to dateString)
					db.collection("patient")
						.add(map)
						.addOnSuccessListener { documentReference ->
							Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
						}
						.addOnFailureListener { e ->
							Log.w(TAG, "Error adding document", e)
						}

					data.add(call)
				}





			}





			/*	val db = Firebase.firestore
                val city = hashMapOf(
                    "name" to "LlA",
                    "state" to "ClA",
                    "country" to "UlSA"

                )
                db.collection("cities").document("LA")
                    .set(city)
                    .addOnSuccessListener { Log.d(ContentValues.TAG,"documentSnapshot") }
                    .addOnFailureListener{e -> Log.w(ContentValues.TAG, "Error",e)}
            */

		}*/

		return binding.root
	}

	fun checkPermission(){
		var result =0
		var permissionList = mutableListOf<String>()

		//더 구현현


	}
	companion object {
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @param param1 Parameter 1.
		 * @param param2 Parameter 2.
		 * @return A new instance of fragment FragmentB.
		 */
		// TODO: Rename and change types and number of parameters
		@JvmStatic
		fun newInstance(param1: String, param2: String) =
			FragmentB().apply {
				arguments = Bundle().apply {
					putString(ARG_PARAM1, param1)
					putString(ARG_PARAM2, param2)
				}
			}
	}
}