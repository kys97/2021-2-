package com.example.viewpager

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.viewpager.databinding.ActivityGoogleMapsBinding
import com.example.viewpager.databinding.FragmentABinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.timer

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
	private lateinit var map: GoogleMap
	private lateinit var mainActivity: CheckRoleActivity

	lateinit var prefs: SharedPreferences
	lateinit var loginData: SharedPreferences
	lateinit var id: String

	var lat = 37.5 as Double
	var long = 127.0 as Double

	val latKey = "lat_bind"
	val longKey = "long_bind"
	val diaKey = "diameter"

	override fun onCreate(savedInstanceState: Bundle?) {
		prefs = mainActivity.getSharedPreferences("coordiBind", Context.MODE_PRIVATE)
		loginData = mainActivity.getSharedPreferences("login_data", Context.MODE_PRIVATE)
		super.onCreate(savedInstanceState)
		arguments?.let {
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		id = arguments?.getString("id").toString()
		binding = FragmentABinding.inflate(inflater, container, false)

		mView = binding.mMap
		mView.onCreate(savedInstanceState)
		mView.getMapAsync(this)
		binding.myLocation.setOnClickListener {
			mView.getMapAsync(this)
		}
		return binding.root
	}

	public fun setGoogleMap(m: GoogleMap) {
		map = m;
	}

	override fun onMapReady(googleMap: GoogleMap) {
		loginData = mainActivity.getSharedPreferences("login_data", Context.MODE_PRIVATE)
		val patientName: String = "user1"
		lateinit var patientMarker: Marker
		val db = Firebase.firestore
		var patient_id = loginData.getString("RelationPatient", "NO ID")
		Log.d("asdf", "${patient_id}")
		var patientLoc = LatLng(lat, long)
		db.collection("Patient").document(patient_id.toString())
			.get()
			.addOnSuccessListener {
				Log.d("get good", "됐다")
				val latString = it.data?.get("위도") as String
				val longString = it.data?.get("경도") as String
				lat = latString.toDouble()
				long = longString.toDouble()
				Log.d("get good", "${lat}")
				Log.d("get good", "${long}")
				patientLoc = LatLng(lat, long)
				patientMarker = googleMap.addMarker(MarkerOptions().position(patientLoc).title(patientName))
				googleMap.moveCamera(CameraUpdateFactory.newLatLng(patientLoc))
				googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
				var locP = Location(LocationManager.NETWORK_PROVIDER)
				var locB = Location(LocationManager.NETWORK_PROVIDER)
				locP.latitude = lat
				locP.longitude = long
				locB.latitude = prefs.getFloat(latKey, lat.toFloat()).toDouble()
				locB.longitude = prefs.getFloat(longKey, lat.toFloat()).toDouble()
				val prefDia = prefs.getFloat(diaKey, 0.0F).toDouble()
				Log.d("Patient", "${lat}, " + "${long}")
				Log.d("Boundary", "${locB.latitude}, " + "${locB.longitude}")
				var distance = locP.distanceTo(locB)
				Log.d("Distance", "${distance.toDouble()}")
				Log.d("Diameter", "${prefDia}")
				if (distance.toDouble() >= prefDia) {
					Log.d("asdf", "asdf")
					Toast.makeText(mainActivity, "환자가 범위 바깥에 있습니다.", Toast.LENGTH_SHORT).show()
				}
			}


		var circle = googleMap.addCircle(
			CircleOptions()
				.center(LatLng(prefs.getFloat(latKey, 0.0F).toDouble(), prefs.getFloat(longKey, 0.0F).toDouble()))
				.radius(prefs.getFloat(diaKey, 100.0F).toDouble())
				.strokeWidth(10f)
				.strokeColor(Color.CYAN)
		)

		fun popUpDialog(latlng: LatLng) {
			val mDialogView = LayoutInflater.from(mainActivity).inflate(R.layout.boundary_dialog, null)
			val mBUilder = AlertDialog.Builder(mainActivity).setView(mDialogView).setTitle("Input diameter")

			val mAlertDialog = mBUilder.show()
			var temp_diameter = prefs.getFloat("diameter", 0.0F).toDouble()
			var editor = prefs.edit()
			mDialogView.findViewById<EditText>(R.id.tempDiameter).setText(temp_diameter.toString() + "m")
			val btnIncrease = mDialogView.findViewById<ImageButton>(R.id.btnIncrease)
			btnIncrease.setOnClickListener {
				temp_diameter += 30.0
				mDialogView.findViewById<EditText>(R.id.tempDiameter).setText(temp_diameter.toString() + "m")
			}
			val btnDecrease = mDialogView.findViewById<ImageButton>(R.id.btnDecrease)
			btnDecrease.setOnClickListener {
				if (temp_diameter <= 30.0) {
					temp_diameter = 0.0
				}
				else {
					temp_diameter -= 30.0
				}
				mDialogView.findViewById<EditText>(R.id.tempDiameter).setText(temp_diameter.toString() + "m")
			}
			val btnOK = mDialogView.findViewById<Button>(R.id.btnOK)
			btnOK.setOnClickListener {
				googleMap.clear()
				temp_diameter = mDialogView.findViewById<EditText>(R.id.tempDiameter).text.toString().replace("m", "").toDouble()
				editor.putFloat(latKey, latlng.latitude.toFloat())
				editor.putFloat(longKey, latlng.longitude.toFloat())
				editor.putFloat(diaKey, temp_diameter.toFloat())
				editor.apply()
				editor.commit()
				circle = googleMap.addCircle(
					CircleOptions()
						.center(LatLng(prefs.getFloat(latKey, 0.0F).toDouble(), prefs.getFloat(longKey, 0.0F).toDouble()))
						.radius(temp_diameter)
						.strokeWidth(10f)
						.strokeColor(Color.CYAN)
				)
				patientMarker = googleMap.addMarker(MarkerOptions().position(patientLoc).title(patientName))
				mAlertDialog.dismiss()
			}
			val btnCancel = mDialogView.findViewById<Button>(R.id.btnCancel)
			btnCancel.setOnClickListener {
				mAlertDialog.dismiss()
			}
		}


		googleMap.setOnMarkerClickListener(object: GoogleMap.OnMarkerClickListener {
			override fun onMarkerClick(marker: Marker): Boolean {
				if (marker.position == LatLng(lat, long)) { // 클릭한 마커가 환자 마커일 때
					popUpDialog(marker.position)
					return true
				}
				return true
			}
		})

		var boundaryMarker = googleMap.addMarker(MarkerOptions().position(LatLng(lat, long)).title("BOUNDARY CENTER"))
		googleMap.setOnMapClickListener(object: GoogleMap.OnMapClickListener {
			override fun onMapClick(p0: LatLng?) {
				if (boundaryMarker != null) {
					boundaryMarker.remove()
				}
				boundaryMarker = googleMap.addMarker(p0?.let {
					MarkerOptions().position(it).title("BOUNDARY CENTER")
				})
				if (p0 != null) {
					popUpDialog(p0)
				}
			}
		})
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

	override fun onAttach(context: Context) {
		super.onAttach(context)
		mainActivity = context as CheckRoleActivity
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