package com.mcmouse88.incorporate_lifecycle_aware_components.step4

import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner


class BoundLocationManager {

    companion object {
        fun bindLocationListenerIn(
            lifecycleOwner: LifecycleOwner,
            listener: LocationListener,
            context: Context
        ) {
            BoundLocationListener(lifecycleOwner, listener, context)
        }
    }

    @SuppressWarnings("MissingPermission")
    class BoundLocationListener(
        private val lifecycleOwner: LifecycleOwner,
        private val listener: LocationListener,
        context: Context
    ) : LifecycleObserver {

        private var mLocationManager: LocationManager? = context.getSystemService(LocationManager::class.java)

        //TODO: Call this on resume
        fun addLocationListener() {
            // Note: Use the Fused Location Provider from Google Play Services instead.
            // https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderApi

            mLocationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, listener)
            Log.d("BoundLocationMgr", "Listener added")

            // Force an update with the last location, if available.
            val lastLocation = mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocation != null) {
                listener.onLocationChanged(lastLocation)
            }
        }

        //TODO: Call this on pause
        fun removeLocationListener() {
            if (mLocationManager == null) {
                return
            }
            mLocationManager?.removeUpdates(listener)
            mLocationManager = null
            Log.d("BoundLocationMgr", "Listener removed")
        }
    }
}
