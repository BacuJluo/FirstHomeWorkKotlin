package com.home.firsthomeworkkotlin

import android.app.Application
import androidx.room.Room
import com.home.firsthomeworkkotlin.domain.room.HistoryDao
import com.home.firsthomeworkkotlin.domain.room.WeatherDataBase
import java.lang.IllegalStateException

class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object{
        private var db: WeatherDataBase?=null
        private var appContext: MyApp?=null
        fun getHistory(): HistoryDao{
            if (db == null){
                if (appContext !=null){
                    db = Room.databaseBuilder(appContext!!,WeatherDataBase::class.java,"testDataBase")
                        .allowMainThreadQueries() //TODO Нужно переделать во вспомогательный поток
                        .build()
                }else{
                    throw IllegalStateException("Что то пошло не так, и у нас пустой appContext")
                }
            }
            return db!!.historyDao()
        }
    }
}