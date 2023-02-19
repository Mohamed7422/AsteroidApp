package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AsteroidDao {
    @Query("SELECT * FROM Asteroid ORDER BY date(closeApproachDate) ASC")
    fun getAllAsteroids(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate =:date ORDER BY date(closeApproachDate) ASC")
    fun getAsteroidToday(date: String): LiveData<List<Asteroid>>


    @Query("SELECT * FROM Asteroid WHERE closeApproachDate BETWEEN :startDate AND :endData ORDER BY date(closeApproachDate)")
    fun getAsteroidSpecificDate(startDate: String, endData: String): LiveData<List<Asteroid>>


    /**Insert*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroid: List<Asteroid>): List<Long>


    @Query("DELETE FROM Asteroid")
    fun deleteAll()

    /**
     * remove the data in the list and
     * update by the new one
     * */
    @Transaction
    fun updateData(asteroids: List<Asteroid>) : List<Long>{
        deleteAll()
        return insertAll(asteroids)
    }



}