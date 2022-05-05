package com.mclowicz.mcorganizer.presentation.spots

import com.google.android.gms.maps.model.LatLng

sealed class SpotsViewEvent {
    class OpenDialog(val isOpen: Boolean) : SpotsViewEvent()
    class OnSpotLongClick(val latLng: LatLng? = null, val isOpen: Boolean? = null) : SpotsViewEvent()
}
