package com.home.firsthomeworkkotlin.repository

import com.home.firsthomeworkkotlin.datasource.Weather

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWorldWeatherFromLocalStorage():List<Weather>
    fun getRussianWeatherFromLocalStorage():List<Weather>
}