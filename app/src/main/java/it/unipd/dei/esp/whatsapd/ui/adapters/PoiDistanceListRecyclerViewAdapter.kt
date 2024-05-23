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
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragment
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragmentDirections
import java.lang.String.format

class PoiDistanceListRecyclerViewAdapter(
	private val fragment: Fragment,
	private var context: Context,
	comparator: DiffUtil.ItemCallback<PoiWrapper> = POI_WRAPPER_COMPARATOR
) : ListAdapter<PoiWrapper, RecyclerView.ViewHolder>(comparator) {
	
	// ViewHolder per contenere le viste degli elementi
	class PoiViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
		private val poiImageView: ImageView = itemView.findViewById(R.id.poi_image)
		private val poiTitle: TextView = itemView.findViewById(R.id.poi_name)
		
		fun bind(poi_name: String, image_id: Int, distance: Double) {
			poiImageView.setImageResource(image_id)
			poiTitle.text = poi_name
			
			val textDistance = format(context.getString(R.string.distance_text), distance)
			itemView.findViewById<TextView>(R.id.poi_distance).text = textDistance
			
			itemView.setOnClickListener {
				val action = HomeFragmentDirections.actionToPoiFragment(poi_name)
				itemView.findNavController().navigate(action)
			}
		}
		
		companion object {
			fun create(parent: ViewGroup, context: Context): PoiViewHolder {
				val view: View = LayoutInflater.from(parent.context)
					.inflate(R.layout.single_poi_with_distance, parent, false)
				return PoiViewHolder(view, context)
			}
		}
	}
	
	
	// ViewHolder per contenere le viste degli elementi
	class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val bannerImageView: ImageView = itemView.findViewById(R.id.home_banner_photo)
		private val bannerTitle: TextView = itemView.findViewById(R.id.home_banner_text)
		
		companion object {
			fun create(parent: ViewGroup): BannerViewHolder {
				val view: View =
					LayoutInflater.from(parent.context).inflate(R.layout.home_banner, parent, false)
				return BannerViewHolder(view)
			}
		}
	}
	
	override fun getItemViewType(position: Int): Int {
		return if (fragment is HomeFragment && position == 0) BANNER_VIEW_TYPE else POI_VIEW_TYPE
	}
	
	// Crea nuovi ViewHolder (invocato dal layout manager)
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		if (viewType == BANNER_VIEW_TYPE) return BannerViewHolder.create(parent)
		else if (viewType == POI_VIEW_TYPE) return PoiViewHolder.create(parent, context)
		else throw IllegalArgumentException("Invalid viewType")
	}
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is BannerViewHolder) {
			// intentionally left blank
			// layout home_banner is inflated, it already contains img and text
		} else if (holder is PoiViewHolder) {
			val currentPoiWrapper = getItem(position)
			val currentPoi = currentPoiWrapper.poi
			val currentDistance = currentPoiWrapper.distance
			holder.bind(currentPoi.name, currentPoi.photoId, currentDistance)
			
		} else throw IllegalArgumentException("Invalid viewType")
	}
	
	
	companion object {
		
		private val BANNER_VIEW_TYPE = 0
		private val POI_VIEW_TYPE = 1
		
		private val POI_WRAPPER_COMPARATOR = object : DiffUtil.ItemCallback<PoiWrapper>() {
			override fun areItemsTheSame(oldItem: PoiWrapper, newItem: PoiWrapper): Boolean {
				return oldItem.poi.name == newItem.poi.name
			}
			
			override fun areContentsTheSame(oldItem: PoiWrapper, newItem: PoiWrapper): Boolean {
				return oldItem.poi.latitude == newItem.poi.latitude &&
						oldItem.poi.longitude == newItem.poi.longitude &&
						oldItem.poi.description == newItem.poi.description &&
						oldItem.poi.photoId == newItem.poi.photoId &&
						oldItem.poi.favourite == newItem.poi.favourite &&
						oldItem.poi.deafAccessible == newItem.poi.deafAccessible &&
						oldItem.poi.wheelchairAccessible == newItem.poi.wheelchairAccessible &&
						oldItem.poi.blindAccessible == newItem.poi.blindAccessible
			}
		}
	}
}







