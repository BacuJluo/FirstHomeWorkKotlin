package com.home.firsthomeworkkotlin.repository

import com.google.gson.GsonBuilder
import com.home.firsthomeworkkotlin.BuildConfig
import com.home.firsthomeworkkotlin.repository.yandexdto.WeatherDTO
import com.home.firsthomeworkkotlin.utlis.YANDEX_DOMAIN
import com.home.firsthomeworkkotlin.utlis.convertors.convertDtoToModel
import com.home.firsthomeworkkotlin.viewmodel.DetailsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsRepositoryOneRetrofit2Implementation:DetailsRepositoryOne {
    override fun getWeatherDetails(city: City, callbackMy: DetailsViewModel.Callback) {
        //Оживление Ретрофита
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)

        //val response = weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY_FIRST,city.lat,city.lon).execute() //Выполнение запроса здесь и сейчас (синхронно)

        //Выполнение запроса через Callback (асинхронно)
        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY_FIRST,city.lat,city.lon).enqueue(object : Callback<WeatherDTO>{
            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                if(response.isSuccessful){
                    response.body()?.let {
                        val weather = convertDtoToModel(it)
                        weather.city = city
                        callbackMy.onResponse(weather)
                    }
                }
            }
            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                //TODO HW
            }

        })
    }
}