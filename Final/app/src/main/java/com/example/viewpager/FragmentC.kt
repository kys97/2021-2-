package com.example.viewpager

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.viewpager.databinding.FragmentABinding
import com.example.viewpager.databinding.FragmentCBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentC.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentC : Fragment() {
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


		val id = arguments?.getString("id")

		
		Log.d("id왓니", "${id}")



		// Inflate the layout for this fragment
		val binding = FragmentCBinding.inflate(inflater, container, false)
		var data: MutableList<Memo> = mutableListOf()
		var adapter = CustomAdapter()
		data.clear()


		val db = Firebase.firestore
		var patient_id = "No id"
		db.collection("Relation")
			.get()
			.addOnSuccessListener { result ->
				for (document in result) {

					Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
					val protector_id = document.data.get("Protector_PK") as String
					if (protector_id == id) {
						patient_id = document.data.get("Patient_PK") as String
						db.collection(patient_id)
							.get()
							.addOnSuccessListener { result ->
								for (document in result) {
									Log.d("good", "잘하구이써")
									Log.d(TAG, "${document.id} => ${document.data}")
									val name = document.data.get("type")as String
									val phoneNumber = document.data.get("number")as String
									val date = document.data.get("date") as String
									Log.d("get good", "${name}")
									Log.d("get good", "${phoneNumber}")
									Log.d("get good", "${date}")
									var memo = Memo(name,phoneNumber,date)
									data.add(memo)

									adapter.notifyDataSetChanged()
								}
								data.sortByDescending ( Memo::timestamp )
							}
						Log.d("data good", "${data}")
					}
					data.sortedBy{it.timestamp}
					//val data:MutableList<Memo> = loadData()

					adapter.listData = data

					binding.recyclerView.adapter = adapter

					binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

				}
			}






		data.sortedBy{it.timestamp}
		//val data:MutableList<Memo> = loadData()

		adapter.listData = data

		binding.recyclerView.adapter = adapter

		binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

		return binding.root
	}

	fun loadData(): MutableList<Memo> {
		val data: MutableList<Memo> = mutableListOf()


		/*firestore?.collection("patient")?.addSnapshotListener{querySnapshot, firebaseFirestoreException->
			data.clear()
			if (querySnapshot==null) return@addSnapshotListener

			for(snapshot in querySnapshot!!.documents){
				var item = snapshot.toObject(Memo::class.java)
				data.add(item!!)
			}
		}*/



//왜 파이어스토어도안되고 리얼타임데이타베이스도 안되냐고 미친놈들아
		/*firestore?.collection("patient")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
			// ArrayList 비워줌
			data.clear()

			for (snapshot in querySnapshot!!.documents) {
				var item = snapshot.toObject(Memo::class.java)
				data.add(item!!)
			}
		}*/



		//문자 통화기록 받아와서  data에 저장

		/*for (no in 1..100){
			val name = "이녀석 ${no}"
			val phoneNumber = "xxxx-xxxx-xxx${no}"
			val date = System.currentTimeMillis()

			var memo = Memo(name,phoneNumber,date)
			data.add(memo)
		}*/


		return data
	}
	companion object {
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @param param1 Parameter 1.
		 * @param param2 Parameter 2.
		 * @return A new instance of fragment FragmentC.
		 */
		// TODO: Rename and change types and number of parameters
		@JvmStatic
		fun newInstance(param1: String, param2: String) =
			FragmentC().apply {
				arguments = Bundle().apply {
					putString(ARG_PARAM1, param1)
					putString(ARG_PARAM2, param2)
				}
			}
	}
}