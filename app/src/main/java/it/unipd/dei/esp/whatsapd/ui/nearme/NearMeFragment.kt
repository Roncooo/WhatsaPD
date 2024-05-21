package it.unipd.dei.esp.whatsapd.ui.nearme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.databinding.FragmentNearMeBinding
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiDistanceListRecyclerViewAdapter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NearMeFragment : Fragment() {

    private var _binding: FragmentNearMeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val nearmeViewModel: NearMeViewModel by viewModels {
        NearMeViewModelFactory((activity?.application as Application).repository)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Invalidate the options menu to ensure it's recreated when the fragment is displayed
        activity?.invalidateOptionsMenu()
        // Inflate the layout for this fragment using View Binding
        _binding = FragmentNearMeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonPosition: Button = binding.nearMeButton
        // Initialize RecyclerView and its adapter
        val recyclerView: RecyclerView = binding.nearMeRecyclerView
        val adapter = PoiDistanceListRecyclerViewAdapter(this, requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        buttonPosition.setOnClickListener {}

        val currentLatitude = 45.40921
        val currentLongitude = 11.89431

        GlobalScope.launch {
            adapter.submitList(nearmeViewModel.getPoisByDistance(currentLatitude, currentLongitude))
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}