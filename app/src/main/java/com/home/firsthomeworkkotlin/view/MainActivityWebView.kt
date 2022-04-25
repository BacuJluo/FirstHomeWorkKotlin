package com.home.firsthomeworkkotlin.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.home.firsthomeworkkotlin.databinding.ActivityMainWebviewBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivityWebView : AppCompatActivity() {
    lateinit var binding: ActivityMainWebviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ok.setOnClickListener{
            val urlText = binding.edTextUrl.text.toString()
            val uri = URL(urlText)
            //посылаем запрос на сервер
            val urlConnection:HttpsURLConnection =
                (uri.openConnection() as HttpsURLConnection).apply {
                connectTimeout = 1000
                readTimeout = 1000
            }
            Thread{
                val headers = urlConnection.headerFields
                //Буфферизация обращения к серверу
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = getLinesAsOneBigString(buffer)
                /*runOnUiThread{ //запуск потоков 1 способ
                    binding.webview.loadDataWithBaseURL(null,result, "text/html; utf-8", "utf-8", null)
                }*/
                Handler(Looper.getMainLooper()).post {//запуск потоков 2 способ
                    binding.webview.settings.javaScriptEnabled = true
                    binding.webview.loadDataWithBaseURL(null,result, "text/html; utf-8", "utf-8", null)
                }
            }.start()
        }
    }

    private fun getLinesAsOneBigString(bufferedReader: BufferedReader):String{
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }

}






