package com.example.viewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.viewpager.databinding.ActivityGoogleMapsBinding
import com.example.viewpager.databinding.FragmentABinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentA.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentA : Fragment(), OnMapReadyCallback {
	private lateinit var mView: MapView
	private lateinit var binding: FragmentABinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
		}
	}

	fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
		var ft: FragmentTransaction = fragmentManager.beginTransaction()
		ft.detach(fragment).attach(fragment).commit()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		binding = FragmentABinding.inflate(inflater, container, false)

		mView = binding.mMap
		mView.onCreate(savedInstanceState)
		mView.getMapAsync(this)
		binding.myLocation.setOnClickListener {
			mView.getMapAsync(this)
		}
		return binding.root
	}

	var lat_bind = 37.5 as Double
	var long_bind = 126.95 as Double
	var latBound_bind = 0.01 as Double
	var longBound_bind = 0.01 as Double
	fun setBind(lat: Double, long: Double, latBound: Double, longBound: Double) {
		lat_bind = lat
		long_bind = long
		latBound_bind = latBound
		longBound_bind = longBound
	}

	fun isInBind(lat: Double, long: Double): Boolean {
		return ((lat_bind - latBound_bind <= lat && lat <= lat_bind + latBound_bind) ||
				(long_bind - longBound_bind <= long && long <= long_bind + longBound_bind))
	}

	override fun onMapReady(googleMap: GoogleMap) {
		val db = Firebase.firestore
		var lat = 37.4 as Double
		var long = 126.0 as Double
		val patientName: String = "user1"
		db.collection("Location").document(patientName)
			.get()
			.addOnSuccessListener {
				val latString = it.data?.get("위도") as String
				val longString = it.data?.get("경도") as String
				lat = latString.toDouble()
				long = longString.toDouble()
				if (!isInBind(lat, long)) {
					Log.d("Out Bound", "${lat}, ${long}")
					Toast.makeText(binding.root.context, "환자가 범위 바깥으로 이동했습니다.", Toast.LENGTH_LONG).show()
				} else {
					Log.d("In Bound", "${lat}, ${long}")
				}
				Log.d("get good", "${lat}")
				Log.d("get good", "${long}")
				val patientLoc = LatLng(lat, long)
				googleMap.addMarker(MarkerOptions().position(patientLoc).title(patientName))
				googleMap.moveCamera(CameraUpdateFactory.newLatLng(patientLoc))
				googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
			}
	}

	override fun onStart() {
		super.onStart()
		mView.onStart()
	}

	override fun onStop() {
		super.onStop()
		mView.onStop()
	}

	override fun onResume() {
		super.onResume()
		mView.onResume()
	}

	override fun onPause() {
		super.onPause()
		mView.onPause()
	}

	override fun onLowMemory() {
		super.onLowMemory()
		mView.onLowMemory()
	}

	override fun onDestroy() {
		super.onDestroy()
		super.onDestroy()
	}

	companion object {
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @param param1 Parameter 1.
		 * @param param2 Parameter 2.
		 * @return A new instance of fragment FragmentA.
		 */
		// TODO: Rename and change types and number of parameters
		@JvmStatic
		fun newInstance(param1: String, param2: String) =
			FragmentA().apply {
				arguments = Bundle().apply {
					putString(ARG_PARAM1, param1)
					putString(ARG_PARAM2, param2)
				}
			}
	}
}