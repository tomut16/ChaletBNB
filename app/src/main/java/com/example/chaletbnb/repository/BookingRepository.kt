package com.example.chaletbnb.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.chaletbnb.data.models.Booking
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class BookingRepository {
    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    fun isChaletAvailable(
        chaletId: String,
        startDate: LocalDate,
        endDate: LocalDate,
        onResult: (Boolean) -> Unit
    ) {
        db.collection("bookings")
            .whereEqualTo("chaletId", chaletId)
            .get()
            .addOnSuccessListener { result ->
                val conflict = result.any { doc ->
                    val existingStart = doc.getTimestamp("startDate")?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                    val existingEnd = doc.getTimestamp("endDate")?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()

                    if (existingStart != null && existingEnd != null) {
                        // Check for overlap
                        !(endDate <= existingStart || startDate >= existingEnd)
                    } else false
                }
                onResult(!conflict) // available if no conflict
            }
            .addOnFailureListener {
                onResult(false) // fail safe: unavailable
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveBooking(
        chaletId: String,
        userId: String,
        startDate: LocalDate,
        endDate: LocalDate,
        onComplete: (Boolean) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        val booking = hashMapOf(
            "chaletId" to chaletId,
            "userId" to userId,
            "startDate" to Timestamp(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000, 0),
            "endDate" to Timestamp(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000, 0),
            "timestamp" to Timestamp.now()
        )

        db.collection("bookings")
            .add(booking)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getBookingsForUser(userId: String, onResult: (List<Booking>) -> Unit) {
        db.collection("bookings")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { bookingDocs ->
                val tasks = mutableListOf<Task<Booking>>()

                for (document in bookingDocs) {
                    val start = document.getTimestamp("startDate")
                        ?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                    val end = document.getTimestamp("endDate")
                        ?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                    val chaletId = document.getString("chaletId") ?: continue

                    if (start != null && end != null) {
                        val bookingId = document.id
                        val chaletTask = db.collection("chalets").document(chaletId).get()

                        tasks.add(chaletTask.continueWith { task ->
                            val chaletDoc = task.result
                            val chaletName = chaletDoc?.getString("name") ?: "Unknown Chalet"
                            val pricePerNight = chaletDoc?.getLong("pricePerNight")?.toInt() ?: 0
                            val total = pricePerNight * ChronoUnit.DAYS.between(start, end).toDouble()

                            Booking(
                                id = bookingId, // ✅ la clé manquante
                                chaletId = chaletId,
                                chaletName = chaletName,
                                startDate = start,
                                endDate = end,
                                totalPrice = total,
                                userId = userId
                            )
                        })
                    }
                }

                Tasks.whenAllSuccess<Booking>(tasks)
                    .addOnSuccessListener { completedBookings ->
                        onResult(completedBookings)
                    }
                    .addOnFailureListener {
                        onResult(emptyList())
                    }
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteBooking(bookingId: String?, onResult: (Boolean) -> Unit) {
        if (bookingId.isNullOrBlank()) {
            Log.e("BookingRepository", "Booking ID is null or blank.")
            onResult(false)
            return
        }

        db.collection("bookings").document(bookingId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener {
                Log.e("BookingRepository", "Failed to delete: ${it.message}")
                onResult(false)
            }
    }

}
