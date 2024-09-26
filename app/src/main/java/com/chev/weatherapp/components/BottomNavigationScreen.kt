package com.chev.weatherapp.components

import com.chev.weatherapp.R

sealed class BottomNavigationScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val icon_focused: Int
){
    object Home: BottomNavigationScreen(
        route = "home",
        title = "Home",
        icon = R.drawable.ic_bottom_home,
        icon_focused = R.drawable.ic_bottom_home_focused
    )
    object Search: BottomNavigationScreen(
        route = "search",
        title = "Search",
        icon = R.drawable.ic_bottom_search,
        icon_focused = R.drawable.ic_bottom_search_focused
    )
    object Collection: BottomNavigationScreen(
        route = "collection",
        title = "Collection",
        icon = R.drawable.ic_bottom_collection,
        icon_focused = R.drawable.ic_bottom_collection_focused
    )
}


