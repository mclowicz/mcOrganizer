package com.mclowicz.mcorganizer.data.database.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mclowicz.mcorganizer.data.database.note.NoteDao
import com.mclowicz.mcorganizer.data.database.note.NoteEntity
import com.mclowicz.mcorganizer.data.database.spot.SpotDao
import com.mclowicz.mcorganizer.data.database.spot.SpotEntity

@Database(entities = [NoteEntity::class, SpotEntity::class], version = 3)
abstract class OrganizerDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun spotDao(): SpotDao
}