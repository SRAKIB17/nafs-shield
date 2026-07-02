package com.rakib.nafsshield.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: CheckInEntity)

    @Query("SELECT * FROM daily_checkins ORDER BY timestamp DESC")
    fun getAllCheckIns(): Flow<List<CheckInEntity>>

    @Query("SELECT * FROM daily_checkins WHERE habitId = :habitId AND timestamp >= :startOfDay AND timestamp < :endOfDay LIMIT 1")
    suspend fun getHabitCheckInForDay(habitId: String, startOfDay: Long, endOfDay: Long): CheckInEntity?

    @Query("SELECT * FROM daily_checkins WHERE timestamp >= :startOfDay LIMIT 1")
    suspend fun getTodayCheckIn(startOfDay: Long): CheckInEntity?

    @Query("SELECT * FROM daily_checkins WHERE timestamp >= :startOfDay")
    suspend fun getCheckInsSince(startOfDay: Long): List<CheckInEntity>

    @Query("SELECT * FROM daily_checkins WHERE timestamp >= :startTime AND timestamp <= :endTime")
    fun getCheckInsInRange(startTime: Long, endTime: Long): Flow<List<CheckInEntity>>

    @Query("SELECT * FROM daily_checkins WHERE habitId = :habitId AND timestamp >= :startTime AND timestamp <= :endTime")
    fun getHabitCheckInsInRange(habitId: String, startTime: Long, endTime: Long): Flow<List<CheckInEntity>>
}
