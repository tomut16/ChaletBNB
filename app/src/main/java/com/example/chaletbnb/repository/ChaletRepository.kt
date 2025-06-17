package com.example.chaletbnb.repository


import com.example.chaletbnb.data.models.Chalet
import com.google.firebase.firestore.FirebaseFirestore

class ChaletRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getChalets(onResult: (List<Chalet>) -> Unit) {
        db.collection("chalets")
            .get()
            .addOnSuccessListener { snapshot ->
                val chaletList = snapshot.documents.mapNotNull {
                    it.toObject(Chalet::class.java)?.copy(id = it.id)
                }
                onResult(chaletList)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getChaletById(chaletId: String, onResult: (Chalet?) -> Unit) {
        db.collection("chalets")
            .document(chaletId)
            .get()
            .addOnSuccessListener { document ->
                val chalet = document.toObject(Chalet::class.java)?.copy(id = document.id)
                onResult(chalet)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}