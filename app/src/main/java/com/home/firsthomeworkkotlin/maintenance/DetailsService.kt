package com.home.firsthomeworkkotlin.maintenance

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.home.firsthomeworkkotlin.BuildConfig
import com.home.firsthomeworkkotlin.repository.yandexdto.WeatherDTO
import com.home.firsthomeworkkotlin.utlis.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailsService(val name: String = "") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        Log.d("@@@", "work MainService")//после создания сервис получает привет от активити
        intent?.let {
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT,0.0)
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON,0.0)
            Log.d("@@@", "work $lat $lon")
            //Thread.sleep(1000L)

            val urlText = "$YANDEX_DOMAIN$YANDEX_ENDPOINT lat=$lat&lon=$lon"
            val uri = URL(urlText)
            //посылаем запрос на сервер
            val urlConnection: HttpsURLConnection =
                (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                    //addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY_FIRST)
                    addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY_SECOND)
                    //addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY_ERROR) //Для проверки ошибки FileNotFoundException
                }
            //коды ошибок
            val responseCode = urlConnection.responseCode
            val responseMessage = urlConnection.responseMessage
            //Буфферизация обращения к серверу
            val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)

            val headers = urlConnection.headerFields

            val serverSide = 500
            val clientSide = 400
            val responseOk = 300

            when {
                responseCode >= serverSide -> {
                    //TODO Сделать через callback
//                    Snackbar.make(view,
//                        "Упс. Что-то пошло не так..\n$responseMessage ошибка сервера",
//                        Snackbar.LENGTH_LONG).show()
                }
                responseCode >= clientSide -> {
                    //TODO Сделать через callback
//                    Snackbar.make(view,
//                        "Упс. Что-то пошло не так..\n$responseMessage ошибка клиента",
//                        Snackbar.LENGTH_LONG).show()
                }
                responseCode < responseOk -> {
                    //TODO Сделать через callback
//                    Snackbar.make(view, "Успешно",
//                        Snackbar.LENGTH_LONG).show()

                }
            }
            urlConnection.disconnect()
            Log.d("@@@", "Закрыли коннект")


            val message = Intent(KEY_WAVE_SERVICE_BROADCAST_WEATHER)//создает ответ (Сервис) на волне myAction
            message.putExtra(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO)
            //sendBroadcast(message)//отправляет сообщение на глобальный приемник
            LocalBroadcastManager.getInstance(this).sendBroadcast(message)//это если мы передаем Локально сообщение


        }
    }
}
