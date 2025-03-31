package com.example.weather

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.example.weather.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    private val binding: ActivitySettingBinding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize preferences
        UserPreferences.init(this)

        // Set current unit labels
        updateUnitLabels()

        binding.btnBackSetting.setOnClickListener {
            finish()
        }

        binding.settingTemperature.setOnClickListener {
            val popupTemp = PopupMenu(this@SettingActivity, it)
            popupTemp.setOnMenuItemClickListener(this@SettingActivity)
            val inflater = popupTemp.menuInflater
            inflater.inflate(R.menu.temperature_menu, popupTemp.menu)
            popupTemp.show()
        }

        binding.settingWind.setOnClickListener {
            val popupWind  = PopupMenu(this@SettingActivity, it)
            popupWind.setOnMenuItemClickListener(this@SettingActivity)
            val inflater = popupWind.menuInflater
            inflater.inflate(R.menu.wind_menu, popupWind.menu)
            popupWind.show()
        }

        binding.settingAir.setOnClickListener {
            val popupAir  = PopupMenu(this@SettingActivity, it)
            popupAir.setOnMenuItemClickListener(this@SettingActivity)
            val inflater = popupAir.menuInflater
            inflater.inflate(R.menu.air_menu, popupAir.menu)
            popupAir.show()
        }

        binding.settingVisibility.setOnClickListener {
            val popupVisibility  = PopupMenu(this@SettingActivity, it)
            popupVisibility.setOnMenuItemClickListener(this@SettingActivity)
            val inflater = popupVisibility.menuInflater
            inflater.inflate(R.menu.visibility_menu, popupVisibility.menu)
            popupVisibility.show()
        }

    }

    private fun updateUnitLabels() {
        binding.settingTempChange.text = when (UserPreferences.temperatureUnit) {
            "celsius" -> "Celsius °C"
            "fahrenheit" -> "Fahrenheit °F"
            "kelvin" -> "Kelvin K"
            else -> "Celsius °C"
        }
        binding.settingWindChange.text = when (UserPreferences.windSpeedUnit) {
            "kmh" -> "Kilometers / hours (km/h)"
            "ms" -> "Meters / second (m/s)"
            "mph" -> "Miles / hour (mph)"
            "knots" -> "Nautical miles / hour (knots)"
            else -> "Kilometers / hours (km/h)"
        }
        binding.settingAirChange.text = when (UserPreferences.pressureUnit) {
            "hpa" -> "Hectopascals (hPa)"
            "mmhg" -> "Millimeters of mercury (mmHg)"
            "inhg" -> "Inches of mercury (inHg)"
            "mb" -> "Millibars (mb)"
            "psi" -> "Pounds per square inch (psi)"
            else -> "Hectopascals (hPa)"
        }
        binding.settingVisibilityChange.text = when (UserPreferences.visibilityUnit) {
            "km" -> "Kilometers (km)"
            "mi" -> "Miles (mi)"
            "m" -> "Metres (m)"
            "ft" -> "Feet (ft)"
            else -> "Kilometers (km)"
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.tMenuCelsius -> {
                UserPreferences.temperatureUnit = "celsius"
                binding.settingTempChange.text = "Celsius °C"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Temperature Changed to Celsius", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.tMenuFahrenheit -> {
                UserPreferences.temperatureUnit = "fahrenheit"
                binding.settingTempChange.text = "Fahrenheit °F"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Temperature Changed to Fahrenheit", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.tMenuKelvin -> {
                UserPreferences.temperatureUnit = "kelvin"
                binding.settingTempChange.text = "Kelvin K"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Temperature Changed to Kelvin", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.wMenuKilometers -> {
                UserPreferences.windSpeedUnit = "kmh"
                binding.settingWindChange.text = "Kilometers / hours (km/h)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Wind changed to Km/h", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.wMenuMeters -> {
                UserPreferences.windSpeedUnit = "ms"
                binding.settingWindChange.text = "Meters / second (m/s)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Wind changed to m/s", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.wMenuFeet -> {
                UserPreferences.windSpeedUnit = "ft/s"
                binding.settingWindChange.text = "Feet per second (ft/s)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Wind changed to knots", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.wMenuMiles -> {
                UserPreferences.windSpeedUnit = "mph"
                binding.settingWindChange.text = "Miles / hour (mph)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Wind changed to mph", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.wMenuNautical -> {
                UserPreferences.windSpeedUnit = "knots"
                binding.settingWindChange.text = "Nautical miles / hour (knots)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Wind changed to knots", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.aMenuHectopascals -> {
                UserPreferences.pressureUnit = "hpa"
                binding.settingAirChange.text = "Hectopascals (hPa)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Air changed to hPa", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.aMenuMilliMeters -> {
                UserPreferences.pressureUnit = "mmhg"
                binding.settingAirChange.text = "Millimeters of mercury (mmHg)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Air changed to mmHg", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.aMenuInches -> {
                UserPreferences.pressureUnit = "inhg"
                binding.settingAirChange.text = "Inches of mercury (inHg)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Air changed to inHg", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.aMenuMillibars -> {
                UserPreferences.pressureUnit = "mb"
                binding.settingAirChange.text = "Millibars (mb)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Air changed to mb", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.aMenuPounds -> {
                UserPreferences.pressureUnit = "psi"
                binding.settingAirChange.text = "Pounds per square inch (psi)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Air changed to psi", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.vMenuKilometers -> {
                UserPreferences.visibilityUnit = "km"
                binding.settingVisibilityChange.text = "Kilometers (km)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Visibility changed to km", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.vMenuMiles -> {
                UserPreferences.visibilityUnit = "mi"
                binding.settingVisibilityChange.text = "Miles (mi)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Visibility changed to mi", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.vMenuMetres -> {
                UserPreferences.visibilityUnit = "m"
                binding.settingVisibilityChange.text = "Metres (m)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Visibility changed to m", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.vMenuFeet -> {
                UserPreferences.visibilityUnit = "ft"
                binding.settingVisibilityChange.text = "Feet (ft)"
                UserPreferences.savePreferences(this)
                updateUnitLabels()
                Toast.makeText(this, "Visibility changed to ft", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> {false}
        }
    }

    private fun tempToCelsius(value: Double, unit: String): Double {
            return when (unit.lowercase()) {
                "kelvin" -> value - 273.15
                "fahrenheit" -> (value - 32) * 5 / 9
                else -> value // Assuming it's already in Celsius
        }

    }
}