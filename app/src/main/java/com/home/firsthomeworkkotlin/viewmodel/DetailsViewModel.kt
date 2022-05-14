package com.home.firsthomeworkkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.firsthomeworkkotlin.datasource.City
import com.home.firsthomeworkkotlin.datasource.Weather
import com.home.firsthomeworkkotlin.repository.DetailsRepository
import com.home.firsthomeworkkotlin.repository.DetailsRepositoryOkHttpImplementation

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private var repository: DetailsRepository = DetailsRepositoryOkHttpImplementation()
) : ViewModel() {

    fun getLiveData() = liveData //возвращает livedata

    fun getWeather(city: City){
        liveData.postValue(DetailsState.Loading)
        repository.getWeatherDetails(city,object : Callback{
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
            }
        })
    }

    interface Callback{
        fun onResponse(weather: Weather)
        //TODO HW Fail ситуация
    }



}