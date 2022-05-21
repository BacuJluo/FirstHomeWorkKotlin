package com.home.firsthomeworkkotlin.view.details

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.home.firsthomeworkkotlin.BuildConfig
import com.home.firsthomeworkkotlin.repository.WeatherDTO
import com.home.firsthomeworkkotlin.utlis.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailsService(val name: String = "") : IntentService(name) {

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        Log.d("@@@", "work DetailsService")//после создания сервис получает привет от активити
        intent?.let {
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT,0.0)
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON,0.0)
            val city = it.getStringExtra(KEY_BUNDLE_CITY)
            Log.d("@@@", "work DetailsService $lat $lon $city")


            val urlText = "$YANDEX_DOMAIN${YANDEX_PATH}lat=$lat&lon=$lon"
            val uri = URL(urlText)
            //посылаем запрос на сервер
            val urlConnection: HttpsURLConnection =
                (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                    addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
                    //addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY_ERROR) //Для проверки ошибки FileNotFoundException
                }

            //коды ошибок
            val responseCode = urlConnection.responseCode
            val responseMessage = urlConnection.responseMessage


            val headers = urlConnection.headerFields

            val serverSide = 500
            val clientSide = 400
            val responseOk = 300

            when {
                responseCode >= serverSide -> {

                }
                responseCode >= clientSide -> {
                    /*make1(this, //TODO попробовть разобраться с SnackBar
                            "Упс. Что-то пошло не так..\n$responseMessage ошибка клиента",
                            Snackbar.LENGTH_LONG).show()*/
                }
                responseCode < responseOk -> {
                    //Буфферизация обращения к серверу
                    val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)


                    val message = Intent(KEY_WAVE_SERVICE_BROADCAST)//создает ответ (Сервис) на волне myAction
                    message.putExtra(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO)

                    //sendBroadcast(message)//отправляет сообщение на глобальный приемник
                    LocalBroadcastManager.getInstance(this).sendBroadcast(message)//это если мы передаем Локально сообщение
                }
                else -> {}
            }
        }
    }
}