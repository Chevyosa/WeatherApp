package com.chev.weatherapp.components

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chev.weatherapp.CollectionPage
import com.chev.weatherapp.CollectionViewModel
import com.chev.weatherapp.HomePage
import com.chev.weatherapp.SearchViewModel
import com.chev.weatherapp.WeatherPage
import com.chev.weatherapp.WeatherViewModel

@Composable
fun BottomNavigationGraph(
    navController: NavHostController,
    collectionViewModel: CollectionViewModel
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavigationScreen.Home.route
    ) {
        composable(route = BottomNavigationScreen.Home.route){
            val weatherViewModel: WeatherViewModel = viewModel()
            HomePage(viewModel = weatherViewModel)
        }
        composable(route = BottomNavigationScreen.Search.route){
            val weatherViewModel: WeatherViewModel = viewModel()
            val searchViewModel: SearchViewModel = viewModel()
            WeatherPage(viewModel = weatherViewModel, searchModel = searchViewModel, collectionViewModel = collectionViewModel)
        }
        composable(route = BottomNavigationScreen.Collection.route){
            val weatherViewModel: WeatherViewModel = viewModel()
            CollectionPage(collectionViewModel = collectionViewModel, viewModel = weatherViewModel)
        }
    }
}