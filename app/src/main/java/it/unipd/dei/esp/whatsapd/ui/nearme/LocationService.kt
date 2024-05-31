package it.unipd.dei.esp.whatsapd.ui.nearme

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class LocationService(private val context: Context) {
	
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
	
	fun requestPermissions(activity: Activity?) {
		ActivityCompat.requestPermissions(
			activity!!, arrayOf(
				Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
			), LOCATION_PERMISSION_REQUEST_CODE
		)
	}
	
	@SuppressLint("MissingPermission")
	fun getCurrentLocation() {
		if (checkPermissions()) {
			fusedLocationClient.lastLocation.addOnSuccessListener { location ->
				onLocationResultListener.onLocationResult(location)
			}
		} else {
			shouldRequestLocationAfterPermissionGranted = true
			onLocationResultListener.onPermissionDenied()
			if (checkPermissions()) {
				fusedLocationClient.lastLocation.addOnSuccessListener { location ->
					onLocationResultListener.onLocationResult(location)
				}
			}
		}
	}
	
	fun setOnLocationResultListener(listener: OnLocationResultListener) {
		onLocationResultListener = listener
	}
	
	fun handlePermissionsResult(requestCode: Int, grantResults: IntArray) {
		if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
			if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				if (shouldRequestLocationAfterPermissionGranted) {
					getCurrentLocation()
				}
			} else {
				if (onLocationResultListener != null) {
					onLocationResultListener.onPermissionDenied()
				}
			}
		}
	}
	
	interface OnLocationResultListener {
		fun onLocationResult(location: Location)
		fun onPermissionDenied()
		
	}
	
	companion object {
		const val LOCATION_PERMISSION_REQUEST_CODE = 1000
	}
	
}

