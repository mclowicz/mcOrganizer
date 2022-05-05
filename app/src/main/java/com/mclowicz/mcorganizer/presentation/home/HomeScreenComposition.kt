package com.mclowicz.mcorganizer.presentation.home

import com.mclowicz.mcorganizer.domain.model.Note
import com.mclowicz.mcorganizer.domain.model.Spot

class HomeScreenComposition() {

    companion object {
        fun compose(
            pair: Pair<MutableList<Note>, MutableList<Spot>>
        ) : List<HomeComponent> {
            return listOf<HomeComponent>(
                HomeComponent.HeaderComponent,
                HomeComponent.NotesComponent(notes = pair.first),
                HomeComponent.SpotsComponent(spots = pair.second)
            )
        }
    }
}