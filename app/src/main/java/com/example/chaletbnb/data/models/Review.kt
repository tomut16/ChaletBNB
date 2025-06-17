package com.example.chaletbnb.data.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import java.time.LocalDate

@Parcelize
data class Review(
    val id: String = "",
    val chaletId: String = "",
    val username: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val timestamp: LocalDate = LocalDate.now(),
    val userAvatar: String? = null,
) : Parcelable