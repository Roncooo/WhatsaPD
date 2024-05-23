package it.unipd.dei.esp.whatsapd.ui.nearme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiDistanceListRecyclerViewAdapter

class NearMeFragment : Fragment() {
	
	private val nearmeViewModel: NearMeViewModel by viewModels {
		NearMeViewModelFactory((activity?.application as Application).repository)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		// Invalidate the options menu to ensure it's recreated when the fragment is displayed
		activity?.invalidateOptionsMenu()
		val root: View = inflater.inflate(R.layout.fragment_near_me, container, false)
		
		
		// todo take real position
		val buttonPosition: Button = root.findViewById(R.id.near_me_button)
		buttonPosition.setOnClickListener {}
		val currentLatitude = -45.407717
		val currentLongitude = -168.126554
		
		
		// Initialize RecyclerView and its adapter
		val recyclerView: RecyclerView = root.findViewById(R.id.near_me_recycler_view)
		val adapter = PoiDistanceListRecyclerViewAdapter(this, requireContext())
		recyclerView.adapter = adapter
		recyclerView.layoutManager = LinearLayoutManager(activity)
		nearmeViewModel.getPoisByDistance(currentLatitude, currentLongitude)
			.observe(viewLifecycleOwner) {
				adapter.submitList(it)
			}
		
		return root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		// Define a callback for the back button press
		// This lets the user return to the previous fragment
		val callback = object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				val navController = findNavController()
				navController.popBackStack()
			}
		}
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
	}
}