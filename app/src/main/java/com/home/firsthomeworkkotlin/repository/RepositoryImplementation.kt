package com.home.firsthomeworkkotlin.repository

class RepositoryImplementation:Repository {
    override fun getWeatherFromServer():Weather {
        Thread.sleep(2000)
        return Weather()
    }

    override fun getWeatherFromLocalStorage():Weather{
        Thread.sleep(20L)
        return Weather()
    }
}