package com.example.chaletbnb.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chaletbnb.data.models.Chalet
import com.example.chaletbnb.repository.ChaletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = ChaletRepository()

    private val _chalets = mutableStateOf<List<Chalet>>(emptyList())
    val chalets: State<List<Chalet>> = _chalets

    init {
        loadChalets()
    }

    private fun loadChalets() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getChalets { result ->
                _chalets.value = result
            }
        }
    }
}
