package com.artushock.materialdesignproject.data.api

import com.artushock.materialdesignproject.data.model.MarsRoverPhotosDTO
import com.artushock.materialdesignproject.data.model.PictureOfTheDayDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String
    ) : Call<PictureOfTheDayDTO>

    @GET("planetary/apod")
    fun getPictureOfTheDayByDate(
        @Query("date") date: String,
        @Query("api_key") apiKey: String
    ) : Call<PictureOfTheDayDTO>

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getCuriosityPhotosByDate(
        @Query("earth_date") earth_date: String,
        @Query("camera") camera: String,
        @Query("api_key") apiKey: String
    ) : Call<MarsRoverPhotosDTO>
}