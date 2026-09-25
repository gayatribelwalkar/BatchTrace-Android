package com.batchtrace.app.ui.batch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.batchtrace.app.data.model.Batch
import com.batchtrace.app.data.model.User
import com.batchtrace.app.data.model.UserRole
import com.batchtrace.app.ui.components.BatchStatusChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchDetailScreen(
    batch: Batch,
    user: User,
    isLoading: Boolean,
    onBack: () -> Unit,
    onUpdateStatus: (
        newStatus: String,
        qualityRemarks: String,
        warehouseLocation: String,
        dispatchReference: String
    ) -> Unit
) {
    var qualityRemarks by remember(batch.id) {
        mutableStateOf(batch.qualityRemarks)
    }

    var warehouseLocation by remember(batch.id) {
        mutableStateOf(batch.warehouseLocation)
    }

    var dispatchReference by remember(batch.id) {
        mutableStateOf(batch.dispatchReference)
    }

    val role = user.getUserRole()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Batch Details")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary,
                    titleContentColor =
                        MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor =
                        MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = batch.batchNumber,
                style =
                    MaterialTheme.typography.headlineSmall
            )

            BatchStatusChip(
                status = batch.status
            )

            Text(
                text = batch.productName,
                style =
                    MaterialTheme.typography.titleLarge
            )

            DetailLine(
                "Product Code",
                batch.productCode
            )

            DetailLine(
                "Quantity",
                "${batch.quantity} ${batch.unit}"
            )

            DetailLine(
                "Manufacturing Date",
                batch.manufacturingDate
            )

            DetailLine(
                "Created By",
                batch.createdByName
            )

            if (batch.lastUpdatedByName.isNotBlank()) {
                DetailLine(
                    "Last Updated By",
                    batch.lastUpdatedByName
                )
            }

            if (batch.qualityRemarks.isNotBlank()) {
                DetailLine(
                    "Quality Remarks",
                    batch.qualityRemarks
                )
            }

            if (batch.warehouseLocation.isNotBlank()) {
                DetailLine(
                    "Warehouse Location",
                    batch.warehouseLocation
                )
            }

            if (batch.dispatchReference.isNotBlank()) {
                DetailLine(
                    "Dispatch Reference",
                    batch.dispatchReference
                )
            }

            when (role) {

                UserRole.PRODUCTION -> {

                    if (batch.status == "CREATED") {
                        Button(
                            onClick = {
                                onUpdateStatus(
                                    "IN PRODUCTION",
                                    "",
                                    "",
                                    ""
                                )
                            },
                            enabled = !isLoading,
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text("Start Production")
                        }
                    }

                    if (batch.status == "IN PRODUCTION") {
                        Button(
                            onClick = {
                                onUpdateStatus(
                                    "PRODUCTION COMPLETED",
                                    "",
                                    "",
                                    ""
                                )
                            },
                            enabled = !isLoading,
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text("Complete Production")
                        }
                    }
                }

                UserRole.QUALITY -> {

                    if (
                        batch.status ==
                        "PRODUCTION COMPLETED" ||
                        batch.status == "ON HOLD"
                    ) {

                        Text(
                            text = "Quality Inspection",
                            style =
                                MaterialTheme.typography.titleMedium
                        )

                        OutlinedTextField(
                            value = qualityRemarks,
                            onValueChange = {
                                qualityRemarks = it
                            },
                            label = {
                                Text("Inspection Remarks")
                            },
                            modifier =
                                Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Button(
                            onClick = {
                                onUpdateStatus(
                                    "PASSED",
                                    qualityRemarks,
                                    "",
                                    ""
                                )
                            },
                            enabled =
                                !isLoading &&
                                        qualityRemarks.isNotBlank(),
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text("Pass Batch")
                        }

                        OutlinedButton(
                            onClick = {
                                onUpdateStatus(
                                    "ON HOLD",
                                    qualityRemarks,
                                    "",
                                    ""
                                )
                            },
                            enabled =
                                !isLoading &&
                                        qualityRemarks.isNotBlank(),
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text("Place On Hold")
                        }

                        OutlinedButton(
                            onClick = {
                                onUpdateStatus(
                                    "REJECTED",
                                    qualityRemarks,
                                    "",
                                    ""
                                )
                            },
                            enabled =
                                !isLoading &&
                                        qualityRemarks.isNotBlank(),
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text("Reject Batch")
                        }
                    }
                }

                UserRole.WAREHOUSE -> {

                    if (batch.status == "PASSED") {

                        OutlinedTextField(
                            value = warehouseLocation,
                            onValueChange = {
                                warehouseLocation = it
                            },
                            label = {
                                Text("Warehouse Location")
                            },
                            placeholder = {
                                Text("e.g. Rack A-12")
                            },
                            modifier =
                                Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                onUpdateStatus(
                                    "IN WAREHOUSE",
                                    "",
                                    warehouseLocation,
                                    ""
                                )
                            },
                            enabled =
                                !isLoading &&
                                        warehouseLocation.isNotBlank(),
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text("Receive Into Warehouse")
                        }
                    }

                    if (batch.status == "IN WAREHOUSE") {

                        OutlinedTextField(
                            value = dispatchReference,
                            onValueChange = {
                                dispatchReference = it
                            },
                            label = {
                                Text("Dispatch Reference")
                            },
                            placeholder = {
                                Text("e.g. DSP-2026-001")
                            },
                            modifier =
                                Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                onUpdateStatus(
                                    "DISPATCHED",
                                    "",
                                    warehouseLocation,
                                    dispatchReference
                                )
                            },
                            enabled =
                                !isLoading &&
                                        dispatchReference.isNotBlank(),
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text("Dispatch Batch")
                        }
                    }
                }

                UserRole.ADMIN,
                null -> Unit
            }

            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun DetailLine(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style =
                MaterialTheme.typography.labelMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style =
                MaterialTheme.typography.bodyLarge
        )
    }
}