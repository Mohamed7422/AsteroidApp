package com.udacity.asteroidradar

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.model.Repo
import com.udacity.asteroidradar.workmanager.WorkerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private lateinit var database: AsteroidDataBase
    private lateinit var repo: Repo
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()

        delayedInit()
        database = AsteroidDataBase.getDataBase(this)
        repo = Repo(database)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun delayedInit() {
        applicationScope.launch() {
            setupRecurringWork()
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("IdleBatteryChargingConstraints")
    private fun setupRecurringWork() {
         val constraints =Constraints.Builder()
             .setRequiresBatteryNotLow(true)
             .setRequiresCharging(true)
             .setRequiredNetworkType(NetworkType.UNMETERED)
             .setRequiresDeviceIdle(true)
             .build()
        val repeatingRequest = PeriodicWorkRequestBuilder<WorkerManager>(
            1,TimeUnit.DAYS)
            .setConstraints(constraints).build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
            WorkerManager.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest

        )
    }
}