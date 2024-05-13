package it.unipd.dei.esp.whatsapd

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView

class AccessibilityBannerAdapter {
    class AccessibilityBannerViewHolder(itemView: View) {

        companion object {

            fun bind(cardView: CardView, poi: Poi) {

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

}
