package it.unipd.dei.esp.whatsapd.ui.nearme

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.databinding.FragmentNearMeBinding
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiListRecyclerViewAdapter


class NearMeFragment : Fragment() {
	
	private val nearmeViewModel: NearMeViewModel by viewModels {
		NearMeViewModelFactory((activity?.application as Application).repository)
	}
	
	private var _binding: FragmentNearMeBinding? = null
	
	// This properties are only valid between onCreateView and onDestroyView.
	private val binding get() = _binding!!
	
	/*
	private lateinit var fusedLocationClient: FusedLocationProviderClient
	private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
	*/
	private lateinit var locationService: LocationService
	
	init {
	
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		
		_binding = FragmentNearMeBinding.inflate(inflater, container, false)
		val root = binding.root
		
		// Invalidate the options menu to ensure it's recreated when the fragment is displayed
		activity?.invalidateOptionsMenu()
		
		return root
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		// Nullify binding to avoid memory leaks
		_binding = null
	}
	
	/**
	 * Overriden to define a callback for the back button press that lets the user return to
	 * previous [Fragment]
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		val callback = object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				val navController = findNavController()
				navController.popBackStack()
			}
		}
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		
		
		locationService = LocationService(this.requireContext())
		val fragment = this
		locationService.setOnLocationResultListener(object :
			LocationService.OnLocationResultListener {
			override fun onLocationResult(location: Location) {
				Log.e("onlocationresult", "onlocationresult")
				
				binding.locationNotAvailable.visibility = GONE
				
				// Initialize RecyclerView and its adapter
				val recyclerView: RecyclerView = binding.nearMeRecyclerView
				val adapter = PoiListRecyclerViewAdapter(fragment)
				recyclerView.adapter = adapter
				recyclerView.layoutManager = LinearLayoutManager(activity)
				
				nearmeViewModel.getPoisByDistance(location).observe(viewLifecycleOwner) {
					adapter.submitList(it.toMutableList())
				}
			}
			
			override fun onPermissionDenied() {
				Log.e("onpermissiondenied", "onpermissiondenied")
				locationService.requestPermissions(activity)
			}
		})
		
		Log.e("getcurrentlocation", "first")
		locationService.getCurrentLocation()
		
	}
	
	
	@Deprecated("Deprecated in Java")
	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == LocationService.LOCATION_PERMISSION_REQUEST_CODE) {
			if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Permission granted
				locationService.handlePermissionsResult(requestCode, grantResults)
			} else {
				// Permission denied
				Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
			}
		}
	}
}
