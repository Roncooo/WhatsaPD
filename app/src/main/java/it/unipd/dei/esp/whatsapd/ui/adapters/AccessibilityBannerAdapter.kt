package it.unipd.dei.esp.whatsapd.ui.adapters

import android.view.View
import androidx.appcompat.app.AlertDialog
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.databinding.AccessibilityBannerBinding
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.ui.poi.PoiFragment

/**
 * Adapter for displaying the accessibility banner ([R.layout.accessibility_banner]) for a [Poi]
 * in [PoiFragment].
 */
class AccessibilityBannerAdapter {
	class AccessibilityBannerViewHolder(private val binding: AccessibilityBannerBinding) {
		fun bind(poi: Poi) {
			// Manages the visibility of the wheelchair accessibility icon
			if (poi.wheelchairAccessible) {
				binding.wheelchairAccessible.apply {
					visibility = View.VISIBLE
					setOnClickListener {
						AlertDialog.Builder(context)
							.setMessage(context.getString(R.string.alert_dialog_wheelchair_accessible))
							.setTitle(context.getString(R.string.alert_dialog_title)).create()
							.show()
					}
				}
				binding.wheelchairNotAccessible.visibility = View.GONE
			} else {
				binding.wheelchairNotAccessible.apply {
					visibility = View.VISIBLE
					setOnClickListener {
						AlertDialog.Builder(context)
							.setMessage(context.getString(R.string.alert_dialog_wheelchair_not_accessible))
							.setTitle(context.getString(R.string.alert_dialog_title)).create()
							.show()
					}
				}
				binding.wheelchairAccessible.visibility = View.GONE
			}
			
			
			// Manages the visibility of the blind accessibility icon
			if (poi.deafAccessible) {
				binding.deafAccessible.apply {
					visibility = View.VISIBLE
					setOnClickListener {
						AlertDialog.Builder(context)
							.setMessage(context.getString(R.string.alert_dialog_deaf_accessible))
							.setTitle(context.getString(R.string.alert_dialog_title)).create()
							.show()
					}
				}
				binding.deafNotAccessible.visibility = View.GONE
			} else {
				binding.deafNotAccessible.apply {
					visibility = View.VISIBLE
					setOnClickListener {
						AlertDialog.Builder(context)
							.setMessage(context.getString(R.string.alert_dialog_deaf_not_accessible))
							.setTitle(context.getString(R.string.alert_dialog_title)).create()
							.show()
					}
				}
				binding.deafAccessible.visibility = View.GONE
			}
			
			
			// Manages the visibility of the deaf accessibility icon
			if (poi.blindAccessible) {
				binding.blindAccessible.apply {
					visibility = View.VISIBLE
					setOnClickListener {
						AlertDialog.Builder(context)
							.setMessage(context.getString(R.string.alert_dialog_blind_accessible))
							.setTitle(context.getString(R.string.alert_dialog_title)).create()
							.show()
					}
				}
				binding.blindNotAccessible.visibility = View.GONE
			} else {
				binding.blindNotAccessible.apply {
					visibility = View.VISIBLE
					setOnClickListener {
						AlertDialog.Builder(context)
							.setMessage(context.getString(R.string.alert_dialog_blind_not_accessible))
							.setTitle(context.getString(R.string.alert_dialog_title)).create()
							.show()
					}
				}
				binding.blindAccessible.visibility = View.GONE
			}
		}
	}
}
