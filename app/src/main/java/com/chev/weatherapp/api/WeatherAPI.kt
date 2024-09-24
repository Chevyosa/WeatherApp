package com.chev.weatherapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("/v1/current.json")
    suspend fun getWeather(
        @Query("key") apikey : String,
        @Query("q") city : String
    ) : Response<WeatherModel>

    @GET("/v1/search.json")
    suspend fun searchCities(
        @Query("key") apikey : String,
        @Query("q") city : String
    ) : Response<List<SearchModelItem>>
}