package com.home.firsthomeworkkotlin.repository

fun interface OnServerResponse {
   fun onResponse(weatherDTO: WeatherDTO)
}