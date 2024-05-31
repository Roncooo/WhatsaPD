package it.unipd.dei.esp.whatsapd.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.databinding.HomeBannerBinding
import it.unipd.dei.esp.whatsapd.databinding.SinglePoiBinding
import it.unipd.dei.esp.whatsapd.databinding.SinglePoiWithDistanceBinding
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiListRecyclerViewAdapter.BannerViewHolder
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiListRecyclerViewAdapter.PoiDistanceViewHolder
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiListRecyclerViewAdapter.PoiViewHolder
import it.unipd.dei.esp.whatsapd.ui.favourites.FavouritesFragment
import it.unipd.dei.esp.whatsapd.ui.favourites.FavouritesViewModel
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragment
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragmentDirections
import it.unipd.dei.esp.whatsapd.ui.nearme.NearMeFragment
import it.unipd.dei.esp.whatsapd.ui.nearme.NearMeViewModel
import it.unipd.dei.esp.whatsapd.ui.nearme.PoiWrapper

/**
 * Adapter for displaying a list of [Poi] in a [RecyclerView].
 * According to fragment, the behaviour of the class changes:
 *  - in [HomeFragment] a banner (see [R.layout.home_banner]) is displayed at the
 *   beginning of the [RecyclerView], before all the [Poi]s in alphabetical order
 *  - in [FavouritesFragment], only the [Poi]s are showed (and only the favourite ones, given
 *   by [FavouritesViewModel] to [FavouritesFragment])
 *  - in [NearMeFragment] the [RecyclerView] holds a list of [PoiWrapper]s holden in
 *   special layout ([R.layout.single_poi_with_distance]) that shows the distance from a
 *   given position (ordered by distance, as given by [NearMeViewModel] to [NearMeFragment])
 *
 * The class has three nested classes that extend [RecyclerView.ViewHolder]: [PoiViewHolder],
 * [PoiDistanceViewHolder], [BannerViewHolder]. The first two classes implement a ```bind```
 * method to map the data of a [Poi] into the graphical container. All three classes have a
 * create function in a companion object: this is needed to let them extend [RecyclerView.ViewHolder]
 * with ```itemView``` while still be bound to the parent ```ViewGroup```.
 */

class PoiListRecyclerViewAdapter(
	private val fragment: Fragment, comparator: DiffUtil.ItemCallback<Poi> = POI_COMPARATOR
) : ListAdapter<Poi, RecyclerView.ViewHolder>(comparator) {
	
	/**
	 * [RecyclerView.ViewHolder] for holding the view of individual [Poi] items.
	 */
	class PoiViewHolder(private val singlePoiBinding: SinglePoiBinding) :
		RecyclerView.ViewHolder(singlePoiBinding.root) {
		
		fun bind(poi: Poi) {
			val poiImageView: ImageView = singlePoiBinding.poiImage
			poiImageView.setImageResource(poi.photoId)
			
			val poiTitle: TextView = singlePoiBinding.poiName
			poiTitle.text = poi.name
			
			singlePoiBinding.root.setOnClickListener {
				val action = HomeFragmentDirections.actionToPoiFragment(poi.name)
				singlePoiBinding.root.findNavController().navigate(action)
			}
		}
		
		companion object {
			fun create(parent: ViewGroup): PoiViewHolder {
				val binding: SinglePoiBinding = SinglePoiBinding.inflate(
					LayoutInflater.from(parent.context), parent, false
				)
				return PoiViewHolder(binding)
			}
		}
	}
	
	
	/**
	 * [RecyclerView.ViewHolder] for holding the view of individual [PoiWrapper] items.
	 */
	class PoiDistanceViewHolder(
		private val singlePoiWithDistanceBinding: SinglePoiWithDistanceBinding, val context: Context
	) : RecyclerView.ViewHolder(singlePoiWithDistanceBinding.root) {
		
		fun bind(poiWrapper: PoiWrapper) {
			val poiImageView: ImageView = singlePoiWithDistanceBinding.poiImage
			poiImageView.setImageResource(poiWrapper.photoId)
			
			val poiTitle: TextView = singlePoiWithDistanceBinding.poiName
			poiTitle.text = poiWrapper.name
			
			val poiDistance: TextView = singlePoiWithDistanceBinding.poiDistance
			val textDistance = poiWrapper.distance.toString() + " m"
			poiDistance.text = textDistance
			
			singlePoiWithDistanceBinding.root.setOnClickListener {
				val action = HomeFragmentDirections.actionToPoiFragment(poiWrapper.name)
				singlePoiWithDistanceBinding.root.findNavController().navigate(action)
			}
		}
		
		companion object {
			fun create(parent: ViewGroup): PoiDistanceViewHolder {
				val binding: SinglePoiWithDistanceBinding = SinglePoiWithDistanceBinding.inflate(
					LayoutInflater.from(parent.context), parent, false
				)
				return PoiDistanceViewHolder(binding, parent.context)
			}
		}
	}
	
	/**
	 * [RecyclerView.ViewHolder] for holding the view of the banner item.
	 */
	class BannerViewHolder(homeBannerBinding: HomeBannerBinding) :
		RecyclerView.ViewHolder(homeBannerBinding.root) {
		companion object {
			fun create(parent: ViewGroup): BannerViewHolder {
				val binding: HomeBannerBinding = HomeBannerBinding.inflate(
					LayoutInflater.from(parent.context), parent, false
				)
				return BannerViewHolder(binding)
			}
		}
	}
	
	/**
	 * Overriden to manage the different behaviour of the recycler view in [HomeFragment]. In that case, a
	 * dummy [Poi] is inserted at the beginning of the [list] so that it can be replaced with the banner.
	 * In all the other cases the list submitted is just the [list] passed as a parameter.
	 */
	override fun submitList(list: MutableList<Poi>?) {
		if (fragment is HomeFragment) {
			val dummy = Poi(
				"", 0.0, 0.0, "", 0, true, true, true, true
			)
			// Adds the dummy Poi at the beginning of the list (index 0)
			list?.add(0, dummy)
		}
		super.submitList(list)
	}
	
	
	/**
	 * Determines the view type for a given position. Returned values are
	 *  - [BANNER_VIEW_TYPE]
	 *  - [POI_DISTANCE_VIEW_TYPE]
	 *  - [POI_VIEW_TYPE]
	 */
	override fun getItemViewType(position: Int): Int {
		return if (fragment is HomeFragment && position == 0) BANNER_VIEW_TYPE
		else if (fragment is NearMeFragment) POI_DISTANCE_VIEW_TYPE
		else POI_VIEW_TYPE
	}
	
	/**
	 * Creates new [RecyclerView.ViewHolder] instances (called by the layout manager).
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return when (viewType) {
			BANNER_VIEW_TYPE -> BannerViewHolder.create(parent)
			POI_VIEW_TYPE -> PoiViewHolder.create(parent)
			POI_DISTANCE_VIEW_TYPE -> PoiDistanceViewHolder.create(parent)
			
			else -> throw IllegalArgumentException("Invalid viewType")
		}
	}
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (holder) {
			is BannerViewHolder -> {
				// intentionally left blank
				// layout home_banner is inflated, it already contains img and text
			}
			
			// smart cast of holder to PoiViewHolder
			is PoiViewHolder -> holder.bind(getItem(position) as Poi)
			
			// smart cast of holder to PoiDistanceViewHolder
			is PoiDistanceViewHolder -> holder.bind(getItem(position) as PoiWrapper)
			
			else -> throw IllegalArgumentException("Invalid viewType")
		}
	}
	
	
	companion object {
		
		/** Tags a [RecyclerView.ViewHolder] as a banner for [HomeFragment]*/
		private const val BANNER_VIEW_TYPE = 0
		
		/** Tags a [RecyclerView.ViewHolder] as a [R.layout.single_poi] for [HomeFragment] and [FavouritesFragment] */
		private const val POI_VIEW_TYPE = 1
		
		/** Tags a [RecyclerView.ViewHolder] as a [R.layout.single_poi_with_distance] for [NearMeFragment] */
		private const val POI_DISTANCE_VIEW_TYPE = 2
		
		/**
		 * Comparator that spots the difference between two [Poi] objects.
		 */
		private val POI_COMPARATOR = object : DiffUtil.ItemCallback<Poi>() {
			/**
			 * Checks if two [Poi] items are the same.
			 */
			override fun areItemsTheSame(oldItem: Poi, newItem: Poi): Boolean {
				return oldItem.name == newItem.name
			}
			
			/**
			 * Checks if the contents of two [Poi] items are the same.
			 */
			override fun areContentsTheSame(oldItem: Poi, newItem: Poi): Boolean {
				return oldItem.latitude == newItem.latitude && oldItem.longitude == newItem.longitude && oldItem.description == newItem.description && oldItem.photoId == newItem.photoId && oldItem.favourite == newItem.favourite && oldItem.deafAccessible == newItem.deafAccessible && oldItem.wheelchairAccessible == newItem.wheelchairAccessible && oldItem.blindAccessible == newItem.blindAccessible
			}
		}
	}
}
