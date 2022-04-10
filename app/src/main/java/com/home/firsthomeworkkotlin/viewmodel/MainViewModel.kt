package com.home.firsthomeworkkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.firsthomeworkkotlin.repository.Repository
import com.home.firsthomeworkkotlin.repository.RepositoryImplementation

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: Repository = RepositoryImplementation()
) : ViewModel() {

    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherRussian() = getWeather(true)
    fun getWeatherWorld() = getWeather(false)


    private fun getWeather(isRussian: Boolean) {
        Thread {
            liveData.postValue(AppState.Loading)
            //после обновления liveData автоматически рассылает всем своим слушателям ее данные
            if(true) {
                val answerLocale = if (!isRussian) repository.getWorldWeatherFromLocalStorage() else repository.getRussianWeatherFromLocalStorage()
                liveData.postValue(AppState.Success(answerLocale))
            }else{liveData.postValue(AppState.Error(IllegalAccessException()))}
        }.start()
    }


}