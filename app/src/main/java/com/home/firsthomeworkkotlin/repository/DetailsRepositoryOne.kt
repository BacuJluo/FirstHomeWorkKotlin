package com.home.firsthomeworkkotlin.repository

import com.home.firsthomeworkkotlin.viewmodel.DetailsViewModel


interface DetailsRepositoryOne {
    fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}