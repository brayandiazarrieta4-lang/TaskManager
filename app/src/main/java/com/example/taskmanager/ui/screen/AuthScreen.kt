package com.example.taskmanager.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.taskmanager.ui.state.AuthUiState
import com.example.taskmanager.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var isRegistering by remember {
        mutableStateOf(false)
    }

    val isLoading =
        uiState is AuthUiState.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text =
                if (isRegistering) {
                    "Crear cuenta"
                } else {
                    "Iniciar sesión"
                }
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Correo")
            },
            enabled = !isLoading
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Contraseña")
            },
            visualTransformation =
                PasswordVisualTransformation(),
            enabled = !isLoading
        )

        if (isRegistering) {

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                },
                label = {
                    Text("Confirmar contraseña")
                },
                visualTransformation =
                    PasswordVisualTransformation(),
                enabled = !isLoading
            )
        }

        Button(
            onClick = {

                if (isRegistering) {

                    viewModel.register(
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword
                    )

                } else {

                    viewModel.login(
                        email = email,
                        password = password
                    )
                }
            },
            enabled = !isLoading
        ) {

            Text(
                if (isRegistering) {
                    "Registrarse"
                } else {
                    "Iniciar sesión"
                }
            )
        }

        Button(
            onClick = {

                if (!isLoading) {
                    email = ""
                    password = ""
                    confirmPassword = ""
                    isRegistering = !isRegistering
                }
            },
            enabled = !isLoading
        ) {
            Text(
                if (isRegistering) {
                    "Ya tengo una cuenta"
                } else {
                    "Crear una cuenta"
                }
            )
        }

        when (val state = uiState) {

            AuthUiState.Idle -> {
            }

            AuthUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AuthUiState.Success -> {

                LaunchedEffect(state.user.uid) {
                    onLoginSuccess()
                }
            }

            is AuthUiState.Error -> {

                Text(
                    text = state.message
                )
            }
        }
    }
}