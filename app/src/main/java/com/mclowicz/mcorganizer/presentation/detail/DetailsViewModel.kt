package com.mclowicz.mcorganizer.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mclowicz.mcorganizer.domain.model.Note
import com.mclowicz.mcorganizer.domain.model.fromNote
import com.mclowicz.mcorganizer.domain.model.fromNoteEntity
import com.mclowicz.mcorganizer.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val notesRepository: NoteRepository
) : ViewModel() {

    private val _note: MutableStateFlow<Note?> = MutableStateFlow(null)
    val note = _note.asStateFlow()

    private val _viewEvents = Channel<DetailsViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun getNoteById(noteId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.getNoteByGivenId(noteId)
                .collect {
                    val note = fromNoteEntity(it)
                    _note.update { note }
                }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteNote(
                noteEntity = fromNote(note = note)
            )
        }
    }

    fun triggerEvent(event: DetailsViewEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewEvents.send(element = event)
        }
    }
}