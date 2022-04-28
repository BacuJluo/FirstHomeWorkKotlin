package com.home.firsthomeworkkotlin.viewmodel

import com.home.firsthomeworkkotlin.datasource.Weather

//запечатанный класс
sealed class AppState {

    object Loading:AppState()
    //Неважно откуда но получаем погоду
    data class Success(val weatherListData:List<Weather>):AppState()
    data class Error(val error:Throwable):AppState()

}