package it.unipd.dei.esp.whatsapd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        activity?.invalidateOptionsMenu()

        _binding = FragmentPoiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val poi_name: String = PoiFragmentArgs.fromBundle(requireArguments()).poiName
        // this gives error
        val poiLiveData: LiveData<Poi> = poiViewModel.getPoiByName(poi_name)
        poiLiveData.observe(viewLifecycleOwner) {
            root.findViewById<TextView>(R.id.poi_title).text = it.name
            root.findViewById<TextView>(R.id.poi_description).text = it.description
            root.findViewById<ImageView>(R.id.poi_image).setImageResource(it.photo_id)
            val isFavourite: Boolean = it.favourite // todo use this to set the app bar icon

            val accessibilityBanner: CardView = root.findViewById(R.id.accessibility_banner)
            AccessibilityBannerAdapter.AccessibilityBannerViewHolder.bind(accessibilityBanner, it)

        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}