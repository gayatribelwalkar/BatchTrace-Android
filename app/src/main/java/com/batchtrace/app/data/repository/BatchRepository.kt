package com.batchtrace.app.data.repository

import com.batchtrace.app.data.model.Batch
import com.batchtrace.app.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class BatchRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private val batchesCollection =
        firestore.collection("batches")

    fun createBatch(
        batch: Batch,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val document = batchesCollection.document()

        val currentTime = System.currentTimeMillis()

        val batchToSave = batch.copy(
            id = document.id,
            createdAt = currentTime,
            updatedAt = currentTime
        )

        document
            .set(batchToSave)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(
                    exception.localizedMessage
                        ?: "Unable to create batch."
                )
            }
    }

    fun getAllBatches(
        onSuccess: (List<Batch>) -> Unit,
        onError: (String) -> Unit
    ) {
        batchesCollection
            .orderBy(
                "createdAt",
                Query.Direction.DESCENDING
            )
            .get()
            .addOnSuccessListener { snapshot ->

                val batches =
                    snapshot.documents.mapNotNull {
                        it.toObject(Batch::class.java)
                    }

                onSuccess(batches)
            }
            .addOnFailureListener { exception ->
                onError(
                    exception.localizedMessage
                        ?: "Unable to load batches."
                )
            }
    }

    fun updateBatchStatus(
        batchId: String,
        newStatus: String,
        user: User,
        qualityRemarks: String = "",
        warehouseLocation: String = "",
        dispatchReference: String = "",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val updates = mutableMapOf<String, Any>(
            "status" to newStatus,
            "updatedAt" to System.currentTimeMillis(),
            "lastUpdatedBy" to user.uid,
            "lastUpdatedByName" to user.name
        )

        if (qualityRemarks.isNotBlank()) {
            updates["qualityRemarks"] =
                qualityRemarks.trim()
        }

        if (warehouseLocation.isNotBlank()) {
            updates["warehouseLocation"] =
                warehouseLocation.trim()
        }

        if (dispatchReference.isNotBlank()) {
            updates["dispatchReference"] =
                dispatchReference.trim()
        }

        batchesCollection
            .document(batchId)
            .update(updates)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(
                    exception.localizedMessage
                        ?: "Unable to update batch."
                )
            }
    }
}