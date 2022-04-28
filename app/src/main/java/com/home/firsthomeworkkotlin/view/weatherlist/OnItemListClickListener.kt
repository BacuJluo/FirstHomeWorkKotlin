package com.home.firsthomeworkkotlin.view.weatherlist

import com.home.firsthomeworkkotlin.datasource.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)

}