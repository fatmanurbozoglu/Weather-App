package com.example.weatherapp.model.modelWeather


import com.google.gson.annotations.SerializedName

data class CurrentCondition(
    @SerializedName("air_quality")
    val airQuality: AirQuality,
    @SerializedName("cloudcover")
    val cloudcover: String,
    @SerializedName("FeelsLikeC")
    val feelsLikeC: String,
    @SerializedName("FeelsLikeF")
    val feelsLikeF: String,
    @SerializedName("humidity")
    val humidity: String,
    @SerializedName("observation_time")
    val observationTime: String,
    @SerializedName("precipMM")
    val precipMM: String,
    @SerializedName("pressure")
    val pressure: String,
    @SerializedName("temp_C")
    val tempC: String,
    @SerializedName("temp_F")
    val tempF: String,
    @SerializedName("uvIndex")
    val uvIndex: String,
    @SerializedName("visibility")
    val visibility: String,
    @SerializedName("weatherCode")
    val weatherCode: String,
    @SerializedName("weatherDesc")
    val weatherDesc: List<WeatherDesc>,
    @SerializedName("weatherIconUrl")
    val weatherIconUrl: List<WeatherIconUrl>,
    @SerializedName("winddir16Point")
    val winddir16Point: String,
    @SerializedName("winddirDegree")
    val winddirDegree: String,
    @SerializedName("windspeedKmph")
    val windspeedKmph: String,
    @SerializedName("windspeedMiles")
    val windspeedMiles: String
)