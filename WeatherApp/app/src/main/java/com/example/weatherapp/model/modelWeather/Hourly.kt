package com.example.weatherapp.model.modelWeather


import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("air_quality")
    val airQuality: AirQuality,
    @SerializedName("chanceoffog")
    val chanceoffog: String,
    @SerializedName("chanceoffrost")
    val chanceoffrost: String,
    @SerializedName("chanceofhightemp")
    val chanceofhightemp: String,
    @SerializedName("chanceofovercast")
    val chanceofovercast: String,
    @SerializedName("chanceofrain")
    val chanceofrain: String,
    @SerializedName("chanceofremdry")
    val chanceofremdry: String,
    @SerializedName("chanceofsnow")
    val chanceofsnow: String,
    @SerializedName("chanceofsunshine")
    val chanceofsunshine: String,
    @SerializedName("chanceofthunder")
    val chanceofthunder: String,
    @SerializedName("chanceofwindy")
    val chanceofwindy: String,
    @SerializedName("cloudcover")
    val cloudcover: String,
    @SerializedName("DewPointC")
    val dewPointC: String,
    @SerializedName("DewPointF")
    val dewPointF: String,
    @SerializedName("FeelsLikeC")
    val feelsLikeC: String,
    @SerializedName("FeelsLikeF")
    val feelsLikeF: String,
    @SerializedName("HeatIndexC")
    val heatIndexC: String,
    @SerializedName("HeatIndexF")
    val heatIndexF: String,
    @SerializedName("humidity")
    val humidity: String,
    @SerializedName("precipMM")
    val precipMM: String,
    @SerializedName("pressure")
    val pressure: String,
    @SerializedName("tempC")
    val tempC: String,
    @SerializedName("tempF")
    val tempF: String,
    @SerializedName("time")
    val time: String,
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
    @SerializedName("WindChillC")
    val windChillC: String,
    @SerializedName("WindChillF")
    val windChillF: String,
    @SerializedName("WindGustKmph")
    val windGustKmph: String,
    @SerializedName("WindGustMiles")
    val windGustMiles: String,
    @SerializedName("winddir16Point")
    val winddir16Point: String,
    @SerializedName("winddirDegree")
    val winddirDegree: String,
    @SerializedName("windspeedKmph")
    val windspeedKmph: String,
    @SerializedName("windspeedMiles")
    val windspeedMiles: String
)