package com.chev.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chev.weatherapp.api.Constant
import com.chev.weatherapp.api.NetworkResponse
import com.chev.weatherapp.api.RetrofitInstance
import com.chev.weatherapp.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel(){

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city: String){
        viewModelScope.launch {
            _weatherResult.value = NetworkResponse.Loading
            try {
                val response = weatherApi.getWeather(Constant.apiKey, city)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                        Log.d("WeatherViewModel", "Weather data: $it")
                    } ?: run {
                        _weatherResult.value = NetworkResponse.Error("No Data Available!")
                        Log.e("WeatherViewModel", "Response body is null")
                    }
                }
                else{
                    _weatherResult.value = NetworkResponse.Error("Failed to Load Data!")
                    Log.e("WeatherViewModel", "Error: ${response.code()} - ${response.message()}")
                }
            }
            catch (e : Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to Load Data!")
                Log.e("WeatherViewModel", "Exception: ${e.message}", e)
            }

        }
    }
}