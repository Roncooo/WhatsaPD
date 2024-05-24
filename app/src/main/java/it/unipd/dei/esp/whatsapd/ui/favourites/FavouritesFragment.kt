package it.unipd.dei.esp.whatsapd.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiListRecyclerViewAdapter

class FavouritesFragment : Fragment() {
	
	private val favouritesViewModel: FavouritesViewModel by viewModels {
		FavouritesViewModelFactory((activity?.application as Application).repository)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		
		// Invalidate the options menu to ensure it's recreated when the fragment is displayed
		activity?.invalidateOptionsMenu()
		
		val root: View = inflater.inflate(R.layout.fragment_favourites, container, false)
		
		// Initialize RecyclerView and its adapter
		val recyclerView: RecyclerView = root.findViewById(R.id.fav_recycler_view)
		val adapter = PoiListRecyclerViewAdapter(this)
		recyclerView.adapter = adapter
		recyclerView.layoutManager = LinearLayoutManager(activity)
		
		// Observe changes in the list of favourite POIs and update the adapter
		favouritesViewModel.favPois.observe(viewLifecycleOwner) { poiList ->
			adapter.submitList(poiList.toMutableList())
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