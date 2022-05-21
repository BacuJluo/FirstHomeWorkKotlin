package com.home.firsthomeworkkotlin.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.home.firsthomeworkkotlin.BuildConfig
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse, val view: View) {

    fun loadWeather(lat: Double, lon: Double) {

        val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        val uri = URL(urlText)

        Thread {
            //посылаем запрос на сервер
            val urlConnection: HttpsURLConnection =
                (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                    addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
                    //addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY_ERROR) //Для проверки ошибки FileNotFoundException
                }

            //коды ошибок
            val responseCode = urlConnection.responseCode
            val responseMessage = urlConnection.responseMessage

            try {
                //Буфферизация обращения к серверу


                val headers = urlConnection.headerFields

                val serverSide = 500..599
                val clientSide = 400..499
                val responseOk = 200..299

                when (responseCode) {
                    in serverSide -> {

                    }
                    in clientSide -> {

                    }
                    in responseOk -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        //Смотрящий на главный поток
                        //Через Handler передаем в Looper (управляющий потоками) новую задачу
                        Handler(Looper.getMainLooper()).post {
                            onServerResponseListener.onResponse(weatherDTO)
                        }
                        Snackbar.make(view, "Успешно",
                            Snackbar.LENGTH_LONG).show()
                    }
                }

            } catch (e: FileNotFoundException) {
                Log.d("111", "Ошибка, файл не найден")
                println("Ошибка, файл не найден")
                Snackbar.make(view,
                    "Упс. Что-то пошло не так..\n$responseMessage ошибка клиента",
                    Snackbar.LENGTH_LONG).show()
            } catch (e: JsonSyntaxException) {
                Log.d("111", "Ошибка")
                println("Ошибка")
                Snackbar.make(view,
                    "Упс. Что-то пошло не так..\n$responseMessage ошибка",
                    Snackbar.LENGTH_LONG).show()
            } finally {
                urlConnection.disconnect()
                Log.d("@@@", "Закрыли коннект 1")
            }
        }.start()

    }
}