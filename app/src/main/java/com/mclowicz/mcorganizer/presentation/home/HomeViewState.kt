package com.mclowicz.mcorganizer.presentation.home

sealed class HomeViewState {
    object Idle : HomeViewState()
    class Content(
        val components: MutableList<HomeComponent> = mutableListOf()
    ) : HomeViewState()
}
