package com.mclowicz.mcorganizer.domain.repository

import com.mclowicz.mcorganizer.data.database.note.NoteEntity
import com.mclowicz.mcorganizer.data.database.spot.SpotEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun insertNote(noteEntity: NoteEntity)

    suspend fun deleteNote(noteEntity: NoteEntity)

    fun getNotesByGivenDate(givenDate: String): Flow<List<NoteEntity>>

    fun getNoteByGivenId(noteId: String): Flow<NoteEntity>

    suspend fun insertSpot(spotEntity: SpotEntity)

    suspend fun deleteSpot(spotEntity: SpotEntity)

    fun getSpotsByGivenDate(givenDate: String): Flow<List<SpotEntity>>

    fun getSpotByGivenId(id: String): Flow<SpotEntity>

    fun getSpots(): Flow<List<SpotEntity>>

    fun getNotes(): Flow<List<NoteEntity>>

    fun searchNote(search: String): Flow<List<NoteEntity>>

    fun searchSpot(search: String): Flow<List<SpotEntity>>
}