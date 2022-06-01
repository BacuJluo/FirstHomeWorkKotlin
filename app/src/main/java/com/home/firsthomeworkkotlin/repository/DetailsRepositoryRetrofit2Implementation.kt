package com.home.firsthomeworkkotlin.repository

import com.google.gson.GsonBuilder
import com.home.firsthomeworkkotlin.BuildConfig
import com.home.firsthomeworkkotlin.repository.yandexdto.WeatherDTO
import com.home.firsthomeworkkotlin.utlis.YANDEX_DOMAIN
import com.home.firsthomeworkkotlin.utlis.convertDtoToModel
import com.home.firsthomeworkkotlin.viewmodel.DetailsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsRepositoryRetrofit2Implementation:DetailsRepository {
    override fun getWeatherDetails(city: City, callbackMy: DetailsViewModel.Callback) {
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)
        //val response = weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY_FIRST,city.lat,city.lon).execute()
        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY_FIRST,city.lat,city.lon).enqueue(object : Callback<WeatherDTO>{
            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                if(response.isSuccessful){
                    response.body()?.let {
                        callbackMy.onResponse(convertDtoToModel(it))
                    }
                }
            }

            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                //TODO HW
            }

        })
    }
}