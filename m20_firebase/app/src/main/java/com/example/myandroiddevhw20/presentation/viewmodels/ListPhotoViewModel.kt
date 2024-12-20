package com.example.myandroiddevhw20.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.myandroiddevhw20.data.db.Repository
import javax.inject.Inject

class ListPhotoViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val listPhotos = repository.getAllData()
}
