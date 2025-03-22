package com.example.weather

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.models.WeatherData
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

//    47561fc40c887a4bbb468d6530937de1

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail)

        fetchWeatherData()

    }

    private fun fetchWeatherData() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(APIInterface::class.java)

        val response = retrofit.getWeatherData("puri", "47561fc40c887a4bbb468d6530937de1", "metric")
        response.enqueue(object : Callback<WeatherData>{
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {

                    val city = responseBody.name
                    val longitude = responseBody.coord.lon
                    val latitude = responseBody.coord.lat
                    val icon = responseBody.weather.firstOrNull()?.icon
                    val temperature = responseBody.main.temp
                    val envDescription = responseBody.weather.firstOrNull()?.description ?: "No data"
                    val feelsLike = responseBody.main.feels_like
                    val humidity = responseBody.main.humidity

                    val visibilityKm = responseBody.visibility / 1000.0
                    val visibilityKmNew = String.format("%.2f", visibilityKm)

                    val windSpeed = responseBody.wind.speed * 3.6
                    val windSpeedNew = String.format("%.2f", windSpeed)

                    val windDirection = responseBody.wind.deg

                    val windGustSpeed = responseBody.wind.gust * 3.6
                    val windGustSpeedNew = String.format("%.2f", windGustSpeed)

                    val pressure = responseBody.main.pressure
                    val groundPressure = responseBody.main.grnd_level
                    val seaLevel = responseBody.main.sea_level

                    val cloud = responseBody.clouds.all
                    val clear = 100 - cloud

                    val minTemperature = responseBody.main.temp_min
                    val maxTemperature = responseBody.main.temp_max

                    val dt = responseBody.dt
                    val formattedDate = convertUnixToDate(dt)

                    val sunrise = responseBody.sys.sunrise
                    val sunriseNew = convertUnixToTime(sunrise)
                    val sunset = responseBody.sys.sunset
                    val sunsetNew = convertUnixToTime(sunset)



                    binding.mainCityName.text = city
                    binding.mainLongitude.text = "Longitude: ${longitude}"
                    binding.mainLatitude.text = "Latitude: ${latitude}"
                    if (icon != null) {
                        val iconUrl = "https://openweathermap.org/img/wn/${icon}@2x.png"
                        Picasso.get()
                            .load(iconUrl)
                            .error(R.drawable.error_image)
                            .into(binding.mainWeatherImage)
                    }
                    binding.mainTempDegree.text = "${temperature} \u00B0C"
                    binding.mainEnv.text = envDescription
                    binding.feelsLikeValue.text = "${feelsLike}°"
                    binding.humidityValue.text = "${humidity} %"
                    binding.visibilityValue.text = "${visibilityKmNew} km/h"
                    binding.windSpeedValue.text = "${windSpeedNew} km/h"
                    binding.windDirectionValue.text = "${windDirection}°"
                    binding.windGustSpeedValue.text = "${windGustSpeedNew} km/h"
                    binding.pressureValue.text = "${pressure} hPa"
                    binding.groundLevelPressureValue.text = "${groundPressure} hPa"
                    binding.seaLevelValue.text = "${seaLevel} hPa"

                    binding.cloudPB.max = 100
                    binding.cloudPB.progress = cloud
                    binding.cloudProgressBarInitialText2.text = "${clear} %"
                    binding.cloudProgressBarFinalText2.text = "${cloud} %"

                    binding.minTempValue.text = "${minTemperature}  °C"
                    binding.maxTempValue.text = "${maxTemperature}  °C"
                    binding.mainTimeDate.text = formattedDate

                    binding.sunRiseSetInitialText2.text = sunriseNew
                    binding.sunRiseSetFinalText2.text = sunsetNew

                    // Calculate and set progress
                    val progress = calculateSunProgress(sunrise, sunset)
                    binding.sunRiseSetPB.max = 100
                    binding.sunRiseSetPB.progress = progress
                }
            }

            override fun onFailure(call: Call<WeatherData>, response: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    fun convertUnixToDate(unixTimestamp: Int): String {
        val date = Date(unixTimestamp.toLong() * 1000)
        val sdf = SimpleDateFormat("yyyy-MM-dd | HH:mm:ss", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    fun convertUnixToTime(unixTimestamp: Int): String {
        val date = Date(unixTimestamp.toLong() * 1000)
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault()) // For 12-hour format with AM/PM
        // If you prefer a 24-hour format, use this instead:
        // val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault() // Device's timezone
        return sdf.format(date)
    }

    fun calculateSunProgress(sunrise: Int, sunset: Int): Int {
        val currentTimeMillis = System.currentTimeMillis()
        val sunriseMillis = sunrise.toLong() * 1000
        val sunsetMillis = sunset.toLong() * 1000

        return when {
            currentTimeMillis < sunriseMillis -> 0
            currentTimeMillis > sunsetMillis -> 100
            else -> ((currentTimeMillis - sunriseMillis).toFloat() / (sunsetMillis - sunriseMillis) * 100).toInt()
        }
    }



//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu, menu)
//        return true
//    }

//    // Handle Menu Item Clicks
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.search -> {
//                // Handle Settings Click
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}