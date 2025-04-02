package com.example.weather

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.ActivityTodayForecastDetailsBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodayForecastDetails : AppCompatActivity() {

    private val binding: ActivityTodayForecastDetailsBinding by lazy {
        ActivityTodayForecastDetailsBinding.inflate(layoutInflater)
    }

    private lateinit var todayDetailFCAdapter: TodayDetailFCAdapter
    private val todayDetailFCAdapterArrayList = ArrayList<TodayModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnBackTFD.setOnClickListener {
            finish()
        }

        val cityName = intent.getStringExtra("CityName")
        binding.todayFDLocation.text = cityName

        binding.todayTimeDate.text = getCurrentDate()

        todayDetailFCAdapter = TodayDetailFCAdapter(this, todayDetailFCAdapterArrayList)
        binding.mainTodayDetailForecastRV.adapter = todayDetailFCAdapter
        binding.mainTodayDetailForecastRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Then populate the data and notify
        val weatherList = intent.getParcelableArrayListExtra<TodayModel>("WeatherData")
        if (weatherList != null) {
            todayDetailFCAdapterArrayList.addAll(weatherList)
            todayDetailFCAdapter.notifyDataSetChanged()
        }

        Log.d("TodayForecastDetails", "onCreate: Data loaded - ${todayDetailFCAdapterArrayList.size} items")
    }

    private fun getCurrentDate(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

}