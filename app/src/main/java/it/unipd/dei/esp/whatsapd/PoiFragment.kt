package it.unipd.dei.esp.whatsapd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.unipd.dei.esp.whatsapd.databinding.FragmentPoiBinding

class PoiFragment : Fragment() {

    private var _binding: FragmentPoiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val poiViewModel: PoiViewModel by viewModels {
        PoiViewModelFactory((activity?.application as Application).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPoiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val poi_name: String = PoiFragmentArgs.fromBundle(requireArguments()).poiName
        // this gives error
        // val poi: Poi = poiViewModel.getPoiByName(poi_name)
        val titleTv: TextView = root.findViewById(R.id.poi_title)
        titleTv.text = poi_name

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}