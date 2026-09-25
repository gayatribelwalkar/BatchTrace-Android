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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBatchScreen(
    isLoading: Boolean,
    onBack: () -> Unit,
    onCreateBatch: (
        batchNumber: String,
        productName: String,
        productCode: String,
        quantity: Int,
        unit: String,
        manufacturingDate: String
    ) -> Unit
) {

    var batchNumber by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var productCode by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var manufacturingDate by remember { mutableStateOf("") }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Create Batch")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
                Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Batch Information",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Enter the manufacturing batch details.",
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = batchNumber,
                onValueChange = { batchNumber = it },
                label = { Text("Batch Number") },
                placeholder = { Text("e.g. BT-2026-001") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = productCode,
                onValueChange = { productCode = it },
                label = { Text("Product Code") },
                placeholder = { Text("e.g. PRD-001") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = {
                    quantity = it.filter(Char::isDigit)
                },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = unit,
                onValueChange = { unit = it },
                label = { Text("Unit") },
                placeholder = {
                    Text("e.g. Pieces, Kg, Litres")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = manufacturingDate,
                onValueChange = { manufacturingDate = it },
                label = {
                    Text("Manufacturing Date")
                },
                placeholder = {
                    Text("DD-MM-YYYY")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,

                onClick = {

                    val quantityValue =
                        quantity.toIntOrNull()

                    errorMessage = when {

                        batchNumber.isBlank() ->
                            "Batch number is required."

                        productName.isBlank() ->
                            "Product name is required."

                        productCode.isBlank() ->
                            "Product code is required."

                        quantityValue == null ||
                                quantityValue <= 0 ->
                            "Enter a valid quantity."

                        unit.isBlank() ->
                            "Unit is required."

                        manufacturingDate.isBlank() ->
                            "Manufacturing date is required."

                        else -> null
                    }

                    if (errorMessage == null) {
                        onCreateBatch(
                            batchNumber.trim(),
                            productName.trim(),
                            productCode.trim(),
                            quantityValue!!,
                            unit.trim(),
                            manufacturingDate.trim()
                        )
                    }
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color =
                            MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Create Batch")
                }
            }
        }
    }
}