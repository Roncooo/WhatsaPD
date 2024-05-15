package it.unipd.dei.esp.whatsapd.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        activity?.invalidateOptionsMenu()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.poiRecyclerView
        val navController = findNavController()
        val adapter = PoiListRecyclerViewAdapter(requireContext(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)


        homeViewModel.allPois.observe(viewLifecycleOwner) { pois ->
            // this is necessary to let the adapter insert the banner at the beginning of the recycler view
            // without skipping a "real" Poi
            val dummy = Poi(
                "", 0.0, 0.0, "", 0, true, true, true, true
            )
            val list = listOf(dummy) + pois
            list.let { adapter.submitList(it) }
        }


        view?.findViewById<SearchView>(R.id.search)
            ?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchString: String?): Boolean {
                    Log.e("fanculo fulvio", "ok")
                    var _searchString: String = searchString ?: ""/*
                    homeViewModel.getAllPoisByName(_searchString)
                        .observe(viewLifecycleOwner) { pois ->
                            pois.let { adapter.submitList(it) }
                        }

                     */
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // Chiama la funzione per filtrare la lista quando il testo cambia
                    //adapter.filterPoi(newText.orEmpty().trim())
                    Log.e("fanculo fulvio", "ok")
                    return true
                }
            })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}