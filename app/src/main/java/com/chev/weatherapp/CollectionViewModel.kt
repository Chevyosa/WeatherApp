package com.chev.weatherapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.chev.weatherapp.api.CollectionItem
import com.chev.weatherapp.api.CollectionRepository

class CollectionViewModel: ViewModel() {
    private val repository = CollectionRepository()
    val collectionList = mutableStateListOf<CollectionItem>()
    fun addToCollection(item: CollectionItem) {
        repository.addToCollection(item)
        collectionList.clear()
        collectionList.addAll(repository.getCollectionList())
    }

}