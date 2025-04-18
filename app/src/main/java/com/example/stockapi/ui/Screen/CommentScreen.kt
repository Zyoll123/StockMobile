package com.example.stockapi.ui.Screen

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockapi.data.model.Comment
import com.example.stockapi.data.model.PutComment
import com.example.stockapi.data.remote.CommentRepository
import com.example.stockapi.ui.Screen.control.CommentViewModel
import com.example.stockapi.ui.Screen.control.CommentViewModelFactory

@Composable
fun CommentScreen(
    stockId: Int,
    navController: NavController
) {
    val context = LocalContext.current

    val viewModel: CommentViewModel = viewModel(
        factory = CommentViewModelFactory(CommentRepository(context))
    )

    LaunchedEffect(stockId) {
        viewModel.getCommentsByStockid(stockId)
    }

    if (viewModel.isLoading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (viewModel.error.value != null) {
        Text(
            text = viewModel.error.value!!,
            color = MaterialTheme.colorScheme.error
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues())
        ) {
            items(viewModel.comment) { comment ->
                CommentCard(comment = comment, editOnClick = {
                    val encodeTitle = Uri.encode(comment.title)
                    val encodeContent = Uri.encode(comment.content)
                    navController.navigate(
                        "putComment/${comment.id}/$encodeTitle/$encodeContent/$stockId"
                    )
                })
            }
        }
    }
}

@Composable
fun CommentCard(
    comment: Comment,
    editOnClick: (Comment) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = comment.title,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = comment.stockId.toString(),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { editOnClick(comment) }
            ) {
                Text("Edit")
            }
        }
    }
}