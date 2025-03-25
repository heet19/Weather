package com.example.weather

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

//    47561fc40c887a4bbb468d6530937de1

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        swipeRefreshLayout = binding.refreshSwipe
        swipeRefreshLayout.setOnRefreshListener(this)

//        setSupportActionBar(binding.toolbarDetail)

        fetchWeatherData()

    }

    private fun fetchWeatherData() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(APIInterface::class.java)

        val response = retrofit.getWeatherData("rajkot", "47561fc40c887a4bbb468d6530937de1", "metric")
        response.enqueue(object : Callback<WeatherData>{
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {

                    val city = responseBody.name
                    val longitude = responseBody.coord.lon
                    val latitude = responseBody.coord.lat

//                    val icon = responseBody.weather.firstOrNull()?.icon

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

                    val sunrise = responseBody.sys.sunrise
                    val sunriseNew = convertUnixToTime(sunrise)
                    val sunset = responseBody.sys.sunset
                    val sunsetNew = convertUnixToTime(sunset)

                    val weatherCondition = responseBody.weather.firstOrNull()?.description ?: "Clear"

                    fun getAnimationRes(weatherCondition: String): Int {
                        return when (weatherCondition.lowercase()) {
//                            Clear Sky and Clouds
                            "clear sky" -> R.raw.clear_sky
                            "few clouds" -> R.raw.partly_cloudy
                            "scattered clouds" -> R.raw.scattered_clouds
                            "broken clouds" -> R.raw.windy
                            "overcast clouds" -> R.raw.windy

//                            Rain and Drizzle
                            "light rain" -> R.raw.light_rain
                            "moderate rain" -> R.raw.moderate_rain
                            "heavy intensity rain" -> R.raw.heavy_intensity_rain
                            "very heavy rain" -> R.raw.heavy_intensity_rain
                            "extreme rain" -> R.raw.heavy_intensity_rain
                            "freezing rain" -> R.raw.freezing_rain
                            "light intensity shower rain" -> R.raw.foggy
                            "shower rain" -> R.raw.shower_rain
                            "heavy intensity shower rain" -> R.raw.shower_rain
                            "ragged shower rain" -> R.raw.shower_rain
                            "light drizzle" -> R.raw.light_drizzle
                            "drizzle" -> R.raw.light_drizzle
                            "heavy intensity drizzle" -> R.raw.light_drizzle
                            "drizzle rain" -> R.raw.light_drizzle
                            "heavy intensity drizzle rain" -> R.raw.light_drizzle
                            "shower drizzle" -> R.raw.light_drizzle

//                            Snow
                            "light snow" -> R.raw.snow_sunny
                            "snow" -> R.raw.snow
                            "heavy snow" -> R.raw.snow
                            "sleet" -> R.raw.sleet
                            "light shower sleet" -> R.raw.sleet
                            "shower sleet" -> R.raw.sleet
                            "light rain and snow" -> R.raw.foggy
                            "rain and snow" -> R.raw.sleet
                            "light shower snow" -> R.raw.heavy_snow
                            "shower snow" -> R.raw.heavy_snow
                            "heavy shower snow" -> R.raw.heavy_snow

//                            Thunderstorm
                            "thunderstorm with light rain" -> R.raw.storm_showers_day
                            "thunderstorm with rain" -> R.raw.storm_showers_day
                            "thunderstorm with heavy rain" -> R.raw.thunder
                            "light thunderstorm" -> R.raw.thunder
                            "thunderstorm" -> R.raw.thunder
                            "heavy thunderstorm" -> R.raw.thunder
                            "ragged thunderstorm" -> R.raw.thunder
                            "thunderstorm with light drizzle" -> R.raw.thunder_rain
                            "thunderstorm with drizzle" -> R.raw.thunder_rain
                            "thunderstorm with heavy drizzle" -> R.raw.thunder_rain

//                            Atmospheric Conditions
                            "mist" -> R.raw.mist_1
                            "smoke" -> R.raw.foggy
                            "haze" -> R.raw.mist
                            "sand/dust whirls" -> R.raw.foggy
                            "fog" -> R.raw.foggy
                            "freezing fog" -> R.raw.foggy

//                            Extreme Conditions
                            "sand" -> R.raw.foggy
                            "dust" -> R.raw.foggy
                            "volcanic ash" -> R.raw.foggy
                            "squalls" -> R.raw.foggy
                            "tornado" -> R.raw.tornado
                            else -> R.raw.galaxy_anim
                        }

                    }

                    binding.lottieAnimation.setAnimation(getAnimationRes(weatherCondition))
                    binding.lottieAnimation.playAnimation()


//                   val apiIcon = if (icon != null) {
//                        val iconUrl = "https://openweathermap.org/img/wn/${icon}@2x.png"
//                        Picasso.get()
//                            .load(iconUrl)
//                            .error(R.drawable.error_image)
//                            .into(binding.mainWeatherImage)
//                    } else {
//                       Toast.makeText(this@MainActivity, "Image is Not Available", Toast.LENGTH_SHORT).show()
//                   }

                    binding.mainCityName.text = city
                    binding.mainLongitude.text = "Longitude: ${longitude}"
                    binding.mainLatitude.text = "Latitude: ${latitude}"
                    binding.mainTempDegree.text = "${temperature} \u00B0C"
                    binding.mainEnv.text = envDescription

                    binding.feelsLikeValue.text = "${feelsLike} °C"
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

    override fun onRefresh() {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "refreshing", Toast.LENGTH_SHORT).show()
            fetchWeatherData()
            swipeRefreshLayout.isRefreshing = false
        }, 1000)
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