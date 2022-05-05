package com.mclowicz.mcorganizer.di

import android.content.Context
import androidx.room.Room
import com.mclowicz.mcorganizer.data.database.note.NoteDao
import com.mclowicz.mcorganizer.data.database.common.OrganizerDatabase
import com.mclowicz.mcorganizer.data.database.spot.SpotDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideOrganizerDatabase(
        @ApplicationContext context: Context
    ): OrganizerDatabase = Room.databaseBuilder(
        context, OrganizerDatabase::class.java,
        "organizer-database"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideNoteDao(
        organizerDatabase: OrganizerDatabase
    ): NoteDao = organizerDatabase.noteDao()

    @Provides
    @Singleton
    fun provideSpotDao(
        organizerDatabase: OrganizerDatabase
    ): SpotDao = organizerDatabase.spotDao()
}