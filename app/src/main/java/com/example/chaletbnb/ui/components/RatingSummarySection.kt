package com.example.chaletbnb.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RatingSummarySection(
    averageRating: Double,
    totalReviews: Int,
    ratingBreakdown: List<Int> // Index 0 = 5 stars, ..., Index 4 = 1 star
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Left column: average rating
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(end = 16.dp)
                .width(72.dp)
        ) {
            Text(
                text = String.format("%.1f", averageRating),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (it < averageRating.toInt()) Color(0xFF4A90E2) else Color.LightGray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = "$totalReviews reviews",
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Right column: 5–1 star breakdown
        Column(modifier = Modifier.fillMaxWidth()) {
            for (i in 5 downTo 1) {
                val percent = ratingBreakdown[5 - i]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text("$i", modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE0E0E0))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(percent / 100f)
                                .background(Color(0xFF4A90E2))
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("$percent%", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
