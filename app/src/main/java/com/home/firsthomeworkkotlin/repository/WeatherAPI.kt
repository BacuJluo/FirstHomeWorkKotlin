package com.home.firsthomeworkkotlin.repository

import com.home.firsthomeworkkotlin.repository.yandexdto.WeatherDTO
import com.home.firsthomeworkkotlin.utlis.KEY_BUNDLE_LAT
import com.home.firsthomeworkkotlin.utlis.KEY_BUNDLE_LON
import com.home.firsthomeworkkotlin.utlis.YANDEX_API_KEY
import com.home.firsthomeworkkotlin.utlis.YANDEX_ENDPOINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {
    @GET(YANDEX_ENDPOINT) //только ENDPOINT
    fun getWeather(
        @Header(YANDEX_API_KEY) apikey: String,
        @Query(KEY_BUNDLE_LAT) lat: Double,
        @Query(KEY_BUNDLE_LON) lon: Double,
    ): Call<WeatherDTO>
}