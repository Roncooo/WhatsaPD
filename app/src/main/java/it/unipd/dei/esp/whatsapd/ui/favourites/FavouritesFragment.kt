package it.unipd.dei.esp.whatsapd.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.databinding.FragmentFavouritesBinding
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiListRecyclerViewAdapter

class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val favouritesViewModel: FavouritesViewModel by viewModels {
        FavouritesViewModelFactory((activity?.application as Application).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Invalidate the options menu to ensure it's recreated when the fragment is displayed
        activity?.invalidateOptionsMenu()

        // Inflate the layout for this fragment using View Binding
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize RecyclerView and its adapter
        val recyclerView: RecyclerView = binding.favRecyclerView
        val adapter = PoiListRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(activity)

        // Observe changes in the list of favourite POIs and update the adapter
        favouritesViewModel.favPois.observe(viewLifecycleOwner) { pois ->
            pois.let { adapter.submitList(it) }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}