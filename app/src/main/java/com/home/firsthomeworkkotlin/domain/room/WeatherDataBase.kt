package com.home.firsthomeworkkotlin.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(HistoryWeatherEntity::class), version = 1) //Тонкая настройка базы данных
abstract class WeatherDataBase: RoomDatabase() {

    abstract fun historyDao():HistoryDao

}