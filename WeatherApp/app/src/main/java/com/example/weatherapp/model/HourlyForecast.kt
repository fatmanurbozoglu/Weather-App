package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class HourlyForecast(
// saatlik hava durumu tahmini
    val time: String,
    val tempC: String,
    val weatherIconUrl: String,
)
data class DailyForecast(
    // 14 günlük hava durumu tahminleri
    val days:  String?,
    val temperatureFirst: String?,
    val temperatureLast: String?,
    val imageDaily: String?
)
data class CurrentForecast(
    // diger hava durumu ogelerı
    val uvIndex: String?

)
