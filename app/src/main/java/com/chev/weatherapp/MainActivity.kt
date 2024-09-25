package com.chev.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.chev.weatherapp.components.BottomNavigationBar
import com.chev.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        val searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        setContent {
            WeatherAppTheme {
                var selectedIndex by remember { mutableIntStateOf(0) }
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                selectedIndex = selectedIndex,
                                onItemSelected = { index ->
                                    selectedIndex = index
                                }
                            )
                        },
                        modifier = Modifier
                            .systemBarsPadding()
                    ){ innerPadding ->
                        when (selectedIndex) {
                            0 -> {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    HomePage()
                                }
                            }
                            1 -> {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    WeatherPage(weatherViewModel, searchViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}