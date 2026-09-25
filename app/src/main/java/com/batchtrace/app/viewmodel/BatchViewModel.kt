package com.batchtrace.app.viewmodel

import androidx.lifecycle.ViewModel
import com.batchtrace.app.data.model.Batch
import com.batchtrace.app.data.model.User
import com.batchtrace.app.data.repository.BatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BatchViewModel : ViewModel() {

    private val repository = BatchRepository()

    private val _uiState =
        MutableStateFlow(BatchUiState())

    val uiState: StateFlow<BatchUiState> =
        _uiState.asStateFlow()

    fun createBatch(
        batchNumber: String,
        productName: String,
        productCode: String,
        quantity: Int,
        unit: String,
        manufacturingDate: String,
        user: User
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            batchCreated = false
        )

        val batch = Batch(
            batchNumber = batchNumber.trim(),
            productName = productName.trim(),
            productCode = productCode.trim(),
            quantity = quantity,
            unit = unit.trim(),
            manufacturingDate = manufacturingDate.trim(),
            status = "CREATED",
            createdBy = user.uid,
            createdByName = user.name
        )

        repository.createBatch(
            batch = batch,

            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    batchCreated = true
                )
            },

            onError = { message ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = message
                )
            }
        )
    }

    fun loadBatches() {

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        repository.getAllBatches(

            onSuccess = { batches ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    batches = batches
                )
            },

            onError = { message ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = message
                )
            }
        )
    }

    fun updateStatus(
        batch: Batch,
        newStatus: String,
        user: User,
        qualityRemarks: String = "",
        warehouseLocation: String = "",
        dispatchReference: String = ""
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            operationMessage = null
        )

        repository.updateBatchStatus(
            batchId = batch.id,
            newStatus = newStatus,
            user = user,
            qualityRemarks = qualityRemarks,
            warehouseLocation = warehouseLocation,
            dispatchReference = dispatchReference,

            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationMessage =
                        "Batch updated to $newStatus."
                )

                loadBatches()
            },

            onError = { message ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = message
                )
            }
        )
    }

    fun consumeBatchCreated() {
        _uiState.value = _uiState.value.copy(
            batchCreated = false
        )
    }

    fun consumeOperationMessage() {
        _uiState.value = _uiState.value.copy(
            operationMessage = null
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}