package com.example.weather

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weather.databinding.ActivityTodayForecastDetailsBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodayForecastDetails : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val binding: ActivityTodayForecastDetailsBinding by lazy {
        ActivityTodayForecastDetailsBinding.inflate(layoutInflater)
    }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var todayDetailFCAdapter: TodayDetailFCAdapter
    private val todayDetailFCAdapterArrayList = ArrayList<TodayModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        swipeRefreshLayout = binding.refreshSwipe
        swipeRefreshLayout.setOnRefreshListener(this)

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
    }

    private fun getCurrentDate(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    override fun onRefresh() {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "refreshing", Toast.LENGTH_SHORT).show()
            getCurrentDate()
            TodayDetailFCAdapter(this, todayDetailFCAdapterArrayList)
            todayDetailFCAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }, 1000)
    }

}