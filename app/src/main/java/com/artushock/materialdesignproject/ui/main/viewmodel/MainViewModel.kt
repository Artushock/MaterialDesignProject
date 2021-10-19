package com.artushock.materialdesignproject.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artushock.materialdesignproject.BuildConfig
import com.artushock.materialdesignproject.data.api.RetrofitImpl
import com.artushock.materialdesignproject.data.model.PictureOfTheDayDTO
import com.artushock.materialdesignproject.data.model.PictureOfTheDayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: RetrofitImpl = RetrofitImpl()
) : ViewModel() {

    fun getData(): LiveData<PictureOfTheDayData> {
        sendServerRequest()
        return liveDataToObserve
    }

    private fun sendServerRequest() {
        liveDataToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey)
                .enqueue(object : Callback<PictureOfTheDayDTO> {
                    override fun onResponse(
                        call: Call<PictureOfTheDayDTO>,
                        response: Response<PictureOfTheDayDTO>
                    ) {
                        if (response.isSuccessful && response.body() != null){
                            liveDataToObserve.value = PictureOfTheDayData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()){
                                liveDataToObserve.value = PictureOfTheDayData.Error(Throwable("Undefined"))
                            } else {
                                liveDataToObserve.value = PictureOfTheDayData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PictureOfTheDayDTO>, t: Throwable) {
                        liveDataToObserve.value = PictureOfTheDayData.Error(t)
                    }

                })
        }
    }
}