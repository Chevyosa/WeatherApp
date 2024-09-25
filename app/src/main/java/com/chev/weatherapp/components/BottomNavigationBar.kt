package com.chev.weatherapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

val NavItems = listOf(
    BottomNavigation(
        title = "Home",
        icon = Icons.Rounded.Home
    ),
    BottomNavigation(
        title = "Search",
        icon = Icons.Rounded.Search
    )
)

@Composable
fun BottomNavigationBar(onItemSelected: (Int) -> Unit, selectedIndex: Int){
    NavigationBar {
        Row(
            modifier = Modifier.background(Color.White)
        ){
            NavItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == selectedIndex,
                    onClick = {
                        onItemSelected(index)  // Trigger callback when a new item is selected
                    },
                    icon = {
                        Icon(imageVector = item.icon as androidx.compose.ui.graphics.vector.ImageVector, contentDescription = "${item.title} Icon")
                    },
                    label = {
                        Text(text = item.title)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewBottomNavigationBar() {
    // State to track the currently selected index
    var selectedIndex by remember { mutableStateOf(0)}

    // Function that updates the selected index based on user interaction
    BottomNavigationBar(
        selectedIndex = selectedIndex,
        onItemSelected = { index ->
            selectedIndex = index
        }
    )
}