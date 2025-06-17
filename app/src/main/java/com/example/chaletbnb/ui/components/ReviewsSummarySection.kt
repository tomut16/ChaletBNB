package com.example.chaletbnb.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chaletbnb.data.models.Chalet
import coil.compose.AsyncImage
import com.example.chaletbnb.data.models.Review
import androidx.compose.ui.graphics.Color

@Composable
fun ReviewsSummarySection(averageRating: Double, totalReviews: Int, breakdown: List<Int>) {
    Column {
        Text(
            text = "$averageRating",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(5) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("$totalReviews reviews")
        }
        Spacer(modifier = Modifier.height(12.dp))
        for (i in 5 downTo 1) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$i", modifier = Modifier.width(12.dp))
                LinearProgressIndicator(
                    progress = breakdown[5 - i] / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 16.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF4A90E2)
                )
            }
        }
    }
}