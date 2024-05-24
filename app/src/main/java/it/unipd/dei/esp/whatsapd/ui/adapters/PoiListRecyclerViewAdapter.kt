package it.unipd.dei.esp.whatsapd.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragment
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragmentDirections
import it.unipd.dei.esp.whatsapd.ui.nearme.NearMeFragment
import it.unipd.dei.esp.whatsapd.ui.nearme.PoiWrapper

/**
 * Adapter for displaying a list of ```Poi``` in a ```RecyclerView```.
 * According to ```fragment```, the behaviour of the class changes:
 *  - in ```HomeFragment``` a banner (see ```res/layout/home_banner.xml```) is displayed at the
 *    beginning of the ```RecyclerView```, before all the ```Poi```s in alphabetical order
 *  - in ```FavouriteFragment```, only the ```Poi```s are showed (and only the favourite ones, given
 *    by ```FavouriteViewModel``` to ```FavouriteFragment```)
 *  - in ```NearMeFragment```, the ```RecyclerView``` holds a list of ```PoiWrapper```s holden in
 *    special layout (```res/layout/single_poi_with_distance.xml```) that shows the distance from a
 *    given position (ordered by distance, as given by ```NearMeViewModel``` to ```NearMeFragment```)
 *
 * The class has three nested classes that extend ```RecyclerView.ViewHolder```: ```PoiViewHolder```,
 * ```PoiDistanceViewHolder```, ```BannerViewHolder```. The first two classes implement a ```bind```
 * method to map the data of a ```Poi``` into the graphical container. All three classes have a
 * ```create``` function in a companion object: this is needed to let them extend ```ViewHolder```
 * with ```itemView``` while still be bound to the ```parent ViewGroup```.
 */

class PoiListRecyclerViewAdapter(
	private val fragment: Fragment, comparator: DiffUtil.ItemCallback<Poi> = POI_COMPARATOR
) : ListAdapter<Poi, RecyclerView.ViewHolder>(comparator) {
	
	/**
	 * ViewHolder for holding the views of individual Poi items.
	 */
	class PoiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		
		fun bind(poi: Poi) {
			val poiImageView: ImageView = itemView.findViewById(R.id.poi_image)
			poiImageView.setImageResource(poi.photoId)
			
			val poiTitle: TextView = itemView.findViewById(R.id.poi_name)
			poiTitle.text = poi.name
			
			itemView.setOnClickListener {
				val action = HomeFragmentDirections.actionToPoiFragment(poi.name)
				itemView.findNavController().navigate(action)
			}
		}
		
		companion object {
			fun create(parent: ViewGroup): PoiViewHolder {
				val view: View =
					LayoutInflater.from(parent.context).inflate(R.layout.single_poi, parent, false)
				return PoiViewHolder(view)
			}
		}
	}
	
	
	/**
	 * ViewHolder for holding the views of individual Poi items with their distance from a given position.
	 */
	class PoiDistanceViewHolder(itemView: View, val context: Context) :
		RecyclerView.ViewHolder(itemView) {
		
		fun bind(poiWrapper: PoiWrapper) {
			val poiImageView: ImageView = itemView.findViewById(R.id.poi_image)
			poiImageView.setImageResource(poiWrapper.photoId)
			
			val poiTitle: TextView = itemView.findViewById(R.id.poi_name)
			poiTitle.text = poiWrapper.name
			
			val poiDistance: TextView = itemView.findViewById(R.id.poi_distance)
			val textDistance = String.format(
				context.getString(R.string.distance_text_format), poiWrapper.distance
			)
			poiDistance.text = textDistance
			
			itemView.setOnClickListener {
				val action = HomeFragmentDirections.actionToPoiFragment(poiWrapper.name)
				itemView.findNavController().navigate(action)
			}
		}
		
		companion object {
			fun create(parent: ViewGroup): PoiDistanceViewHolder {
				val view: View = LayoutInflater.from(parent.context)
					.inflate(R.layout.single_poi_with_distance, parent, false)
				return PoiDistanceViewHolder(view, parent.context)
			}
		}
	}
	
	/**
	 * ViewHolder for holding the views of the banner item.
	 */
	class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		companion object {
			fun create(parent: ViewGroup): BannerViewHolder {
				val view: View =
					LayoutInflater.from(parent.context).inflate(R.layout.home_banner, parent, false)
				return BannerViewHolder(view)
			}
		}
	}
	
	/**
	 * Overriden to manage the different behaviour of the recycler view in Home fragment. In that case, a
	 * dummy Poi is inserted at the beginning of the list so that it can be replaced with the banner.
	 * In all the other cases the list submitted is just the list passed as a parameter.
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
	 * Determines the view type for a given position.
	 */
	override fun getItemViewType(position: Int): Int {
		return if (fragment is HomeFragment && position == 0) BANNER_VIEW_TYPE
		else if (fragment is NearMeFragment) POI_DISTANCE_VIEW_TYPE
		else POI_VIEW_TYPE
	}
	
	/**
	 * Creates new ViewHolder instances (called by the layout manager).
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
		
		private const val BANNER_VIEW_TYPE = 0
		private const val POI_VIEW_TYPE = 1
		private const val POI_DISTANCE_VIEW_TYPE = 2
		
		/**
		 * Comparator that spots the difference between two Poi objects.
		 */
		private val POI_COMPARATOR = object : DiffUtil.ItemCallback<Poi>() {
			override fun areItemsTheSame(oldItem: Poi, newItem: Poi): Boolean {
				return oldItem.name == newItem.name
			}
			
			override fun areContentsTheSame(oldItem: Poi, newItem: Poi): Boolean {
				return oldItem.latitude == newItem.latitude && oldItem.longitude == newItem.longitude && oldItem.description == newItem.description && oldItem.photoId == newItem.photoId && oldItem.favourite == newItem.favourite && oldItem.deafAccessible == newItem.deafAccessible && oldItem.wheelchairAccessible == newItem.wheelchairAccessible && oldItem.blindAccessible == newItem.blindAccessible
			}
		}
	}
}







