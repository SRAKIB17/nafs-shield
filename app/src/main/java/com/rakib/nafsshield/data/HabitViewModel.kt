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
    private val preferenceManager = PreferenceManager(application)

    val habits = habitDao.getAllHabits().map { entities ->
        entities.map { it.toDomain() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isDarkMode = preferenceManager.isDarkMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val language = preferenceManager.language.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "bn")

    val allCheckIns = checkInDao.getAllCheckIns().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

    private val _isTodayCheckInDone = mutableStateOf(false)
    val isTodayCheckInDone: androidx.compose.runtime.State<Boolean> = _isTodayCheckInDone

    init {
        checkTodayStatus()
    }

    private fun checkTodayStatus() {
        viewModelScope.launch {
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            val startOfDay = calendar.timeInMillis
            _isTodayCheckInDone.value = checkInDao.getTodayCheckIn(startOfDay) != null
        }
    }

    fun performDailyCheckIn(mood: String) {
        viewModelScope.launch {
            checkInDao.insertCheckIn(CheckInEntity(mood = mood))
            _isTodayCheckInDone.value = true
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
