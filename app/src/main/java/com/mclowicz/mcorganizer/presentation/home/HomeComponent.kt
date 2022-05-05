package com.mclowicz.mcorganizer.presentation.home

import com.mclowicz.mcorganizer.domain.model.Note
import com.mclowicz.mcorganizer.domain.model.Spot

sealed class HomeComponent {
    object HeaderComponent : HomeComponent()
    class NotesComponent(val notes: MutableList<Note>) : HomeComponent()
    class SpotsComponent(val spots: MutableList<Spot>) : HomeComponent()
}