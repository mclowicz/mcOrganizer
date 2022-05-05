package com.mclowicz.mcorganizer.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mclowicz.mcorganizer.domain.model.fromNoteEntity
import com.mclowicz.mcorganizer.domain.model.fromSpotEntity
import com.mclowicz.mcorganizer.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository
): ViewModel() {

    private val _homeViewState: MutableStateFlow<HomeViewState> = MutableStateFlow(HomeViewState.Idle)
    val homeViewState = _homeViewState.asStateFlow()

    private val _viewEvents = Channel<HomeViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNotes().combine(repository.getSpots()) { notes, spots ->
                val sortedNotes = notes.sortedByDescending { it.timeStamp }.take(3)
                val sortedSpots = spots.sortedByDescending { it.timeStamp }.take(3)
                Pair(
                    sortedNotes.map { fromNoteEntity(noteEntity = it) }.toMutableList(),
                    sortedSpots.map { fromSpotEntity(spotEntity = it) }.toMutableList()
                )
            }.collect {
                val homeComponents = HomeScreenComposition.compose(it)
                _homeViewState.update {
                    HomeViewState.Content(
                        components = homeComponents.toMutableList()
                    )
                }
            }
        }
    }

    fun triggerEvent(event: HomeViewEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewEvents.send(element = event)
        }
    }
}