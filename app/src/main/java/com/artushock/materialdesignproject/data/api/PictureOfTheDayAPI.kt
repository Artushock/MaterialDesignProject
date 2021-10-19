package com.artushock.materialdesignproject.data.api

import com.artushock.materialdesignproject.data.model.PictureOfTheDayDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String
    ) : Call<PictureOfTheDayDTO>
}