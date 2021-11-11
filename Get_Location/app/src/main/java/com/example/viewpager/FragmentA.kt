package com.example.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.viewpager.databinding.ActivityGoogleMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

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
	private lateinit var binding: ActivityGoogleMapsBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {

		// Inflate the layout for this fragment
		var rootView = inflater.inflate(R.layout.fragment_a, container, false)

		mView = rootView.findViewById(R.id.mMap)
		mView.onCreate(savedInstanceState)
		mView.getMapAsync(this)
		return rootView
	}

	override fun onMapReady(googleMap: GoogleMap) {
		val seoul = LatLng(37.5, 127.0)
		googleMap.addMarker(MarkerOptions().position(seoul).title("서울"))
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(seoul))
		googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
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