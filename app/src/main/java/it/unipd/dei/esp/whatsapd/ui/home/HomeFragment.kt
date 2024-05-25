package it.unipd.dei.esp.whatsapd.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiListRecyclerViewAdapter
import it.unipd.dei.esp.whatsapd.ui.nearme.HomeViewModel
import it.unipd.dei.esp.whatsapd.ui.nearme.HomeViewModelFactory

class HomeFragment : Fragment() {
	
	private val homeViewModel: HomeViewModel by viewModels {
		HomeViewModelFactory((activity?.application as Application).repository)
	}
	private lateinit var adapter: PoiListRecyclerViewAdapter
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		
		// Invalidate the options menu to ensure it's recreated when the fragment is displayed
		activity?.invalidateOptionsMenu()
		
		val root: View = inflater.inflate(R.layout.fragment_home, container, false)
		
		// Initialize RecyclerView and its adapter
		val recyclerView: RecyclerView = root.findViewById(R.id.poi_recycler_view)
		adapter = PoiListRecyclerViewAdapter(this)
		recyclerView.adapter = adapter
		recyclerView.layoutManager = LinearLayoutManager(activity)
		
		homeViewModel.allPois.observe(viewLifecycleOwner) { poiList ->
			adapter.submitList(poiList.toMutableList())
		}
		
		return root
	}
	
	// Functions for the menu bar.
	/**
	 * Overriden to call [Fragment.setHasOptionsMenu] that signals that this fragment manages
	 * its menu bar icons and functions (in this case the [SearchView]).
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		setHasOptionsMenu(true)
		super.onCreate(savedInstanceState)
	}
	
	/**
	 * Overriden to set the behaviour of the [SearchView] in the menu.
	 */
	@Deprecated("Deprecated in Java")
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		
		val search: MenuItem = menu.findItem(R.id.search)
		val searchView: SearchView = search.actionView as SearchView
		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			
			// Filter as you press Enter
			override fun onQueryTextSubmit(query: String?): Boolean {
				if (query != null) {
					// Observe Pois filtered by name and update the adapter
					homeViewModel.getPoisByNameAlphabetized(query)
						.observe(viewLifecycleOwner) { poiList ->
							adapter.submitList(poiList.toMutableList())
						}
					
					//  When you press Enter, the keyboard disappears
					searchView.clearFocus()
				}
				return true
			}
			
			// Filter as you enter text
			override fun onQueryTextChange(newText: String?): Boolean {
				if (newText != null) {
					// Observe Pois filtered by name and update the adapter
					homeViewModel.getPoisByNameAlphabetized(newText)
						.observe(viewLifecycleOwner) { poiList ->
							adapter.submitList(poiList.toMutableList())
						}
				}
				return true
			}
		})
	}
}
