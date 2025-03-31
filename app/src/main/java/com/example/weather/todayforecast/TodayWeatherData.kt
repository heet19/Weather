package com.example.weather.todayforecast

data class TodayWeatherData(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Item0>,
    val message: Int
)