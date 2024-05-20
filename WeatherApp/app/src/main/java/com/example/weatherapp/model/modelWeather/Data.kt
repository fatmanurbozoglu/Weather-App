package com.example.weatherapp.model.modelWeather


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("current_condition")
    val currentCondition: List<CurrentCondition>,
    @SerializedName("request")
    val request: List<Request>,
    @SerializedName("weather")
    val weather: List<Weather>
)