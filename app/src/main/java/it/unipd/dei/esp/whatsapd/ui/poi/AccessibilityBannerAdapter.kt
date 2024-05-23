package it.unipd.dei.esp.whatsapd.ui.poi

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.repository.database.Poi

//Adapter to manage the accessibility banner for Points of Interest (POI).
class AccessibilityBannerAdapter {
	class AccessibilityBannerViewHolder(val cardView: CardView) {
		fun bind(poi: Poi) {
			// Manages the visibility of the wheelchair accessibility icon
			cardView.findViewById<ImageView>(R.id.wheelchair_accessible).visibility =
				if (poi.wheelchairAccessible) View.VISIBLE else View.GONE
			cardView.findViewById<ImageView>(R.id.wheelchair_not_accessible).visibility =
				if (poi.wheelchairAccessible) View.GONE else View.VISIBLE
			// Manages the visibility of the blind accessibility icon
			cardView.findViewById<ImageView>(R.id.blind_accessible).visibility =
				if (poi.blindAccessible) View.VISIBLE else View.GONE
			cardView.findViewById<ImageView>(R.id.blind_not_accessible).visibility =
				if (poi.blindAccessible) View.GONE else View.VISIBLE
			// Manages the visibility of the deaf accessibility icon
			cardView.findViewById<ImageView>(R.id.deaf_accessible).visibility =
				if (poi.deafAccessible) View.VISIBLE else View.GONE
			cardView.findViewById<ImageView>(R.id.deaf_not_accessible).visibility =
				if (poi.deafAccessible) View.GONE else View.VISIBLE
		}
	}
}
