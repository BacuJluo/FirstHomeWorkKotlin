package com.home.firsthomeworkkotlin.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener:OnServerResponse, val view: View) {

    fun loadWeather(lat: Double, lon: Double) {
        Thread {
            val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
            val uri = URL(urlText)
            //посылаем запрос на сервер
            val urlConnection: HttpsURLConnection =
                (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                    addRequestProperty("X-Yandex-API-Key", "dcfed173-ae28-4526-a15e-1ed1b3e24ad1")
                }
            try {
                val headers = urlConnection.headerFields
                //коды
                val responseCode = urlConnection.responseCode
                //Буфферизация обращения к серверу
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))

                val weatherDTO:WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                //Смотрящий на главный поток
                Handler(Looper.getMainLooper()).post { onServerResponseListener.onResponse(weatherDTO) }
            } catch (e : JsonSyntaxException){
                    Snackbar.make(view, "Упс. Что-то пошло не так..\nСкорее всего ошибка приложения", Snackbar.LENGTH_LONG).show()
            } finally  {
                urlConnection.disconnect()
                Log.d("@@@", "Закрыли коннект")
            }

        }.start()
    }
}