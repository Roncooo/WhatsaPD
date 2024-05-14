package it.unipd.dei.esp.whatsapd.ui.nearme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.PoiListRecyclerViewAdapter
import it.unipd.dei.esp.whatsapd.databinding.FragmentNearMeBinding

class NearMeFragment : Fragment() {

    private var _binding: FragmentNearMeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val nearmeViewModel: NearMeViewModel by viewModels {
        NearMeViewModelFactory((activity?.application as Application).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        
        activity?.invalidateOptionsMenu()

        _binding = FragmentNearMeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonPosition: Button = binding.nearMeButton
        val recyclerView: RecyclerView = binding.nearMeRecyclerView
        val navController = findNavController()
        val adapter = PoiListRecyclerViewAdapter(navController)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        buttonPosition.setOnClickListener {
            // nearmeViewModel.update()
        }

        nearmeViewModel.allPois.observe(viewLifecycleOwner) { pois ->
            pois.let { adapter.submitList(it) }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}