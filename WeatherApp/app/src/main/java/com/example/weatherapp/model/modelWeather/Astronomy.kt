package com.example.weatherapp.model.modelWeather


import com.google.gson.annotations.SerializedName

data class Astronomy(
    @SerializedName("moon_phase")
    val moonPhase: String,
    @SerializedName("moonrise")
    val moonrise: String,
    @SerializedName("moonset")
    val moonset: String,
    @SerializedName("moon_illumination")
    val moonIllumination: String,
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("sunset")
    val sunset: String
)