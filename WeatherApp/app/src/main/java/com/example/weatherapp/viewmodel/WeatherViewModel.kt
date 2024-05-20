package com.example.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.model.modelWeather.WeatherData
import com.example.weatherapp.service.WeatherHelper
import io.reactivex.disposables.CompositeDisposable

class WeatherViewModel(application: Application): BaseWeatherViewModel(application) {
    val errorMessage = MutableLiveData<Boolean>()
    val weatherLoading = MutableLiveData<Boolean>()
    val weatherForecast = MutableLiveData<WeatherData>()
    private val weatherApiServis = WeatherHelper
    private val disposable = CompositeDisposable()
    // disposable -> istekleri kullandıktan sonra disposable i cagırıp onlardan kurtulabiliriz
}