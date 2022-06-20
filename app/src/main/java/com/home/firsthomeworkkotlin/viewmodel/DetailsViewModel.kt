package com.home.firsthomeworkkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.firsthomeworkkotlin.repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryRoomImplementation()
) : ViewModel() {

    private var repositoryOne: DetailsRepositoryOne = DetailsRepositoryOneRetrofit2Implementation()

    fun getLiveData() = liveData //возвращает livedata

    fun getWeather(city: City){
        liveData.postValue(DetailsState.Loading)

        repositoryOne = if(isInternet()){
            Log.d("@@@","DetailsRepositoryOneRetrofit2Implementation")
            DetailsRepositoryOneRetrofit2Implementation() //isInternet true
        }else{
            Log.d("@@@","DetailsRepositoryRoomImplementation")
            DetailsRepositoryRoomImplementation() //isInternet false
        }
        repositoryOne.getWeatherDetails(city,object : Callback{
            override fun onResponse(weather: Weather) {
                Log.d("@@@","Details - $weather")
                liveData.postValue(DetailsState.Success(weather))
                repositoryAdd.addWeather(weather)
            }

            override fun onFail() {
                //liveData.postValue(DetailsState.Error())
            }
        })
    }

    private fun isInternet(): Boolean {
        //TODO("Переделать эмуляцию в нормальную работу(переключатель если isInternet true или false)")
        return false
    }

    interface Callback{
        fun onResponse(weather: Weather)
        fun onFail()
    }




}