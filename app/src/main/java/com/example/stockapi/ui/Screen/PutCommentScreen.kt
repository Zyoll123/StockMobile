package com.example.stockapi.ui.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.navigation.NavController
import com.example.stockapi.data.model.PutComment
import com.example.stockapi.data.remote.CommentRepository
import com.example.stockapi.ui.Screen.control.CommentUiState
import com.example.stockapi.ui.Screen.control.CommentViewModel
import com.example.stockapi.ui.Screen.control.CommentViewModelFactory

@Composable
fun PutCommentScreen(
    id: Int,
    title: String,
    content: String,
    stockId: Int,
    navController: NavController
) {
    var title by remember { mutableStateOf(title) }
    var content by remember { mutableStateOf(content) }
    val context = LocalContext.current

    val viewModel: CommentViewModel = viewModel(
        factory = CommentViewModelFactory(CommentRepository(context))
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is CommentUiState.Success) {
            Toast.makeText(context, "Comment put successfully!", Toast.LENGTH_SHORT).show()
            navController.navigate("comment/$stockId") {
                navController.popBackStack()
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
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

        when(uiState) {
            is CommentUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is CommentUiState.Error -> {
                Text(
                    text = viewModel.error.value ?: "Put Error",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        if (title.isBlank() || content.isBlank()) {
                            Toast.makeText(context, "Please enter title and content!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.putComment(title, content, id)
                    }
                ) {
                    Text("Try Again")
                }
            }
            else -> {
                Button(
                    onClick = {
                        if (title.isBlank() || content.isBlank()) {
                            Toast.makeText(context, "Please enter title and content!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.putComment(title, content, id)
                    }
                ) {
                    Text("Edit")
                }
            }
        }
    }
}