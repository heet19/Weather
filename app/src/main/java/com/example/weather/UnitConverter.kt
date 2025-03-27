package com.example.weather

object UnitConverter {

    // Temperature conversion
    fun convertTemperature(value: Double, toUnit: String): Pair<Double, String> {
        return when (toUnit.lowercase()) {
            "celsius" -> Pair(value, "°C")
            "fahrenheit" -> Pair((value * 9 / 5) + 32, "°F")
            "kelvin" -> Pair(value + 273.15, "K")
            else -> Pair(value, "°C")
        }
    }

    // Wind speed conversion (input in m/s)
    fun convertWindSpeed(value: Double, toUnit: String): Pair<Double, String> {
        return when (toUnit.lowercase()) {
            "kmh" -> Pair(value * 3.6, "km/h")
            "ms" -> Pair(value, "m/s")
            "mph" -> Pair(value * 2.237, "mph")
            "knots" -> Pair(value * 1.944, "knots")
            else -> Pair(value * 3.6, "km/h")
        }
    }

    // Pressure conversion (input in hPa)
    fun convertPressure(value: Double, toUnit: String): Pair<Double, String> {
        return when (toUnit.lowercase()) {
            "hpa" -> Pair(value, "hPa")
            "mmhg" -> Pair(value * 0.750062, "mmHg")
            "inhg" -> Pair(value * 0.02953, "inHg")
            "mb" -> Pair(value, "mb") // 1 hPa = 1 mb
            "psi" -> Pair(value * 0.0145038, "psi")
            else -> Pair(value, "hPa")
        }
    }

    // Visibility conversion (input in meters)
    fun convertVisibility(value: Double, toUnit: String): Pair<Double, String> {
        return when (toUnit.lowercase()) {
            "km" -> Pair(value / 1000, "km")
            "mi" -> Pair(value / 1609.34, "mi")
            "m" -> Pair(value, "m")
            "ft" -> Pair(value * 3.28084, "ft")
            else -> Pair(value / 1000, "km")
        }
    }

}