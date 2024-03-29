package com.home.firsthomeworkkotlin.repository

import android.util.Log
import com.google.gson.Gson
import com.home.firsthomeworkkotlin.BuildConfig
import com.home.firsthomeworkkotlin.repository.yandexdto.WeatherDTO
import com.home.firsthomeworkkotlin.utlis.YANDEX_API_KEY
import com.home.firsthomeworkkotlin.utlis.YANDEX_DOMAIN
import com.home.firsthomeworkkotlin.utlis.YANDEX_ENDPOINT
import com.home.firsthomeworkkotlin.utlis.convertors.convertDtoToModel
import com.home.firsthomeworkkotlin.viewmodel.DetailsViewModel
import okhttp3.OkHttpClient
import okhttp3.Request

class DetailsRepositoryOneOkHttpImplementation:DetailsRepositoryOne {
    override fun getWeatherDetails(city: City,callback: DetailsViewModel.Callback) {
        val client = OkHttpClient()
        val builder = Request.Builder() //билдер запроса
        //builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY_FIRST)
        builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY_SECOND)
        builder.url("$YANDEX_DOMAIN${YANDEX_ENDPOINT}lat=${city.lat}&lon=${city.lon}")
        Log.d("@@@", "${city.lat} ${city.lon} ${city.name}")
        val request = builder.build()
        val call = client.newCall(request)
        Thread {
            val response = call.execute()
            if(response.isSuccessful){

                val weatherDTO = Gson().fromJson(response.body()!!.string(), WeatherDTO::class.java)
                Log.d("@@@","$weatherDTO")

                val weather = convertDtoToModel(weatherDTO)
                Log.d("@@@","$weather")
                weather.city = city

                callback.onResponse(weather)
            }else{
                //TODO HW
            }
        }.start()
    }
}
