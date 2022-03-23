package com.home.firsthomeworkkotlin.datasource

import android.util.Log


data class MyDataClass(var weather:Int, var town:String)


object MyTestObject {

    fun getMyTest()="Hello my friend"

    fun getTestForEach() {
        val list = listOf(1, 2, 3, 4)
        //Инициализация forEach'a
        list.forEach {
            Log.d("myLogs", "$it Hello my friend")
        }
    }

}
