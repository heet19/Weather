package com.example.weather

import android.animation.LayoutTransition
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.TodayDetailForecastBinding

class TodayDetailFCAdapter(val context : Activity, var todayDetailFCAdapterArrayList: List<TodayModel>) : RecyclerView.Adapter<TodayDetailFCAdapter.MyViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = TodayDetailForecastBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("Sizeee", "${todayDetailFCAdapterArrayList.size}")
        return todayDetailFCAdapterArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentData = todayDetailFCAdapterArrayList[position]

        val (tempValue, tempUnit) = UnitConverter.convertTemperature(currentData.temperature.toDouble(), UserPreferences.temperatureUnit)
        val (feelsLikeValue, feelsLikeUnit) = UnitConverter.convertTemperature(currentData.feelsLike, UserPreferences.temperatureUnit)
        val (minTempValue, minTempUnit) = UnitConverter.convertTemperature(currentData.minTemp, UserPreferences.temperatureUnit)
        val (windSpeedValue, windSpeedUnit) = UnitConverter.convertWindSpeed(currentData.windSpeed, UserPreferences.windSpeedUnit)
        val (seaLevelValue, seaLevelUnit) = UnitConverter.convertPressure(currentData.seaLevel.toDouble(), UserPreferences.pressureUnit)
        val (visibilityValue, visibilityUnit) = UnitConverter.convertVisibility(currentData.visibility.toDouble(), UserPreferences.visibilityUnit)

        holder.binding.todayDTemp.text = "${String.format("%.1f", tempValue)} $tempUnit"
        holder.binding.todayDTime.text = currentData.todayTime
        holder.binding.todayDFDescription.text = currentData.description
        holder.binding.todayDFFeelsLike.text = "${String.format("%.1f", feelsLikeValue)} $feelsLikeUnit"
        holder.binding.todayDFWindDeg.text = "${currentData.windDeg}°"
        holder.binding.todayDFWindSpeed.text = "${String.format("%.1f", windSpeedValue)} $windSpeedUnit"
        holder.binding.todayDFHumidity.text = "${currentData.humidity} %"
        holder.binding.todayDFVisibility.text = "${String.format("%.1f", visibilityValue)} $visibilityUnit"
        holder.binding.todayDFCloud.text = "${currentData.clouds} %"
        holder.binding.todayDFPop.text = currentData.pop.toString()
        holder.binding.todayDFTemperature.text = "${String.format("%.1f", minTempValue)} $minTempUnit"
        holder.binding.todayDFSeaLevel.text = "${String.format("%.1f", seaLevelValue)} $seaLevelUnit"

        holder.binding.todayDIcon.setAnimation(currentData.animation)
        holder.binding.todayDIcon.playAnimation()


        // Set initial visibility states
        holder.binding.gridLayoutMain.visibility = View.GONE
        holder.binding.todayDFDescription.visibility = View.GONE
        holder.binding.dividerBelowDescription.visibility = View.GONE
        holder.binding.dividerAboveDescription.visibility = View.GONE
        holder.binding.expandHDetail.setImageResource(R.drawable.baseline_expand_more_24)

        holder.binding.gridLayoutMain.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        holder.binding.todayTimeLayout.setOnClickListener {
            val isExpanded = holder.binding.gridLayoutMain.visibility == View.VISIBLE

            if (isExpanded) {
                // Collapse
                holder.binding.gridLayoutMain.visibility = View.GONE
                holder.binding.todayDFDescription.visibility = View.GONE
                holder.binding.dividerBelowDescription.visibility = View.GONE
                holder.binding.dividerAboveDescription.visibility = View.GONE
                holder.binding.expandHDetail.setImageResource(R.drawable.baseline_expand_more_24)
            } else {
                // Expand
                holder.binding.gridLayoutMain.visibility = View.VISIBLE
                holder.binding.todayDFDescription.visibility = View.VISIBLE
                holder.binding.dividerBelowDescription.visibility = View.VISIBLE
                holder.binding.dividerAboveDescription.visibility = View.VISIBLE
                holder.binding.expandHDetail.setImageResource(R.drawable.baseline_expand_less_24)
            }
        }

    }

    class MyViewHolder(val binding: TodayDetailForecastBinding) : RecyclerView.ViewHolder(binding.root)  {

    }

}