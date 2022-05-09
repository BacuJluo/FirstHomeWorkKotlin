package com.home.firsthomeworkkotlin.repository

import com.home.firsthomeworkkotlin.repository.yandexdto.WeatherDTO

fun interface OnServerResponse {
   fun onResponse(weatherDTO: WeatherDTO)
}