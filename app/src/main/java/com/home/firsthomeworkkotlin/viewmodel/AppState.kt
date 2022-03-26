package com.home.firsthomeworkkotlin.viewmodel

//запечатанный класс
sealed class AppState {

    object Loading:AppState()
    data class Success(val weatherData:Any):AppState()
    data class Error(val error:Throwable):AppState()

}