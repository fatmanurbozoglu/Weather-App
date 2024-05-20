package com.example.weatherapp.model.modelWeather


import com.google.gson.annotations.SerializedName

data class AirQuality(
    @SerializedName("co")
    val co: String,
    @SerializedName("gb-defra-index")
    val gbDefraIndex: String,
    @SerializedName("no2")
    val no2: String,
    @SerializedName("o3")
    val o3: String,
    @SerializedName("pm10")
    val pm10: String,
    @SerializedName("pm2_5")
    val pm25: String,
    @SerializedName("so2")
    val so2: String,
    @SerializedName("us-epa-index")
    val usEpaIndex: String
)