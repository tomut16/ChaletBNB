package com.example.chaletbnb.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chaletbnb.data.models.Booking
import com.example.chaletbnb.data.models.Chalet
import com.example.chaletbnb.repository.BookingRepository
import com.example.chaletbnb.repository.ChaletRepository
import com.example.chaletbnb.ui.components.BookingCard
import com.example.chaletbnb.ui.components.BookingDetailsDialog
import com.example.chaletbnb.ui.components.LeaveReviewDialog
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingHistory(navController: NavController) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid


    if (userId == null) {
        Text("Please log in to see your bookings.")
        return
    }

    val context = LocalContext.current
    val bookingRepository = remember { BookingRepository() }
    val chaletRepository = remember { ChaletRepository() }

    var upcomingBookings by remember { mutableStateOf<List<Booking>>(emptyList()) }
    var pastBookings by remember { mutableStateOf<List<Booking>>(emptyList()) }

    var selectedBooking by remember { mutableStateOf<Booking?>(null) }
    var selectedChalet by remember { mutableStateOf<Chalet?>(null) }

    var isViewingPast by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }

    fun refreshBookings() {
        bookingRepository.getBookingsForUser(userId) { allBookings ->
            val now = LocalDate.now()
            upcomingBookings = allBookings.filter { it.endDate.isAfter(now) }
            pastBookings = allBookings.filter { !it.endDate.isAfter(now) }
        }
    }

    if (selectedBooking != null && selectedChalet != null) {
        BookingDetailsDialog(
            booking = selectedBooking!!,
            chalet = selectedChalet!!,
            isPastBooking = pastBookings.contains(selectedBooking),
            onDismiss = {
                selectedBooking = null
                selectedChalet = null
            },
            onCancel = {
                // Delete booking from Firestore
                BookingRepository().deleteBooking(selectedBooking!!.id) { success ->
                    if (success) {
                        Toast.makeText(context, "Booking deleted", Toast.LENGTH_SHORT).show()
                        selectedBooking = null
                        selectedChalet = null
                        refreshBookings() // ✅ Recharge les données
                    } else {
                        Toast.makeText(context, "Failed to delete booking", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onLeaveReview = {
                showReviewDialog = true
            }
        )
    }

    if (showReviewDialog && selectedBooking != null) {
        LeaveReviewDialog(
            chaletId = selectedBooking!!.chaletId,
            onDismiss = { showReviewDialog = false },
            onReviewSubmitted = {
                showReviewDialog = false
                Toast.makeText(context, "Review submitted!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    LaunchedEffect(userId) {
        bookingRepository.getBookingsForUser(userId) { allBookings ->
            val now = LocalDate.now()
            upcomingBookings = allBookings.filter { it.endDate.isAfter(now) }
            pastBookings = allBookings.filter { !it.endDate.isAfter(now) }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Upcoming Bookings", style = MaterialTheme.typography.titleMedium)
        }

        if (upcomingBookings.isEmpty()) {
            item {
                Text("No upcoming bookings.")
            }
        } else {
            items(upcomingBookings) { booking ->
                var chalet by remember { mutableStateOf<Chalet?>(null) }

                LaunchedEffect(booking.chaletId) {
                    chaletRepository.getChaletById(booking.chaletId) {
                        chalet = it
                    }
                }

                chalet?.let {
                    BookingCard(booking = booking, chalet = it) {
                        selectedBooking = booking
                        selectedChalet = it
                    }
                }
            }
        }

        item {
            Text("Past Bookings", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 24.dp))
        }

        if (pastBookings.isEmpty()) {
            item {
                Text("No past bookings.")
            }
        } else {
            items(pastBookings) { booking ->
                var chalet by remember { mutableStateOf<Chalet?>(null) }

                LaunchedEffect(booking.chaletId) {
                    chaletRepository.getChaletById(booking.chaletId) {
                        chalet = it
                    }
                }

                chalet?.let {
                    BookingCard(booking = booking, chalet = it) {
                        selectedBooking = booking
                        selectedChalet = it
                        isViewingPast = true
                    }
                }
            }
        }
    }


}


