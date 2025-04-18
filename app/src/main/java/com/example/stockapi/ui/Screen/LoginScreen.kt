package com.example.stockapi.ui.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.stockapi.ui.Screen.control.AccountUiState
import com.example.stockapi.ui.Screen.control.AccountViewModel

@Composable
fun LoginScreen(
    viewModel: AccountViewModel = viewModel(),
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Login")

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(4.dp))

        when(val currentState = uiState) {
            is AccountUiState.Loading -> {
                CircularProgressIndicator()
            }
            is AccountUiState.Error -> {
                Text(currentState.message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        if(username.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.login(context, username, password)
                    }
                ) {
                    Text("Try Again")
                }
            }
            is AccountUiState.Success -> {
                LaunchedEffect(key1 = Unit) {
                    viewModel.login(context, username, password)
                }
            }
            else -> {
                Button(
                    onClick = {
                        if(username.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.login(context, username, password)
                    }
                ) {
                    Text("Login")
                }
            }
        }
    }
}