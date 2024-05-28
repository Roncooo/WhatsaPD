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
import it.unipd.dei.esp.whatsapd.databinding.FragmentNearMeBinding
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiListRecyclerViewAdapter

class NearMeFragment : Fragment() {
	
	private val nearmeViewModel: NearMeViewModel by viewModels {
		NearMeViewModelFactory((activity?.application as Application).repository)
	}
	
	private var _binding: FragmentNearMeBinding? = null
	
	// This properties are only valid between onCreateView and onDestroyView.
	private val binding get() = _binding!!
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		
		_binding = FragmentNearMeBinding.inflate(inflater, container, false)
		val root = binding.root
		
		// Invalidate the options menu to ensure it's recreated when the fragment is displayed
		activity?.invalidateOptionsMenu()
		
		// todo take real position
		val buttonPosition: Button = binding.nearMeButton
		buttonPosition.setOnClickListener {}
		val currentLatitude = -45.407717
		val currentLongitude = -168.126554
		
		
		// Initialize RecyclerView and its adapter
		val recyclerView: RecyclerView = binding.nearMeRecyclerView
		val adapter = PoiListRecyclerViewAdapter(this)
		recyclerView.adapter = adapter
		recyclerView.layoutManager = LinearLayoutManager(activity)
		nearmeViewModel.getPoisByDistance(currentLatitude, currentLongitude)
			.observe(viewLifecycleOwner) {
				adapter.submitList(it.toMutableList())
			}
		
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
	}
}
