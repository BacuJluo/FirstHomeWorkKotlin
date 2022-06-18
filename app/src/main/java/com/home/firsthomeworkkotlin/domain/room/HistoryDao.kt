package com.home.firsthomeworkkotlin.domain.room

import androidx.room.*


//Data Asset Object
@Dao
interface HistoryDao {
    //Нативный метод
    @Query("INSERT INTO history_table(city,temperature,feelsLike,icon) VALUES(:city,:temperature,:feelsLike,:icon)")
    fun nativeInsert(city:String, temperature:Int, feelsLike:Int, icon:String
    )

    //Метод SQLite
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryWeatherEntity)

    @Delete
    fun delete(entity: HistoryWeatherEntity) //удалить

    @Update
    fun update(entity: HistoryWeatherEntity) //обновить

    @Query("SELECT * FROM history_table")
    fun getAll():List<HistoryWeatherEntity> //узнать погоду для всех городов

    @Query("SELECT * FROM history_table WHERE city = :city")
    fun getHistoryForCIty(city: String):List<HistoryWeatherEntity> //получить историю погоды для какого то города

}