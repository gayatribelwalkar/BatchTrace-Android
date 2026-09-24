package com.batchtrace.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.batchtrace.app.ui.auth.LoginScreen
import com.batchtrace.app.ui.theme.BatchTraceTheme
import com.batchtrace.app.viewmodel.AuthUiState
import com.batchtrace.app.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            BatchTraceTheme {

                val authViewModel: AuthViewModel = viewModel()

                val authState by authViewModel.authState
                    .collectAsStateWithLifecycle()

                LaunchedEffect(authState) {
                    when (val state = authState) {

                        is AuthUiState.Success -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Welcome ${state.user.name} (${state.user.role})",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is AuthUiState.Error -> {
                            Toast.makeText(
                                this@MainActivity,
                                state.message,
                                Toast.LENGTH_LONG
                            ).show()

                            authViewModel.clearError()
                        }

                        else -> Unit
                    }
                }

                LoginScreen(
                    isLoading = authState is AuthUiState.Loading,
                    onLoginClick = { email, password ->
                        authViewModel.login(
                            email = email,
                            password = password
                        )
                    }
                )
            }
        }
    }
}