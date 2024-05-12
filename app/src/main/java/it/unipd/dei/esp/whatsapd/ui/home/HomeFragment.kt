package it.unipd.dei.esp.whatsapd.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.PoiListRecyclerViewAdapter
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.databinding.FragmentHomeBinding
import it.unipd.dei.esp.whatsapd.ui.nearme.HomeViewModel
import it.unipd.dei.esp.whatsapd.ui.nearme.HomeViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        val adapter = PoiListRecyclerViewAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val textView: TextView = binding.textHome
        homeViewModel.allPois.observe(viewLifecycleOwner) { pois ->
            pois.let { adapter.submitList(it) }
        }

        val button: Button = root.findViewById(R.id.button)
        button.setOnClickListener {
            // todo, take pois from db and use recycler view
            val action = HomeFragmentDirections.actionNavHomeToPoiFragment("Prato della valle")
            root.findNavController().navigate(action)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}