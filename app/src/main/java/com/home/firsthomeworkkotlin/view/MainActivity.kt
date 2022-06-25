package com.home.firsthomeworkkotlin.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.home.firsthomeworkkotlin.R
import com.home.firsthomeworkkotlin.BuildConfig
import com.home.firsthomeworkkotlin.MyApp
import com.home.firsthomeworkkotlin.lesson6.MainService
import com.home.firsthomeworkkotlin.lesson6.MyBroadcastReceiver
import com.home.firsthomeworkkotlin.lesson6.ThreadsFragment
import com.home.firsthomeworkkotlin.lesson9.WorkWithContentProviderFragment
import com.home.firsthomeworkkotlin.utlis.KEY_BUNDLE_ACTIVITY_MESSAGE
import com.home.firsthomeworkkotlin.utlis.KEY_WAVE
import com.home.firsthomeworkkotlin.view.historylist.HistoryWeatherListFragment
import com.home.firsthomeworkkotlin.view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }

        //Активити создает сервис (но не может от сервиса получить ответ)
        startService(Intent(this,MainService::class.java).apply {
            putExtra(KEY_BUNDLE_ACTIVITY_MESSAGE, BuildConfig.KEY_ONE_HELLO_SERVICE)//key должен быть в константах
        })

        //тут Активити создает приемник и уже может принять ответ от сервиса
        val receiver = MyBroadcastReceiver()
        //регистрация приемника на волну myAction
        registerReceiver(receiver, IntentFilter(KEY_WAVE)) //глобальный уровень броадкастресивера
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(KEY_WAVE)) //локальный уровень броадкастресивера

        //TODO HW Сделать через SharedPref сохранение состояния, кнопки Россия или Мир
        val sp = getSharedPreferences("FileName", MODE_PRIVATE)
        val editor = sp.edit()
        val getDefaultValueOfRussia = true
        editor.putBoolean("IsRussian",getDefaultValueOfRussia)
        editor.apply()

        MyApp.getHistory().getAll()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionThread -> {
                supportFragmentManager.beginTransaction().replace(R.id.container, ThreadsFragment.newInstance()).addToBackStack("rty").commit()
            }
            R.id.action_history -> {
                supportFragmentManager.beginTransaction().add(R.id.container, HistoryWeatherListFragment.newInstance()).addToBackStack("qwe").commit()

            }
            R.id.action_work_with_content_provider -> {
                supportFragmentManager.beginTransaction().add(R.id.container, WorkWithContentProviderFragment.newInstance()).addToBackStack("").commit()

            }
        }
        return super.onOptionsItemSelected(item)
    }


}






