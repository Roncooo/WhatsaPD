package it.unipd.dei.esp.whatsapd.ui.poi

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.Application
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.databinding.FragmentPoiBinding
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.repository.database.Review
import it.unipd.dei.esp.whatsapd.ui.adapters.AccessibilityBannerAdapter
import it.unipd.dei.esp.whatsapd.ui.adapters.ReviewListRecyclerViewAdapter
import kotlinx.coroutines.launch

class PoiFragment : Fragment() {
	
	private val poiViewModel: PoiViewModel by viewModels {
		PoiViewModelFactory((activity?.application as Application).repository)
	}
	private val reviewViewModel: ReviewViewModel by viewModels {
		ReviewViewModelFactory((activity?.application as Application).repository)
	}
	
	/** Initialized in [onCreateView] */
	private lateinit var currentPoiLiveData: LiveData<Poi>
	
	/** Initialized in [onCreateView] */
	private lateinit var poiName: String
	
	private var _binding: FragmentPoiBinding? = null
	
	// This properties are only valid between onCreateView and onDestroyView.
	private val binding get() = _binding!!
	private val accessibilityBannerBinding get() = binding.accessibilityBanner
	private val reviewsLayoutBinding get() = binding.reviewsLayout
	private val newReviewBinding get() = reviewsLayoutBinding.newReview
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		
		_binding = FragmentPoiBinding.inflate(inflater, container, false)
		val root = binding.root
		
		// Invalidates the options menu to ensure it's recreated when the fragment is displayed
		activity?.invalidateOptionsMenu()
		
		// Takes the poiName from nagivation arguments and uses it to retreive a Poi in the viewModel
		poiName = PoiFragmentArgs.fromBundle(requireArguments()).poiName
		currentPoiLiveData = poiViewModel.getPoiByName(poiName)
		
		currentPoiLiveData.observe(viewLifecycleOwner) { poi ->
			
			binding.poiTitle.text = poi.name
			
			// The webview is used to display the description of each Poi as an HTML text
			val webView = binding.poiDescription
			webView.setBackgroundColor(Color.TRANSPARENT)
			val html: String = addStyle(poi.description)
			webView.loadData(
				html, "text/html", "UTF-8"
			)
			
			binding.poiImage.setImageResource(poi.photoId)
			
			// Bind accessibility features to UI elements
			AccessibilityBannerAdapter.AccessibilityBannerViewHolder(accessibilityBannerBinding)
				.bind(poi)
			
			// Initialize RecyclerView for the reviews and its adapter
			val reviewsRecyclerView: RecyclerView = reviewsLayoutBinding.reviewsRecyclerView
			val adapter = ReviewListRecyclerViewAdapter()
			reviewsRecyclerView.adapter = adapter
			reviewsRecyclerView.layoutManager = LinearLayoutManager(activity)
			
			// Observe changes in reviews LiveData and update RecyclerView accordingly
			val reviewLiveData: LiveData<List<Review>> =
				reviewViewModel.getAllReviewsOfPoiByRating(poiName)
			reviewLiveData.observe(viewLifecycleOwner) { reviews ->
				reviews.let { adapter.submitList(reviews) }
			}
			
		}
		
		// Submission of new review
		val newReviewSubmitButton: ImageButton = newReviewBinding.newReviewSubmit
		newReviewSubmitButton.setOnClickListener {
			val newReview = takeReview(poiName)
			if (newReview != null) {
				reviewViewModel.insert(newReview)
				clearNewReview(root)
			}
		}
		
		return root
	}
	
	/**
	 * Overriden to define a callback for the back button press that lets the user return to
	 * previous [Fragment].
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
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
		// Nullify binding to avoid memory leaks
		_binding = null
	}
	
	// Functions for the menu bar.
	/**
	 * Overriden to call [Fragment.setHasOptionsMenu] that signals that this fragment manages
	 * its menu bar icons and functions (in this case the favorite menu button).
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		setHasOptionsMenu(true)
		super.onCreate(savedInstanceState)
	}
	
	/**
	 * Overriden to manage the behaviour of favourite icon button.
	 * This function sets an observer to keep the icon updated to the status of the [Poi] and
	 * sets the click listener to change the status of the [Poi].
	 * In the lifecycle of [Fragment], [onCreateOptionsMenu] comes after [onCreateView] so at this time
	 * [currentPoiLiveData] is certainly already initialized.
	 */
	@RequiresApi(Build.VERSION_CODES.O)
	@Deprecated("Deprecated in Java")
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		
		val favoriteMenuButton: MenuItem = menu.findItem(R.id.favorite)
		
		// Sets the icon of the favourite button based on the value of boolean var favourite
		// Thanks to the observer, the icon will change each time the poi changes
		currentPoiLiveData.observe(viewLifecycleOwner) { poi ->
			if (poi.favourite) {
				favoriteMenuButton.setIcon(R.drawable.baseline_favorite_24)
				favoriteMenuButton.setChecked(true)
				favoriteMenuButton.setContentDescription(
					requireContext().getString(R.string.favourite_set_description)
				)
			} else {
				favoriteMenuButton.setIcon(R.drawable.baseline_favorite_border_24)
				favoriteMenuButton.setChecked(false)
				favoriteMenuButton.setContentDescription(
					requireContext().getString(R.string.favourite_unset_description)
				)
			}
		}
		
		// When the favourite button is clicked, the value of poi.favourite is changed
		favoriteMenuButton.setOnMenuItemClickListener {
			currentPoiLiveData.observe(viewLifecycleOwner, (object : Observer<Poi> {
				// Necessary to remove the observer
				var obs = this
				override fun onChanged(value: Poi) {
					lifecycleScope.launch {
						poiViewModel.setFavourite(poiName, !value.favourite)
						
						if (!value.favourite) {
							view?.announceForAccessibility(
								requireContext().getString(R.string.announce_favourite_set)
							)
						} else {
							view?.announceForAccessibility(
								requireContext().getString(R.string.announce_favourite_unset)
							)
						}
						
						// This is a one-time (per click) operation so the observer is removed
						currentPoiLiveData.removeObserver(obs)
					}
				}
			}))
			
			true
		}
	}
	
	/**
	 * Adds CSS styling to the given HTML string [innerHtml].
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
	 * Clears the input fields after the submission of a new review.
	 */
	private fun clearNewReview(root: View) {
		val usernameEditText = newReviewBinding.newReviewUsername
		val reviewTextEditText = newReviewBinding.newReviewText
		val reviewRatingBar = newReviewBinding.newReviewRatingBar
		
		usernameEditText.text.clear()
		reviewRatingBar.rating = resources.getInteger(R.integer.default_star_number).toFloat()
		reviewTextEditText.text.clear()
	}
	
	/**
	 * Extracts a review from the input fields and returns a [Review] object if the inputs are valid
	 * (not empty); otherwise, returns null.
	 */
	private fun takeReview(poiName: String): Review? {
		val usernameEditText = newReviewBinding.newReviewUsername
		val reviewTextEditText = newReviewBinding.newReviewText
		val reviewRatingBar = newReviewBinding.newReviewRatingBar
		
		val username: String = usernameEditText.text.toString()
		val text: String = reviewTextEditText.text.toString()
		val rating: Byte = reviewRatingBar.rating.toInt().toByte()
		
		if (username == "" || text == "") return null
		
		return Review(username, poiName, rating, text)
	}
}
