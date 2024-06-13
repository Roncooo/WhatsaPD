package it.unipd.dei.esp.whatsapd.ui.nearme

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.databinding.FragmentNearMeBinding
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiListRecyclerViewAdapter

class NearMeFragment : Fragment() {
	
	private val nearmeViewModel: NearMeViewModel by viewModels {
		NearMeViewModelFactory((activity?.application as Application).repository)
	}
	
	private var _binding: FragmentNearMeBinding? = null
	
	// Initialized in onViewCreated
	private lateinit var locationService: LocationService
	
	// This property in only valid between onCreateView and onDestroyView.
	private val binding get() = _binding!!
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		
		_binding = FragmentNearMeBinding.inflate(inflater, container, false)
		val root = binding.root
		
		// Invalidate the options menu to ensure it's recreated when the fragment is displayed
		activity?.invalidateOptionsMenu()
		
		return root
	}
	
	/**
	 * Overriden to set up the back button function and to instantiate [locationService].
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		/**
		 * Define a callback for the back button press that lets the user return to
		 * previous [Fragment]
		 */
		val callback = object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				val navController = findNavController()
				navController.popBackStack()
			}
		}
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		
		/**
		 * Set up location service
		 */
		var maxLocationPermissionRequests = 2
		locationService = LocationService(this)
		locationService.setOnLocationResultListener(object :
			LocationService.OnLocationResultListener {
			override fun onLocationResult(location: Location?) {
				
				// hide error message
				binding.locationNotAvailable.visibility = GONE
				
				// Initialize RecyclerView and its adapter
				val recyclerView: RecyclerView = binding.nearMeRecyclerView
				val adapter = PoiListRecyclerViewAdapter(this@NearMeFragment)
				recyclerView.adapter = adapter
				recyclerView.layoutManager = LinearLayoutManager(activity)
				
				nearmeViewModel.getPoisByDistance(location!!).observe(viewLifecycleOwner) {
					adapter.submitList(it.toMutableList())
				}
			}
			
			override fun onLocationWaiting() {
				binding.locationNotAvailable.text =
					requireContext().getString(R.string.location_waiting_message)
			}
			
			override fun onPermissionDenied() {
				maxLocationPermissionRequests -= 1
				if (maxLocationPermissionRequests > 0)
					locationService.requestPermissions()
				else
					binding.locationNotAvailable.text =
						requireContext().getString(R.string.location_permission_denied_message)
			}
		})
	}
	
	override fun onResume() {
		super.onResume()
		// getCurrentLocation is called inside onResume so that that it is called after
		// onViewCreated (which initialize locationService) and every time the fragment becomes
		// visible to the user (e.g. after the user goes in the settings to activate GPS)
		locationService.getCurrentLocation()
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		// Nullify binding to avoid memory leaks
		_binding = null
	}
}
