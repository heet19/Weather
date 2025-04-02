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
        Log.d("rvBindingHolder", "onBindViewHolder: ")
        holder.binding.todayDTemp.text = currentData.temperature
        holder.binding.todayDTime.text = currentData.todayTime
        holder.binding.todayDFDescription.text = currentData.description
        holder.binding.todayDFFeelsLike.text = currentData.feelsLike.toString()

        // Set initial visibility states
        holder.binding.gridLayoutMain.visibility = View.GONE
        holder.binding.todayDFDescription.visibility = View.GONE
        holder.binding.dividerBelowDescription.visibility = View.GONE
        holder.binding.expandHDetail.setImageResource(R.drawable.baseline_expand_more_24)

        holder.binding.gridLayoutMain.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        holder.binding.expandHDetail.setOnClickListener {
            val isExpanded = holder.binding.gridLayoutMain.visibility == View.VISIBLE

            if (isExpanded) {
                // Collapse
                holder.binding.gridLayoutMain.visibility = View.GONE
                holder.binding.todayDFDescription.visibility = View.GONE
                holder.binding.dividerBelowDescription.visibility = View.GONE
                holder.binding.expandHDetail.setImageResource(R.drawable.baseline_expand_more_24)
            } else {
                // Expand
                holder.binding.gridLayoutMain.visibility = View.VISIBLE
                holder.binding.todayDFDescription.visibility = View.VISIBLE
                holder.binding.dividerBelowDescription.visibility = View.VISIBLE
                holder.binding.expandHDetail.setImageResource(R.drawable.baseline_expand_less_24)
            }
        }

        holder.binding.todayDFWindDeg.text = currentData.windDeg.toString()
        holder.binding.todayDFWindSpeed.text = currentData.windSpeed.toString()
        holder.binding.todayDFHumidity.text = currentData.humidity.toString()
        holder.binding.todayDFVisibility.text = currentData.visibility.toString()
        holder.binding.todayDFCloud.text = currentData.clouds.toString()
        holder.binding.todayDFPop.text = currentData.pop.toString()
        holder.binding.todayDFTemperature.text = currentData.minTemp.toString()
        holder.binding.todayDFSeaLevel.text = currentData.seaLevel.toString()

    }

    class MyViewHolder(val binding: TodayDetailForecastBinding) : RecyclerView.ViewHolder(binding.root)  {

    }

}