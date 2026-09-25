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
import com.batchtrace.app.data.model.UserRole
import com.batchtrace.app.ui.admin.AdminDashboardScreen
import com.batchtrace.app.ui.auth.LoginScreen
import com.batchtrace.app.ui.production.ProductionDashboardScreen
import com.batchtrace.app.ui.quality.QualityDashboardScreen
import com.batchtrace.app.ui.theme.BatchTraceTheme
import com.batchtrace.app.ui.warehouse.WarehouseDashboardScreen
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
                    if (authState is AuthUiState.Error) {
                        val message =
                            (authState as AuthUiState.Error).message

                        Toast.makeText(
                            this@MainActivity,
                            message,
                            Toast.LENGTH_LONG
                        ).show()

                        authViewModel.clearError()
                    }
                }

                when (val state = authState) {

                    is AuthUiState.Success -> {

                        when (state.user.getUserRole()) {

                            UserRole.ADMIN -> {
                                AdminDashboardScreen(
                                    user = state.user,
                                    onLogout = {
                                        authViewModel.logout()
                                    }
                                )
                            }

                            UserRole.PRODUCTION -> {
                                ProductionDashboardScreen(
                                    user = state.user,
                                    onLogout = {
                                        authViewModel.logout()
                                    }
                                )
                            }

                            UserRole.QUALITY -> {
                                QualityDashboardScreen(
                                    user = state.user,
                                    onLogout = {
                                        authViewModel.logout()
                                    }
                                )
                            }

                            UserRole.WAREHOUSE -> {
                                WarehouseDashboardScreen(
                                    user = state.user,
                                    onLogout = {
                                        authViewModel.logout()
                                    }
                                )
                            }

                            null -> {
                                LoginScreen(
                                    isLoading = false,
                                    onLoginClick = authViewModel::login
                                )
                            }
                        }
                    }

                    else -> {
                        LoginScreen(
                            isLoading =
                                state is AuthUiState.Loading,
                            onLoginClick = authViewModel::login
                        )
                    }
                }
            }
        }
    }
}