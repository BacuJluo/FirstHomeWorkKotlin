package com.home.firsthomeworkkotlin.utlis

import android.util.Log
import com.home.firsthomeworkkotlin.repository.Weather
import com.home.firsthomeworkkotlin.repository.getDefaultCity
import com.home.firsthomeworkkotlin.repository.yandexdto.Fact
import com.home.firsthomeworkkotlin.repository.yandexdto.WeatherDTO

const val KEY_BUNDLE_WEATHER = "weather"

const val YANDEX_ENDPOINT = "v2/informers?"
const val YANDEX_DOMAIN = "https://api.weather.yandex.ru/"
const val YANDEX_API_KEY = "X-Yandex-API-Key"

const val KEY_BUNDLE_SERVICE_BROADCAST_WEATHER = "weather_s_b"
const val KEY_WAVE_SERVICE_BROADCAST = "myaction_wave"
const val KEY_BUNDLE_ACTIVITY_MESSAGE = "key1"
const val KEY_BUNDLE_SERVICE_MESSAGE = "key2"
const val KEY_BUNDLE_LAT = "lat"
const val KEY_BUNDLE_LON = "lon"
const val KEY_BUNDLE_CITY = "city"

const val KEY_WAVE = "myaction"
const val KEY_BROADCAST = "keyBroadcast"
const val KEY_SERVICE = "keyService"
class Utlis {
}

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: Fact = weatherDTO.fact
    Log.d("@@@","${fact.temperature} - ${fact.feelsLike} - Фактическое")
    return (Weather(getDefaultCity(),fact.temperature,fact.feelsLike))
}