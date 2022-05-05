package com.mclowicz.mcorganizer.presentation.search

sealed class SearchViewEvent {
    class NavigateToNoteDetailScreen(val id: String) : SearchViewEvent()
    class NavigateToSpotDetailScreen(val id: String) : SearchViewEvent()
}