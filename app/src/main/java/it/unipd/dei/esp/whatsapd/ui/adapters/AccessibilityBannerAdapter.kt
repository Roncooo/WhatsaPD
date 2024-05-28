package it.unipd.dei.esp.whatsapd.ui.adapters

import android.view.View
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.databinding.AccessibilityBannerBinding
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.ui.poi.PoiFragment

/**
 * Adapter for displaying the accessibility banner ([R.layout.accessibility_banner]) for a [Poi]
 * in [PoiFragment].
 */
class AccessibilityBannerAdapter {
	class AccessibilityBannerViewHolder(private val accessibilityBannerBinding: AccessibilityBannerBinding) {
		fun bind(poi: Poi) {
			// Manages the visibility of the wheelchair accessibility icon
			accessibilityBannerBinding.wheelchairAccessible.visibility =
				if (poi.wheelchairAccessible) View.VISIBLE else View.GONE
			accessibilityBannerBinding.wheelchairNotAccessible.visibility =
				if (poi.wheelchairAccessible) View.GONE else View.VISIBLE
			
			// Manages the visibility of the blind accessibility icon
			accessibilityBannerBinding.blindAccessible.visibility =
				if (poi.blindAccessible) View.VISIBLE else View.GONE
			accessibilityBannerBinding.blindNotAccessible.visibility =
				if (poi.blindAccessible) View.GONE else View.VISIBLE
			
			// Manages the visibility of the deaf accessibility icon
			accessibilityBannerBinding.deafAccessible.visibility =
				if (poi.deafAccessible) View.VISIBLE else View.GONE
			accessibilityBannerBinding.deafNotAccessible.visibility =
				if (poi.deafAccessible) View.GONE else View.VISIBLE
		}
	}
}
