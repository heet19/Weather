package com.example.weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodayModel (
    val todayTime: String,
    val temperature: String,
    val animation: Int,
    val description: String,
    val feelsLike: Double,
    val windDeg: Int,
    val windSpeed: Double,
    val humidity: Int,
    val visibility: Int,
    val clouds: Int,
    val pop: Number,
    val minTemp: Double,
    val maxTemp: Double,
    val seaLevel: Int
) : Parcelable