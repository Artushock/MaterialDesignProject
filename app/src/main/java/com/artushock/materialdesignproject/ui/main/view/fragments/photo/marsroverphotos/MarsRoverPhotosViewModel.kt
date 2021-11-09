package com.artushock.materialdesignproject.ui.main.view.fragments.photo.marsroverphotos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artushock.materialdesignproject.BuildConfig
import com.artushock.materialdesignproject.data.api.RetrofitImpl
import com.artushock.materialdesignproject.data.model.MarsRoverPhotosDTO
import com.artushock.materialdesignproject.data.model.MarsRoverPhotosData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsRoverPhotosViewModel(
    private val marsRoverPhotoDataToObserve: MutableLiveData<MarsRoverPhotosData> = MutableLiveData(),
    private val retrofitImpl: RetrofitImpl = RetrofitImpl()
) : ViewModel() {

    private val apiKey = BuildConfig.NASA_API_KEY

    fun getCuriosityPhotosByDate(date: String): LiveData<MarsRoverPhotosData>{
        sendRequest(marsRoverPhotoDataToObserve, date)
        return marsRoverPhotoDataToObserve
    }

    private fun sendRequest(liveData: MutableLiveData<MarsRoverPhotosData>, date: String) {
        liveData.value = MarsRoverPhotosData.Loading(0)
        if (apiKey.isBlank()) {
            MarsRoverPhotosData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getCuriosityPhotosByDate(date, apiKey)
                .enqueue(object : Callback<MarsRoverPhotosDTO>{
                    override fun onResponse(
                        call: Call<MarsRoverPhotosDTO>,
                        response: Response<MarsRoverPhotosDTO>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveData.value = MarsRoverPhotosData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveData.value =
                                    MarsRoverPhotosData.Error(Throwable("Undefined"))
                            } else {
                                liveData.value =
                                    MarsRoverPhotosData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<MarsRoverPhotosDTO>, t: Throwable) {
                        marsRoverPhotoDataToObserve.value = MarsRoverPhotosData.Error(t)
                    }

                })

        }
    }

}