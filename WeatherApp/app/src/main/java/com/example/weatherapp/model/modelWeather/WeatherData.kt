package com.example.weatherapp.model.modelWeather


import com.example.weatherapp.model.modelWeather.Data
import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("data")
    val data: Data
)