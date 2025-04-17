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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockapi.data.model.Comment
import com.example.stockapi.data.model.Stock
import com.example.stockapi.data.remote.StockRepository
import com.example.stockapi.ui.Screen.control.StockViewModel
import com.example.stockapi.ui.Screen.control.StockViewModelFactory

@Composable
fun HomeScreen(
    onCommentsClick: (Int) -> Unit
) {
//    val repository = remember { StockRepository() }
    val viewModel: StockViewModel = viewModel(
        factory = StockViewModelFactory(StockRepository())
    )

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues())
        ) {
            items(viewModel.stocks) {stock ->
                CardStock(stock, onCommentsClick = { onCommentsClick(stock.id)})
            }
        }
    }
}

@Composable
fun CardStock(
    stock: Stock,
    onCommentsClick: (Int) -> Unit
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
        }
    }
}