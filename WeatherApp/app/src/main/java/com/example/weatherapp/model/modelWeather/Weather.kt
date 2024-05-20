package com.example.weatherapp.model.modelWeather


import com.example.weatherapp.model.modelWeather.AirQuality
import com.example.weatherapp.model.modelWeather.Astronomy
import com.example.weatherapp.model.modelWeather.Hourly
import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("air_quality")
    val airQuality: AirQuality,
    @SerializedName("astronomy")
    val astronomy: List<Astronomy>,
    @SerializedName("date")
    val date: String,
    @SerializedName("hourly")
    val hourly: List<Hourly>,
    @SerializedName("maxtempC")
    val maxtempC: String,
    @SerializedName("maxtempF")
    val maxtempF: String,
    @SerializedName("mintempC")
    val mintempC: String,
    @SerializedName("mintempF")
    val mintempF: String,
    @SerializedName("sunHour")
    val sunHour: String,
    @SerializedName("totalSnow_cm")
    val totalSnowCm: String,
    @SerializedName("uvIndex")
    val uvIndex: String
)