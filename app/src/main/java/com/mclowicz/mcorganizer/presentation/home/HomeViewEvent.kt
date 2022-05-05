package com.mclowicz.mcorganizer.presentation.home

sealed class HomeViewEvent() {
    class OpenDetailScreen(val id: String): HomeViewEvent()
    class OpenDetailSpotScreen(val id: String): HomeViewEvent()
}