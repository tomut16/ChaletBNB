package com.example.chaletbnb.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.chaletbnb.data.models.Review
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.time.LocalDate
import java.time.ZoneId

class ReviewRepository {
    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReviewsForChalet(
        chaletId: String,
        onResult: (List<Review>) -> Unit
    ) {
        db.collection("reviews")
            .whereEqualTo("chaletId", chaletId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val reviewList = result.documents.mapNotNull { doc ->
                    try {
                        val id = doc.id
                        val username = doc.getString("username") ?: "Anonymous"
                        val rating = doc.getLong("rating")?.toInt() ?: 0
                        val comment = doc.getString("comment") ?: ""
                        val timestamp = doc.getTimestamp("timestamp")
                            ?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                            ?: LocalDate.now()
                        val userAvatar = doc.getString("userAvatar")
                        val chaletId = doc.getString("chaletId") ?: return@mapNotNull null

                        Review(
                            id = id,
                            chaletId = chaletId,
                            username = username,
                            rating = rating,
                            comment = comment,
                            timestamp = timestamp,
                            userAvatar = userAvatar
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                onResult(reviewList)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun saveReview(
        chaletId: String,
        userId: String,
        rating: Int,
        comment: String,
        onResult: (Boolean) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        val username = user?.displayName ?: "Anonymous"
        val userAvatar = user?.photoUrl?.toString()

        val reviewData = hashMapOf(
            "chaletId" to chaletId,
            "userId" to userId,
            "username" to username,
            "userAvatar" to userAvatar,
            "rating" to rating,
            "comment" to comment,
            "timestamp" to Timestamp.now()
        )

        db.collection("reviews")
            .add(reviewData)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

}