package com.mcmouse88.incorporate_lifecycle_aware_components.step4_solution

import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
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
        lifecycleOwner: LifecycleOwner,
        private val listener: LocationListener,
        context: Context
    ) : DefaultLifecycleObserver {

        private var mLocationManager: LocationManager? = context.getSystemService(LocationManager::class.java)

        init {
            lifecycleOwner.lifecycle.addObserver(this)
        }

        override fun onResume(owner: LifecycleOwner) {
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

        override fun onPause(owner: LifecycleOwner) {
            if (mLocationManager == null) {
                return
            }
            mLocationManager?.removeUpdates(listener)
            mLocationManager = null
            Log.d("BoundLocationMgr", "Listener removed")
        }
    }
}
