package com.home.firsthomeworkkotlin.repository

import com.home.firsthomeworkkotlin.MyApp
import com.home.firsthomeworkkotlin.utlis.convertors.convertHistoryEntityToWeather
import com.home.firsthomeworkkotlin.utlis.convertors.convertWeatherToEntity
import com.home.firsthomeworkkotlin.viewmodel.DetailsViewModel
import com.home.firsthomeworkkotlin.viewmodel.HistoryViewModel

class DetailsRepositoryRoomImplementation:DetailsRepositoryOne,DetailsRepositoryAll,DetailsRepositoryAdd {
    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackAll) {
        callback.onResponse(convertHistoryEntityToWeather(MyApp.getHistory().getAll()))
    }

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        val list = convertHistoryEntityToWeather(MyApp.getHistory().getHistoryForCIty(city.name))
        if (list.isEmpty()){
            callback.onFail()
            //TODO("HW отобразить ошибку")
        }else{
            callback.onResponse(list.last())
        }
    }

    override fun addWeather(weather: Weather) {
        MyApp.getHistory().insert(convertWeatherToEntity(weather))
    }

}