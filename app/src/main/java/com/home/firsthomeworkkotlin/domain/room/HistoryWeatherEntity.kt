package com.home.firsthomeworkkotlin.domain.room
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryWeatherEntity(
    @PrimaryKey(autoGenerate = true)//Генерирует id
    val id:Long,
    val city: String,
    /*val timestamp: Long, TODO HW Сделать первичный ключ Город + Время??(city и timestamp)*/
    val temperature:Int,
    val feelsLike:Int,
    val icon:String = "bkn_n"
)
