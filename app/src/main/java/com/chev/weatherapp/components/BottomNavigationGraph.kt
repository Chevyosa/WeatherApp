package com.chev.weatherapp.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chev.weatherapp.CollectionPage
import com.chev.weatherapp.HomePage
import com.chev.weatherapp.SearchViewModel
import com.chev.weatherapp.WeatherPage
import com.chev.weatherapp.WeatherViewModel

@Composable
fun BottomNavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavigationScreen.Home.route
    ) {
        composable(route = BottomNavigationScreen.Home.route){
            HomePage(viewModel = WeatherViewModel())
        }
        composable(route = BottomNavigationScreen.Search.route){
            WeatherPage(viewModel = WeatherViewModel(), searchModel = SearchViewModel())
        }
        composable(route = BottomNavigationScreen.Collection.route){
            CollectionPage()
        }
    }
}