package it.unipd.dei.esp.whatsapd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.findNavController
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragmentDirections

    class PoiListRecyclerViewAdapter(private val navController: NavController?, comparator: DiffUtil.ItemCallback<Poi> = ALPHABETICAL_COMPARATOR) :
        ListAdapter<Poi, PoiListRecyclerViewAdapter.PoiViewHolder>(comparator) {

        // Crea nuovi ViewHolder (invocato dal layout manager)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiViewHolder {
            return PoiViewHolder.create(parent)
        }

        override fun onBindViewHolder(holder: PoiViewHolder, position: Int) {
            val current = getItem(position)
            holder.bind(current.name, current.photo_id)
        }

        // ViewHolder per contenere le viste degli elementi
        class PoiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val poiImageView: ImageView = itemView.findViewById(R.id.poi_image)
            private val poiTitle: TextView = itemView.findViewById(R.id.poi_name)

            fun bind(poi_name: String, image_id: Int) {
                poiImageView.setImageResource(image_id)
                poiTitle.text = poi_name

                itemView.setOnClickListener{
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


        companion object {
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





