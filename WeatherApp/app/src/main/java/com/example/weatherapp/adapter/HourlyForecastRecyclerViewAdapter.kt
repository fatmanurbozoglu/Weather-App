package com.example.weatherapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.RecyclerviewHourlyForecastBinding
import com.example.weatherapp.model.HourlyForecast
import com.example.weatherapp.service.WeatherHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HourlyForecastRecyclerViewAdapter(val hourlyForecastList: ArrayList<HourlyForecast>, private val context: Context): RecyclerView.Adapter<HourlyForecastRecyclerViewAdapter.HourlyForecastHolder>() {
    private lateinit var binding: RecyclerviewHourlyForecastBinding

    var city = "Karaman"
    val url ="https://world-weather-online-api1.p.rapidapi.com/weather.ashx?q=$city&num_of_days=7&tp=1&lang=en&aqi=yes&alerts=no&format=json"
    private val ioScope = CoroutineScope(Dispatchers.IO)

    class HourlyForecastHolder(val binding:RecyclerviewHourlyForecastBinding):RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RecyclerviewHourlyForecastBinding.inflate(inflater,parent,false)
        val view = inflater.inflate(R.layout.recyclerview_hourly_forecast,parent,false)
        return HourlyForecastHolder(binding)
    }
    override fun getItemCount(): Int {
        return hourlyForecastList.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HourlyForecastHolder, position: Int) {
        ioScope.launch {
            val data = WeatherHelper.getWeather(url)

            if (data != null){
                Log.d("HOURLY ADAPTER", "$data")
                val hourlyUrl = data.data.weather[0].hourly[0]
                val hourlyItem = hourlyForecastList[position]

                withContext(Dispatchers.Main){
                    if(position == 0){
                        holder.binding.timeTextView.text = "Now"
                    }else{
                        holder.binding.timeTextView.text = hourlyItem.time
                    }
                    holder.binding.temperatureTextView.text = "${hourlyItem.tempC}°"
                    Glide.with(context).load(hourlyItem.weatherIconUrl).into(holder.binding.imageView)
                }
            }
        }
    }
}