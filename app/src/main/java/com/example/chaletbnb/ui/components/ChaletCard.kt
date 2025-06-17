package com.example.chaletbnb.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.chaletbnb.data.models.Chalet
import coil.compose.AsyncImage


@Composable
fun ChaletCard(chalet: Chalet, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF79A08B)), // set background
        elevation = CardDefaults.cardElevation()
    ) {
        Column {
            AsyncImage(
                model = chalet.imageUrl,
                contentDescription = chalet.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(chalet.name, style = MaterialTheme.typography.titleMedium)
                Text(chalet.location, style = MaterialTheme.typography.bodyMedium)
                Text("${chalet.pricePerNight} $ / night", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
