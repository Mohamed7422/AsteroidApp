package com.udacity.asteroidradar.main

import android.app.Application
import android.graphics.Picture
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.Factory
import com.udacity.asteroidradar.Network.ASTEROIDAPI
import com.udacity.asteroidradar.Network.NasaService
import com.udacity.asteroidradar.PictureOfDay


import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.model.Repo
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel (private val repo: Repo)
                     : ViewModel() {


   private val savedAsteroidList  = repo.asteroidDataList
   @RequiresApi(Build.VERSION_CODES.O)
   private val todayAsteroidList  = repo.todayAsteroidList
   @RequiresApi(Build.VERSION_CODES.O)
   private val weekAsteroidList  = repo.weekAsteroidList


   val asteroidList: MediatorLiveData<List<Asteroid>> = MediatorLiveData()

    //navigation
    private val _objectnavigation = MutableLiveData<Asteroid>()
    val objectnavigation: LiveData<Asteroid>
    get() = _objectnavigation

    //picture
    private val _picture = MutableLiveData<PictureOfDay>()
    val picture : LiveData<PictureOfDay>
    get() = _picture



    init {
        getAsteroidData()
    }

    fun onAstroidClick(asteroid: Asteroid)
    {
     _objectnavigation.value = asteroid
    }

    fun onAstroidClickDone(){
        _objectnavigation.value = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAsteroidData() {
     /**
      * on co-routine scope to be in the background thread
      * and add list of data for offline cashing*/
        viewModelScope.launch {
              repo.getAsteroidList()
             asteroidList.addSource(savedAsteroidList){
                  asteroidList.value = it
             }

            _picture.value = ASTEROIDAPI.nasaService.getImageOfDay()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onListItemSelected(enum: MainFragment.MenuSelectionEnum){
        clearDataFromSource()
        when(enum){
            MainFragment.MenuSelectionEnum.SAVE -> {
               asteroidList.addSource(savedAsteroidList){
                   asteroidList.value = it
               }
            }
            MainFragment.MenuSelectionEnum.WEEK -> {
                asteroidList.addSource(weekAsteroidList){
                    asteroidList.value = it
                }

            }

            MainFragment.MenuSelectionEnum.TODAY -> {
                asteroidList.addSource(todayAsteroidList){
                    asteroidList.value = it
                }
            }

        }

    }

    private fun clearDataFromSource() {
        asteroidList.removeSource(savedAsteroidList)
        asteroidList.removeSource(todayAsteroidList)
        asteroidList.removeSource(weekAsteroidList)
    }


}