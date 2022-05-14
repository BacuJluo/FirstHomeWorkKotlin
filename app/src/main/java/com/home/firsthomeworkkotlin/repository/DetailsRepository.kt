package com.home.firsthomeworkkotlin.repository

import com.home.firsthomeworkkotlin.datasource.City
import com.home.firsthomeworkkotlin.viewmodel.DetailsViewModel


interface DetailsRepository {
    fun getWeatherDetails(city: City, myCallback: DetailsViewModel.Callback)
}