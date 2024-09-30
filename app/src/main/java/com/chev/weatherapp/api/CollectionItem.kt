package com.chev.weatherapp.api

data class CollectionItem(
    val locationName: String,
    val region: String,
    val country: String,
    val localTime: String,
    val temperature: String,
    val iconUrl: String
)
