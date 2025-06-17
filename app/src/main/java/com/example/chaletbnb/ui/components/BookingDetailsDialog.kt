package com.example.chaletbnb.ui.components

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.chaletbnb.data.models.Booking
import com.example.chaletbnb.data.models.Chalet
import com.example.chaletbnb.repository.BookingRepository
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun BookingDetailsDialog(
    booking: Booking,
    chalet: Chalet,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    isPastBooking: Boolean = false,
    onLeaveReview: () -> Unit = {}
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    onCancel()
                }) {
                    Text("Yes", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("No")
                }
            },
            title = { Text("Cancel Reservation") },
            text = { Text("Are you sure you want to cancel your reservation?") }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(16.dp), color = Color.White) {
            Column(modifier = Modifier.padding(16.dp)) {
                AsyncImage(
                    model = chalet.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(8.dp))
                Text(chalet.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(chalet.location, fontSize = 14.sp, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                Text(chalet.description)

                Spacer(Modifier.height(12.dp))
                Text("From: ${booking.startDate}")
                Text("To: ${booking.endDate}")
                Spacer(Modifier.height(8.dp))
                Text("Total: ${booking.totalPrice} $", fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(24.dp))
                val context = LocalContext.current
                if (isPastBooking) {
                    Button(
                        onClick = onLeaveReview,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.fillMaxWidth()
                        ) {
                        Text("Leave a Review")
                    }
                } else {
                    Button(
                        onClick = {
                            // confirmation dialog to cancel
                            showConfirmationDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel Reservation")
                    }
                }
            }
        }
    }
}
