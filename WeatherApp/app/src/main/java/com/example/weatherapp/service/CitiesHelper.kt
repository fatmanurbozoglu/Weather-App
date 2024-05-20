package com.example.weatherapp.service

import android.util.Log
import com.example.weatherapp.model.modelCity.CitiesDataItem
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request


// https://raw.githubusercontent.com/lutangar/cities.json/master/cities.json
object CitiesHelper {
    suspend fun getCities(citiesUrl: String): CitiesDataItem? {
        var data: CitiesDataItem? = null
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(citiesUrl)
                .get()
                .build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val result = response.body?.string()
                Log.d("RESULT", result.toString())
                val gson = GsonBuilder().create()
                val citiesDataItem = gson.fromJson(result, CitiesDataItem::class.java)
                data = citiesDataItem

            }
            // response'nin body kısmını kapat
            response.body?.close()
        } catch (e: Exception) {
            Log.e("RESULT", "HATA $e")
        }
        return data
    }

}

