package it.unipd.dei.esp.whatsapd.ui.poi

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.R

class AccessibilityBannerAdapter {
    class AccessibilityBannerViewHolder(val cardView: CardView) {
        fun bind(poi: Poi) {

            cardView.findViewById<ImageView>(R.id.wheelchair_accessible).visibility =
                if (poi.wheelchair_accessible) View.VISIBLE else View.GONE
            cardView.findViewById<ImageView>(R.id.wheelchair_not_accessible).visibility =
                if (poi.wheelchair_accessible) View.GONE else View.VISIBLE

            cardView.findViewById<ImageView>(R.id.blind_accessible).visibility =
                if (poi.blind_accessible) View.VISIBLE else View.GONE
            cardView.findViewById<ImageView>(R.id.blind_not_accessible).visibility =
                if (poi.blind_accessible) View.GONE else View.VISIBLE

            cardView.findViewById<ImageView>(R.id.deaf_accessible).visibility =
                if (poi.deaf_accessible) View.VISIBLE else View.GONE
            cardView.findViewById<ImageView>(R.id.deaf_not_accessible).visibility =
                if (poi.deaf_accessible) View.GONE else View.VISIBLE
        }
    }
}
