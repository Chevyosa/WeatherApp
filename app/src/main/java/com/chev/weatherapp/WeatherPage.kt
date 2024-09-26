package com.chev.weatherapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.toSize
import com.chev.weatherapp.api.NetworkResponse
import com.chev.weatherapp.api.SearchModelItem
import com.chev.weatherapp.api.SearchNetworkResponse
import com.chev.weatherapp.api.WeatherModel
import com.chev.weatherapp.ui.theme.Orange
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun WeatherPage(viewModel: WeatherViewModel, searchModel: SearchViewModel){

    var city by remember {
        mutableStateOf("")
    }

    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val weatherResult = viewModel.weatherResult.observeAsState()
    val cityList = searchModel.cityList.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val (gradientBrush, textColor, timePeriod) = remember(weatherResult.value) {
        when (weatherResult.value) {
            is NetworkResponse.Success -> {
                val localTimeString = (weatherResult.value as NetworkResponse.Success).data.location.localtime
                val hour = getHourFromLocalTime(localTimeString)
                when (hour) {
                    in 5..11 -> {
                        Triple(
                            Brush.linearGradient(colors = listOf(Color.Cyan, Color.Blue)),
                            Color.Black,
                            "AM"
                        )
                    }
                    in 12..18 -> {
                        Triple(
                            Brush.linearGradient(colors = listOf(Orange, Color.Red)),
                            Color.White,
                            "PM"
                        )
                    }
                    else -> {
                        Triple(
                            Brush.linearGradient(colors = listOf(Color.DarkGray, Color.Black)),
                            Color.White,
                            "PM"
                        )
                    }
                }
            }
            else -> Triple(
                Brush.linearGradient(colors = listOf(Color.White, Color.LightGray)),
                Color.Black,
                "PM"
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
            .padding(16.dp)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 8.dp, top = 8.dp,  bottom = 64.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Box(
                modifier = Modifier
                    .weight(1f)
            ){
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(10.dp, RoundedCornerShape(20.dp))
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    value = city,
                    onValueChange = {
                        city = it
                        expanded = true
                        searchModel.searchCities(it)},
                    label = {
                        Text(text = "Search for Any Location", color = Color.Black)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                    shape = RoundedCornerShape(20.dp),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.getData(city)
                            keyboardController?.hide()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search for Any Locations",
                            )
                        }
                    }
                )
            }
        }
        AnimatedVisibility(visible = expanded && city.isNotBlank()) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(textFieldSize.width.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ){
                when (val result = cityList.value) {
                    is SearchNetworkResponse.Loading -> {
                        CircularProgressIndicator(modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally))
                    }
                    is SearchNetworkResponse.Success -> {
                        val cityData: List<SearchModelItem> = if (city.isBlank()) {
                            result.data
                        } else {
                            result.data.filter { it.name.contains(city, ignoreCase = true) }
                        }
                        LazyColumn(modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 60.dp)
                        ){
                            items(cityData) { cityItem ->
                                CityItems(city = cityItem.name, country = cityItem.country, searchModel) { selectedCity ->
                                    city = selectedCity
                                    expanded = false
                                    viewModel.getData(selectedCity)
                                    keyboardController?.hide()
                                }
                            }
                        }
                    }
                    is SearchNetworkResponse.Error -> {
                        Text(
                            text = result.message,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    else -> {
                        Text(text = "Error Wak")
                    }
                }
            }
        }
        when(val result = weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message, color = textColor)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data, textColor, timePeriod)
            }
            null -> {}
        }
    }
}

@Composable
fun CityItems(
    city: String,
    country: String,
    searchModel: SearchViewModel,
    onSelect: (String) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(city) }
            .padding(20.dp)
    ){
        Text(text = "$city, $country", fontSize = 16.sp)
    }
}

@Composable
fun WeatherDetails(data: WeatherModel, textColor: Color, timePeriod: String){
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
                .padding(horizontal = 8.dp)
                .border(width = 1.dp, color = textColor, shape = RoundedCornerShape(16.dp)),
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
                    tint = textColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = data.location.name, fontSize = 30.sp, color = textColor)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.padding(4.dp)){
            Row{
                Text(text = "${data.location.region}, ${data.location.country}", color = textColor, fontSize = 20.sp, textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            Text(text = "Local Time: ${data.location.localtime.split(" ")[1]} $timePeriod", color = textColor)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${data.current.temp_c}°c",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = textColor
        )
        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Condition Icon"
        )
        Text(
            text = data.current.condition.text,
            color = textColor,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
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
                    WeatherKeyValue(key = "Humidity", value = data.current.humidity)
                    WeatherKeyValue(key = "Wind Speed", value = "${data.current.wind_kph} Km/h")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    WeatherKeyValue(key = "UV", value = data.current.uv)
                    WeatherKeyValue(key = "Feels Like", value = "${data.current.feelslike_c}°c")
                }
            }
        }
    }
}


@Composable
fun WeatherKeyValue(key: String, value: String){
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

fun getHourFromLocalTime(localTimeString: String): Int {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val date = dateFormat.parse(localTimeString)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.HOUR_OF_DAY)
}