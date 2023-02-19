package com.udacity.asteroidradar.Network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()



private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl("https://api.nasa.gov/")
    .build()

interface NasaService{


    @GET("neo/rest/v1/feed")
    suspend fun getList(
        @Query("start_date")start:String
        ,@Query("end_date")end:String,
        @Query("api_key") key:String=Constants.API_KEY):
            ResponseBody  //or object

    @GET("planetary/apod")
    suspend fun getImageOfDay(@Query("api_key") key:String=Constants.API_KEY): PictureOfDay

}

object ASTEROIDAPI{

    val nasaService: NasaService by lazy {
        retrofit.create(NasaService::class.java)
    }
}

