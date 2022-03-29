package com.home.firsthomeworkkotlin.repository

data class Weather(val city:City = getDefaultCity(), val temperature:Int=0, val feelsLike:Int=0)
data class City(val name:String, val lat:Double, val lon:Double)
fun getDefaultCity() = City("Москва", 55.75, 37.61)
