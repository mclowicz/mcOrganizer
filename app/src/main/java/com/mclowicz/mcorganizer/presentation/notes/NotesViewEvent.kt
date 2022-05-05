package com.mclowicz.mcorganizer.presentation.notes

import com.mclowicz.mcorganizer.domain.model.Note

sealed class NotesViewEvent {
    class OpenDialog(val isOpen: Boolean) : NotesViewEvent()
    class OpenDeleteDialog(val isOpen: Boolean, val note: Note? = null) : NotesViewEvent()
}