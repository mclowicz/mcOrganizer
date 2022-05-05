package com.mclowicz.mcorganizer.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.mclowicz.mcorganizer.domain.model.Spot

fun Context.openSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Context.openMap(spot: Spot) {
    val gmmIntentUri = Uri.parse("geo:${spot.lat},${spot.lng}")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    mapIntent.resolveActivity(this.packageManager)?.let {
        ContextCompat.startActivity(this, mapIntent, null)
    }
}