package com.example.weatherapp.model.modelReverseGeocoding


import com.google.gson.annotations.SerializedName

data class ReverseGeocodingData(
    @SerializedName("Country")
    val country: String,
    @SerializedName("CountryId")
    val countryId: String,
    @SerializedName("GMT_offset")
    val gMTOffset: Int,
    @SerializedName("LocalTime_Now")
    val localTimeNow: String,
    @SerializedName("TimeZoneId")
    val timeZoneId: String,
    @SerializedName("TimeZoneName")
    val timeZoneName: String
)