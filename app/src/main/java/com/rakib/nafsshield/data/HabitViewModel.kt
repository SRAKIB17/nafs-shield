package com.rakib.nafsshield.data

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rakib.nafsshield.RecoveryHabit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val habitDao = database.habitDao()
    private val checkInDao = database.checkInDao()
    private val urgeLogDao = database.urgeLogDao()
    private val preferenceManager = PreferenceManager(application)

    val habits = habitDao.getAllHabits().map { entities ->
        entities.map { it.toDomain() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isDarkMode = preferenceManager.isDarkMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val language = preferenceManager.language.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "bn")

    val allCheckIns = checkInDao.getAllCheckIns().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allUrgeLogs = urgeLogDao.getAllUrgeLogs().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalUrges = urgeLogDao.getAllUrgeLogs().map { it.size }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val successRate = urgeLogDao.getAllUrgeLogs().map { logs ->
        if (logs.isEmpty()) 0 else (logs.count { it.wasRecovered } * 100) / logs.size
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun logUrgeSession(duration: Int, trigger: String, wasRecovered: Boolean, endLevel: String, notes: String = "") {
        viewModelScope.launch {
            urgeLogDao.insertUrgeLog(
                UrgeLogEntity(
                    duration = duration,
                    trigger = trigger,
                    wasRecovered = wasRecovered,
                    endUrgeLevel = endLevel,
                    notes = notes
                )
            )
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferenceManager.setDarkMode(enabled)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            preferenceManager.setLanguage(lang)
        }
    }

    private val _checkedInHabitIds = androidx.compose.runtime.mutableStateOf(setOf<String>())
    val checkedInHabitIds: androidx.compose.runtime.State<Set<String>> = _checkedInHabitIds

    private val _isTodayCheckInDone = mutableStateOf(false)
    val isTodayCheckInDone: androidx.compose.runtime.State<Boolean> = _isTodayCheckInDone

    init {
        checkTodayStatus()
    }

    fun checkTodayStatus() {
        viewModelScope.launch {
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            val startOfDay = calendar.timeInMillis
            
            val todayCheckIns = checkInDao.getCheckInsSince(startOfDay)
            _checkedInHabitIds.value = todayCheckIns.map { it.habitId }.toSet()
            _isTodayCheckInDone.value = todayCheckIns.isNotEmpty()
        }
    }

    fun performDailyCheckIn(checkIn: CheckInEntity) {
        viewModelScope.launch {
            // Check if there's already a check-in for this habit today to update it
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            val startOfDay = calendar.timeInMillis
            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
            val endOfDay = calendar.timeInMillis
            
            val existing = checkInDao.getHabitCheckInForDay(checkIn.habitId, startOfDay, endOfDay)
            val finalCheckIn = if (existing != null) {
                checkIn.copy(id = existing.id)
            } else {
                checkIn
            }

            checkInDao.insertCheckIn(finalCheckIn)
            
            habits.value.find { it.id == checkIn.habitId }?.let { habit ->
                if (checkIn.isRelapse) {
                    relapse(habit)
                } else if (existing == null) {
                    // Update stats only if it's the FIRST check-in of the day
                    val diff = System.currentTimeMillis() - habit.startDate
                    val days = (diff / (24 * 60 * 60 * 1000)).toInt()
                    
                    val updated = habit.copy(
                        currentStreak = days,
                        totalCleanDays = habit.totalCleanDays + 1,
                        longestStreak = maxOf(habit.longestStreak, days)
                    )
                    habitDao.updateHabit(updated.toEntity())
                }
            }
            checkTodayStatus()
        }
    }

    fun getCheckInForHabitToday(habitId: String, callback: (CheckInEntity?) -> Unit) {
        viewModelScope.launch {
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            val startOfDay = calendar.timeInMillis
            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
            val endOfDay = calendar.timeInMillis
            val result = checkInDao.getHabitCheckInForDay(habitId, startOfDay, endOfDay)
            callback(result)
        }
    }

    fun addHabit(habit: RecoveryHabit) {
        viewModelScope.launch {
            habitDao.insertHabit(habit.toEntity())
        }
    }

    fun updateHabit(habit: RecoveryHabit) {
        viewModelScope.launch {
            habitDao.updateHabit(habit.toEntity())
        }
    }

    fun deleteHabit(habit: RecoveryHabit) {
        viewModelScope.launch {
            habitDao.deleteHabit(habit.toEntity())
        }
    }

    fun relapse(habit: RecoveryHabit) {
        viewModelScope.launch {
            val updated = habit.copy(
                currentStreak = 0,
                totalRelapses = habit.totalRelapses + 1,
                lastRelapseDate = System.currentTimeMillis(),
                startDate = System.currentTimeMillis() // Reset start date to now
            )
            habitDao.updateHabit(updated.toEntity())
        }
    }

    fun checkIn(habit: RecoveryHabit) {
        viewModelScope.launch {
            val updated = habit.copy(
                currentStreak = habit.currentStreak + 1,
                totalCleanDays = habit.totalCleanDays + 1,
                longestStreak = maxOf(habit.longestStreak, habit.currentStreak + 1)
            )
            habitDao.updateHabit(updated.toEntity())
        }
    }

    private fun HabitEntity.toDomain(): RecoveryHabit {
        return RecoveryHabit(
            id = id,
            name = name,
            icon = getIconByName(iconName),
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            totalCleanDays = totalCleanDays,
            totalRelapses = totalRelapses,
            targetDays = targetDays,
            color = Color(colorHex.toInt()),
            startDate = startDate,
            lastRelapseDate = lastRelapseDate,
            lastMilestoneCelebrated = lastMilestoneCelebrated
        )
    }

    private fun RecoveryHabit.toEntity(): HabitEntity {
        return HabitEntity(
            id = id,
            name = name,
            iconName = getIconName(icon),
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            totalCleanDays = totalCleanDays,
            totalRelapses = totalRelapses,
            targetDays = targetDays,
            colorHex = color.toArgb().toLong(),
            startDate = startDate,
            lastRelapseDate = lastRelapseDate,
            lastMilestoneCelebrated = lastMilestoneCelebrated
        )
    }

    fun markMilestoneCelebrated(habit: RecoveryHabit, milestoneDay: Int) {
        viewModelScope.launch {
            val updated = habit.copy(lastMilestoneCelebrated = milestoneDay)
            habitDao.updateHabit(updated.toEntity())
        }
    }

    private fun getIconName(icon: ImageVector): String {
        return when (icon) {
            Icons.Default.Psychology -> "Psychology"
            Icons.Default.Shield -> "Shield"
            Icons.Default.Star -> "Star"
            Icons.Default.Park -> "Park"
            Icons.Default.MilitaryTech -> "MilitaryTech"
            Icons.Default.EmojiEvents -> "EmojiEvents"
            Icons.Default.FitnessCenter -> "FitnessCenter"
            Icons.Default.Timer -> "Timer"
            Icons.Default.WaterDrop -> "WaterDrop"
            Icons.Default.SmokeFree -> "SmokeFree"
            Icons.Default.Computer -> "Computer"
            Icons.Default.PhoneAndroid -> "PhoneAndroid"
            else -> "Shield"
        }
    }

    private fun getIconByName(name: String): ImageVector {
        return when (name) {
            "Psychology" -> Icons.Default.Psychology
            "Shield" -> Icons.Default.Shield
            "Star" -> Icons.Default.Star
            "Park" -> Icons.Default.Park
            "MilitaryTech" -> Icons.Default.MilitaryTech
            "EmojiEvents" -> Icons.Default.EmojiEvents
            "FitnessCenter" -> Icons.Default.FitnessCenter
            "Timer" -> Icons.Default.Timer
            "WaterDrop" -> Icons.Default.WaterDrop
            "SmokeFree" -> Icons.Default.SmokeFree
            "Computer" -> Icons.Default.Computer
            "PhoneAndroid" -> Icons.Default.PhoneAndroid
            else -> Icons.Default.Shield
        }
    }
}
