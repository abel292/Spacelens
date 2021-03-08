package com.abel.spacelens.google_map.geolocalizacion

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

class GeocoderUtil(var context: Context) {

    fun getCityName(myCoordinates: LatLng): String {
        val myCity = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses =
                geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1)
            addresses?.let {
                if (it.size > 0) {
                    return addresses[0].getAddressLine(0)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return myCity
    }

}