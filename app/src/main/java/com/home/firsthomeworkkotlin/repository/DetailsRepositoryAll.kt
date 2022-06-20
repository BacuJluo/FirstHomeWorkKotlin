package com.home.firsthomeworkkotlin.repository

import com.home.firsthomeworkkotlin.viewmodel.HistoryViewModel


interface DetailsRepositoryAll {
    fun getAllWeatherDetails(callback: HistoryViewModel.CallbackAll)
}