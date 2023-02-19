package com.udacity.asteroidradar.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.model.Repo
import retrofit2.HttpException

class WorkerManager(context: Context, parameters: WorkerParameters)
              : CoroutineWorker(context,parameters){

    companion object{
        const val WORK_NAME = "GetAsteroidList"
    }

    override suspend fun doWork(): Result {

        val dataBase = AsteroidDataBase.getDataBase(applicationContext)
        val repo = Repo(dataBase)

        return try {
            repo.getAsteroidList()
            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }

    }
}