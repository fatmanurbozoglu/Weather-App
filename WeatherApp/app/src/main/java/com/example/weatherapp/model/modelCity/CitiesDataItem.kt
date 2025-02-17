package com.example.weatherapp.model.modelCity


import com.google.gson.annotations.SerializedName

data class CitiesDataItem(
    @SerializedName("country")
    val country: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String,
    @SerializedName("name")
    val name: String
)