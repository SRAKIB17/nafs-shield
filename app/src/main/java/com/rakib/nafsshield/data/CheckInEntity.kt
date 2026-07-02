package com.rakib.nafsshield.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_checkins")
data class CheckInEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val habitId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val mood: String, // 😊, 🙂, 😐, 😔, 😣
    val urgeLevel: String, // None, Low, Medium, High, Extreme
    val isRelapse: Boolean,
    val relapseReasons: String = "", // Comma separated: Stress, Lonely...
    val activities: String = "", // Comma separated: Prayer, Exercise...
    val note: String = ""
)
