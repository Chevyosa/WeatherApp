package com.chev.weatherapp.api

data class SearchModelItem(
    val country: String,
    val id: String,
    val lat: String,
    val lon: String,
    val name: String,
    val region: String,
    val url: String
)