package it.unipd.dei.esp.whatsapd.ui.nearme

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
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
		
		if (!this::onLocationResultListener.isInitialized) {
			Log.e(
				this::class.qualifiedName,
				"onLocationResultListener is not initialized and so getCurrentLocation cannot be called"
			)
			return
		}
		
		if (!checkPermissions()) {
			onLocationResultListener.onPermissionDenied()
			return
		}
		
		if (!isLocationEnabled()) {
			AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.location_services_disabled))
				.setMessage(context.getString(R.string.please_enable_location)).setPositiveButton(
					context.getString(R.string.settings_button)
				) { _, _ ->
					val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
					context.startActivity(intent)
				}.setNegativeButton(context.getString(R.string.cancel), null).show()
			return
		}
		
		/*
		 * Sets up a request to obtain the current location of the device with high accuracy and
		 * with a maximum duration of 10 seconds. Uses cancellation token to allow the request to
		 * be canceled if needed.
		 */
		val currentLocationRequest =
			CurrentLocationRequest.Builder().setDurationMillis(10000).setMaxUpdateAgeMillis(10000)
				.setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
		fusedLocationClient.getCurrentLocation(
			currentLocationRequest, CancellationTokenSource().token
		).addOnSuccessListener { location ->
			if (location != null) onLocationResultListener.onLocationResult(location)
		}
	}
	
	fun isLocationEnabled(): Boolean {
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
