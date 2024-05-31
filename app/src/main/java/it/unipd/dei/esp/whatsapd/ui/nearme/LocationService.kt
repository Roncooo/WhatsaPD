package it.unipd.dei.esp.whatsapd.ui.nearme

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import it.unipd.dei.esp.whatsapd.R


/**
 * Manages the logic of permissions and location for [NearMeFragment] using
 * [FusedLocationProviderClient]. To use [LocationService] you need to
 * 1. create an instance of [LocationService]
 * 2. set a location result listener using [setOnLocationResultListener]
 * 3. call [getCurrentLocation]
 */
class LocationService(private val fragment: Fragment) {
	
	private val context = fragment.requireContext()
	
	private val fusedLocationClient: FusedLocationProviderClient =
		LocationServices.getFusedLocationProviderClient(context)
	
	private lateinit var onLocationResultListener: OnLocationResultListener
	
	private var shouldRequestLocationAfterPermissionGranted = false
	
	fun checkPermissions(): Boolean {
		return (ContextCompat.checkSelfPermission(
			context, Manifest.permission.ACCESS_FINE_LOCATION
		) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
			context, Manifest.permission.ACCESS_COARSE_LOCATION
		) == PackageManager.PERMISSION_GRANTED)
	}
	
	fun requestPermissions() {
		fragment.requestPermissions(
			arrayOf(
				Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
			), LOCATION_PERMISSION_REQUEST_CODE
		)
	}
	
	@SuppressLint("MissingPermission")
	fun getCurrentLocation() {
		if (!checkPermissions()) {
			shouldRequestLocationAfterPermissionGranted = true
			onLocationResultListener.onPermissionDenied()
			requestPermissions()
			return
		}
		
		if (!isLocationEnabled()) {
			AlertDialog.Builder(context)
				.setTitle(fragment.requireContext().getString(R.string.location_services_disabled))
				.setMessage(fragment.requireContext().getString(R.string.please_enable_location))
				.setPositiveButton(
					fragment.requireContext().getString(R.string.settings_button)
				) { _, _ ->
					val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
					context.startActivity(intent)
				}.setNegativeButton(fragment.requireContext().getString(R.string.cancel), null)
				.show()
			return
		}
		
		val currentLocationRequest =
			CurrentLocationRequest.Builder().setDurationMillis(10000).setMaxUpdateAgeMillis(10000)
				.setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
		val cancellationToken = CancellationTokenSource().token
		fusedLocationClient.getCurrentLocation(currentLocationRequest, cancellationToken)
			.addOnSuccessListener { location ->
				if (location != null) onLocationResultListener.onLocationResult(location)
			}
		
	}
	
	private fun isLocationEnabled(): Boolean {
		val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
			LocationManager.NETWORK_PROVIDER
		)
	}
	
	fun setOnLocationResultListener(listener: OnLocationResultListener) {
		onLocationResultListener = listener
	}
	
	interface OnLocationResultListener {
		fun onLocationResult(location: Location?)
		fun onPermissionDenied()
	}
	
	companion object {
		const val LOCATION_PERMISSION_REQUEST_CODE = 1000
	}
	
}
