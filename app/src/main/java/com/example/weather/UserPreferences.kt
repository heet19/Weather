package com.example.weather

import android.content.Context

object UserPreferences {
    private const val PREFS_NAME = "WeatherAppPrefs"

    var lastCity: String = "vapi"
        private set

    var temperatureUnit: String = "celsius"
    var windSpeedUnit: String = "kmh"
    var pressureUnit: String = "hpa"
    var visibilityUnit: String = "km"

    fun setLastCity(context: Context, city: String) {
        lastCity = city  // Directly assign to the property
        savePreferences(context)
    }

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        lastCity = prefs.getString("lastCity", "vapi") ?: "vapi"

        temperatureUnit = prefs.getString("temperatureUnit", "celsius") ?: "celsius"
        windSpeedUnit = prefs.getString("windSpeedUnit", "kmh") ?: "kmh"
        pressureUnit = prefs.getString("pressureUnit", "hpa") ?: "hpa"
        visibilityUnit = prefs.getString("visibilityUnit", "km") ?: "km"
    }

    fun savePreferences(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString("lastCity", lastCity)

            putString("temperatureUnit", temperatureUnit)
            putString("windSpeedUnit", windSpeedUnit)
            putString("pressureUnit", pressureUnit)
            putString("visibilityUnit", visibilityUnit)

            apply()
        }
    }
}

//object UserPreferences {
//    private const val PREFS_NAME = "WeatherAppPrefs"
//
//    var temperatureUnit: String = "celsius"
//        set(value) {
//            field = value
//            savePreferences()
//        }
//
//    var windSpeedUnit: String = "kmh"
//        set(value) {
//            field = value
//            savePreferences()
//        }
//
//    var pressureUnit: String = "hpa"
//        set(value) {
//            field = value
//            savePreferences()
//        }
//
//    var visibilityUnit: String = "km"
//        set(value) {
//            field = value
//            savePreferences()
//        }
//
//    fun init(context: Context) {
//        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        temperatureUnit = prefs.getString("temperatureUnit", "celsius") ?: "celsius"
//        windSpeedUnit = prefs.getString("windSpeedUnit", "kmh") ?: "kmh"
//        pressureUnit = prefs.getString("pressureUnit", "hpa") ?: "hpa"
//        visibilityUnit = prefs.getString("visibilityUnit", "km") ?: "km"
//    }
//
//    private fun savePreferences() {
//        val prefs = MyApplication.appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        with(prefs.edit()) {
//            putString("temperatureUnit", temperatureUnit)
//            putString("windSpeedUnit", windSpeedUnit)
//            putString("pressureUnit", pressureUnit)
//            putString("visibilityUnit", visibilityUnit)
//            apply()
//        }
//    }
//}