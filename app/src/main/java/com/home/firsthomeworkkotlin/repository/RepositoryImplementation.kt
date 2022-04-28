package com.home.firsthomeworkkotlin.repository

import com.home.firsthomeworkkotlin.datasource.Weather
import com.home.firsthomeworkkotlin.datasource.getRussianCities
import com.home.firsthomeworkkotlin.datasource.getWorldCities

class RepositoryImplementation:Repository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(2000)
        return Weather()
    }

    override fun getWorldWeatherFromLocalStorage():List<Weather> {
        return getWorldCities()// эмуляция ответа
    }
    override fun getRussianWeatherFromLocalStorage():List<Weather> {
        return getRussianCities() // эмуляция ответа
    }
}