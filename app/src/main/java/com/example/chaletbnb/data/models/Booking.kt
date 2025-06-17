package com.example.chaletbnb.data.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class Booking @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: String = "",
    val chaletId: String = "",
    val chaletName: String = "",
    val userId: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val totalPrice: Double = 0.0
)