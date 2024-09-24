package com.chev.weatherapp.api

sealed class SearchNetworkResponse<out T> {
    data class Success <out T>(val data : T) : SearchNetworkResponse<T>()
    data class Error (val message : String) : SearchNetworkResponse<Nothing>()
    object Loading : SearchNetworkResponse<Nothing>()
}