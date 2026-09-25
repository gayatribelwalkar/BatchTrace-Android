package com.batchtrace.app.viewmodel

import com.batchtrace.app.data.model.Batch

data class BatchUiState(
    val isLoading: Boolean = false,
    val batches: List<Batch> = emptyList(),
    val errorMessage: String? = null,
    val batchCreated: Boolean = false,
    val operationMessage: String? = null
)