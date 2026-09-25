package com.batchtrace.app.data.repository

import com.batchtrace.app.data.model.Batch
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

        val batchToSave = batch.copy(
            id = document.id,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
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
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->

                val batches = snapshot.documents.mapNotNull {
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
}