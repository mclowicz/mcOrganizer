package com.mclowicz.mcorganizer.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mclowicz.mcorganizer.domain.model.Note
import com.mclowicz.mcorganizer.domain.model.Spot
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
class SearchViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _notes: MutableStateFlow<List<Note>> = MutableStateFlow(mutableListOf())
    val notes = _notes.asStateFlow()

    private val _spots: MutableStateFlow<List<Spot>> = MutableStateFlow(mutableListOf())
    val spots = _spots.asStateFlow()

    private val _viewEvents = Channel<SearchViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun search(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchNote(search).combine(repository.searchSpot(search)) { notes, spots ->
                Pair(notes, spots)
            }
                .collect { results ->
                    _notes.update { results.first.map { fromNoteEntity(it) } }
                    _spots.update { results.second.map { fromSpotEntity(it) } }
                }
        }
    }

    fun triggerViewEvent(event: SearchViewEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewEvents.send(element = event)
        }
    }

    fun resetView() {
        _notes.value = mutableListOf()
        _spots.value = mutableListOf()
    }
}