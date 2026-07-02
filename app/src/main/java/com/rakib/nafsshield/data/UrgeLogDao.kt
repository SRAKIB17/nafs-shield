package com.rakib.nafsshield.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UrgeLogDao {
    @Insert
    suspend fun insertUrgeLog(log: UrgeLogEntity)

    @Query("SELECT * FROM urge_logs ORDER BY timestamp DESC")
    fun getAllUrgeLogs(): Flow<List<UrgeLogEntity>>

    @Query("SELECT COUNT(*) FROM urge_logs")
    suspend fun getTotalUrgeCount(): Int

    @Query("SELECT COUNT(*) FROM urge_logs WHERE wasRecovered = 1")
    suspend fun getSuccessCount(): Int
}
