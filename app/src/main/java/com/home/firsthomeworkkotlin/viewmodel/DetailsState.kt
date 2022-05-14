package com.home.firsthomeworkkotlin.viewmodel

import com.home.firsthomeworkkotlin.datasource.Weather

//запечатанный класс
sealed class DetailsState {

    object Loading:DetailsState()
    data class Success(val weather:Weather):DetailsState()
    data class Error(val error:Throwable):DetailsState()

}