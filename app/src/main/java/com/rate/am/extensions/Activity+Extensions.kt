package com.rate.am.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.rate.am.model.LatLng

fun Activity.mapIntent(latLng: LatLng, title: String) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("geo:<${latLng.lat.toFloat()}>,<${latLng.lng.toFloat()}>?q=<" +
                "${latLng.lat.toFloat()}>,<${latLng.lng.toFloat()}>($title)")
    )
    startActivity(intent)
}

fun Activity.callIntent(phone: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phone"))
    startActivity(intent)
}