package com.mclowicz.mcorganizer.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.mclowicz.mcorganizer.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepositoryImpl(private val context: Context) : DataStoreRepository {

    private val Context.dataStore by preferencesDataStore(
        name = ON_BOARDING_PREFERENCES_NAME
    )

    override suspend fun saveOnBoardingState(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ON_BOARDING_KEY] = completed
        }
    }

    override fun readOnBoardingState(): Flow<Boolean> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val onBoardingState = preferences[ON_BOARDING_KEY] ?: false
                onBoardingState
            }
    }

    companion object {
        val ON_BOARDING_KEY = booleanPreferencesKey(name = "on_boarding_completed")
        const val ON_BOARDING_PREFERENCES_NAME = "com.mclowicz.mcorganizer.on_boarding"
    }
}