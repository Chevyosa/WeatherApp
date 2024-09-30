package com.chev.weatherapp.api

import androidx.compose.runtime.mutableStateListOf

class CollectionRepository {
    private val collectionList = mutableStateListOf<CollectionItem>()

    fun addToCollection(item: CollectionItem) {
        collectionList.add(item)
    }

    fun getCollectionList(): List<CollectionItem> {
        return collectionList
    }
}