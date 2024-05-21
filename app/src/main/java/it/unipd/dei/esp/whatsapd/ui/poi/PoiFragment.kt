package it.unipd.dei.esp.whatsapd.ui.poi

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.R.id.new_review_submit
import it.unipd.dei.esp.whatsapd.repository.database.Review
import it.unipd.dei.esp.whatsapd.databinding.FragmentPoiBinding
import it.unipd.dei.esp.whatsapd.ui.adapters.ReviewListRecyclerViewAdapter
import kotlinx.coroutines.launch


class PoiFragment : Fragment() {

    private var _binding: FragmentPoiBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val poiViewModel: PoiViewModel by viewModels {
        PoiViewModelFactory((activity?.application as Application).repository)
    }
    private val reviewViewModel: ReviewViewModel by viewModels {
        ReviewViewModelFactory((activity?.application as Application).repository)
    }
    lateinit var poiLiveData: LiveData<Poi>
    lateinit var poiName: String
    private var favoriteMenuButton: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        //Invalidate the options menu to ensure it's recreated when the fragment is displayed
        activity?.invalidateOptionsMenu()
        // Inflate the layout for this fragment using View Binding
        _binding = FragmentPoiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        poiName = PoiFragmentArgs.fromBundle(requireArguments()).poiName
        poiLiveData = poiViewModel.getPoiByName(poiName)
        // Observing changes in the POI LiveData
        poiLiveData.observe(viewLifecycleOwner) {
            root.findViewById<TextView>(R.id.poi_title).text = it.name

            //The webview is used to display the description of each POI
            //The description is a html text in the pois.csv file
            val webView = root.findViewById<WebView>(R.id.poi_description)
            webView.setBackgroundColor(Color.TRANSPARENT)
            val html: String = addStyle(it.description)
            webView.loadData(
                html, "text/html", "UTF-8"
            )

            root.findViewById<ImageView>(R.id.poi_image).setImageResource(it.photo_id)

            //Sets the icon of the favourite button in each POI, based on the value of boolean var favourite
            favoriteMenuButton?.setIcon(
                if (it.favourite) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )
            //Bind accessibility features to UI elements
            val accessibilityBanner: CardView = root.findViewById(R.id.accessibility_banner)
            AccessibilityBannerAdapter.AccessibilityBannerViewHolder(accessibilityBanner).bind(it)
        }

        // Initialize RecyclerView and its adapter
        val reviewsRecyclerView: RecyclerView = root.findViewById(R.id.reviews_recycler_view)
        val adapter = ReviewListRecyclerViewAdapter()
        reviewsRecyclerView.adapter = adapter
        reviewsRecyclerView.layoutManager = LinearLayoutManager(activity)

        //Reviews functions
        reviewViewModel.getAllReviewsOfPoiByRating(poiName).observe(viewLifecycleOwner) { pois ->
            pois.let { adapter.submitList(it) }
        }

        // Observe changes in reviews LiveData and update RecyclerView accordingly
        val reviewLiveData: LiveData<List<Review>> =
            reviewViewModel.getAllReviewsOfPoiByRating(poiName)
        reviewLiveData.observe(viewLifecycleOwner) { reviews ->
            reviews.let { adapter.submitList(it) }
        }

        val newReviewSubmit: ImageButton = root.findViewById(new_review_submit)
        newReviewSubmit.setOnClickListener {
            val newReview = takeReview(root, poiName)
            if (newReview != null) {
                reviewViewModel.insert(newReview)
                clearNewReview(root)
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Define a callback for the back button press
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                navController.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable options menu for fragment
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Functions for the favourite button on the toolbar
     */
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        favoriteMenuButton = menu.findItem(R.id.favorite)!!
        favoriteMenuButton!!.setChecked(true)
        favoriteMenuButton!!.setOnMenuItemClickListener {

            // set the new favourite state of the poi
            poiLiveData.observe(viewLifecycleOwner, (object : Observer<Poi> {
                var obs = this
                override fun onChanged(poi: Poi) {
                    lifecycleScope.launch {
                        poiViewModel.changeFavourite(poiName, !poi.favourite)
                        poiLiveData.removeObserver(obs)
                    }
                }
            }))
            true
        }
    }

    /**
    Adds CSS styling to the given HTML string
    */
    private fun addStyle(innerHtml: String): String {
        val styleStart = "<style>"
        val styleEnd = "</style>"
        val pStyle = "p {text-align: justify}"
        var colorStyle = "*{color:"
        // Determine if the device is in dark mode
        val isDarkMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        // Set the text color based on the mode
        val textColor = if (isDarkMode) "white" else "black"
        colorStyle = "$colorStyle$textColor}"

        return styleStart + pStyle + colorStyle + styleEnd + innerHtml
    }

    /**
    Clears the input fields for a new review.
    */
    private fun clearNewReview(root: View) {
        val usernameEditText = root.findViewById<EditText>(R.id.new_review_username)
        val reviewTextEditText = root.findViewById<EditText>(R.id.new_review_text)
        val reviewRatingBar = root.findViewById<RatingBar>(R.id.new_review_rating_bar)

        usernameEditText.text.clear()
        reviewRatingBar.rating = 3F
        reviewTextEditText.text.clear()
    }
    /**
    Extracts a review from the input fields and returns a Review object if the inputs are valid.
     */
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
}