package com.mclowicz.mcorganizer.di

import android.content.Context
import com.mclowicz.mcorganizer.data.repository.DataStoreRepositoryImpl
import com.mclowicz.mcorganizer.data.repository.NoteRepositoryImpl
import com.mclowicz.mcorganizer.data.database.note.NoteDao
import com.mclowicz.mcorganizer.data.database.spot.SpotDao
import com.mclowicz.mcorganizer.domain.repository.DataStoreRepository
import com.mclowicz.mcorganizer.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDataStoreRepositoryImpl(
        @ApplicationContext context: Context
    ) = DataStoreRepositoryImpl(context = context)

    @Provides
    @Singleton
    fun providesDataStoreRepository(
        dataStoreRepositoryImpl: DataStoreRepositoryImpl
    ): DataStoreRepository = dataStoreRepositoryImpl

    @Provides
    @Singleton
    fun provideNoteRepositoryImpl(
        noteDao: NoteDao,
        spotDao: SpotDao
    ) = NoteRepositoryImpl(noteDao = noteDao, spotDao = spotDao)

    @Provides
    @Singleton
    fun provideNoteRepository(
        noteRepositoryImpl: NoteRepositoryImpl
    ) : NoteRepository = noteRepositoryImpl
}