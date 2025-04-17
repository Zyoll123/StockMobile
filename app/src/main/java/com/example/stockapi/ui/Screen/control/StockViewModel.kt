package com.example.stockapi.ui.Screen.control

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stockapi.data.model.Stock
import com.example.stockapi.data.remote.StockRepository
import kotlinx.coroutines.launch

class StockViewModel(
    private val repository: StockRepository,
): ViewModel() {
    private val _stock = mutableStateListOf<Stock>()
    val stocks: List<Stock> = _stock

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun getStock() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _stock.clear()
                _stock.addAll(repository.getAllStock())
            } catch (e: Exception) {
                _error.value = "Gagal memuat data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class StockViewModelFactory(
    private val repository: StockRepository,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            return StockViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel tidak valid")
    }
}