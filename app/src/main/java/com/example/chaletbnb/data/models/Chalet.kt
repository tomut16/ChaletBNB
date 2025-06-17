package com.example.chaletbnb.data.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Chalet(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val description: String = "",
    val pricePerNight: Double = 0.0,
    val imageUrl: String = ""
) : Parcelable