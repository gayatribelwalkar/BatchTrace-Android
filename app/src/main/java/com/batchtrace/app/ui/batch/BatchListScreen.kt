package com.batchtrace.app.ui.batch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.batchtrace.app.data.model.Batch
import com.batchtrace.app.ui.components.BatchStatusChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchListScreen(
    batches: List<Batch>,
    isLoading: Boolean,
    onBack: () -> Unit,
    onBatchClick: (Batch) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Manufacturing Batches")
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

        if (isLoading && batches.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment =
                    Alignment.CenterHorizontally,
                verticalArrangement =
                    Arrangement.Center
            ) {
                CircularProgressIndicator()
            }

        } else if (batches.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally,
                verticalArrangement =
                    Arrangement.Center
            ) {
                Text(
                    "No batches available.",
                    style =
                        MaterialTheme.typography.titleMedium
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = batches,
                    key = { it.id }
                ) { batch ->

                    BatchCard(
                        batch = batch,
                        onClick = {
                            onBatchClick(batch)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BatchCard(
    batch: Batch,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(7.dp)
        ) {

            Text(
                batch.batchNumber,
                style =
                    MaterialTheme.typography.titleMedium
            )

            Text(
                batch.productName,
                style =
                    MaterialTheme.typography.bodyLarge
            )

            Text(
                "Product Code: ${batch.productCode}",
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                "Quantity: ${batch.quantity} ${batch.unit}",
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            BatchStatusChip(
                status = batch.status
            )

            Text(
                text = "Tap to view details",
                style =
                    MaterialTheme.typography.labelMedium,
                color =
                    MaterialTheme.colorScheme.primary
            )
        }
    }
}