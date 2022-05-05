package com.mclowicz.mcorganizer.presentation.notes

import com.mclowicz.mcorganizer.domain.model.Note

sealed class NotesViewState {
    object Idle : NotesViewState()
    class Content(
        val currentDate: String,
        val notes: MutableList<Note> = mutableListOf()
    ) : NotesViewState()
}
