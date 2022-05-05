package com.mclowicz.mcorganizer.presentation.detailSpot

import com.mclowicz.mcorganizer.domain.model.Spot

sealed class DetailSpotViewEvent {
    class OpenDeleteDialog(val isOpen: Boolean) : DetailSpotViewEvent()
    class OpenMapIntent(val spot: Spot) : DetailSpotViewEvent()
}