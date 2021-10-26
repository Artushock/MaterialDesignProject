package com.artushock.materialdesignproject.ui.main.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel(
    private val currentDayLiveDataToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val yesterdayDayDataToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val theDayBeforeYesterdayDayDataToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: RetrofitImpl = RetrofitImpl()
) : ViewModel() {

    private val apiKey = BuildConfig.NASA_API_KEY

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDayData(): LiveData<PictureOfTheDayData> {
        sendServerRequest(currentDayLiveDataToObserve, null)
        return currentDayLiveDataToObserve
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getYesterdayData(): LiveData<PictureOfTheDayData> {
        val date = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)
        sendServerRequest(yesterdayDayDataToObserve, date)
        return yesterdayDayDataToObserve
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayBeforeYesterdayData(): LiveData<PictureOfTheDayData> {
        val date = LocalDate.now().minusDays(2).format(DateTimeFormatter.ISO_DATE)
        sendServerRequest(theDayBeforeYesterdayDayDataToObserve, date)
        return theDayBeforeYesterdayDayDataToObserve
    }

    private fun sendServerRequest(liveData: MutableLiveData<PictureOfTheDayData>, date: String?) {
        liveData.value = PictureOfTheDayData.Loading(null)
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            if (date == null) {
                retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey)
                    .enqueue(object : Callback<PictureOfTheDayDTO> {

                        override fun onResponse(
                            call: Call<PictureOfTheDayDTO>,
                            response: Response<PictureOfTheDayDTO>
                        ) {
                            handleResponse(response, liveData)
                        }

                        override fun onFailure(call: Call<PictureOfTheDayDTO>, t: Throwable) {
                            currentDayLiveDataToObserve.value = PictureOfTheDayData.Error(t)
                        }
                    })
            } else {
                retrofitImpl.getRetrofitImpl().getPictureOfTheDayByDate(date, apiKey)
                    .enqueue(object : Callback<PictureOfTheDayDTO> {

                        override fun onResponse(
                            call: Call<PictureOfTheDayDTO>,
                            response: Response<PictureOfTheDayDTO>
                        ) {
                            handleResponse(response, liveData)
                        }

                        override fun onFailure(call: Call<PictureOfTheDayDTO>, t: Throwable) {
                            currentDayLiveDataToObserve.value = PictureOfTheDayData.Error(t)
                        }
                    })
            }
        }
    }

    private fun handleResponse(
        response: Response<PictureOfTheDayDTO>,
        liveData: MutableLiveData<PictureOfTheDayData>
    ) {
        if (response.isSuccessful && response.body() != null) {
            liveData.value = PictureOfTheDayData.Success(response.body()!!)
        } else {
            val message = response.message()
            if (message.isNullOrEmpty()) {
                liveData.value =
                    PictureOfTheDayData.Error(Throwable("Undefined"))
            } else {
                liveData.value =
                    PictureOfTheDayData.Error(Throwable(message))
            }
        }
    }
}