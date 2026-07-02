package com.rakib.nafsshield.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "urge_logs")
data class UrgeLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val duration: Int, // in seconds
    val trigger: String, // Stress, Lonely, etc.
    val wasRecovered: Boolean, // Yes / No
    val endUrgeLevel: String, // Fully gone, decreased, etc.
    val notes: String = ""
)
