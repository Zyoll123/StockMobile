package com.example.stockapi.ui.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockapi.data.remote.CommentRepository
import com.example.stockapi.ui.Screen.control.CommentUiState
import com.example.stockapi.ui.Screen.control.CommentViewModel
import com.example.stockapi.ui.Screen.control.CommentViewModelFactory

@Composable
fun AddCommentScreen(
    stockId: Int
) {
    val viewModel: CommentViewModel = viewModel(
        factory = CommentViewModelFactory(CommentRepository())
    )
    val uiState by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(stockId) {
        viewModel.addCommentByStockId(title, content, stockId)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title: ") }
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content: ") }
        )

        Spacer(modifier = Modifier.height(4.dp))

        when(val currentState = uiState) {
            is CommentUiState.Loading -> {
                CircularProgressIndicator()
            }
            is CommentUiState.Error -> {
                Text(
                    text = viewModel.error.value!!,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        if (title.isBlank() || content.isBlank()) {
                            Toast.makeText(context, "Please enter title and content", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.addCommentByStockId(title, content, stockId)
                    }
                ) {
                    Text("Try Again")
                }
            }
            is CommentUiState.Success -> {
                LaunchedEffect(stockId) {
                    viewModel.addCommentByStockId(title, content, stockId)
                }
            }
            else -> {
                Button(
                    onClick = {
                        if (title.isBlank() || content.isBlank()) {
                            Toast.makeText(context, "Please enter title and content", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.addCommentByStockId(title, content, stockId)
                    }
                ) {
                    Text("Add")
                }
            }
        }

//        if (viewModel.isLoading.value) {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator()
//            }
//        } else if (viewModel.error.value != null) {
//            Text(
//                text = viewModel.error.value!!,
//                color = MaterialTheme.colorScheme.error
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Button(
//                onClick = {
//                    if (title.isBlank() || content.isBlank()) {
//                        Toast.makeText(context, "Please enter title and content", Toast.LENGTH_SHORT).show()
//                        return@Button
//                    }
//                    viewModel.addCommentByStockId(title, content, stockId)
//                }
//            ) {
//                Text("Try Again")
//            }
//        } else {
//            Button(
//                onClick = {
//                    if (title.isBlank() || content.isBlank()) {
//                        Toast.makeText(context, "Please enter title and content", Toast.LENGTH_SHORT).show()
//                        return@Button
//                    }
//                    viewModel.addCommentByStockId(title, content, stockId)
//                }
//            ) {
//                Text("Login")
//            }
//        }
    }
}