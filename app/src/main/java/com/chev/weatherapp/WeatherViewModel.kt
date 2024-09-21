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
                    }
                }
                else{
                    _weatherResult.value = NetworkResponse.Error("Failed to Load Data!")
                }
            }
            catch (e : Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to Load Data!")
            }

        }
    }

    private val cityRepository = RetrofitInstance.cityRepository
    private val _cityList = MutableLiveData<NetworkResponse<WeatherModel>>()
    val cityList : LiveData<NetworkResponse<WeatherModel>> = _cityList

    fun searchCities(city: String) {
        viewModelScope.launch {
            _cityList.value = NetworkResponse.Loading
            try {
                val response = cityRepository.searchCities(Constant.apiKey, city)
                if (response.isSuccessful){
                    response.body()?.let {
                        _cityList.value = NetworkResponse.Success(it)
                    }
                }
                else{
                    _cityList.value = NetworkResponse.Error("Failed to Search Data")
                }
            }
            catch (e: Exception){
                _cityList.value = NetworkResponse.Error("Failed to Search Data")
            }
        }
    }
}