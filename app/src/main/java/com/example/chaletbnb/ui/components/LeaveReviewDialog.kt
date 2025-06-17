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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import androidx.compose.material3.*
import com.example.chaletbnb.repository.ReviewRepository

@Composable
fun LeaveReviewDialog(
    chaletId: String,
    onDismiss: () -> Unit,
    onReviewSubmitted: () -> Unit
) {
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }

    val reviewRepo = ReviewRepository()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Leave a Review", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Rating", fontWeight = FontWeight.SemiBold)
                Row {
                    for (i in 1..5) {
                        IconButton(onClick = { rating = i }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (i <= rating) Color(0xFFFFC107) else Color.LightGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Comment", fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4,
                    placeholder = { Text("Write your comment here...") }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        reviewRepo.saveReview(
                            chaletId = chaletId,
                            userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button,
                            rating = rating,
                            comment = comment
                        ) { success ->
                            if (success) {
                                Toast.makeText(context, "Review submitted!", Toast.LENGTH_SHORT).show()
                                onReviewSubmitted()
                            //                                navController.popBackStack() // Go back to previous screen
                            } else {
                                Toast.makeText(context, "Failed to submit review.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),)
                    {
                        Text("Submit Review")
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel",
                            color = Color(0xFF2E4A3E))
                    }
                }
            }
        }
    }
}