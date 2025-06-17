package com.example.chaletbnb.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chaletbnb.data.models.Chalet
import coil.compose.AsyncImage
import com.example.chaletbnb.data.models.Booking
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingCard(
    booking: Booking,
    chalet: Chalet,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF79A08B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = booking.chaletName, fontWeight = FontWeight.Bold)
            Text(text = "From: ${booking.startDate} To: ${booking.endDate}")
            Text(text = "Nights: ${ChronoUnit.DAYS.between(booking.startDate, booking.endDate)}")
            Text(text = "Total: $${booking.totalPrice}")
        }
    }
}
