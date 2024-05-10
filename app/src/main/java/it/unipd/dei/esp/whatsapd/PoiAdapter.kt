package it.unipd.dei.esp.whatsapd

import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PoiAdapter( private val poiImageView: ImageView ) : RecyclerView.Adapter<PoiAdapter.PoiViewHolder>() {

    // ViewHolder per contenere le viste degli elementi
    class PoiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poiImageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    // Crea nuovi ViewHolder (invocato dal layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_poi, parent, false)
        return PoiViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoiViewHolder, position: Int) {

    }

    // Restituisce la dimensione dell'elenco (invocato dal layout manager)
    override fun getItemCount(): Int {
        return 1 //messo a caso sennò dava errore
    }



}


