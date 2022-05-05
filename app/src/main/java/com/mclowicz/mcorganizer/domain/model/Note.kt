package com.mclowicz.mcorganizer.domain.model

import com.mclowicz.mcorganizer.data.database.note.NoteEntity

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val date: String,
    val time: String,
    val timeStamp: Long
)

fun fromNoteEntity(noteEntity: NoteEntity) = Note(
    id = noteEntity.id,
    title = noteEntity.title,
    content = noteEntity.content,
    date = noteEntity.date,
    time = noteEntity.time,
    timeStamp = noteEntity.timeStamp
)

fun fromNote(note: Note) = NoteEntity(
    id = note.id,
    title = note.title,
    content = note.content,
    date = note.date,
    time = note.time,
    timeStamp = note.timeStamp
)