package com.home.firsthomeworkkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.firsthomeworkkotlin.repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private var repository: DetailsRepository = DetailsRepositoryOkHttpImplementation()
) : ViewModel() {

    fun getLiveData() = liveData //возвращает livedata

    fun getWeather(city: City){
        liveData.postValue(DetailsState.Loading)
        repository.getWeatherDetails(city,object : Callback{
            override fun onResponse(weather: Weather) {
                Log.d("@@@","Details - $weather")
                liveData.postValue(DetailsState.Success(weather))
            }
        })
    }

    interface Callback{
        fun onResponse(weather: Weather)
        //TODO HW Fail ситуация
    }



}