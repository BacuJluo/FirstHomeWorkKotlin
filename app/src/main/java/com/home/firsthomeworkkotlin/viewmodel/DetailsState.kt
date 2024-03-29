package com.home.firsthomeworkkotlin.viewmodel

import com.home.firsthomeworkkotlin.repository.Weather


//запечатанный класс
sealed class DetailsState {

    object Loading:DetailsState()
    data class Success(var weather : Weather) : DetailsState()
    data class Error(val error:Throwable):DetailsState()

}