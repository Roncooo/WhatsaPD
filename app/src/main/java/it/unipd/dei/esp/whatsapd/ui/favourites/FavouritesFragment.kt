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
import it.unipd.dei.esp.whatsapd.databinding.FragmentFavouritesBinding
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiListRecyclerViewAdapter

class FavouritesFragment : Fragment() {
	
	private val favouritesViewModel: FavouritesViewModel by viewModels {
		FavouritesViewModelFactory((activity?.application as Application).repository)
	}
	
	private var _binding: FragmentFavouritesBinding? = null
	
	// This properties are only valid between onCreateView and onDestroyView.
	private val binding get() = _binding!!
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		
		_binding = FragmentFavouritesBinding.inflate(inflater, container, false)
		val root = binding.root
		
		// Invalidate the options menu to ensure it's recreated when the fragment is displayed
		activity?.invalidateOptionsMenu()
		
		// Initialize RecyclerView and its adapter
		val recyclerView: RecyclerView = binding.favRecyclerView
		val adapter = PoiListRecyclerViewAdapter(this)
		recyclerView.adapter = adapter
		recyclerView.layoutManager = LinearLayoutManager(activity)
		
		// Observe changes in the list of favourite Pois and update the adapter
		favouritesViewModel.favPoi.observe(viewLifecycleOwner) { poiList ->
			adapter.submitList(poiList.toMutableList())
		}
		
		return root
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
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		// Nullify binding to avoid memory leaks
		_binding = null
	}
}
