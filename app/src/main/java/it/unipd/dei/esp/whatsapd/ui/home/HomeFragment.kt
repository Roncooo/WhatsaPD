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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.Poi
import it.unipd.dei.esp.whatsapd.PoiListRecyclerViewAdapter
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.databinding.FragmentHomeBinding
import it.unipd.dei.esp.whatsapd.ui.nearme.HomeViewModel
import it.unipd.dei.esp.whatsapd.ui.nearme.HomeViewModelFactory

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((activity?.application as Application).repository)
    }
    private lateinit var adapter: PoiListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        activity?.invalidateOptionsMenu()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.poiRecyclerView
        adapter = PoiListRecyclerViewAdapter(requireContext(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            GridLayoutManager(activity, resources.getInteger(R.integer.grid_column_count))

        homeViewModel.allPois.observe(viewLifecycleOwner) { pois ->
            // this is necessary to let the adapter insert the banner at the beginning of the recycler view
            // without skipping a "real" Poi
            val dummy = Poi(
                "", 0.0, 0.0, "", 0, true, true, true, true
            )
            val list = listOf(dummy) + pois
            list.let { adapter.submitList(it) }
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Functions for the search bar
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar_home, menu)
        val search = menu.findItem(R.id.search)
        val searchView = search?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    homeViewModel.getPoisByNameAlphabetized(query)
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

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
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