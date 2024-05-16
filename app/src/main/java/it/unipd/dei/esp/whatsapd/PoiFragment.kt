package it.unipd.dei.esp.whatsapd

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.R.id.new_review_submit
import it.unipd.dei.esp.whatsapd.databinding.FragmentPoiBinding


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        activity?.invalidateOptionsMenu()

        _binding = FragmentPoiBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val poiName: String = PoiFragmentArgs.fromBundle(requireArguments()).poiName
        // this gives error
        val poiLiveData: LiveData<Poi> = poiViewModel.getPoiByName(poiName)

        poiLiveData.observe(viewLifecycleOwner) {
            root.findViewById<TextView>(R.id.poi_title).text = it.name
            root.findViewById<WebView>(R.id.poi_description).loadData(it.description, "text/html", "UTF-8")
            root.findViewById<ImageView>(R.id.poi_image).setImageResource(it.photo_id)
            val isFavourite: Boolean = it.favourite // todo use this to set the app bar icon
            //da prendere quando apro il poi
            val accessibilityBanner: CardView = root.findViewById(R.id.accessibility_banner)
            AccessibilityBannerAdapter.AccessibilityBannerViewHolder(accessibilityBanner).bind(it)
        }

        val reviewsRecyclerView: RecyclerView = root.findViewById(R.id.reviews_recycler_view)
        val adapter = ReviewListRecyclerViewAdapter()
        reviewsRecyclerView.adapter = adapter
        reviewsRecyclerView.layoutManager = LinearLayoutManager(activity)

        val webView = root.findViewById<WebView>(R.id.poi_description)
        val isDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        if (isDarkMode) {
            webView.setBackgroundColor(resources.getColor(android.R.color.black))
            val webSettings: WebSettings = webView.getSettings()
            webSettings.javaScriptEnabled = true
        } else {
            webView.setBackgroundColor(resources.getColor(android.R.color.white))
        }
        val textColor: Int = if (isDarkMode) {
            android.R.color.white // Colore del testo per dark mode
        } else {
            android.R.color.black // Colore del testo per light mode
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                // Ottieni il valore esadecimale del colore del testo
                val textColorHex = String.format("#%06X", ContextCompat.getColor(requireContext(), textColor))

                // Esegui uno script JavaScript per cambiare il colore del testo nel DOM
                val js = "document.body.style.color = '$textColorHex';"
                webView.evaluateJavascript(js, null)
                //{ result ->
                  //  Log.d("WebView", "Script executed: $result")}
            }
        }



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

        if (username == "" || text == "")
            return null

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

}