package com.mclowicz.mcorganizer.data.database.note

import androidx.room.*

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteEntity: NoteEntity)

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)

    @Query("SELECT * FROM noteentity WHERE date LIKE :givenDate")
    fun getNotesByGivenDate(givenDate: String): List<NoteEntity>

    @Query("SELECT * FROM noteentity WHERE id LIKE :noteId")
    fun getNoteByGivenId(noteId: String): NoteEntity

    @Query("SELECT * FROM noteentity")
    fun getNotes(): List<NoteEntity>

    @Query("SELECT * FROM noteentity WHERE title LIKE '%' || :search || '%' OR content LIKE '%' || :search || '%'")
    fun searchNote(search: String): List<NoteEntity>

}