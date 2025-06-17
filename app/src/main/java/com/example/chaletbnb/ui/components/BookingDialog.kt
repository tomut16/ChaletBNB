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
import com.example.chaletbnb.data.models.Chalet
import com.example.chaletbnb.repository.BookingRepository
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingDialog(
    chalet: Chalet,
    initialStartDate: LocalDate? = null,
    initialEndDate: LocalDate? = null,
    onConfirmBooking: (LocalDate, LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var startDate by remember { mutableStateOf(initialStartDate ?: LocalDate.now()) }
    var endDate by remember { mutableStateOf(initialEndDate ?: LocalDate.now().plusDays(1)) }

    val nights = ChronoUnit.DAYS.between(startDate, endDate).toInt()
    val price = chalet.pricePerNight * nights

    val startDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                startDate = LocalDate.of(year, month + 1, day)
                if (!startDate.isBefore(endDate)) {
                    endDate = startDate.plusDays(1)
                }
            },
            startDate.year, startDate.monthValue - 1, startDate.dayOfMonth
        )
    }

    val endDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                endDate = LocalDate.of(year, month + 1, day)
                if (!endDate.isAfter(startDate)) {
                    startDate = endDate.minusDays(1)
                }
            },
            endDate.year, endDate.monthValue - 1, endDate.dayOfMonth
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

                Spacer(Modifier.height(12.dp))
                Text("Select Dates", fontWeight = FontWeight.SemiBold)

                Spacer(Modifier.height(4.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { startDatePickerDialog.show() }) {
                        Text("Start: $startDate", color = Color(0xFF2E4A3E))
                    }
                    TextButton(onClick = { endDatePickerDialog.show() }) {
                        Text("End: $endDate", color = Color(0xFF2E4A3E))
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text("Nights: $nights")
                Text("Total: $price $", fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(16.dp))
                Button(onClick = launch@{
                    val bookingRepository = BookingRepository()
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
                    bookingRepository.isChaletAvailable(chalet.id, startDate, endDate) { available ->
                        if (available) {
                            bookingRepository.saveBooking(chalet.id, userId, startDate, endDate) { success ->
                                if (success) {
                                    Toast.makeText(context, "Booking confirmed!", Toast.LENGTH_SHORT).show()
                                    onDismiss() // Close the dialog
                                } else {
                                    Toast.makeText(context, "Failed to save booking.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "This chalet is already booked for those dates.", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.fillMaxWidth()) {
                    Text("Confirm Booking")
                }
            }
        }
    }
}
