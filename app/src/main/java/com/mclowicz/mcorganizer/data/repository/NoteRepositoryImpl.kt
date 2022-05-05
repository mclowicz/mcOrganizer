package com.mclowicz.mcorganizer.data.repository

import com.mclowicz.mcorganizer.data.database.note.NoteDao
import com.mclowicz.mcorganizer.data.database.note.NoteEntity
import com.mclowicz.mcorganizer.data.database.spot.SpotDao
import com.mclowicz.mcorganizer.data.database.spot.SpotEntity
import com.mclowicz.mcorganizer.domain.repository.NoteRepository
import kotlinx.coroutines.flow.flow

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val spotDao: SpotDao
) : NoteRepository {

    override suspend fun insertNote(noteEntity: NoteEntity) =
        noteDao.insertNote(noteEntity = noteEntity)

    override suspend fun deleteNote(noteEntity: NoteEntity) =
        noteDao.deleteNote(noteEntity = noteEntity)

    override fun getNotesByGivenDate(givenDate: String) = flow {
            emit(noteDao.getNotesByGivenDate(givenDate = givenDate))
    }

    override fun getNoteByGivenId(noteId: String) = flow {
        emit(noteDao.getNoteByGivenId(noteId = noteId))
    }

    override suspend fun insertSpot(spotEntity: SpotEntity) {
        spotDao.insertSpot(spotEntity = spotEntity)
    }

    override suspend fun deleteSpot(spotEntity: SpotEntity) {
        spotDao.deleteSpot(spotEntity = spotEntity)
    }

    override fun getSpotsByGivenDate(givenDate: String) = flow {
        emit(spotDao.getSpotsByGivenDate(givenDate = givenDate))
    }

    override fun getSpotByGivenId(id: String) = flow {
        emit(spotDao.getSpotByGivenId(id))
    }

    override fun getSpots() = flow {
        emit(spotDao.getSpots())
    }

    override fun getNotes() = flow {
        emit(noteDao.getNotes())
    }

    override fun searchNote(search: String) = flow {
        emit(noteDao.searchNote(search = search))
    }

    override fun searchSpot(search: String) = flow {
        emit(spotDao.searchSpot(search = search))
    }
}