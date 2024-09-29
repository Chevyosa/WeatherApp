package com.chev.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import java.util.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.chev.weatherapp.api.NetworkResponse
import com.chev.weatherapp.api.WeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@Composable
fun HomePage(viewModel: WeatherViewModel) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var location by remember { mutableStateOf<Location?>(null) }
    var city by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val weatherResult by viewModel.weatherResult.observeAsState()
    val timePeriod = getTimePeriod(weatherResult)

    LaunchedEffect(Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                location = loc
                loc?.let {
                    city = getCityNameFromLocation(context, it.latitude, it.longitude)
                    if (city.isNotEmpty()) {
                        viewModel.getData(city)
                    }
                } ?: run {
                    errorMessage = "Location Not Found"
                    Toast.makeText(context, "Location Not Found", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(context, "Location Not Granted", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (val result = weatherResult) {
            is NetworkResponse.Error -> {
                Text(text = result.message, color = Color.Red)
            }
            NetworkResponse.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Loading Data..")
                }
            }
            is NetworkResponse.Success -> {
                HomeWeatherDetails(data = result.data, timePeriod = timePeriod)
            }
            null -> {
                Text(text = "No Data Fetched")
            }
        }
    }
}

@Composable
fun HomeWeatherDetails(data: WeatherModel, timePeriod: String){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                modifier = Modifier
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.LocationOn,
                    contentDescription ="Location Icon" ,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = data.location.name, fontSize = 30.sp, color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.padding(4.dp)){
            Row{
                Text(text = "${data.location.region}, ${data.location.country}", color = Color.Black, fontSize = 20.sp, textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            Text(text = "Local Time: ${data.location.localtime.split(" ")[1]} $timePeriod", color = Color.Black)
        }

        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Condition Icon"
        )
        Text(
            text = data.current.condition.text,
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${data.current.temp_c}°c",
            fontSize = 48.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "___")
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardColors(
                contentColor = Color.Black,
                disabledContentColor = Color.Transparent,
                containerColor = Color.White,
                disabledContainerColor = Color.Transparent
            )
        ){
            Column(modifier = Modifier.fillMaxWidth()){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    WeatherKeyValue(icon = R.drawable.ic_humidity, key = "Humidity", value = "${data.current.humidity}%")
                    WeatherKeyValue(icon = R.drawable.ic_wind_speed, key = "Wind Speed", value = "${data.current.wind_kph} Km/h")
                    WeatherKeyValue(icon = R.drawable.ic_feels_like, key = "Feels Like", value = "${data.current.feelslike_c}°c")
                }
            }
        }
    }
}


fun getCityNameFromLocation(context: Context, latitude: Double, longitude: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses?.isNotEmpty() == true) {
            addresses[0]?.locality ?: "Tidak Diketahui"
        } else {
            "Tidak Diketahui"
        }
    } catch (e: Exception) {
        "Tidak Diketahui"
    }
}

fun getTimePeriod(weatherResult: NetworkResponse<WeatherModel>?): String {
    return when (weatherResult) {
        is NetworkResponse.Success -> {
            val localTimeString = weatherResult.data.location.localtime
            val hour = getHourFromLocalTime(localTimeString)
            when (hour) {
                in 5..11 -> "AM"
                in 12..18 -> "PM"
                else -> "PM"
            }
        }
        else -> "PM"
    }
}
