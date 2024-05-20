package com.example.weatherapp.model.modelWeather


import com.google.gson.annotations.SerializedName

data class WeatherDesc(
    @SerializedName("value")
    var value: String
)