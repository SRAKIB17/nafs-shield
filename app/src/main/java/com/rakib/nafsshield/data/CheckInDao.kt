package com.rakib.nafsshield.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    @Insert
    suspend fun insertCheckIn(checkIn: CheckInEntity)

    @Query("SELECT * FROM daily_checkins ORDER BY timestamp DESC")
    fun getAllCheckIns(): Flow<List<CheckInEntity>>

    @Query("SELECT * FROM daily_checkins WHERE timestamp >= :startOfDay")
    suspend fun getTodayCheckIn(startOfDay: Long): CheckInEntity?
}
