package com.chev.weatherapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chev.weatherapp.api.NetworkResponse
import com.chev.weatherapp.api.WeatherModel

@Composable
fun CollectionPage(collectionViewModel: CollectionViewModel, viewModel: WeatherViewModel) {

    val weatherResult by viewModel.weatherResult.observeAsState()
    val timePeriod = getTimePeriod(weatherResult)

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Text(
            text = "My Collection",
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        if (collectionViewModel.collectionList.isEmpty()) {
            Text(text = "No items in collection", modifier = Modifier.padding(16.dp))
        } else {
            collectionViewModel.collectionList.forEach { item ->
                Card(
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = item.locationName, fontWeight = FontWeight.SemiBold)
                            Text(text = "${item.region}, ${item.country}")
                            Text(text = "${item.temperature}°C")
                            Text(text = "Local Time: ${item.localTime.split(" ")[1]} $timePeriod", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        AsyncImage(
                            model = item.iconUrl,
                            contentDescription = "Weather Icon",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }
        }
    }
}

fun timePeriod(weatherResult: NetworkResponse<WeatherModel>?): String {
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
