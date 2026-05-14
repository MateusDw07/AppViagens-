package com.example.logincompose.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import java.util.Locale

class LocationHelper(
    private val context: Context
) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getCidade(onResult: (String?) -> Unit) {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                if (location != null) {

                    val geocoder = Geocoder(
                        context,
                        Locale.getDefault()
                    )

                    val addresses = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )

                    val cidade = addresses
                        ?.firstOrNull()
                        ?.subAdminArea

                    onResult(cidade)

                } else {

                    onResult(null)
                }
            }
    }
}