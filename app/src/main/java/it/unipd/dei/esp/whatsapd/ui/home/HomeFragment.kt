package it.unipd.dei.esp.whatsapd.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiListRecyclerViewAdapter
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.databinding.FragmentHomeBinding
import it.unipd.dei.esp.whatsapd.ui.nearme.HomeViewModel
import it.unipd.dei.esp.whatsapd.ui.nearme.HomeViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((activity?.application as Application).repository)
    }
    private lateinit var adapter: PoiListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Invalidate the options menu to ensure it's recreated when the fragment is displayed
        activity?.invalidateOptionsMenu()

        // Inflate the layout for this fragment using View Binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize RecyclerView and its adapter
        val recyclerView: RecyclerView = binding.poiRecyclerView
        adapter = PoiListRecyclerViewAdapter( this, R.layout.single_poi)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        homeViewModel.allPois.observe(viewLifecycleOwner) { pois ->
            // this is necessary to let the adapter insert the banner at the beginning of the recycler view
            // without skipping a "real" Poi
            // Create a dummy POI to be inserted as a banner at the beginning of the RecyclerView
            val dummy = Poi(
                "", 0.0, 0.0, "", 0, true, true, true, true
            )
            // Combine the dummy POI with the list of actual POIs
            val list = listOf(dummy) + pois
            // Submit the combined list to the adapter
            list.let { adapter.submitList(it) }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Functions for the search bar
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //searchView implementation
        val search = menu.findItem(R.id.search)
        val searchView = search?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            //Filter as you press Enter
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    // Observe POIs filtered by name and update the adapter
                    homeViewModel.getPoisByNameAlphabetized(query)
                        .observe(viewLifecycleOwner) { pois ->
                            val dummy = Poi(
                                "", 0.0, 0.0, "", 0, true, true, true, true
                            )
                            val list = listOf(dummy) + pois
                            list.let { adapter.submitList(it) }
                        }
                    searchView.clearFocus() //When you press Enter, the keyboard disappears
                }
                return true
            }

            //Filter as you enter text
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    // Observe POIs filtered by name and update the adapter
                    homeViewModel.getPoisByNameAlphabetized(newText)
                        .observe(viewLifecycleOwner) { pois ->
                            val dummy = Poi(
                                "", 0.0, 0.0, "", 0, true, true, true, true
                            )
                            val list = listOf(dummy) + pois
                            list.let { adapter.submitList(it) }
                        }
                }
                return true
            }
        })
    }


}