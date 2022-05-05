package com.mclowicz.mcorganizer.presentation.spots

import com.mclowicz.mcorganizer.domain.model.Spot

sealed class SpotsViewState {
    object Idle : SpotsViewState()
    class Content(
        val spots: MutableList<Spot> = mutableListOf()
    ) : SpotsViewState()
}
