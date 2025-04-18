package com.example.stockapi.ui.Screen

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockapi.data.model.Comment
import com.example.stockapi.data.model.Stock
import com.example.stockapi.data.remote.StockRepository
import com.example.stockapi.ui.Screen.control.StockViewModel
import com.example.stockapi.ui.Screen.control.StockViewModelFactory

@Composable
fun HomeScreen(
    onCommentsClick: (Int) -> Unit,
    onAddCommentClick: (Int) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
//    val repository = remember { StockRepository() }
    val viewModel: StockViewModel = viewModel(
        factory = StockViewModelFactory(StockRepository(context))
    )
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getStock()
    }

    if (viewModel.isLoading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (viewModel.error.value != null) {
        Text(
            text = viewModel.error.value!!,
            color = MaterialTheme.colorScheme.error,
        )
    } else {
        val filterStock = viewModel.stocks.filter {
            it.companyName.contains(query, ignoreCase = true) || it.symbol.contains(query, ignoreCase = true)
        }
        Scaffold(
            topBar = { TopBar(title = "Home Screen", navController) }
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search: ") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(WindowInsets.systemBars.asPaddingValues())
            ) {
                items(filterStock) {stock ->
                    CardStock(
                        stock,
                        onCommentsClick = { onCommentsClick(stock.id)},
                        onAddCommentClick = { onAddCommentClick(stock.id)}
                    )
                }
            }
        }
    }
}

@Composable
fun CardStock(
    stock: Stock,
    onCommentsClick: (Int) -> Unit,
    onAddCommentClick: (Int) -> Unit
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
                text = stock.symbol,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stock.companyName,
                fontSize = 24.sp,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stock.industry,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { onCommentsClick(stock.id) }) {
                Text("View Comments")
            }

            Button(onClick = { onAddCommentClick(stock.id)}) {
                Text("Add Comment")
            }
        }
    }
}