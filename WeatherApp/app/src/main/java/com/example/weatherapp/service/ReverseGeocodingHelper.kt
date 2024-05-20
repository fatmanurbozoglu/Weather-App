package com.example.weatherapp.service

import android.util.Log
import com.example.weatherapp.model.modelReverseGeocoding.ReverseGeocodingData
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request

object ReverseGeocodingHelper {
    suspend fun getReverseGeocoding(url: String): ReverseGeocodingData?{
        var data: ReverseGeocodingData? = null
        try {
            val client = OkHttpClient()
            // "https://geocodeapi.p.rapidapi.com/GetTimezone?latitude=37.4226711&longitude=-122.0849872"
            val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "324e75cdd7msh263c525eb16a1bep1303c4jsn2cdbba3b3f17")
                .addHeader("X-RapidAPI-Host", "geocodeapi.p.rapidapi.com")
                .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful){
                val result = response.body?.string()
                Log.d("REVERSE",result.toString())
                val gson = GsonBuilder().create()
                val reverseGeocodingData = gson.fromJson(result, ReverseGeocodingData::class.java)
                data = reverseGeocodingData
            }
            // response'nin body kısmını kapat
            response.body?.close()
        }catch (e: Exception){
            Log.e("REVERSE","HATA $e")
        }
        return data
    }
}