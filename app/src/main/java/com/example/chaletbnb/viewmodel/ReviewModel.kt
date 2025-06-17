package com.example.chaletbnb.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chaletbnb.data.models.Review
import com.example.chaletbnb.repository.ReviewRepository
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {
    private val repository = ReviewRepository()

    private val _reviews = mutableStateOf<List<Review>>(emptyList())
    val reviews: State<List<Review>> = _reviews

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadReviewsForChalet(chaletId: String) {
        viewModelScope.launch {
            repository.getReviewsForChalet(chaletId) {
                Log.d("ReviewViewModel", "Found ${it.size} reviews for $chaletId")
                _reviews.value = it
            }
        }
    }

}