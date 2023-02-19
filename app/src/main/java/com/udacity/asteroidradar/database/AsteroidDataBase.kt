package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*

@Database(entities = [Asteroid::class], version = 1)
abstract class AsteroidDataBase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    companion object {

        private var Instance: AsteroidDataBase? = null

        fun getDataBase(context: Context): AsteroidDataBase {
            var instance = Instance

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDataBase::class.java,
                    "AsteroidData"
                ).fallbackToDestructiveMigration().build()
                Instance = instance
            }

            return instance
        }


    }
}