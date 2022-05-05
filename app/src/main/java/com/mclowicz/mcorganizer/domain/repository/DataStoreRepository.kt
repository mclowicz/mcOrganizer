package com.mclowicz.mcorganizer.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveOnBoardingState(completed: Boolean)

    fun readOnBoardingState(): Flow<Boolean>
}