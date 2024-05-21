package it.unipd.dei.esp.whatsapd.ui.adapters

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
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragment
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragmentDirections

/**
 * Adapter for displaying a list of POIs (Points of Interest) in a RecyclerView.
 */
class PoiListRecyclerViewAdapter(
    private val fragment: Fragment,
    private val singlePoiLayout: Int,
    comparator: DiffUtil.ItemCallback<Poi> = POI_COMPARATOR
) : ListAdapter<Poi, RecyclerView.ViewHolder>(comparator) {

    /**
     * ViewHolder for holding the views of individual POI items.
     */
    class PoiViewHolder(itemView: View, singlePoiLayout: Int) : RecyclerView.ViewHolder(itemView) {
        private val poiImageView: ImageView = itemView.findViewById(R.id.poi_image)
        private val poiTitle: TextView = itemView.findViewById(R.id.poi_name)

        fun bind(poi_name: String, image_id: Int) {
            poiImageView.setImageResource(image_id)
            poiTitle.text = poi_name

            itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionToPoiFragment(poi_name)
                itemView.findNavController().navigate(action)
            }
        }

        companion object {
            /**
             * Creates a new instance of PoiViewHolder.
             */
            fun create(parent: ViewGroup, singlePoiLayout: Int): PoiViewHolder {
                val view: View =
                    LayoutInflater.from(parent.context).inflate(singlePoiLayout, parent, false)
                return PoiViewHolder(view, singlePoiLayout)
            }
        }
    }


    /**
     * ViewHolder for holding the views of the banner item.
     */
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

    /**
     * Determines the view type for a given position.
     */
    override fun getItemViewType(position: Int): Int {
        return if (fragment is HomeFragment && position == 0) BANNER_VIEW_TYPE else POI_VIEW_TYPE
    }

    /**
     * Creates new ViewHolder instances (called by the layout manager).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == BANNER_VIEW_TYPE) return BannerViewHolder.create(parent)
        else if (viewType == POI_VIEW_TYPE) return PoiViewHolder.create(parent, singlePoiLayout)
        else throw IllegalArgumentException("Invalid viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BannerViewHolder) {
            // intentionally left blank
            // layout home_banner is inflated, it already contains img and text
        } else if (holder is PoiViewHolder) {
            val current = getItem(position)
            holder.bind(current.name, current.photo_id)
        } else throw IllegalArgumentException("Invalid viewType")
    }


    companion object {

        private val BANNER_VIEW_TYPE = 0
        private val POI_VIEW_TYPE = 1

        /**
         * Comparator for calculating the difference between two POI objects.
         */
        private val POI_COMPARATOR = object : DiffUtil.ItemCallback<Poi>() {
            override fun areItemsTheSame(oldItem: Poi, newItem: Poi): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Poi, newItem: Poi): Boolean {
                return oldItem.latitude == newItem.latitude && oldItem.longitude == newItem.longitude && oldItem.description == newItem.description && oldItem.photo_id == newItem.photo_id && oldItem.favourite == newItem.favourite && oldItem.deaf_accessible == newItem.deaf_accessible && oldItem.wheelchair_accessible == newItem.wheelchair_accessible && oldItem.blind_accessible == newItem.blind_accessible

            }
        }
    }
}







