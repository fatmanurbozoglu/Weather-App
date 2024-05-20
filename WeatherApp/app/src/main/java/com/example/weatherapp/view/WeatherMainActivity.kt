package com.example.weatherapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.DailyWeatherForecastRecyclerViewAdapter
import com.example.weatherapp.adapter.HourlyForecastRecyclerViewAdapter
import com.example.weatherapp.databinding.ActivityWeatherMainBinding
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.model.HourlyForecast
import com.example.weatherapp.service.CitiesHelper
import com.example.weatherapp.service.ReverseGeocodingHelper
import com.example.weatherapp.service.WeatherHelper
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WeatherMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherMainBinding
    private lateinit var viewModel: WeatherViewModel

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var hourlyForecastRecyclerViewAdapter : HourlyForecastRecyclerViewAdapter
    private lateinit var dailyWeatherForecastRecyclerViewAdapter: DailyWeatherForecastRecyclerViewAdapter
    var hourlyForecastList = ArrayList<HourlyForecast>()
    var hourlyForecastFirstList = ArrayList<HourlyForecast>()
    val dailyForecastList = ArrayList<DailyForecast>()

    val cal = Calendar.getInstance()
    val currentHour = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)
    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    val currentDate = Date()

    var isPermission =false
    var isFromCity = false
    var latitude = 0.0
    var longitude = 0.0

    var enteredCity = "Karaman"
    val citiesUrl = "https://raw.githubusercontent.com/lutangar/cities.json/master/cities.json"
    var url ="https://world-weather-online-api1.p.rapidapi.com/weather.ashx?q=$enteredCity&num_of_days=7&tp=1&lang=en&aqi=yes&alerts=no&format=json"
  //  var url1 = "https://geocodeapi.p.rapidapi.com/GetTimezone?latitude=$latitude&longitude=-$longitude"

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel in tanımlamasını bitirmek için
        viewModel = ViewModelProviders.of(this)[WeatherViewModel ::class.java]

        isFromCity = true // kontrol
        if (isFromCity){
             url ="https://world-weather-online-api1.p.rapidapi.com/weather.ashx?q=$enteredCity&num_of_days=7&tp=1&lang=en&aqi=yes&alerts=no&format=json"
        }else{
             url = "https://world-weather-online-api1.p.rapidapi.com/weather.ashx?q=$latitude%2C$longitude&num_of_days=7&tp=1&lang=en&aqi=yes&alerts=no&format=json"
        }

        // FusedLocationProviderClient nesnesini oluşturun
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        defineRecyclerView()
        cities()
        checkPermissionRequest()

        binding.button.setOnClickListener {
            enteredCity = binding.editTextText.text.toString()
            if (enteredCity.isNotEmpty()){
                requestFromApi(url)
            }
            binding.editTextText.setText("")
        }

        // kullanıcı asagı dogru cektıgınde yapılacak islemler
        binding.swiperefreshlayout.setOnRefreshListener {
            val result = requestFromApi(url)
            requestFromApiReverseGeocoding(url)
            if(binding.swiperefreshlayout.isRefreshing){
                binding.swiperefreshlayout.isRefreshing=false
            }
            if(!result){
                // Toast mesajı ile kullanıcıya bilgi ver
                Toast.makeText(this, "refresh not done",Toast.LENGTH_SHORT)
            }

        }

    }


    private fun checkPermissionRequest(){
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){ // TIRAMUSU ve üstü versiyonsa
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
                // izin verilmemis, izin iste
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            }else{
                // izin verilmiş, konum bilgilerine göre hava durumu verilerini goster
                isPermission = true
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                fusedLocationClient.lastLocation.addOnSuccessListener { it ->
                    latitude = it.latitude
                    longitude = it.longitude
                    println("latitude: $latitude")
                    println("longitude: $longitude")
                    url = "https://world-weather-online-api1.p.rapidapi.com/weather.ashx?q=$latitude%2C$longitude&num_of_days=7&tp=1&lang=en&aqi=yes&alerts=no&format=json"
                   // url1 = "https://geocodeapi.p.rapidapi.com/GetTimezone?latitude=$latitude&longitude=-$longitude"
                    requestFromApiReverseGeocoding(url)
                    requestFromApi(url)
                }
            }
        }else{ // TIRAMUSU altı versiyonlarsa
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
                // izin verilmemis, izin iste
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            }else{
                // izin verilmis, konum bilgilerine göre hava durumu verilerini goster
                isPermission=true
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location != null){
                    latitude = location.latitude
                    longitude = location.longitude

                    url = "https://world-weather-online-api1.p.rapidapi.com/weather.ashx?q=$latitude%2C$longitude&num_of_days=7&tp=1&lang=en&aqi=yes&alerts=no&format=json"
                   // url1 = "https://geocodeapi.p.rapidapi.com/GetTimezone?latitude=$latitude&longitude=-$longitude"
                    requestFromApiReverseGeocoding(url)
                    requestFromApi(url)
                }
            }
        }
    }
   override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       if (requestCode == 1){
           if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               // izin verilince yapılacaklar
              checkPermissionRequest()
           }
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
   }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun saveDataToSharedPreferences(context: Context, data: Any, key: String){
        val sharedPreferences = context.getSharedPreferences("WeatherApp",Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonData = gson.toJson(data)
        // JSON veriyi SharedPreferences'e kaydetme
        sharedPreferences.edit().putString(key,jsonData).apply()
    }
    fun  getDataFromSharedPreferences(context: Context, data: Any, key: String) {
        // SharedPreferences'ten veriyi almak için kullanılacak fonksiyon
        var sharedPreferences = context.getSharedPreferences("WeatherApp", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonData = gson.toJson(data)
        sharedPreferences.edit().putString(key,jsonData).apply()

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    fun requestFromApi(url: String):Boolean {
        ioScope.launch {
          //  val url ="https://world-weather-online-api1.p.rapidapi.com/weather.ashx?q=$city&num_of_days=7&tp=1&lang=en&aqi=yes&alerts=no&format=json"
            val data =WeatherHelper.getWeather(url)
            if(data!=null){
                Log.d("WEATHER","$data")
               // val degerUrl = data.data.currentCondition[0].weatherIconUrl[0].value
                val locationUrl = data.data.request[0].query
                val currentConditionUrl = data.data.currentCondition[0]
                val weatherUrl = data.data.weather[0]
                val hourlyData = data.data.weather[0].hourly
                val dailyData = data.data.weather

                withContext(Dispatchers.Main){
                   // Glide.with(applicationContext).load(degerUrl).into(binding.imageView)
                    val formattedDate = dateFormat.format(currentDate)
                    val formattedTime = "$currentHour:$minute"
                    binding.localDate.text = formattedDate
                    binding.localTime.text = formattedTime
                    binding.temperatureTextView.text = "${currentConditionUrl.tempC}°"
                    binding.locationTextView.text = locationUrl
                    val weatherText = currentConditionUrl.weatherDesc[0].value
                    binding.weatherTextView.text = weatherText
                    binding.maxTempTextView.text = "H:${weatherUrl.maxtempC}°"
                    binding.minTempTextView.text = "L:${weatherUrl.mintempC}°"
                    binding.uvIndex.text = weatherUrl.uvIndex.toInt().toString()
                    binding.sunrise.text = "Sunrise: ${weatherUrl.astronomy[0].sunrise}"
                    binding.sunset.text = weatherUrl.astronomy[0].sunset
                    binding.wind.text = "${currentConditionUrl.windspeedKmph}km/h"
                    binding.feeling.text = "${currentConditionUrl.feelsLikeC}°"
                    binding.humidity.text = "%${currentConditionUrl.humidity}"
                    binding.visibility.text = "${currentConditionUrl.visibility} km"
                    binding.pressure.text = "${currentConditionUrl.pressure}\n hPa"

                    when(weatherUrl.uvIndex.toInt()){
                        // uv index
                        in 1..2 -> binding.uvIndexState.text = "Low"
                        in 3..5 -> binding.uvIndexState.text = "Medium"
                        in 6..7 -> binding.uvIndexState.text = "High"
                        in 8..10 -> binding.uvIndexState.text = "Very high"
                        in 11..12 -> binding.uvIndexState.text = "Extreme"
                    }
                    // background
                    updateUIWeatherBg(weatherText)

                    val progressBarWidth = binding.horizontalProgressBar.width
                    val progressBarMax = binding.horizontalProgressBar.max
                    val progressBarSegmentWidth = progressBarWidth / progressBarMax
                    val progressBarPosition = progressBarSegmentWidth * weatherUrl.uvIndex.toInt()
                    // Beyaz dairenin koordinatları ayarlandı
                    val params = binding.uvIndexImageView.layoutParams as FrameLayout.LayoutParams
                    params.marginStart = progressBarPosition - (binding.uvIndexImageView.width / 2)
                    binding.uvIndexImageView.layoutParams = params
                    binding.uvIndexImageView.visibility = View.VISIBLE
                    hourlyForecastRecyclerViewAdapter.notifyDataSetChanged()
                    dailyWeatherForecastRecyclerViewAdapter.notifyDataSetChanged()

                    // hourly forecast
                    for (hourly in hourlyData) {
                        var time = hourly.time
                        val temperature = hourly.tempC
                        val weatherIconUrl = hourly.weatherIconUrl[0].value

                        if (time.length == 4){
                            val hour = time.substring(0,2)
                            val minute = time.substring(4)
                            var timeHourly = " $hour $minute"
                            time = timeHourly
                        }else if (time.length == 3){
                            val hour = time.substring(0,1)
                            val minute = time.substring(3)
                            var timeHourly = "  $hour $minute"
                            time = timeHourly
                        }else{
                            println("invalid")
                        }

                        if (time.trim().toInt() < currentHour){
                            hourlyForecastFirstList.add(HourlyForecast(time, temperature, weatherIconUrl))
                        }else{
                            hourlyForecastFirstList.addAll(hourlyForecastList)
                        }
                        hourlyForecastList.add(HourlyForecast(time, temperature, weatherIconUrl))
                    }


                    // daily forecast
                    for (daily in dailyData){
                        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        var date = daily.date
                        val minTempC = daily.mintempC
                        val maxTempC = daily.maxtempC
                        val weatherIconUrl = daily.hourly[12].weatherIconUrl[0].value

                        try {
                         val inputDate = inputDateFormat.parse(date)
                            val calendar = Calendar.getInstance()
                            calendar.time = inputDate
                            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                            val dayName = when (dayOfWeek) {
                                Calendar.SUNDAY -> "Sunday"
                                Calendar.MONDAY -> "Monday"
                                Calendar.TUESDAY -> "Tuesday"
                                Calendar.WEDNESDAY -> "Wednesday"
                                Calendar.THURSDAY -> "Thursday"
                                Calendar.FRIDAY -> "Friday"
                                Calendar.SATURDAY -> "Saturday"
                                else -> "unknown day"
                            }
                            date = dayName
                        }catch (e: Exception){
                            println("invalid date")
                        }
                       dailyForecastList.add(DailyForecast(date, minTempC,maxTempC,weatherIconUrl))
                    }

                    binding.group.visibility =View.VISIBLE
                    binding.progressBar.visibility = View.GONE

               }

                saveDataToSharedPreferences(applicationContext, data, "weather_data")
                // Veriyi kaydettikten sonra SharedPreferences'ten okuma
                //getDataFromSharedPreferences<WeatherData>(applicationContext, "weather_data")
                val savedData = getDataFromSharedPreferences(applicationContext, data,"weather_data")
                if (savedData != null){
                    // Veriyi kullanabilirsiniz
                    Log.d("WEATHER","$savedData")
                    println("data received")
                }else{
                    println("data not received")
                }
            }
        }
        return true
    }
    fun cities(){
        ioScope.launch {
            val data = CitiesHelper.getCities(citiesUrl)
            if (data != null){
                Log.d("CITIES","$data")
                val cityName = data.name
                withContext(Dispatchers.Main){
                    binding.editTextText.setText(cityName)
                    enteredCity = cityName
                }
                saveDataToSharedPreferences(applicationContext, data,"cities_data")
                // getDataFromSharedPreferences<CitiesData>(applicationContext,"cities_data")
                val savedData = getDataFromSharedPreferences(applicationContext, data,"cities_data")
                if (savedData != null){
                    // Veriyi kullanabilirsiniz
                    Log.d("WEATHER","$savedData")
                    println("data received")
                }else{
                    println("data not received")
                }
            }
        }
    }
    fun requestFromApiReverseGeocoding(url: String){
        // https://geocodeapi.p.rapidapi.com/GetTimezone?latitude=40.63380384389354&longitude=-74.4075357036940
        ioScope.launch {
            val data = ReverseGeocodingHelper.getReverseGeocoding(url)
            if (data != null){
                Log.d("REVERSE GEOCODİNG","$data")
                val locationUrl = data.timeZoneId
                val localTime = data.localTimeNow
                withContext(Dispatchers.Main){
                    binding.locationTextView.text = locationUrl
                    binding.localTime.text = localTime
                    println("time $localTime")
                    println("url $locationUrl")

                }
                saveDataToSharedPreferences(applicationContext,data,"reverse_geocoding_data")
                //getDataFromSharedPreferences<ReverseGeocodingData>(applicationContext,"reverse_geocoding_data")
                val savedData = getDataFromSharedPreferences(applicationContext, data,"reverse_geocoding_data")
                if (savedData != null){
                    // Veriyi kullanabilirsiniz
                    Log.d("WEATHER","$savedData")
                    println("data received")
                }else{
                    println("data not received")
                }
            }
        }
    }
    private fun updateUIWeatherBg(weatherText : String){
        when(weatherText) {
            "Partly cloudy" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.partly_cloudy)
            }

            "Sunny" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.sunny_photo)
            }

            "Cloudy" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.cloudy)
            }

            "Overcast" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.overcast)
            }

            "Mist" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.mist)
            }

            "Patchy rain possible" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.rainn)
            }

            "Patchy snow possible" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.snow)
            }

            "Patchy sleet possible" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.sleet)
            }

            "Patchy freezing drizzle possible" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.freezing_drizzle)
            }

            "Thundery outbreaks possible" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.thundary_rain)
            }

            "Blowing snow" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.snow)
            }

            "Blizzard" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.blizzard)
            }

            "Fog" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.foggy)
            }

            "Freezing fog" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.freezing_fog)
            }

            "Patchy light drizzle" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.patchy_light_drizzle)
            }

            "Light drizzle" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.patchy_light_drizzle)
            }

            "Freezing drizzle" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.heavy_rain)
            }

            "Heavy freezing drizzle" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.heavy_rain)
            }

            "Patchy light rain" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.patchy_light_drizzle)
            }

            "Light rain" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.rainn)
            }

            "Moderate rain at times" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.rain_at_times)
            }

            "Moderate rain" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.rain_at_times)
            }

            "Heavy rain at times" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.heavy_rain)
            }

            "Heavy rain" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.heavy_rain)
            }

            "Light freezing rain" -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.light_freezing_rain)
            }
            else -> {
                binding.swiperefreshlayout.setBackgroundResource(R.drawable.sky)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun defineRecyclerView(){
        // Hourly Forecast RecyclerView tanımlama
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewHourlyForecast.layoutManager = layoutManager
        hourlyForecastRecyclerViewAdapter = HourlyForecastRecyclerViewAdapter(hourlyForecastList, applicationContext)
        binding.recyclerViewHourlyForecast.adapter = hourlyForecastRecyclerViewAdapter
        binding.recyclerViewHourlyForecast.adapter?.notifyDataSetChanged()

        // Daily Forecast RecyclerView tanımlama
        val layoutManagerDaily = LinearLayoutManager(this)
        binding.recyclerViewDailyWeatherForecast.layoutManager = layoutManagerDaily
        dailyWeatherForecastRecyclerViewAdapter = DailyWeatherForecastRecyclerViewAdapter(dailyForecastList, applicationContext)
        dailyWeatherForecastRecyclerViewAdapter.notifyDataSetChanged()
        binding.recyclerViewDailyWeatherForecast.adapter = dailyWeatherForecastRecyclerViewAdapter
    }
}