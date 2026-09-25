package com.batchtrace.app.data.model

data class Batch(
    val id: String = "",
    val batchNumber: String = "",
    val productName: String = "",
    val productCode: String = "",
    val quantity: Int = 0,
    val unit: String = "",
    val manufacturingDate: String = "",

    val status: String = "CREATED",

    val createdBy: String = "",
    val createdByName: String = "",
    val createdAt: Long = 0L,

    val updatedAt: Long = 0L,
    val lastUpdatedBy: String = "",
    val lastUpdatedByName: String = "",

    val qualityRemarks: String = "",
    val warehouseLocation: String = "",
    val dispatchReference: String = ""
)