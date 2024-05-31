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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


/**
 * Manages the logic of permissions and location for [NearMeFragment] using
 * [FusedLocationProviderClient]. To use [LocationService] you need to
 * 1. create an instance of [LocationService]
 * 2. set a location result listener using [setOnLocationResultListener]
 * 3. call [getCurrentLocation]
 * 4. override [Fragment.onRequestPermissionsResult] or [AppCompatActivity.onRequestPermissionsResult]
 * 	  to make something happen when the user interacts with system default Permission Request Dialog
 * 	  (e.g. calling [getCurrentLocation] is the permission is given or signal something to the user
 * 	  if permission is denied)
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
			AlertDialog.Builder(context).setTitle("Location Services Disabled")
				.setMessage("Please enable location services to use this feature.")
				.setPositiveButton("Settings") { _, _ ->
					val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
					context.startActivity(intent)
				}.setNegativeButton("Cancel", null).show()
			// return
		}
		
		
		fusedLocationClient.lastLocation.addOnSuccessListener { location ->
			Log.e("", "on success listener with " + (location == null))
			if (location != null) onLocationResultListener.onLocationResult(location)
			else {
				Toast.makeText(
					fragment.requireContext(), "location is null", Toast.LENGTH_SHORT
				).show()
				
				val currentLocationRequest =
					CurrentLocationRequest.Builder().setDurationMillis(2000)
						.setMaxUpdateAgeMillis(100).setPriority(Priority.PRIORITY_HIGH_ACCURACY)
						.build()
				
				fusedLocationClient.getCurrentLocation(currentLocationRequest, null)
			}
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

