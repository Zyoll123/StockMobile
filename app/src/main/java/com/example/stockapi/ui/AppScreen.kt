package com.example.stockapi.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stockapi.ui.Screen.AddCommentScreen
import com.example.stockapi.ui.Screen.CommentScreen
import com.example.stockapi.ui.Screen.control.AccountUiState
import com.example.stockapi.ui.Screen.control.AccountViewModel
import com.example.stockapi.ui.Screen.HomeScreen
import com.example.stockapi.ui.Screen.LoginScreen
import com.example.stockapi.ui.Screen.PutCommentScreen

@Composable
fun AppScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val accountViewModel: AccountViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (accountViewModel.isLoggedIn(context)) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen()
        }
        composable("home") {
            HomeScreen(
                onCommentsClick = { stockId ->
                    navController.navigate("comment/$stockId")
                },
                onAddCommentClick = { stockId ->
                    navController.navigate("addComment/$stockId")
                },
                navController = navController
            )
        }
        composable("comment/{stockId}") { backStackEntry ->
            val stockId = backStackEntry.arguments?.getString("stockId")?.toIntOrNull()
            if (stockId != null) {
                CommentScreen(stockId = stockId, navController)
            }
        }
        composable("addComment/{stockId}") { value ->
            val stockId = value.arguments?.getString("stockId")?.toIntOrNull()
            if (stockId != null) {
                AddCommentScreen(stockId, navController)
            }
        }
        composable(
            route = "putComment/{id}/{title}/{content}/{stockId}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType },
                navArgument("stockId") { type = NavType.IntType }
            )
        ) {backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")?: 0
            val title = backStackEntry.arguments?.getString("title")?: ""
            val content =backStackEntry.arguments?.getString("content")?: ""
            val stockId = backStackEntry.arguments?.getInt("stockId")?: 0

            if (id != null) {
                PutCommentScreen(id, title, content, stockId, navController)
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