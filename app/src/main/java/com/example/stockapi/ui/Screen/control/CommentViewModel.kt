package com.example.stockapi.ui.Screen.control

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stockapi.data.model.Comment
import com.example.stockapi.data.remote.CommentRepository
import kotlinx.coroutines.launch

class CommentViewModel(
    private val repository: CommentRepository
): ViewModel() {
    private val _comment = mutableStateListOf<Comment>()
    val comment: List<Comment> = _comment

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun getCommentsByStockid(stockId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _comment.clear()
                _comment.addAll(repository.getCommentByStockId(stockId))
            } catch (e: Exception) {
                _error.value = "Gagal memuat data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class CommentViewModelFactory(
    private val repository: CommentRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            return CommentViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel tidak valid")
    }
}