package com.home.firsthomeworkkotlin.repository

import com.home.firsthomeworkkotlin.viewmodel.DetailsViewModel


interface DetailsRepository {
    fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}