package com.udacity.asteroidradar.model

import android.icu.util.LocaleData
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Network.ASTEROIDAPI
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class Repo (private val dataBase: AsteroidDataBase){

    /**
     * get the data from the database
     * for offline cashing*/

     val asteroidDataList:LiveData<List<Asteroid>>
     get() =dataBase.asteroidDao.getAllAsteroids()

    private val now: String = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
    private val endTime: String = LocalDateTime.now().plusDays(7).format(DateTimeFormatter.ISO_DATE)

    /**get the list for today*/
    val todayAsteroidList:LiveData<List<Asteroid>>
    @RequiresApi(Build.VERSION_CODES.O)
    get() {

        return dataBase.asteroidDao.getAsteroidToday(now)
    }

    /**get the list for week*/
    val weekAsteroidList:LiveData<List<Asteroid>>
        @RequiresApi(Build.VERSION_CODES.O)
        get() {

            return dataBase.asteroidDao.getAsteroidSpecificDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE),
                endTime.format(DateTimeFormatter.ISO_DATE)
            )
        }



    /**
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     */

    suspend fun getAsteroidList(){
        withContext(Dispatchers.IO){
           try {
               val allAsteroids = ASTEROIDAPI.nasaService.getList(now,endTime)

               val asteroids = parseAsteroidsJsonResult(JSONObject(allAsteroids.string()))
               dataBase.asteroidDao.updateData(asteroids)
              Log.i("Repo", "Success: Size of Data" + asteroids.size)
           }catch (e: java.lang.Exception){
               e.localizedMessage?.let { Log.e("Repo", it) }
           }
        }
    }
}