package it.unipd.dei.esp.whatsapd

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
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragment
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragmentDirections

class PoiListRecyclerViewAdapter(
    private val context: Context,
    private val fragment: Fragment,
    comparator: DiffUtil.ItemCallback<Poi> = ALPHABETICAL_COMPARATOR
) : ListAdapter<Poi, RecyclerView.ViewHolder>(comparator) {

    // ViewHolder per contenere le viste degli elementi
    class PoiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            fun create(parent: ViewGroup): PoiViewHolder {
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.single_poi, parent, false)
                return PoiViewHolder(view)
            }
        }
    }


    // ViewHolder per contenere le viste degli elementi
    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bannerImageView: ImageView = itemView.findViewById(R.id.home_banner_photo)
        private val bannerTitle: TextView = itemView.findViewById(R.id.home_banner_text)

        fun bind(title_string: String, image_id: Int) {
            bannerImageView.setImageResource(image_id)
            bannerTitle.text = title_string
        }

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
        else if (viewType == POI_VIEW_TYPE) return PoiViewHolder.create(parent)
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

        private val ALPHABETICAL_COMPARATOR = object : DiffUtil.ItemCallback<Poi>() {
            override fun areItemsTheSame(oldItem: Poi, newItem: Poi): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Poi, newItem: Poi): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}





