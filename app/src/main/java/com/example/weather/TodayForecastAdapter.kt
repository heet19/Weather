package com.example.weather

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.TodayForecastBinding

class TodayForecastAdapter(val context : Activity, var todayWeatherDataArrayList: List<TodayModel>) : RecyclerView.Adapter<TodayForecastAdapter.MyViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = TodayForecastBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return todayWeatherDataArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = todayWeatherDataArrayList[position]
        holder.binding.todayTime.text = currentItem.todayTime
        holder.binding.todayAnimation.setAnimation(currentItem.animation)

        val temperature = currentItem.temperature.toDouble()
        val (tempValue, tempUnit) = UnitConverter.convertTemperature(temperature, UserPreferences.temperatureUnit)
        holder.binding.todayTemperature.text = "${String.format("%.1f", tempValue)} $tempUnit"


    }

    class MyViewHolder(val binding: TodayForecastBinding) : RecyclerView.ViewHolder(binding.root)  {

    }

}