package it.unipd.dei.esp.whatsapd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PoiListRecyclerViewAdapter(private val poiList: List<Poi>) :
    RecyclerView.Adapter<PoiListRecyclerViewAdapter.PoiViewHolder>() {

    // ViewHolder per contenere le viste degli elementi
    class PoiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poiImageView: ImageView = itemView.findViewById(R.id.poi_image)
        private val poiTitle: TextView = itemView.findViewById(R.id.poi_name)

        fun bind(poi_name: String, image_name: String) {
            // poiImageView.src // TODO
            poiTitle.text = poi_name
        }
    }

    // Crea nuovi ViewHolder (invocato dal layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_poi, parent, false)
        return PoiViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoiViewHolder, position: Int) {
        val poi: Poi = poiList[position]
        holder.bind(poi.name, poi.photo_path)
    }

    // Restituisce la dimensione dell'elenco (invocato dal layout manager)
    override fun getItemCount(): Int {
        return poiList.size
    }


}


