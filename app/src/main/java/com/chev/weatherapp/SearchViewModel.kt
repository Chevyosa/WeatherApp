package com.chev.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chev.weatherapp.api.Constant
import com.chev.weatherapp.api.RetrofitInstance
import com.chev.weatherapp.api.SearchModelItem
import com.chev.weatherapp.api.SearchNetworkResponse
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val cityRepository = RetrofitInstance.cityRepository
    private val _cityList = MutableLiveData<SearchNetworkResponse<List<SearchModelItem>>>()
    val cityList : LiveData<SearchNetworkResponse<List<SearchModelItem>>> = _cityList

    fun searchCities(city: String) {
        viewModelScope.launch {
            _cityList.value = SearchNetworkResponse.Loading
            try {
                val response = cityRepository.searchCities(Constant.apiKey, city)
                if (response.isSuccessful){
                    response.body()?.let {
                        Log.d("API Response", it.toString())
                        _cityList.value = SearchNetworkResponse.Success(it)
                    }
                }
                else{
                    _cityList.value = SearchNetworkResponse.Error("Failed to Search Data")
                }
            }
            catch (e: Exception){
                _cityList.value = SearchNetworkResponse.Error("Failed to Search Data")
            }
        }
    }
}