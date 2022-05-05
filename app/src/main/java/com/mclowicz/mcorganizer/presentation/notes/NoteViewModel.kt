package com.mclowicz.mcorganizer.presentation.notes

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
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _notesViewState: MutableStateFlow<NotesViewState> =
        MutableStateFlow(NotesViewState.Idle)
    val notesViewState = _notesViewState.asStateFlow()

    private val _viewEvents = Channel<NotesViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun setCurrentDate(currentDate: String) {
        _notesViewState.update {
            NotesViewState.Content(
                currentDate = currentDate
            )
        }
        getNotesByGivenDate(currentDate)
    }

    fun getNotesByGivenDate(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getNotesByGivenDate(date)
                .collect { list ->
                    if (list.isEmpty()) {

                    } else {
                        _notesViewState.update {
                            NotesViewState.Content(
                                currentDate = date,
                                notes = list.map {
                                    fromNoteEntity(noteEntity = it)
                                }.toMutableList()
                            )
                        }
                    }
                }
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.insertNote(noteEntity = fromNote(note = note))
            (_notesViewState.value as? NotesViewState.Content)?.let { content ->
                _notesViewState.update {
                    NotesViewState.Content(
                        currentDate = note.date,
                        notes = content.notes.apply { add(note) }
                    )
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(noteEntity = fromNote(note = note))
            (_notesViewState.value as? NotesViewState.Content)?.let { content ->
                _notesViewState.update {
                    NotesViewState.Content(
                        currentDate = note.date,
                        notes = content.notes.apply { remove(note) }
                    )
                }
            }
        }
    }

    fun triggerViewEvent(event: NotesViewEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewEvents.send(element = event)
        }
    }
}