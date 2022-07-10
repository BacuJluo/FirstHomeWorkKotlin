package com.home.firsthomeworkkotlin.domain.room
import androidx.room.*


const val ID = "id"
const val NAME = "city"
const val TEMPERATURE = "temperature"
const val FEELS_LIKE = "feelsLike"
const val ICON = "icon"

@Entity(tableName = "history_table")
data class HistoryWeatherEntity(
    @PrimaryKey(autoGenerate = true)//Генерирует id
    val id:Long,
    val city: String,
    /*val timestamp: Long, TODO HW Сделать первичный ключ Город + Время??(city и timestamp)*/
    val temperature:Int,
    val feelsLike:Int = 10,
    val icon:String = "bkn_n"
)
