package com.batchtrace.app.ui.warehouse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.batchtrace.app.data.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarehouseDashboardScreen(
    user: User,
    onLogout: () -> Unit,
    onWarehouseBatches: () -> Unit = {},
    onScanBatch: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("BatchTrace")
                        Text(
                            "Warehouse",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Welcome, ${user.name}",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "${user.employeeId} • ${user.department}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Warehouse Operations",
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                onClick = onWarehouseBatches,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.LocalShipping, null)
                Text(
                    "Warehouse & Dispatch",
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            Button(
                onClick = onScanBatch,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.QrCodeScanner, null)
                Text(
                    "Scan Batch QR",
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }
}