package com.home.firsthomeworkkotlin.repository


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