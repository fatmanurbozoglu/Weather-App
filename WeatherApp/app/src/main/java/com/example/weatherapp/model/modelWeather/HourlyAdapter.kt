package com.example.weatherapp.model.modelWeather

import com.example.weatherapp.model.modelWeather.Hourly
import com.google.gson.annotations.SerializedName

data class HourlyAdapter(
    @SerializedName("hourly")
    val hourly: List<Hourly>
    )
