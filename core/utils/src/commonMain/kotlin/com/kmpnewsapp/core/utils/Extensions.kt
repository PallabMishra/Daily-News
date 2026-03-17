package com.kmpnewsapp.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<T>.handleErrors(onError: (Throwable) -> Unit): Flow<T> {
    return this.catch { e -> onError(e) }
}

fun String.toReadableDate(): String {
    // Simple formatter: "2024-03-10T12:00:00Z" -> "March 10, 2024"
    return try {
        val parts = this.take(10).split("-")
        if (parts.size == 3) {
            val months = listOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )
            val monthIndex = parts[1].toIntOrNull()?.minus(1) ?: 0
            val month = months.getOrElse(monthIndex) { "Unknown" }
            "$month ${parts[2]}, ${parts[0]}"
        } else this
    } catch (e: Exception) {
        this
    }
}

fun String?.orDefault(default: String = "N/A"): String {
    return if (this.isNullOrBlank()) default else this
}
