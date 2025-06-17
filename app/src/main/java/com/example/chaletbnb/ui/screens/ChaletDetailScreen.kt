package com.example.chaletbnb.ui.screens

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chaletbnb.data.models.Chalet
import com.example.chaletbnb.data.models.Review
import com.example.chaletbnb.ui.components.BookingDialog
import com.example.chaletbnb.ui.components.LeaveReviewDialog
import com.example.chaletbnb.ui.components.RatingSummarySection
import com.example.chaletbnb.ui.components.ReviewCard
import com.example.chaletbnb.viewmodel.ReviewViewModel
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChaletDetailScreen(navController: NavController, chalet: Chalet) {
    val reviewViewModel: ReviewViewModel = viewModel()
    LaunchedEffect(chalet.id) {
        reviewViewModel.loadReviewsForChalet(chalet.id)
    }
    val reviews = reviewViewModel.reviews.value
    val context = LocalContext.current
    var checkInDate by remember { mutableStateOf("") }
    var checkOutDate by remember { mutableStateOf("") }
    var showBookingDialog by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }

    val dateFormatter = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    }

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(year, month, day)
                onDateSelected(dateFormatter.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            AsyncImage(
                model = chalet.imageUrl,
                contentDescription = chalet.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(bottom = 12.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = chalet.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = chalet.location,
                color = Color(0xFF4A90E2),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            Text(
                text = chalet.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Price",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = "$${chalet.pricePerNight} / night",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Dates",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { showDatePicker { checkInDate = it } },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = checkInDate.ifEmpty { "Select date" }, color = Color(0xFF2E4A3E))
                }

                OutlinedButton(
                    onClick = { showDatePicker { checkOutDate = it } },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = checkOutDate.ifEmpty { "Select date" }, color = Color(0xFF2E4A3E))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    showBookingDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E4A3E), // background
                contentColor = Color.White          // text color
                )
            ) {
                Text("Book Now")
            }
        }
        item{
            RatingSummarySection(
                averageRating = 4.8,
                totalReviews = 125,
                ratingBreakdown = listOf(75, 15, 5, 3, 2) // 5 to 1 stars
            )
        }
        item {
            Text("Reviews", style = MaterialTheme.typography.titleMedium)
        }

        items(reviews) { review ->
            ReviewCard(review = review)
        }
    }

    if (showBookingDialog) {
        BookingDialog(
            chalet = chalet,
            initialStartDate = checkInDate.takeIf { it.isNotEmpty() }?.let {
                SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).parse(it)
                    ?.toInstant()?.atZone(java.time.ZoneId.systemDefault())?.toLocalDate()
            },
            initialEndDate = checkOutDate.takeIf { it.isNotEmpty() }?.let {
                SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).parse(it)
                    ?.toInstant()?.atZone(java.time.ZoneId.systemDefault())?.toLocalDate()
            },
            onConfirmBooking = { start, end ->
                checkInDate = dateFormatter.format(Date.from(start.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()))
                checkOutDate = dateFormatter.format(Date.from(end.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()))
            },
            onDismiss = { showBookingDialog = false }
        )
    }
    if (showReviewDialog) {
        LeaveReviewDialog(
            chaletId = chalet.id,
            onDismiss = { showReviewDialog = false },
            onReviewSubmitted = {
                reviewViewModel.loadReviewsForChalet(chalet.id)
                showReviewDialog = false
            }
        )
    }
}

