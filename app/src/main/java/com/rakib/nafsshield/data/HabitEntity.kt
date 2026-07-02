package com.rakib.nafsshield.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconName: String,
    val currentStreak: Int,
    val longestStreak: Int,
    val totalCleanDays: Int,
    val totalRelapses: Int,
    val targetDays: Int,
    val colorHex: Long,
    val startDate: Long,
    val lastRelapseDate: Long? = null,
    val lastMilestoneCelebrated: Int = 0
)
