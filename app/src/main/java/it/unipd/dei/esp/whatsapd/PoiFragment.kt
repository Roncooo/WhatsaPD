package it.unipd.dei.esp.whatsapd

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.R.id.new_review_submit
import it.unipd.dei.esp.whatsapd.databinding.FragmentPoiBinding
import kotlinx.coroutines.launch


class PoiFragment : Fragment() {

    private var _binding: FragmentPoiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val poiViewModel: PoiViewModel by viewModels {
        PoiViewModelFactory((activity?.application as Application).repository)
    }
    private val reviewViewModel: ReviewViewModel by viewModels {
        ReviewViewModelFactory((activity?.application as Application).repository)
    }
    lateinit var poiLiveData: LiveData<Poi>
    lateinit var poiName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        activity?.invalidateOptionsMenu()
        _binding = FragmentPoiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        poiName = PoiFragmentArgs.fromBundle(requireArguments()).poiName
        poiLiveData = poiViewModel.getPoiByName(poiName)
        poiLiveData.observe(viewLifecycleOwner) {
            root.findViewById<TextView>(R.id.poi_title).text = it.name

            val webView = root.findViewById<WebView>(R.id.poi_description)
            webView.setBackgroundColor(Color.TRANSPARENT)
            val html: String = addStyle(it.description)
            webView.loadData(
                html, "text/html", "UTF-8"
            )

            root.findViewById<ImageView>(R.id.poi_image).setImageResource(it.photo_id)
            val isFavourite: Boolean = it.favourite // todo use this to set the app bar icon

            val accessibilityBanner: CardView = root.findViewById(R.id.accessibility_banner)
            AccessibilityBannerAdapter.AccessibilityBannerViewHolder(accessibilityBanner).bind(it)
        }

        val reviewsRecyclerView: RecyclerView = root.findViewById(R.id.reviews_recycler_view)
        val adapter = ReviewListRecyclerViewAdapter()
        reviewsRecyclerView.adapter = adapter
        reviewsRecyclerView.layoutManager = LinearLayoutManager(activity)

        reviewViewModel.getAllReviewsOfPoiByRating(poiName).observe(viewLifecycleOwner) { pois ->
            pois.let { adapter.submitList(it) }
        }

        val reviewLiveData: LiveData<List<Review>> =
            reviewViewModel.getAllReviewsOfPoiByRating(poiName)
        reviewLiveData.observe(viewLifecycleOwner) { reviews ->
            reviews.let { adapter.submitList(it) }
        }

        val newReviewSubmit: ImageButton = root.findViewById<ImageButton>(new_review_submit)
        newReviewSubmit.setOnClickListener {
            val newReview = takeReview(root, poiName)
            if (newReview != null) {
                reviewViewModel.insert(newReview)
                clearNewReview(root)
            }
        }

        return root
    }


    private fun addStyle(innerHtml: String): String {
        val styleStart = "<style>"
        val styleEnd = "</style>"
        val pStyle = "p {text-align: justify}"

        var colorStyle = "*{color:"
        val isDarkMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        val textColor = if (isDarkMode) "white" else "black"
        colorStyle = "$colorStyle$textColor}"

        return styleStart + pStyle + colorStyle + styleEnd + innerHtml
    }


    private fun clearNewReview(root: View) {
        val usernameEditText = root.findViewById<EditText>(R.id.new_review_username)
        val reviewTextEditText = root.findViewById<EditText>(R.id.new_review_text)
        val reviewRatingBar = root.findViewById<RatingBar>(R.id.new_review_rating_bar)

        usernameEditText.text.clear()
        reviewRatingBar.rating = 3F
        reviewTextEditText.text.clear()
    }

    private fun takeReview(root: View, poiName: String): Review? {
        val usernameEditText = root.findViewById<EditText>(R.id.new_review_username)
        val reviewTextEditText = root.findViewById<EditText>(R.id.new_review_text)
        val reviewRatingBar = root.findViewById<RatingBar>(R.id.new_review_rating_bar)

        val username: String = usernameEditText.text.toString()
        val text: String = reviewTextEditText.text.toString()
        val rating: Byte = reviewRatingBar.rating.toInt().toByte()

        if (username == "" || text == "") return null

        return Review(username, poiName, rating, text)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // callback definition
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                navController.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Functions for the favourite button on the toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar_home, menu)
        val favorite = menu.findItem(R.id.favorite)
        favorite.setChecked(true)   // può servire per cambiare l'icona, al momento non serve a niente
        favorite.setOnMenuItemClickListener {

            poiLiveData.observe(viewLifecycleOwner, (object : Observer<Poi> {
                var obs = this
                override fun onChanged(poi: Poi) {
                    lifecycleScope.launch {
                        poiViewModel.changeFavourite(poiName, !poi.favourite)
                        poiLiveData.removeObserver(obs)
                    }
                }
            }))

            // todo qua bisogna fare la query per mettere come preferito il poi e cambiare l'icona

            true
        }
    }
}


