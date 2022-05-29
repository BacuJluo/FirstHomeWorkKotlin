package com.home.firsthomeworkkotlin.view.weatherlist

import com.home.firsthomeworkkotlin.repository.Weather


interface OnItemListClickListener {
    fun onItemClick(weather: Weather)

}