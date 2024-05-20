package com.example.weatherapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Half.toFloat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.RecyclerviewDailyWeatherForecastBinding
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.service.WeatherHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.BitSet
import kotlin.math.min

class DailyWeatherForecastRecyclerViewAdapter(val dailyForecastList: ArrayList<DailyForecast>, private val context: Context): RecyclerView.Adapter<DailyWeatherForecastRecyclerViewAdapter.DailyWeatherForecastHolder>() {
    private lateinit var binding: RecyclerviewDailyWeatherForecastBinding
    private val ioScope = CoroutineScope(Dispatchers.IO)
    var city = "Karaman"
    val url ="https://world-weather-online-api1.p.rapidapi.com/weather.ashx?q=$city&num_of_days=7&tp=1&lang=en&aqi=yes&alerts=no&format=json"
    var maxValue = 0
    var minValue = 0

    class DailyWeatherForecastHolder(val binding: RecyclerviewDailyWeatherForecastBinding): RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherForecastHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RecyclerviewDailyWeatherForecastBinding.inflate(inflater,parent,false)
        val view = inflater.inflate(R.layout.recyclerview_daily_weather_forecast,parent,false)
        return DailyWeatherForecastHolder(binding)
    }
    override fun getItemCount(): Int {
        return dailyForecastList.size
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: DailyWeatherForecastHolder, position: Int) {
        ioScope.launch {
            val data = WeatherHelper.getWeather(url)
            if (data != null){
                Log.d("DAILY ADAPTER", "$data")
                val dailyItem = dailyForecastList[position]
                val dailyUrl = data.data.weather[0]

                withContext(Dispatchers.Main){
                if(position == 0){
                        holder.binding.daysTextView.text = "Today"
                    }else{
                        holder.binding.daysTextView.text = dailyItem.days
                    }
                    val temperatureFirst = dailyItem.temperatureFirst?.toInt()
                    val temperatureLast = dailyItem.temperatureLast?.toInt()
                    holder.binding.firstTemperatureTextView.text = "${temperatureFirst}°"
                    holder.binding.lastTemperatureTextView.text = "${temperatureLast}°"
                    Glide.with(context).load(dailyItem.imageDaily).into(holder.binding.weatherImageView)

                    val minValue = dailyForecastList.minByOrNull { it.temperatureFirst!! }
                    val maxValue = dailyForecastList.maxByOrNull { it.temperatureLast!! }
                    val minTemperature = minValue?.temperatureFirst
                    val maxTemperature = maxValue?.temperatureLast

                    holder.binding.dailyRangeSlider.apply {
                        holder.binding.dailyRangeSlider.valueTo = maxTemperature.toString().toFloat()
                        holder.binding.dailyRangeSlider.valueFrom= minTemperature.toString().toFloat()
                        setValues(temperatureFirst!!.toFloat(), temperatureLast!!.toFloat())
                        holder.binding.dailyRangeSlider.setOnTouchListener { _, _ ->
                            true
                        }
                    }
                    setMaxMinValues(minTemperature!!.trim().toInt(),maxTemperature!!.trim().toInt())
                    notifyDataSetChanged()
                }
            }
        }
    }
    fun setMaxMinValues(min : Int, max : Int){
        minValue = min
        maxValue = max
    }
}