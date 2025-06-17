package com.example.chaletbnb.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chaletbnb.data.models.Chalet
import coil.compose.AsyncImage
import com.example.chaletbnb.data.models.Review
import androidx.compose.ui.graphics.Color
import java.util.Locale
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewCard(review: Review) {
    val dateFormatted = remember(review.timestamp) {
        try {
            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
            review.timestamp.format(formatter)
        } catch (e: Exception) {
            ""
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {

        // Reviewer info with avatar fallback
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!review.userAvatar.isNullOrBlank()) {
                AsyncImage(
                    model = review.userAvatar,
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Default Icon",
                    modifier = Modifier.size(32.dp)
                )
            }

            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(review.username, fontWeight = FontWeight.Bold)
                Text(dateFormatted, style = MaterialTheme.typography.bodySmall)
            }
        }

        // Star rating
        Row(modifier = Modifier.padding(top = 4.dp)) {
            repeat(review.rating) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Comment
        Text(
            review.comment,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}
