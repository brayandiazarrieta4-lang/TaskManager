package com.example.taskmanager.ui.navhost

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.ui.screen.AuthScreen
import com.example.taskmanager.ui.screen.TaskScreen
import com.example.taskmanager.ui.viewmodel.AuthViewModel
import com.example.taskmanager.ui.screen.DraftScreen

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()

    var isCheckingSession by remember {
        mutableStateOf(true)
    }

    var isLoggedIn by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        val currentUser =
            authViewModel.getCurrentUser()

        isLoggedIn =
            currentUser != null

        isCheckingSession = false
    }

    if (isCheckingSession) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    } else {

        NavHost(
            navController = navController,

            startDestination =
                if (isLoggedIn) {
                    "tasks"
                } else {
                    "auth"
                }
        ) {

            composable("auth") {

                AuthScreen(
                    viewModel = authViewModel,

                    onLoginSuccess = {

                        navController.navigate("tasks") {

                            popUpTo("auth") {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable("tasks") {

                TaskScreen(
                    viewModel = hiltViewModel(),

                    onLogout = {

                        authViewModel.logout()

                        navController.navigate("auth") {
                            popUpTo("tasks") {
                                inclusive = true
                            }
                        }
                    },

                    onOpenDrafts = {
                        navController.navigate("drafts")
                    }
                )
            }
            composable("drafts") {

                DraftScreen(
                    viewModel = hiltViewModel(),
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}