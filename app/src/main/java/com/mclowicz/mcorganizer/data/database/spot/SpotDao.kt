package com.mclowicz.mcorganizer.data.database.spot

import androidx.room.*

@Dao
interface SpotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpot(spotEntity: SpotEntity)

    @Delete
    suspend fun deleteSpot(spotEntity: SpotEntity)

    @Query("SELECT * FROM spotentity WHERE date LIKE :givenDate")
    fun getSpotsByGivenDate(givenDate: String): List<SpotEntity>

    @Query("SELECT * FROM spotentity WHERE id LIKE :id")
    fun getSpotByGivenId(id: String): SpotEntity

    @Query("SELECT * FROM spotentity")
    fun getSpots(): List<SpotEntity>

    @Query("SELECT * FROM spotentity WHERE name LIKE '%' || :search || '%'")
    fun searchSpot(search: String): List<SpotEntity>
}