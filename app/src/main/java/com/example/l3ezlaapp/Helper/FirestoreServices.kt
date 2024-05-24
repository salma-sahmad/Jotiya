package com.example.l3ezlaapp.Helper

import com.example.l3ezlaapp.Model.ItemModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getNextItemId(): Int {
        val counterDoc = db.collection("counters").document("itemCounter")
        val snapshot = counterDoc.get().await()
        var currentId = snapshot.getLong("currentId")?.toInt() ?: 0
        currentId += 1
        counterDoc.update("currentId", currentId).await()
        return currentId
    }

    suspend fun getWishlistItems(userId: String): List<ItemModel> {
        val snapshot = db.collection("users").document(userId).collection("wishlist").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(ItemModel::class.java)
        }
    }
}
