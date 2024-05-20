package com.example.weatherapp.service

import android.util.Log
import com.example.weatherapp.model.modelWeather.WeatherData
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request

object WeatherHelper {
    suspend fun getWeather(url: String): WeatherData? {
            var data : WeatherData?=null
            try {
                val client = OkHttpClient()

                //val weatherUrl = "https://world-weather-online-api1.p.rapidapi.com/weather.ashx?q=${latitudeLocation}%2C${longitudeLocation}&num_of_days=7&lang=en&aqi=yes&alerts=no&format=json"
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("X-RapidAPI-Key", "c562746eafmsh6cf7a036ed58141p1a596bjsn0ec4561d590c")
                    .addHeader("X-RapidAPI-Host", "world-weather-online-api1.p.rapidapi.com")
                    .build()
                val response = client.newCall(request).execute()

                if(response.isSuccessful){
                    val result = response.body?.string()

                    Log.d("RESULT",result.toString())
                    val gson = GsonBuilder().create()
                    val weatherData = gson.fromJson(result, WeatherData::class.java)
                    data = weatherData
                }
                // response'nin body kısmını kapat
                response.body?.close()
            }catch (e:Exception){
                Log.e("RESULT","HATA $e")
            }
        return data
    }
}