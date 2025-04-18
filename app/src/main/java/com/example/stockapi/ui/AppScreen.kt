package com.example.stockapi.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stockapi.ui.Screen.AddCommentScreen
import com.example.stockapi.ui.Screen.CommentScreen
import com.example.stockapi.ui.Screen.control.AccountUiState
import com.example.stockapi.ui.Screen.control.AccountViewModel
import com.example.stockapi.ui.Screen.HomeScreen
import com.example.stockapi.ui.Screen.LoginScreen

@Composable
fun AppScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val accountViewModel: AccountViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            when(val state = accountViewModel.uiState.collectAsState().value) {
                is AccountUiState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") { popUpTo("login") }
                    }
                }
                else -> {
                    LoginScreen(accountViewModel)
                }
            }
        }
        composable("home") {
            HomeScreen(
                onCommentsClick = { stockId ->
                    navController.navigate("comment/$stockId")
                },
                onAddCommentClick = { stockId ->
                    navController.navigate("addComment/$stockId")
                }
            )
        }
        composable("comment/{stockId}") { backStackEntry ->
            val stockId = backStackEntry.arguments?.getString("stockId")?.toIntOrNull()
            if (stockId != null) {
                CommentScreen(stockId = stockId)
            }
        }
        composable("addComment/{stockId}") { value ->
            val stockId = value.arguments?.getString("stockId")?.toIntOrNull()
            if (stockId != null) {
                AddCommentScreen(stockId)
            }
        }
    }

//    val accountViewModel: AccountViewModel = viewModel()
//    val accountUiState = accountViewModel.uiState.collectAsState().value
//
//    when(accountUiState) {
//        is AccountUiState.Success -> {
//            val token = accountUiState.response.token
//            HomeScreen(token = token)
//        }
//        else -> LoginScreen(accountViewModel)
//    }
}