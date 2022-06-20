package com.home.firsthomeworkkotlin.utlis.convertors

import android.util.Log
import com.home.firsthomeworkkotlin.domain.room.HistoryWeatherEntity
import com.home.firsthomeworkkotlin.repository.City
import com.home.firsthomeworkkotlin.repository.Weather
import com.home.firsthomeworkkotlin.repository.getDefaultCity
import com.home.firsthomeworkkotlin.repository.yandexdto.Fact
import com.home.firsthomeworkkotlin.repository.yandexdto.WeatherDTO

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: Fact = weatherDTO.fact
    Log.d("@@@","${fact.temperature} - ${fact.feelsLike} - Фактическое")
    return (Weather(getDefaultCity(),fact.temperature,fact.feelsLike,fact.icon))
}

fun convertHistoryEntityToWeather(entityList: List<HistoryWeatherEntity>):List<Weather>{
    return entityList.map {
        Weather(City(it.city,0.0,0.0),it.temperature,it.feelsLike,it.icon)
    }
}

fun convertWeatherToEntity(weather: Weather):HistoryWeatherEntity{
    return HistoryWeatherEntity(0,weather.city.name,weather.temperature,weather.feelsLike,weather.icon);TODO("научиться хранить широту и долготу в БД")
}