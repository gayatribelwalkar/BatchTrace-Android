package com.batchtrace.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.batchtrace.app.data.model.Batch
import com.batchtrace.app.data.model.UserRole
import com.batchtrace.app.ui.admin.AdminDashboardScreen
import com.batchtrace.app.ui.auth.LoginScreen
import com.batchtrace.app.ui.batch.BatchDetailScreen
import com.batchtrace.app.ui.batch.BatchListScreen
import com.batchtrace.app.ui.batch.CreateBatchScreen
import com.batchtrace.app.ui.production.ProductionDashboardScreen
import com.batchtrace.app.ui.quality.QualityDashboardScreen
import com.batchtrace.app.ui.theme.BatchTraceTheme
import com.batchtrace.app.ui.warehouse.WarehouseDashboardScreen
import com.batchtrace.app.viewmodel.AuthUiState
import com.batchtrace.app.viewmodel.AuthViewModel
import com.batchtrace.app.viewmodel.BatchViewModel

private enum class AppScreen {
    DASHBOARD,
    CREATE_BATCH,
    BATCH_LIST,
    BATCH_DETAIL
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            BatchTraceTheme {

                val authViewModel: AuthViewModel =
                    viewModel()

                val batchViewModel: BatchViewModel =
                    viewModel()

                val authState by authViewModel.authState
                    .collectAsStateWithLifecycle()

                val batchState by batchViewModel.uiState
                    .collectAsStateWithLifecycle()

                var currentScreen by remember {
                    mutableStateOf(
                        AppScreen.DASHBOARD
                    )
                }

                var selectedBatch by remember {
                    mutableStateOf<Batch?>(null)
                }

                LaunchedEffect(authState) {

                    if (authState is AuthUiState.Error) {

                        Toast.makeText(
                            this@MainActivity,
                            (authState as AuthUiState.Error)
                                .message,
                            Toast.LENGTH_LONG
                        ).show()

                        authViewModel.clearError()
                    }
                }

                LaunchedEffect(
                    batchState.errorMessage
                ) {

                    batchState.errorMessage?.let {

                        Toast.makeText(
                            this@MainActivity,
                            it,
                            Toast.LENGTH_LONG
                        ).show()

                        batchViewModel.clearError()
                    }
                }

                LaunchedEffect(
                    batchState.operationMessage
                ) {

                    batchState.operationMessage?.let {

                        Toast.makeText(
                            this@MainActivity,
                            it,
                            Toast.LENGTH_LONG
                        ).show()

                        batchViewModel
                            .consumeOperationMessage()

                        selectedBatch = null

                        currentScreen =
                            AppScreen.BATCH_LIST
                    }
                }

                LaunchedEffect(
                    batchState.batchCreated
                ) {

                    if (batchState.batchCreated) {

                        Toast.makeText(
                            this@MainActivity,
                            "Batch created successfully.",
                            Toast.LENGTH_LONG
                        ).show()

                        batchViewModel
                            .consumeBatchCreated()

                        batchViewModel.loadBatches()

                        currentScreen =
                            AppScreen.BATCH_LIST
                    }
                }

                when (val state = authState) {

                    is AuthUiState.Success -> {

                        when (currentScreen) {

                            AppScreen.CREATE_BATCH -> {

                                CreateBatchScreen(
                                    isLoading =
                                        batchState.isLoading,

                                    onBack = {
                                        currentScreen =
                                            AppScreen.DASHBOARD
                                    },

                                    onCreateBatch = {
                                            batchNumber,
                                            productName,
                                            productCode,
                                            quantity,
                                            unit,
                                            manufacturingDate ->

                                        batchViewModel
                                            .createBatch(
                                                batchNumber,
                                                productName,
                                                productCode,
                                                quantity,
                                                unit,
                                                manufacturingDate,
                                                state.user
                                            )
                                    }
                                )
                            }

                            AppScreen.BATCH_LIST -> {

                                BatchListScreen(
                                    batches =
                                        batchState.batches,

                                    isLoading =
                                        batchState.isLoading,

                                    onBack = {
                                        currentScreen =
                                            AppScreen.DASHBOARD
                                    },

                                    onBatchClick = {
                                        selectedBatch = it
                                        currentScreen =
                                            AppScreen.BATCH_DETAIL
                                    }
                                )
                            }

                            AppScreen.BATCH_DETAIL -> {

                                selectedBatch?.let { batch ->

                                    BatchDetailScreen(
                                        batch = batch,
                                        user = state.user,
                                        isLoading =
                                            batchState.isLoading,

                                        onBack = {
                                            currentScreen =
                                                AppScreen.BATCH_LIST
                                        },

                                        onUpdateStatus = {
                                                newStatus,
                                                remarks,
                                                location,
                                                dispatchReference ->

                                            batchViewModel
                                                .updateStatus(
                                                    batch =
                                                        batch,
                                                    newStatus =
                                                        newStatus,
                                                    user =
                                                        state.user,
                                                    qualityRemarks =
                                                        remarks,
                                                    warehouseLocation =
                                                        location,
                                                    dispatchReference =
                                                        dispatchReference
                                                )
                                        }
                                    )
                                }
                            }

                            AppScreen.DASHBOARD -> {

                                when (
                                    state.user.getUserRole()
                                ) {

                                    UserRole.ADMIN -> {

                                        AdminDashboardScreen(
                                            user = state.user,

                                            onLogout = {
                                                currentScreen =
                                                    AppScreen.DASHBOARD
                                                authViewModel.logout()
                                            },

                                            onCreateBatch = {
                                                currentScreen =
                                                    AppScreen.CREATE_BATCH
                                            },

                                            onViewBatches = {
                                                batchViewModel
                                                    .loadBatches()

                                                currentScreen =
                                                    AppScreen.BATCH_LIST
                                            }
                                        )
                                    }

                                    UserRole.PRODUCTION -> {

                                        ProductionDashboardScreen(
                                            user = state.user,

                                            onLogout = {
                                                currentScreen =
                                                    AppScreen.DASHBOARD
                                                authViewModel.logout()
                                            },

                                            onViewBatches = {
                                                batchViewModel
                                                    .loadBatches()

                                                currentScreen =
                                                    AppScreen.BATCH_LIST
                                            }
                                        )
                                    }

                                    UserRole.QUALITY -> {

                                        QualityDashboardScreen(
                                            user = state.user,

                                            onLogout = {
                                                currentScreen =
                                                    AppScreen.DASHBOARD
                                                authViewModel.logout()
                                            },

                                            onInspections = {
                                                batchViewModel
                                                    .loadBatches()

                                                currentScreen =
                                                    AppScreen.BATCH_LIST
                                            }
                                        )
                                    }

                                    UserRole.WAREHOUSE -> {

                                        WarehouseDashboardScreen(
                                            user = state.user,

                                            onLogout = {
                                                currentScreen =
                                                    AppScreen.DASHBOARD
                                                authViewModel.logout()
                                            },

                                            onWarehouseBatches = {
                                                batchViewModel
                                                    .loadBatches()

                                                currentScreen =
                                                    AppScreen.BATCH_LIST
                                            }
                                        )
                                    }

                                    null -> Unit
                                }
                            }
                        }
                    }

                    else -> {

                        LoginScreen(
                            isLoading =
                                state is AuthUiState.Loading,
                            onLoginClick =
                                authViewModel::login
                        )
                    }
                }
            }
        }
    }
}