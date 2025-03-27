package com.example.weather

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Resources
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import androidx.appcompat.widget.SearchView

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

//    47561fc40c887a4bbb468d6530937de1

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var currentCity: String = "rajkot"

    private var isAnimationStartedSRSS = false
    private var isAnimationStartedCSCS = false

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize preferences
        UserPreferences.init(this)

        // Set the toolbar as action bar
        setSupportActionBar(binding.toolbarDetail)

        currentCity = UserPreferences.lastCity
        fetchWeatherData(currentCity)

        swipeRefreshLayout = binding.refreshSwipe
        swipeRefreshLayout.setOnRefreshListener(this)

    }

    private fun fetchWeatherData(city: String) {
        currentCity = city
        UserPreferences.setLastCity(this, city)
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(APIInterface::class.java)

        val response = retrofit.getWeatherData(city, "47561fc40c887a4bbb468d6530937de1", "metric")
        response.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.d("onResponse", "onResponse: ")
                    displayWeatherData(responseBody)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown Error"
                    Log.e("API Error", errorMessage)
                    Toast.makeText(this@MainActivity,"City not found: $errorMessage",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<WeatherData>, response: Throwable) {
                Log.e("API Error", "Failure: ${response.message}")
                Toast.makeText(this@MainActivity,"Failed to fetch data: ${response.message}",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayWeatherData(responseBody: WeatherData) {
        val city = responseBody.name
        val longitude = responseBody.coord.lon
        val latitude = responseBody.coord.lat
        val temperature = responseBody.main.temp
        val envDescription = responseBody.weather.firstOrNull()?.description ?: "No data"
        val feelsLike = responseBody.main.feels_like
        val humidity = responseBody.main.humidity
        val visibilityMeters = responseBody.visibility.toDouble()
        val windSpeedMs = responseBody.wind.speed
        val windDirection = responseBody.wind.deg
        val windGustMs = responseBody.wind.gust
        val pressureHpa = responseBody.main.pressure.toDouble()
        val groundPressureHpa = responseBody.main.grnd_level?.toDouble() ?: 0.0
        val seaLevelHpa = responseBody.main.sea_level?.toDouble() ?: 0.0
        val cloud = responseBody.clouds.all
        val clear = 100 - cloud
        clearSkyProgress(clear)

        val minTemperature = responseBody.main.temp_min
        val maxTemperature = responseBody.main.temp_max
        val sunrise = responseBody.sys.sunrise
        val sunriseNew = convertUnixToTime(sunrise)
        val sunset = responseBody.sys.sunset
        val sunsetNew = convertUnixToTime(sunset)
        val weatherCondition = responseBody.weather.firstOrNull()?.description ?: "Clear"

        // Convert units based on user preferences
        val (tempValue, tempUnit) = UnitConverter.convertTemperature(temperature, UserPreferences.temperatureUnit)
        val (feelsLikeValue, feelsLikeUnit) = UnitConverter.convertTemperature(feelsLike, UserPreferences.temperatureUnit)
        val (minTempValue, minTempUnit) = UnitConverter.convertTemperature(minTemperature, UserPreferences.temperatureUnit)
        val (maxTempValue, maxTempUnit) = UnitConverter.convertTemperature(maxTemperature, UserPreferences.temperatureUnit)

        val (windSpeedValue, windSpeedUnit) = UnitConverter.convertWindSpeed(windSpeedMs, UserPreferences.windSpeedUnit)
        val (windGustValue, windGustUnit) = UnitConverter.convertWindSpeed(windGustMs, UserPreferences.windSpeedUnit)

        val (pressureValue, pressureUnit) = UnitConverter.convertPressure(pressureHpa, UserPreferences.pressureUnit)
        val (groundPressureValue, groundPressureUnit) = UnitConverter.convertPressure(groundPressureHpa, UserPreferences.pressureUnit)
        val (seaLevelValue, seaLevelUnit) = UnitConverter.convertPressure(seaLevelHpa, UserPreferences.pressureUnit)

        val (visibilityValue, visibilityUnit) = UnitConverter.convertVisibility(visibilityMeters, UserPreferences.visibilityUnit)

        // Set animation
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

        // Update UI with converted values
        binding.mainCityName.text = city
        binding.mainLongitude.text = "Longitude: ${longitude}"
        binding.mainLatitude.text = "Latitude: ${latitude}"
        binding.mainTempDegree.text = "${String.format("%.1f", tempValue)} $tempUnit"
        binding.mainEnv.text = envDescription

        binding.feelsLikeValue.text = "${String.format("%.1f", feelsLikeValue)} $feelsLikeUnit"
        binding.humidityValue.text = "${humidity} %"
        binding.visibilityValue.text = "${String.format("%.1f", visibilityValue)} $visibilityUnit"

        binding.windSpeedValue.text = "${String.format("%.1f", windSpeedValue)} $windSpeedUnit"
        binding.windDirectionValue.text = "${windDirection}°"
        binding.windGustSpeedValue.text = "${String.format("%.1f", windGustValue)} $windGustUnit"

        binding.pressureValue.text = "${String.format("%.1f", pressureValue)} $pressureUnit"
        binding.groundLevelPressureValue.text = "${String.format("%.1f", groundPressureValue)} $groundPressureUnit"
        binding.seaLevelValue.text = "${String.format("%.1f", seaLevelValue)} $seaLevelUnit"

        binding.cloudPB.max = 100
        binding.cloudPB.progress = clear
        binding.cloudProgressBarInitialText2.text = "${clear} %"
        binding.cloudProgressBarFinalText2.text = "${cloud} %"

        binding.minTempValue.text = "${String.format("%.1f", minTempValue)} $minTempUnit"
        binding.maxTempValue.text = "${String.format("%.1f", maxTempValue)} $maxTempUnit"

        binding.sunRiseSetInitialText2.text = sunriseNew
        binding.sunRiseSetFinalText2.text = sunsetNew

        // Calculate and set progress
        val progress = calculateSunProgress(sunrise, sunset)
        binding.sunRiseSetPB.max = 100
        binding.sunRiseSetPB.progress = progress
        sunriseSunsetProgress(progress)

    }

    private fun clearSkyProgress(clear: Int) {
        binding.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            if (!isAnimationStartedCSCS) { // Check if animation has already run
                val location = IntArray(2)
                binding.cloudProgressBarCard.getLocationOnScreen(location)
                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val viewTop = location[1]
                if (viewTop in 0..screenHeight) { // If visible
                    isAnimationStartedCSCS = true // Set flag to true
                    startProgressAnimationCSCS(clear.toFloat())
                }
            }
        }

    }

    private fun startProgressAnimationCSCS(till: Float) {
        val animator = ObjectAnimator.ofFloat(1f, till) // Use Float values explicitly
        animator.duration = 3500 // 10 seconds
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float // Ensure Float type
            binding.cloudPB.progress = animatedValue.toInt() // Convert to Int safely
        }
        animator.start()

    }

    private fun sunriseSunsetProgress(progress: Int) {
        binding.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            if (!isAnimationStartedSRSS) { // Check if animation has already run
                val location = IntArray(2)
                binding.sunRiseSetCard.getLocationOnScreen(location)
                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val viewTop = location[1]
                if (viewTop in 0..screenHeight) { // If visible
                    isAnimationStartedSRSS = true // Set flag to true
                    startProgressAnimationSRSS(progress.toFloat())
                }
            }
        }
    }

    private fun startProgressAnimationSRSS(till: Float) {
        val animator = ObjectAnimator.ofFloat(1f, till) // Use Float values explicitly
        animator.duration = 1500 // 10 seconds
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float // Ensure Float type
            binding.sunRiseSetPB.progress = animatedValue.toInt() // Convert to Int safely
        }
        animator.start()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        // Get the SearchView
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "Enter city name"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    Log.d("SearchQuery", "Search submitted: $query")
                    currentCity = query
                    fetchWeatherData(query)

                    searchView.clearFocus()
                    searchView.setQuery("", false)
                    searchItem.collapseActionView()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("SearchQuery", "Text changing: $newText")
                return true
            }
        })
        return true
    }

    // Handle Menu Item Clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("onOptionsItemSelected", "Selected Item: ${item.title}")
        return when (item.itemId) {
            R.id.search -> {
                true
            }
            R.id.setting -> {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRefresh() {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "refreshing", Toast.LENGTH_SHORT).show()
            fetchWeatherData(currentCity)
            swipeRefreshLayout.isRefreshing = false
        }, 1000)
    }

}