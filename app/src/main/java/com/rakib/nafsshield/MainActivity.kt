package com.rakib.nafsshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmokeFree
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rakib.nafsshield.data.CheckInEntity
import com.rakib.nafsshield.data.HabitViewModel
import com.rakib.nafsshield.ui.theme.NafsShieldTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

data class RecoveryHabit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val icon: ImageVector,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalCleanDays: Int = 0,
    val totalRelapses: Int = 0,
    val targetDays: Int,
    val color: Color,
    val startDate: Long = System.currentTimeMillis(),
    val lastRelapseDate: Long? = null,
    val lastMilestoneCelebrated: Int = 0
) {
    val recoveryRate: Int
        get() = if (totalCleanDays + totalRelapses == 0) 0 
                else (totalCleanDays * 100) / (totalCleanDays + totalRelapses)
}

data class Milestone(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val days: Int,
    val hadith: String = ""
)

fun getMilestone(days: Int): Milestone {
    return when {
        days >= 1460 -> Milestone("Lifetime Victory", Icons.Default.AutoAwesome, Color(0xFF00E5FF), 1460, "“আল্লাহর কাছে সবচেয়ে প্রিয় আমল হলো যা নিয়মিত করা হয়, যদিও তা অল্প হয়।” (বুখারী)")
        days >= 730 -> Milestone("Deep Recovery", Icons.Default.EmojiEvents, Color(0xFFFFD700), 730, "“প্রকৃত বীর সে নয় যে কুস্তিতে অন্যকে হারিয়ে দেয়, বরং সেই প্রকৃত বীর যে রাগের সময় নিজেকে নিয়ন্ত্রণ করে।” (মুসলিম)")
        days >= 365 -> Milestone("New Life", Icons.Default.Park, Color(0xFF4CAF50), 365, "“যে ব্যক্তি আল্লাহকে ভয় করবে, আল্লাহ তার জন্য বের হওয়ার পথ করে দিবেন।” (সূরা তালাক: ২)")
        days >= 180 -> Milestone("Unshaken", Icons.Default.Terrain, Color(0xFF795548), 180, "“নিশ্চয়ই কষ্টের সাথেই স্বস্তি রয়েছে।” (সূরা ইনশিরাহ: ৬)")
        days >= 90 -> Milestone("Strong Mind", Icons.Default.MilitaryTech, Color(0xFFFF9800), 90, "“নিশ্চয়ই সফল হয়েছে সেই, যে নিজের আত্মাকে শুদ্ধ করেছে।” (সূরা আশ-শামস: ৯)")
        days >= 60 -> Milestone("Steady Mind", Icons.Default.FitnessCenter, Color(0xFF607D8B), 60, "“আল্লাহর সাহায্য ধৈর্য ও সালাতের মাধ্যমে অন্বেষণ করো।” (সূরা বাকারা: ৪৫)")
        days >= 30 -> Milestone("Self Mastery", Icons.Default.Spa, Color(0xFF8BC34A), 30, "“মুমিনের প্রতিটি কাজই বিস্ময়কর; তার জন্য তার সবকিছুতেই কল্যাণ রয়েছে।” (মুসলিম)")
        days >= 14 -> Milestone("Steady Warrior", Icons.Default.Shield, Color(0xFF2196F3), 14, "“যে ব্যক্তি তার লজ্জাস্থান ও জিহ্বার হেফাজত করবে, আমি তার জান্নাতের দায়িত্ব নিব।” (বুখারী)")
        days >= 7 -> Milestone("First Victory", Icons.Default.Shield, Color(0xFFFFEB3B), 7, "“তোমরা ধৈর্য ও সালাতের মাধ্যমে সাহায্য প্রার্থনা করো।”")
        days >= 3 -> Milestone("Early Progress", Icons.Default.Star, Color(0xFFFFC107), 3, "“আল্লাহর পথে এক সকাল বা এক সন্ধ্যা ব্যয় করা দুনিয়া ও তার মধ্যকার সবকিছুর চেয়ে উত্তম।”")
        days >= 1 -> Milestone("Start", Icons.Default.Shield, Color(0xFFCDDC39), 1, "“প্রতিটি আমল নিয়তের ওপর নির্ভরশীল।”")
        else -> Milestone("Initialization", Icons.Default.Shield, Color.Gray, 0)
    }
}

interface AppStrings {
    val appName: String
    val dashboard: String
    val settings: String
    val addHabit: String
    val habitName: String
    val currentStreak: String
    val longestStreak: String
    val totalCleanDays: String
    val relapses: String
    val recoveryRate: String
    val todayHadith: String
    val journeyTitle: String
    val stepIndicator: String
    val selectIcon: String
    val startDate: String
    val selectTarget: String
    val nextStep: String
    val start: String
    val darkMode: String
    val language: String
    val relapseConfirmTitle: String
    val relapseConfirmText: String
    val deleteConfirmTitle: String
    val deleteConfirmText: String
    val confirm: String
    val cancel: String
    val todayQuestion: String
    val hadithContent: String
    val hadithSource: String
    val daysLabel: String
    val cleanLabel: String
    val congratulations: String
    val milestoneAchieved: String
    val alhamdulillah: String
    val deleteHabitTitle: String
    val deleteHabitText: String
    val deleteConfirm: String
    val history: String
    val allMoods: String
    val noCheckIns: String
    val emergency: String
    val urgeTitle: String
    val breatheIn: String
    val breatheOut: String
    val hold: String
    val urgeMotivation: String
    val urgeDua: String
    val waterReminder: String
    val walkReminder: String
    val journalReminder: String
    val stayStrong: String
    val selectColor: String
    val target30Days: String
    val target90Days: String
    val target180Days: String
    val target1Year: String
    val target2Years: String
    val target4Years: String
    val targetUnlimited: String
    
    val urgeButtonTitle: String
    val urgeButtonSubtitle: String
    val safeMessage: String
    val feelingPassMessage: String
    val stepTogether: String
    val stayWithUs: String
    val readyButton: String
    val stopTitle: String
    val stopDesc: String
    val breathingTitle: String
    val motivationTitle: String
    val motivationDesc: String
    val streakMessage: String
    val halChharben: String
    val drinkWaterTitle: String
    val drankWaterButton: String
    val walkTitle: String
    val finishedWalkButton: String
    val aloneQuestion: String
    val changeEnvTitle: String
    val changeEnvDesc: String
    val triggerTitle: String
    val journalTitle: String
    val journalHint: String
    val sessionCompleteTitle: String
    val proudMessage: String
    val howFeelingNow: String
    val optionFullyGone: String
    val optionDecreased: String
    val optionSmallUrge: String
    val optionSame: String
    val optionIncreased: String

    val todayCheckIn: String
    val urgeQuestion: String
    val relapseQuestion: String
    val relapseReasonTitle: String
    val activitiesQuestion: String
    val noteHint: String
    val checkInCompleted: String
    val excellent: String
    val keepGoing: String
    val editCheckIn: String
    val save: String
    
    val analytics: String
    val summary: String
    val currentMonth: String
    val lastMonth: String
    val currentYear: String
    val lastYear: String
    val allHabits: String
    val moodDistribution: String
    val urgeAnalysis: String
    val consistency: String
    val triggerAnalysis: String
    val recoveryScore: String
    val insights: String
    val noData: String
}

object BanglaStrings : AppStrings {
    override val appName = "Habit Recovery"
    override val dashboard = "Dashboard"
    override val settings = "সেটিংস"
    override val addHabit = "নতুন হ্যাবিট"
    override val habitName = "অভ্যাসের নাম"
    override val currentStreak = "বর্তমান স্ট্রেইক"
    override val longestStreak = "সর্বোচ্চ"
    override val totalCleanDays = "মোট ক্লিন দিন"
    override val relapses = "ব্যর্থতা"
    override val recoveryRate = "রিকভারি হার"
    override val todayHadith = "আজকের হাদিস"
    override val journeyTitle = "আপনার রিকভারি জার্নি"
    override val stepIndicator = "ধাপ"
    override val selectIcon = "আইকন বেছে নিন"
    override val startDate = "শুরু করার তারিখ"
    override val selectTarget = "আপনার লক্ষ্য নির্বাচন করুন"
    override val nextStep = "পরবর্তী ধাপ"
    override val start = "শুরু করুন"
    override val darkMode = "ডার্ক মোড"
    override val language = "ভাষা"
    override val relapseConfirmTitle = "নতুন করে শুরু করতে চান?"
    override val relapseConfirmText = "আপনি কি নিশ্চিত যে আপনার রিল্যাপস হয়েছে? বর্তমান স্ট্রেইক ০ হয়ে যাবে।"
    override val deleteConfirmTitle = "মুছে ফেলতে চান?"
    override val deleteConfirmText = "এই অভ্যাসটি মুছে ফেললে আর ফিরে পাওয়া যাবে না।"
    override val confirm = "হ্যাঁ, নিশ্চিত"
    override val cancel = "না, ফিরে যান"
    override val todayQuestion = "আজ আপনার কেমন লাগছে?"
    override val hadithContent = "“নিশ্চয়ই আল্লাহ তাদের সাথে থাকেন যারা সবর করে।” (সূরা আল-বাকারা: ১৫৩)"
    override val hadithSource = "আল-কুরআন"
    override val daysLabel = "দিন"
    override val cleanLabel = "ক্লিন"
    override val congratulations = "অভিনন্দন!"
    override val milestoneAchieved = "আপনি মাইলস্টোন অর্জন করেছেন"
    override val alhamdulillah = "আলহামদুলিল্লাহ"
    override val deleteHabitTitle = "অভ্যাসটি মুছে ফেলতে চান?"
    override val deleteHabitText = "আপনি কি নিশ্চিত যে আপনি এই অভ্যাসটি চিরতরে মুছে ফেলতে চান?"
    override val deleteConfirm = "মুছে ফেলুন"
    override val history = "চেক-ইন ইতিহাস"
    override val allMoods = "সব মুড"
    override val noCheckIns = "এখনও কোনো চেক-ইন করা হয়নি"
    override val emergency = "জরুরি সাহায্য"
    override val urgeTitle = "ধৈর্য ধরুন, এই সময়টুকু কেটে যাবে"
    override val breatheIn = "শ্বাস নিন"
    override val breatheOut = "শ্বাস ছাড়ুন"
    override val hold = "ধরে রাখুন"
    override val urgeMotivation = "“একটি মুহূর্তের আনন্দ সারাজীবনের অনুশোচনার কারণ হতে পারে। আল্লাহ আপনাকে দেখছেন।”"
    override val urgeDua = "আল্লাহুম্মা ইন্নি আউযুবিকা মিন শাররি সাময়ি, ওয়া মিন শাররি বাসারি..."
    override val waterReminder = "এক গ্লাস পানি পান করুন"
    override val walkReminder = "৫ মিনিট হাঁটাহাঁটি করুন"
    override val journalReminder = "আপনার অনুভূতি লিখে রাখুন"
    override val stayStrong = "অটল থাকুন"
    override val selectColor = "রঙ নির্বাচন করুন"
    override val target30Days = "৩০ দিন"
    override val target90Days = "৯০ দিন"
    override val target180Days = "১৮০ দিন"
    override val target1Year = "১ বছর"
    override val target2Years = "২ বছর"
    override val target4Years = "৪ বছর"
    override val targetUnlimited = "আনলিমিটেড"

    override val urgeButtonTitle = "জরুরি রিকভারি মোড"
    override val urgeButtonSubtitle = "আমার এখন খুব Urge হচ্ছে"
    override val safeMessage = "আপনি নিরাপদ আছেন।"
    override val feelingPassMessage = "এই অনুভূতিটা কিছু সময়ের মধ্যেই কমে যাবে।"
    override val stepTogether = "চলুন ধাপে ধাপে একসাথে এগোই।"
    override val stayWithUs = "আমাদের সাথে থাকুন"
    override val readyButton = "আমি প্রস্তুত"
    override val stopTitle = "থামুন।"
    override val stopDesc = "এখন কোনো সিদ্ধান্ত নেবেন না। আপনি এই অনুভূতির চেয়ে শক্তিশালী।"
    override val breathingTitle = "গভীর শ্বাস নিন"
    override val motivationTitle = "মনে রাখবেন"
    override val motivationDesc = "এই Urge স্থায়ী নয়। কয়েক মিনিট পর এটি কমে যাবে। আজকে নিজেকে জয় করুন।"
    override val streakMessage = "আপনি %d দিন ধরে রেখেছেন।"
    override val halChharben = "এখনই হাল ছাড়বেন?"
    override val drinkWaterTitle = "এক গ্লাস পানি পান করুন।"
    override val drankWaterButton = "আমি পানি পান করেছি"
    override val walkTitle = "৫ মিনিট হাঁটুন অথবা রুম পরিবর্তন করুন।"
    override val finishedWalkButton = "হাঁটা শেষ"
    override val aloneQuestion = "আপনি কি এখনও একা আছেন?"
    override val changeEnvTitle = "পরিবেশ পরিবর্তন করুন"
    override val changeEnvDesc = "বারান্দায় যান অথবা পরিবারের কাছে যান। আলো জ্বালান।"
    override val triggerTitle = "কেন Urge হলো?"
    override val journalTitle = "মিনি জার্নাল"
    override val journalHint = "আপনি এখন কী অনুভব করছেন?"
    override val sessionCompleteTitle = "অভিনন্দন!"
    override val proudMessage = "আপনি একটি Urge মোকাবিলা করেছেন। নিজের উপর গর্ব করুন।"
    override val howFeelingNow = "Urge এখন কেমন?"
    override val optionFullyGone = "পুরোপুরি চলে গেছে"
    override val optionDecreased = "অনেক কমেছে"
    override val optionSmallUrge = "অল্প আছে"
    override val optionSame = "একই আছে"
    override val optionIncreased = "আরও বেড়েছে"

    override val todayCheckIn = "আজকের চেক-ইন"
    override val urgeQuestion = "আজ কি Urge হয়েছে?"
    override val relapseQuestion = "আজ কি রিল্যাপস হয়েছে?"
    override val relapseReasonTitle = "রিল্যাপসের কারণ (একাধিক হতে পারে)"
    override val activitiesQuestion = "আজকে কী কী ভালো কাজ করেছ?"
    override val noteHint = "আজকের বিশেষ কোনো মুহূর্ত বা অনুভূতি..."
    override val checkInCompleted = "আজকের চেক-ইন সম্পন্ন হয়েছে"
    override val excellent = "অসাধারণ!"
    override val keepGoing = "এগিয়ে যান"
    override val editCheckIn = "চেক-ইন পরিবর্তন করুন"
    override val save = "সংরক্ষণ করুন"

    override val analytics = "অ্যানালিটিক্স"
    override val summary = "সংক্ষিপ্ত বিবরণ"
    override val currentMonth = "এই মাস"
    override val lastMonth = "গত মাস"
    override val currentYear = "এই বছর"
    override val lastYear = "গত বছর"
    override val allHabits = "সব হ্যাবিট"
    override val moodDistribution = "মুড ডিস্ট্রিবিউশন"
    override val urgeAnalysis = "Urge বিশ্লেষণ"
    override val consistency = "ধারাবাহিকতা"
    override val triggerAnalysis = "ট্রিগার বিশ্লেষণ"
    override val recoveryScore = "রিকভারি স্কোর"
    override val insights = "ইনসাইটস"
    override val noData = "কোনো তথ্য পাওয়া যায়নি"
}

object EnglishStrings : AppStrings {
    override val appName = "Habit Recovery"
    override val dashboard = "Dashboard"
    override val settings = "Settings"
    override val addHabit = "Add Habit"
    override val habitName = "Habit Name"
    override val currentStreak = "Current Streak"
    override val longestStreak = "Longest"
    override val totalCleanDays = "Total Clean"
    override val relapses = "Relapses"
    override val recoveryRate = "Rate"
    override val todayHadith = "Today's Hadith"
    override val journeyTitle = "Your Recovery Journey"
    override val stepIndicator = "Step"
    override val selectIcon = "Choose an Icon"
    override val startDate = "Start Date"
    override val selectTarget = "Select Your Target"
    override val nextStep = "Next"
    override val start = "Start"
    override val darkMode = "Dark Mode"
    override val language = "Language"
    override val relapseConfirmTitle = "Start Over?"
    override val relapseConfirmText = "Are you sure you relapsed? Current streak will be reset."
    override val deleteConfirmTitle = "Delete Habit?"
    override val deleteConfirmText = "This habit will be permanently deleted."
    override val confirm = "Yes, Confirm"
    override val cancel = "Cancel"
    override val todayQuestion = "How do you feel today?"
    override val hadithContent = "“Truly, Allah is with those who are patient.” (Surah Al-Baqarah: 153)"
    override val hadithSource = "Al-Quran"
    override val daysLabel = "DAYS"
    override val cleanLabel = "clean"
    override val congratulations = "Congratulations!"
    override val milestoneAchieved = "You have achieved a milestone"
    override val alhamdulillah = "Alhamdulillah"
    override val deleteHabitTitle = "Delete Habit?"
    override val deleteHabitText = "Are you sure you want to delete this habit permanently?"
    override val deleteConfirm = "Delete"
    override val history = "Check-In History"
    override val allMoods = "All Moods"
    override val noCheckIns = "No check-ins yet"
    override val emergency = "Emergency"
    override val urgeTitle = "Stay Calm, This Will Pass"
    override val breatheIn = "Breathe In"
    override val breatheOut = "Breathe Out"
    override val hold = "Hold"
    override val urgeMotivation = "\"A moment of pleasure is not worth a lifetime of regret. Allah is watching you.\""
    override val urgeDua = "Allahumma inni a'udhu bika min sharri sam'i..."
    override val waterReminder = "Drink a glass of water"
    override val walkReminder = "Take a 5-minute walk"
    override val journalReminder = "Write down your feelings"
    override val stayStrong = "Stay Strong"
    override val selectColor = "Choose Color"
    override val target30Days = "30 Days"
    override val target90Days = "90 Days"
    override val target180Days = "180 Days"
    override val target1Year = "1 Year"
    override val target2Years = "2 Years"
    override val target4Years = "4 Years"
    override val targetUnlimited = "Unlimited"

    override val urgeButtonTitle = "Emergency Recovery Mode"
    override val urgeButtonSubtitle = "I'm having a strong urge now"
    override val safeMessage = "You are safe."
    override val feelingPassMessage = "This feeling will pass in a few minutes."
    override val stepTogether = "Let's walk through this together."
    override val stayWithUs = "Stay With Us"
    override val readyButton = "I'm Ready"
    override val stopTitle = "Stop."
    override val stopDesc = "Don't make any decisions now. You are stronger than this feeling."
    override val breathingTitle = "Deep Breathing"
    override val motivationTitle = "Remember"
    override val motivationDesc = "This urge is not permanent. It will decrease in a few minutes. Conquer yourself today."
    override val streakMessage = "You have held on for %d days."
    override val halChharben = "Will you give up now?"
    override val drinkWaterTitle = "Drink a glass of water."
    override val drankWaterButton = "I drank water"
    override val walkTitle = "Walk for 5 minutes or change your room."
    override val finishedWalkButton = "Finished Walk"
    override val aloneQuestion = "Are you still alone?"
    override val changeEnvTitle = "Change Environment"
    override val changeEnvDesc = "Go to the balcony or join family. Turn on the lights."
    override val triggerTitle = "Why did the urge happen?"
    override val journalTitle = "Mini Journal"
    override val journalHint = "How are you feeling right now?"
    override val sessionCompleteTitle = "Congratulations!"
    override val proudMessage = "You tackled an urge. Be proud of yourself."
    override val howFeelingNow = "How is the urge now?"
    override val optionFullyGone = "Fully gone"
    override val optionDecreased = "Decreased a lot"
    override val optionSmallUrge = "Small urge remains"
    override val optionSame = "Still same"
    override val optionIncreased = "Increased"

    override val todayCheckIn = "Today's Check-In"
    override val urgeQuestion = "Did you have any urges today?"
    override val relapseQuestion = "Did you relapse today?"
    override val relapseReasonTitle = "Relapse Reason (Multiple choice)"
    override val activitiesQuestion = "What positive activities did you do today?"
    override val noteHint = "Special moments or feelings from today..."
    override val checkInCompleted = "Today's Check-In Completed"
    override val excellent = "Excellent!"
    override val keepGoing = "Keep Going"
    override val editCheckIn = "Edit Check-In"
    override val save = "Save"

    override val analytics = "Analytics"
    override val summary = "Summary"
    override val currentMonth = "Current Month"
    override val lastMonth = "Last Month"
    override val currentYear = "Current Year"
    override val lastYear = "Last Year"
    override val allHabits = "All Habits"
    override val moodDistribution = "Mood Distribution"
    override val urgeAnalysis = "Urge Analysis"
    override val consistency = "Consistency"
    override val triggerAnalysis = "Trigger Analysis"
    override val recoveryScore = "Recovery Score"
    override val insights = "Insights"
    override val noData = "No data available"
}

enum class Screen {
    Dashboard, AddHabit, Settings, CheckInHistory, UrgeEmergency, Analytics, DailyCheckIn
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: HabitViewModel = viewModel()
            val darkModePref by viewModel.isDarkMode.collectAsState()
            val useDarkTheme = darkModePref ?: androidx.compose.foundation.isSystemInDarkTheme()
            
            NafsShieldTheme(darkTheme = useDarkTheme) {
                MainApp(viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: HabitViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.Dashboard) }
    val habits by viewModel.habits.collectAsState()
    val language by viewModel.language.collectAsState()
    var celebratedMilestone by remember { mutableStateOf<Pair<RecoveryHabit, Milestone>?>(null) }
    var selectedHabitIdForCheckIn by remember { mutableStateOf<String?>(null) }

    val strings = if (language == "bn") BanglaStrings else EnglishStrings

    LaunchedEffect(habits) {
        habits.forEach { habit ->
            val diff = System.currentTimeMillis() - habit.startDate
            val days = (diff / (24 * 60 * 60 * 1000)).toInt()
            
            val milestone = getMilestone(days)
            if (milestone.days > 0 && milestone.days > habit.lastMilestoneCelebrated && days >= milestone.days) {
                celebratedMilestone = habit to milestone
            }
        }
    }

    if (celebratedMilestone != null) {
        MilestoneCelebrationDialog(
            habit = celebratedMilestone!!.first,
            milestone = celebratedMilestone!!.second,
            strings = strings,
            onDismiss = {
                viewModel.markMilestoneCelebrated(celebratedMilestone!!.first, celebratedMilestone!!.second.days)
                celebratedMilestone = null
            }
        )
    }

    Scaffold(
        bottomBar = {
            if (currentScreen == Screen.Dashboard || currentScreen == Screen.Analytics) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen == Screen.Dashboard,
                        onClick = { currentScreen = Screen.Dashboard },
                        icon = { Icon(Icons.Default.Spa, contentDescription = null) },
                        label = { Text(strings.dashboard) }
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.Analytics,
                        onClick = { currentScreen = Screen.Analytics },
                        icon = { Icon(Icons.Default.History, contentDescription = null) },
                        label = { Text(strings.analytics) }
                    )
                }
            }
        }
    ) { padding ->
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ScreenTransition",
            modifier = Modifier.padding(padding)
        ) { screen ->
            when (screen) {
                Screen.Dashboard -> DashboardScreen(
                    viewModel = viewModel,
                    onAddHabitClick = { currentScreen = Screen.AddHabit },
                    onSettingsClick = { currentScreen = Screen.Settings },
                    onHistoryClick = { currentScreen = Screen.CheckInHistory },
                    onUrgeClick = { currentScreen = Screen.UrgeEmergency },
                    onRelapse = { viewModel.relapse(it) },
                    onCheckIn = { habit -> 
                        selectedHabitIdForCheckIn = habit.id
                        currentScreen = Screen.DailyCheckIn 
                    },
                    onDelete = { viewModel.deleteHabit(it) },
                    isCheckInDone = viewModel.isTodayCheckInDone.value,
                    onPerformCheckIn = { /* Not used in full check-in flow */ },
                    strings = strings
                )
                Screen.DailyCheckIn -> {
                    selectedHabitIdForCheckIn?.let { habitId ->
                        DailyCheckInScreen(
                            habitId = habitId,
                            viewModel = viewModel,
                            onBack = { currentScreen = Screen.Dashboard },
                            strings = strings
                        )
                    }
                }
                Screen.Analytics -> AnalyticsScreen(
                    viewModel = viewModel,
                    strings = strings
                )
                Screen.AddHabit -> AddHabitScreen(
                    onHabitAdded = { newHabit ->
                        viewModel.addHabit(newHabit)
                        currentScreen = Screen.Dashboard
                    },
                    onBack = { currentScreen = Screen.Dashboard },
                    strings = strings
                )
                Screen.Settings -> SettingsScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = Screen.Dashboard },
                    strings = strings
                )
                Screen.CheckInHistory -> CheckInHistoryScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = Screen.Dashboard },
                    strings = strings
                )
                Screen.UrgeEmergency -> UrgeEmergencyScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = Screen.Dashboard },
                    strings = strings
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: HabitViewModel,
    onAddHabitClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onUrgeClick: () -> Unit,
    onRelapse: (RecoveryHabit) -> Unit,
    onCheckIn: (RecoveryHabit) -> Unit,
    onDelete: (RecoveryHabit) -> Unit,
    isCheckInDone: Boolean,
    onPerformCheckIn: (String) -> Unit,
    strings: AppStrings
) {
    val habits by viewModel.habits.collectAsState()
    val totalUrges by viewModel.totalUrges.collectAsState()
    val successRate by viewModel.successRate.collectAsState()
    
    val todayDate = SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault()).format(Date())
    var habitToRelapse by remember { mutableStateOf<RecoveryHabit?>(null) }
    var habitToDelete by remember { mutableStateOf<RecoveryHabit?>(null) }

    if (habitToRelapse != null) {
        AlertDialog(
            onDismissRequest = { habitToRelapse = null },
            title = { Text(strings.relapseConfirmTitle) },
            text = { Text(strings.relapseConfirmText) },
            confirmButton = {
                TextButton(onClick = {
                    habitToRelapse?.let { onRelapse(it) }
                    habitToRelapse = null
                }) { Text(strings.confirm, color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { habitToRelapse = null }) { Text(strings.cancel) }
            }
        )
    }

    if (habitToDelete != null) {
        AlertDialog(
            onDismissRequest = { habitToDelete = null },
            title = { Text(strings.deleteConfirmTitle) },
            text = { Text(strings.deleteConfirmText) },
            confirmButton = {
                TextButton(onClick = {
                    habitToDelete?.let { onDelete(it) }
                    habitToDelete = null
                }) { Text(strings.confirm, color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { habitToDelete = null }) { Text(strings.cancel) }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(strings.appName, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                        Text(todayDate, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    }
                },
                actions = {
                    IconButton(onClick = onUrgeClick) {
                        Icon(Icons.Default.Notifications, contentDescription = "Urge", tint = Color.Red)
                    }
                    IconButton(onClick = onHistoryClick) {
                        Icon(Icons.Default.History, contentDescription = "History", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHabitClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                InspirationCard(
                    title = strings.todayHadith,
                    content = strings.hadithContent,
                    source = strings.hadithSource
                )
            }

            if (habits.isNotEmpty()) {
                val checkedInIds by viewModel.checkedInHabitIds
                val allHabitsDone = habits.all { it.id in checkedInIds }
                
                if (!allHabitsDone) {
                    val nextHabitToCheckIn = habits.find { it.id !in checkedInIds } ?: habits.first()
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable { onCheckIn(nextHabitToCheckIn) },
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                        ) {
                            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(strings.todayCheckIn, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                    Text("${checkedInIds.size}/${habits.size} ${strings.appName} সম্পন্ন", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }

            if (totalUrges > 0) {
                item {
                    UrgeSummaryCard(totalUrges, successRate)
                }
            }

            item {
                Text(
                    text = strings.journeyTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(habits) { habit ->
                val checkedInHabitIds by viewModel.checkedInHabitIds
                RecoveryJourneyCard(
                    habit = habit,
                    isCheckedInToday = habit.id in checkedInHabitIds,
                    onRelapse = { habitToRelapse = habit },
                    onCheckIn = { onCheckIn(habit) },
                    onDelete = { onDelete(habit) },
                    strings = strings
                )
            }

            item {
                UrgeEmergencyButton(onClick = onUrgeClick, strings = strings)
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun UrgeSummaryCard(total: Int, rate: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A).copy(alpha = 0.05f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$total", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                Text(text = "Urges Logged", style = MaterialTheme.typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$rate%", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                Text(text = "Success Rate", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun UrgeEmergencyButton(onClick: () -> Unit, strings: AppStrings) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(12.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A)) // Calming Deep Blue
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = strings.urgeButtonTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = strings.urgeButtonSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: HabitViewModel,
    onBack: () -> Unit,
    strings: AppStrings
) {
    val darkModePref by viewModel.isDarkMode.collectAsState()
    val language by viewModel.language.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.settings, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Theme Setting
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(strings.darkMode, fontWeight = FontWeight.Bold)
                    }
                    Switch(
                        checked = darkModePref ?: androidx.compose.foundation.isSystemInDarkTheme(),
                        onCheckedChange = { viewModel.setDarkMode(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Language Setting
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(strings.language, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = language == "bn", onClick = { viewModel.setLanguage("bn") })
                        Text("বাংলা (Bangla)", modifier = Modifier.clickable { viewModel.setLanguage("bn") })
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = language == "en", onClick = { viewModel.setLanguage("en") })
                        Text("English", modifier = Modifier.clickable { viewModel.setLanguage("en") })
                    }
                }
            }
        }
    }
}

@Composable
fun DailyCheckInCard(onCheckIn: (String) -> Unit, strings: AppStrings) {
    val moods = listOf(
        "😊" to (if(strings is BanglaStrings) "ভালো" else "Good"),
        "😐" to (if(strings is BanglaStrings) "মোটামুটি" else "Neutral"),
        "😞" to (if(strings is BanglaStrings) "খারাপ" else "Bad"),
        "😖" to "Urge",
        "😔" to "Relapse"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(strings.todayQuestion, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                moods.forEach { (emoji, label) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onCheckIn(label) }
                    ) {
                        Text(emoji, fontSize = 32.sp)
                        Text(label, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(onHabitAdded: (RecoveryHabit) -> Unit, onBack: () -> Unit, strings: AppStrings) {
    var step by remember { mutableIntStateOf(1) }
    var habitName by remember { mutableStateOf("") }
    val nameSuggestions = listOf("Masturbation", "Smoking", "Gaming", "Social Media", "Junk Food")
    
    val availableIcons = listOf(
        Icons.Default.Psychology, Icons.Default.Shield, Icons.Default.Star,
        Icons.Default.Park, Icons.Default.MilitaryTech, Icons.Default.EmojiEvents,
        Icons.Default.FitnessCenter, Icons.Default.Timer, Icons.Default.WaterDrop,
        Icons.Default.SmokeFree, Icons.Default.Computer, Icons.Default.PhoneAndroid,
        Icons.Default.AutoAwesome, Icons.Default.Spa, Icons.Default.LocalDrink,
        Icons.Default.DirectionsWalk, Icons.Default.EditNote, Icons.Default.Terrain
    )
    
    val availableColors = listOf(
        Color(0xFF6366F1), Color(0xFFEC4899), Color(0xFF10B981),
        Color(0xFFF59E0B), Color(0xFF3B82F6), Color(0xFF8B5CF6),
        Color(0xFFEF4444), Color(0xFF06B6D4), Color(0xFF71717A)
    )

    var selectedIcon by remember { mutableStateOf(availableIcons[0]) }
    var selectedColor by remember { mutableStateOf(availableColors[0]) }
    var startDateMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val targetOptions = listOf(
        30 to strings.target30Days, 
        90 to strings.target90Days, 
        180 to strings.target180Days,
        365 to strings.target1Year, 
        730 to strings.target2Years, 
        1460 to strings.target4Years,
        9999 to strings.targetUnlimited
    )
    var selectedTarget by remember { mutableIntStateOf(90) }

    val infiniteTransition = rememberInfiniteTransition(label = "Background")
    val animValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GradientAnim"
    )

    BackHandler { if (step > 1) step-- else onBack() }

    Box(modifier = Modifier.fillMaxSize().background(
        Brush.linearGradient(
            colors = listOf(
                lerp(MaterialTheme.colorScheme.surface, selectedColor.copy(alpha = 0.15f), animValue),
                lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, (1f - animValue) * 0.2f)
            )
        )
    )) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(strings.addHabit, fontWeight = FontWeight.Black) },
                    navigationIcon = {
                        IconButton(onClick = { if (step > 1) step-- else onBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Professional Step Progress
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(5) { index ->
                        val isActive = step > index
                        val barWidth by animateDpAsState(if (step == index + 1) 24.dp else 8.dp, label = "BarWidth")
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(barWidth)
                                .clip(CircleShape)
                                .background(
                                    if (isActive) selectedColor 
                                    else selectedColor.copy(alpha = 0.2f)
                                )
                        )
                    }
                }

                AnimatedContent(
                    targetState = step,
                    transitionSpec = {
                        if (targetState > initialState) {
                            fadeIn(tween(400)) + expandVertically() togetherWith fadeOut(tween(400))
                        } else {
                            fadeIn(tween(400)) togetherWith fadeOut(tween(400)) + shrinkVertically()
                        }
                    },
                    label = "StepContent",
                    modifier = Modifier.weight(1f)
                ) { currentStep ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        when (currentStep) {
                            1 -> {
                                Text(strings.habitName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.height(24.dp))
                                OutlinedTextField(
                                    value = habitName, 
                                    onValueChange = { habitName = it }, 
                                    modifier = Modifier.fillMaxWidth(), 
                                    shape = RoundedCornerShape(20.dp),
                                    label = { Text("What are you recovering from?") },
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(), 
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    nameSuggestions.take(3).forEach { suggestion ->
                                        FilterChip(
                                            selected = habitName == suggestion, 
                                            onClick = { habitName = suggestion }, 
                                            label = { Text(suggestion) },
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                    }
                                }
                            }
                            2 -> {
                                Text(strings.selectIcon, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.height(24.dp))
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4), 
                                    verticalArrangement = Arrangement.spacedBy(16.dp), 
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp, bottom = 16.dp)
                                ) {
                                    items(availableIcons) { icon ->
                                        val isSelected = selectedIcon == icon
                                        val iconScale by animateFloatAsState(if (isSelected) 1.2f else 1f, label = "IconScale")
                                        Box(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .scale(iconScale)
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(
                                                    if (isSelected) selectedColor 
                                                    else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                                                )
                                                .clickable { selectedIcon = icon }
                                                .padding(12.dp), 
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = icon, 
                                                contentDescription = null, 
                                                tint = if (isSelected) Color.White else selectedColor, 
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            3 -> {
                                Text(strings.selectColor, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.height(24.dp))
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(3),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                                ) {
                                    items(availableColors) { color ->
                                        val isSelected = selectedColor == color
                                        val circleScale by animateFloatAsState(if (isSelected) 1.2f else 1f, label = "CircleScale")
                                        Box(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .scale(circleScale)
                                                .clip(CircleShape)
                                                .background(color)
                                                .clickable { selectedColor = color }
                                                .padding(12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSelected) Icon(Icons.Default.Check, null, tint = Color.White)
                                        }
                                    }
                                }
                            }
                            4 -> {
                                Text(strings.startDate, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.height(32.dp))
                                val dateTimeFormatted = SimpleDateFormat("dd MMMM, yyyy  •  hh:mm a", Locale.getDefault()).format(Date(startDateMillis))
                                
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }, 
                                    shape = RoundedCornerShape(24.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Row(modifier = Modifier.padding(28.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CalendarToday, null, tint = selectedColor, modifier = Modifier.size(32.dp))
                                        Spacer(modifier = Modifier.width(20.dp))
                                        Text(text = dateTimeFormatted, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text("The counter will start from this exact second", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            }
                            5 -> {
                                Text(strings.selectTarget, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.height(24.dp))
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                                ) {
                                    items(targetOptions) { (days, label) ->
                                        val isSelected = selectedTarget == days
                                        Card(
                                            modifier = Modifier.fillMaxWidth().clickable { selectedTarget = days }, 
                                            shape = RoundedCornerShape(20.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (isSelected) selectedColor 
                                                                else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                                            ),
                                            elevation = CardDefaults.cardElevation(defaultElevation = if(isSelected) 8.dp else 0.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(20.dp).fillMaxWidth(), 
                                                horizontalArrangement = Arrangement.SpaceBetween, 
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    label, 
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.ExtraBold, 
                                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                                )
                                                if (isSelected) Icon(Icons.Default.Check, null, tint = Color.White)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = { 
                        if (step < 5) step++ 
                        else onHabitAdded(RecoveryHabit(name = habitName, icon = selectedIcon, targetDays = selectedTarget, color = selectedColor, startDate = startDateMillis)) 
                    }, 
                    modifier = Modifier.fillMaxWidth().height(64.dp), 
                    shape = RoundedCornerShape(20.dp), 
                    enabled = step != 1 || habitName.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = selectedColor),
                    elevation = ButtonDefaults.buttonColors().let { ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp) }
                ) {
                    Text(if (step < 5) strings.nextStep else strings.start, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = startDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false }, 
            confirmButton = { 
                TextButton(onClick = { 
                    val selectedDate = datePickerState.selectedDateMillis ?: startDateMillis
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = selectedDate
                        val currentCal = Calendar.getInstance().apply { timeInMillis = startDateMillis }
                        set(Calendar.HOUR_OF_DAY, currentCal.get(Calendar.HOUR_OF_DAY))
                        set(Calendar.MINUTE, currentCal.get(Calendar.MINUTE))
                    }
                    startDateMillis = calendar.timeInMillis
                    showDatePicker = false
                    showTimePicker = true 
                }) { Text("Next: Time") } 
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val calendar = Calendar.getInstance().apply { timeInMillis = startDateMillis }
        val timePickerState = rememberTimePickerState(
            initialHour = calendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = calendar.get(Calendar.MINUTE)
        )
        
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val cal = Calendar.getInstance().apply {
                        timeInMillis = startDateMillis
                        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        set(Calendar.MINUTE, timePickerState.minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    startDateMillis = cal.timeInMillis
                    showTimePicker = false
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}


@Composable
fun MilestoneCelebrationDialog(
    habit: RecoveryHabit,
    milestone: Milestone,
    strings: AppStrings,
    onDismiss: () -> Unit
) {
    val scale = remember { Animatable(0.7f) }
    LaunchedEffect(Unit) { scale.animateTo(targetValue = 1.05f, animationSpec = infiniteRepeatable(animation = tween(1200), repeatMode = RepeatMode.Reverse)) }
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(modifier = Modifier.fillMaxWidth(0.85f).padding(16.dp).scale(scale.value), shape = RoundedCornerShape(32.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 24.dp)) {
            Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) { IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, null) } }
                Box(modifier = Modifier.size(120.dp).clip(CircleShape).background(milestone.color.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) { Icon(imageVector = milestone.icon, contentDescription = null, tint = milestone.color, modifier = Modifier.size(70.dp)) }
                Spacer(modifier = Modifier.height(24.dp))
                Text(strings.congratulations, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = milestone.color)
                Spacer(modifier = Modifier.height(8.dp))
                Text("${strings.milestoneAchieved} '${milestone.name}'", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = milestone.color.copy(alpha = 0.05f)), shape = RoundedCornerShape(20.dp)) { Text(text = milestone.hadith, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, textAlign = TextAlign.Center, lineHeight = 26.sp) }
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = milestone.color)) { Text(strings.alhamdulillah, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
            }
        }
    }
}

@Composable
fun RecoveryJourneyCard(habit: RecoveryHabit, isCheckedInToday: Boolean, onRelapse: () -> Unit, onCheckIn: () -> Unit, onDelete: () -> Unit, strings: AppStrings) {
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(key1 = habit.id) { while (true) { currentTime = System.currentTimeMillis(); delay(1000) } }
    val diff = maxOf(0L, currentTime - habit.startDate)
    val days = (diff / (24 * 60 * 60 * 1000)).toInt()
    val hours = (diff / (60 * 60 * 1000) % 24).toInt()
    val minutes = (diff / (60 * 1000) % 60).toInt()
    val seconds = (diff / 1000 % 60).toInt()
    val milestone = getMilestone(days)
    val progress = diff.toFloat() / (habit.targetDays.toLong() * 24 * 60 * 60 * 1000)

    Card(modifier = Modifier.fillMaxWidth().shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp), ambientColor = habit.color).clickable { /* Detail */ }, shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(90.dp)) {
                    CircularIntervalIndicator(progress = progress, color = habit.color, modifier = Modifier.fillMaxSize())
                    Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(habit.color.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = habit.icon, contentDescription = null, tint = habit.color, modifier = Modifier.size(24.dp))
                            Text(text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds), style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = habit.color, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = habit.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1F2937))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(milestone.icon, null, tint = milestone.color, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = milestone.name, style = MaterialTheme.typography.labelMedium, color = milestone.color, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "${hours}h ${minutes}m ${strings.cleanLabel}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "$days", fontSize = 32.sp, fontWeight = FontWeight.Black, color = habit.color)
                    Text(text = strings.daysLabel, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.weight(1f)) {
                    StatItem(label = strings.longestStreak, value = "${habit.longestStreak}d")
                    Spacer(modifier = Modifier.width(16.dp))
                    StatItem(label = strings.totalCleanDays, value = "${habit.totalCleanDays}d")
                    Spacer(modifier = Modifier.width(16.dp))
                    StatItem(label = strings.relapses, value = "${habit.totalRelapses}")
                    Spacer(modifier = Modifier.width(16.dp))
                    StatItem(label = strings.recoveryRate, value = "${habit.recoveryRate}%")
                }
                Row {
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray.copy(alpha = 0.6f)) }
                    IconButton(onClick = onRelapse) { Icon(Icons.Default.RestartAlt, contentDescription = "Relapse", tint = Color.Red) }
                    Button(
                        onClick = onCheckIn,
                        colors = if (isCheckedInToday) 
                                    ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Gray)
                                 else 
                                    ButtonDefaults.buttonColors(containerColor = habit.color.copy(alpha = 0.1f), contentColor = habit.color),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        modifier = androidx.compose.ui.Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Default.Check, null, modifier = androidx.compose.ui.Modifier.size(16.dp))
                        Spacer(modifier = androidx.compose.ui.Modifier.width(4.dp))
                        Text(if (isCheckedInToday) "Done" else "Check-in", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun CircularIntervalIndicator(progress: Float, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val strokeWidth = 6.dp.toPx()
        drawCircle(color = color.copy(alpha = 0.1f), style = Stroke(width = strokeWidth))
        drawArc(color = color, startAngle = -90f, sweepAngle = 360f * progress.coerceIn(0f, 1f), useCenter = false, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
    }
}

@Composable
fun InspirationCard(title: String, content: String, source: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(content, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, lineHeight = 24.sp, color = Color(0xFF374151))
            Text(text = "— $source", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInHistoryScreen(
    viewModel: HabitViewModel,
    onBack: () -> Unit,
    strings: AppStrings
) {
    val checkIns by viewModel.allCheckIns.collectAsState()
    var selectedMood by remember { mutableStateOf<String?>(null) }
    var showFilterMenu by remember { mutableStateOf(false) }

    val filteredCheckIns = if (selectedMood == null) {
        checkIns
    } else {
        checkIns.filter { it.mood == selectedMood }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.history, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showFilterMenu = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter")
                        }
                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = { showFilterMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(strings.allMoods) },
                                onClick = { selectedMood = null; showFilterMenu = false }
                            )
                            listOf("ভালো", "মোটামুটি", "খারাপ", "Urge", "Relapse").forEach { mood ->
                                DropdownMenuItem(
                                    text = { Text(mood) },
                                    onClick = { selectedMood = mood; showFilterMenu = false }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (filteredCheckIns.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(strings.noCheckIns, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredCheckIns) { checkIn ->
                    CheckInHistoryItem(checkIn)
                }
            }
        }
    }
}

@Composable
fun CheckInHistoryItem(checkIn: CheckInEntity) {
    val date = SimpleDateFormat("dd MMMM, yyyy • hh:mm a", Locale.getDefault()).format(Date(checkIn.timestamp))
    val emoji = when (checkIn.mood) {
        "😊 খুব ভালো" -> "😊"
        "🙂 ভালো" -> "🙂"
        "😐 মোটামুটি" -> "😐"
        "😔 খারাপ" -> "😞"
        "😣 খুব খারাপ" -> "😖"
        else -> "🤔"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(checkIn.mood, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}

data class QuranHadith(val content: String, val source: String)
data class DuaContent(val arabic: String, val translation: String, val pronunciation: String)

object RecoveryContent {
    val quranHadith = listOf(
        QuranHadith("“Truly, Allah is with those who are patient.”", "Surah Al-Baqarah: 153"),
        QuranHadith("“Whoever guards his tongue and his private parts, I guarantee him Paradise.”", "Sahih Bukhari"),
        QuranHadith("“The strong man is not the one who can wrestle, but the one who can control himself when angry.”", "Sahih Muslim")
    )
    val duas = listOf(
        DuaContent(
            "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ شَرِّ سَمْعِي، وَمِنْ شَرِّ بَصَرِي...",
            "O Allah, I seek refuge in You from the evil of my hearing, and from the evil of my seeing...",
            "Allahumma inni a'udhu bika min sharri sam'i, wa min sharri basari..."
        )
    )
    val triggers = listOf("Stress", "Lonely", "Late Night", "Instagram", "TikTok", "Facebook", "YouTube", "Porn", "Gaming", "Boredom", "Anger", "Anxiety")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyCheckInScreen(
    habitId: String,
    viewModel: HabitViewModel,
    onBack: () -> Unit,
    strings: AppStrings
) {
    var step by remember { mutableIntStateOf(1) }
    var mood by remember { mutableStateOf("") }
    var urgeLevel by remember { mutableStateOf("") }
    var isRelapse by remember { mutableStateOf(false) }
    var selectedReasons by remember { mutableStateOf(setOf<String>()) }
    var selectedActivities by remember { mutableStateOf(setOf<String>()) }
    var note by remember { mutableStateOf("") }

    LaunchedEffect(habitId) {
        viewModel.getCheckInForHabitToday(habitId) { existing ->
            existing?.let {
                mood = it.mood
                urgeLevel = it.urgeLevel
                isRelapse = it.isRelapse
                selectedReasons = it.relapseReasons.split(",").filter { s -> s.isNotBlank() }.toSet()
                selectedActivities = it.activities.split(",").filter { s -> s.isNotBlank() }.toSet()
                note = it.note
            }
        }
    }

    val habits by viewModel.habits.collectAsState()
    val habit = habits.find { it.id == habitId } ?: return

    val moodOptions = listOf("😊 খুব ভালো", "🙂 ভালো", "😐 মোটামুটি", "😔 খারাপ", "😣 খুব খারাপ")
    val urgeOptions = listOf("❌ একদম না", "🙂 অল্প", "😐 মাঝারি", "😣 অনেক", "🔥 খুব বেশি")
    val relapseReasons = listOf("Late Night", "Stress", "Loneliness", "Instagram", "Facebook", "TikTok", "YouTube", "Porn", "Gaming", "Boredom", "Anger", "Anxiety")
    val activityOptions = listOf("Exercise", "Walk", "Prayer", "Meditation", "Reading", "Cold Shower", "Healthy Food", "Early Sleep", "Journal", "Dhikr")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.todayCheckIn, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { if (step > 1) step-- else onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step Progress
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(6) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(if (step > index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }

            AnimatedContent(targetState = step, label = "CheckInStep", modifier = Modifier.weight(1f)) { currentStep ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    when (currentStep) {
                        1 -> {
                            Text(strings.todayQuestion, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(24.dp))
                            moodOptions.forEach { option ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { mood = option; step++ },
                                    colors = CardDefaults.cardColors(containerColor = if (mood == option) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                ) {
                                    Text(option, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                        }
                        2 -> {
                            Text(strings.urgeQuestion, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(24.dp))
                            urgeOptions.forEach { option ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { urgeLevel = option; step++ },
                                    colors = CardDefaults.cardColors(containerColor = if (urgeLevel == option) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                ) {
                                    Text(option, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                        }
                        3 -> {
                            Text(strings.relapseQuestion, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(32.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Button(onClick = { isRelapse = false; step = 5 }, modifier = Modifier.weight(1f).height(64.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))) { Text("No", fontSize = 18.sp) }
                                Button(onClick = { isRelapse = true; step = 4 }, modifier = Modifier.weight(1f).height(64.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Yes", fontSize = 18.sp) }
                            }
                        }
                        4 -> {
                            Text(strings.relapseReasonTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(relapseReasons) { reason ->
                                    FilterChip(
                                        selected = selectedReasons.contains(reason),
                                        onClick = { if (selectedReasons.contains(reason)) selectedReasons -= reason else selectedReasons += reason },
                                        label = { Text(reason) }
                                    )
                                }
                            }
                            Button(onClick = { step++ }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text(strings.nextStep) }
                        }
                        5 -> {
                            Text(strings.activitiesQuestion, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(activityOptions) { activity ->
                                    FilterChip(
                                        selected = selectedActivities.contains(activity),
                                        onClick = { if (selectedActivities.contains(activity)) selectedActivities -= activity else selectedActivities += activity },
                                        label = { Text(activity) }
                                    )
                                }
                            }
                            Button(onClick = { step++ }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text(strings.nextStep) }
                        }
                        6 -> {
                            Text(strings.journalTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = note,
                                onValueChange = { note = it },
                                modifier = Modifier.fillMaxWidth().height(200.dp),
                                placeholder = { Text(strings.noteHint) },
                                shape = RoundedCornerShape(16.dp)
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Button(
                                onClick = {
                                    viewModel.performDailyCheckIn(
                                        CheckInEntity(
                                            habitId = habitId,
                                            mood = mood,
                                            urgeLevel = urgeLevel,
                                            isRelapse = isRelapse,
                                            relapseReasons = selectedReasons.joinToString(","),
                                            activities = selectedActivities.joinToString(","),
                                            note = note
                                        )
                                    )
                                    onBack()
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp)
                            ) { Text(strings.save) }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: HabitViewModel,
    strings: AppStrings
) {
    val checkIns by viewModel.allCheckIns.collectAsState()
    val habits by viewModel.habits.collectAsState()
    
    var selectedHabitId by remember { mutableStateOf<String?>(null) }

    val filteredCheckIns = checkIns.filter { 
        (selectedHabitId == null || it.habitId == selectedHabitId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.analytics, fontWeight = FontWeight.Black) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedHabitId == null,
                            onClick = { selectedHabitId = null },
                            label = { Text(strings.allHabits) }
                        )
                    }
                    items(habits) { habit ->
                        FilterChip(
                            selected = selectedHabitId == habit.id,
                            onClick = { selectedHabitId = habit.id },
                            label = { Text(habit.name) }
                        )
                    }
                }
            }

            if (filteredCheckIns.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text(strings.noData, color = Color.Gray)
                    }
                }
            } else {
                item { RecoverySummaryCard(filteredCheckIns, strings) }
                item { MoodDistributionCard(filteredCheckIns, strings) }
                item { UrgeAnalysisCard(filteredCheckIns, strings) }
                item { AnalyticsCalendarCard(filteredCheckIns, strings) }
            }
        }
    }
}

@Composable
fun RecoverySummaryCard(checkIns: List<CheckInEntity>, strings: AppStrings) {
    val cleanDays = checkIns.count { !it.isRelapse }
    val relapses = checkIns.count { it.isRelapse }
    val successRate = if (checkIns.isEmpty()) 0 else (cleanDays * 100) / checkIns.size

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(strings.summary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatCol("Clean", "$cleanDays", Color(0xFF16A34A))
                StatCol("Relapses", "$relapses", Color.Red)
                StatCol("Success", "$successRate%", MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun StatCol(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun MoodDistributionCard(checkIns: List<CheckInEntity>, strings: AppStrings) {
    val moods = checkIns.groupBy { it.mood.take(2) }
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(strings.moodDistribution, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            moods.forEach { (emoji, list) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(emoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f).height(8.dp).clip(CircleShape).background(Color.LightGray)) {
                        Box(modifier = Modifier.fillMaxWidth(list.size.toFloat() / checkIns.size).fillMaxHeight().clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${list.size}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun UrgeAnalysisCard(checkIns: List<CheckInEntity>, strings: AppStrings) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(strings.urgeAnalysis, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth().height(150.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("❌", "🙂", "😐", "😣", "🔥").forEach { icon ->
                    val count = checkIns.count { it.urgeLevel.contains(icon) }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.width(24.dp).height((count.toFloat() / maxOf(1, checkIns.size) * 100).dp).clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)).background(MaterialTheme.colorScheme.secondary))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(icon)
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsCalendarCard(checkIns: List<CheckInEntity>, strings: AppStrings) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Activity Calendar", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            val calendar = Calendar.getInstance()
            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            
            LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.height(250.dp)) {
                items(daysInMonth) { index ->
                    val day = index + 1
                    val dayCheckIns = checkIns.filter { 
                        val cal = Calendar.getInstance().apply { timeInMillis = it.timestamp }
                        cal.get(Calendar.DAY_OF_MONTH) == day
                    }
                    val hasRelapse = dayCheckIns.any { it.isRelapse }
                    val hasCheckIn = dayCheckIns.isNotEmpty()
                    
                    Box(
                        modifier = Modifier.aspectRatio(1f).padding(4.dp).clip(CircleShape).background(
                            when {
                                hasRelapse -> Color.Red.copy(alpha = 0.6f)
                                hasCheckIn -> Color(0xFF16A34A).copy(alpha = 0.6f)
                                day == currentDay -> MaterialTheme.colorScheme.primaryContainer
                                else -> Color.Transparent
                            }
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$day", fontSize = 12.sp, fontWeight = if(day == currentDay) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrgeEmergencyScreen(viewModel: HabitViewModel, onBack: () -> Unit, strings: AppStrings) {
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 12
    var timerSeconds by remember { mutableIntStateOf(300) }
    
    val habits by viewModel.habits.collectAsState()
    val totalStreakDays = habits.sumOf { it.currentStreak }

    LaunchedEffect(Unit) {
        while (timerSeconds > 0) {
            delay(1000)
            timerSeconds--
        }
    }

    Scaffold(containerColor = Color(0xFFF8FAFC)) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = String.format("%02d:%02d", timerSeconds / 60, timerSeconds % 60), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Color(0xFF1E3A8A))
                IconButton(onClick = onBack) { Icon(Icons.Default.Close, contentDescription = null) }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(totalSteps) { index ->
                    Box(modifier = Modifier.weight(1f).height(6.dp).clip(CircleShape).background(if (currentStep > index) Color(0xFF1E3A8A) else Color(0xFFE2E8F0)))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            AnimatedContent(targetState = currentStep, label = "UrgeStep", modifier = Modifier.weight(1f)) { step ->
                when (step) {
                    1 -> StopStep(onNext = { currentStep++ }, strings = strings)
                    2 -> BreathingStep(onNext = { currentStep++ }, strings = strings)
                    3 -> MotivationStep(streak = totalStreakDays, onNext = { currentStep++ }, strings = strings)
                    4 -> QuranHadithStep(onNext = { currentStep++ }, strings = strings)
                    5 -> DuaStep(onNext = { currentStep++ }, strings = strings)
                    6 -> WaterStep(onNext = { currentStep++ }, strings = strings)
                    7 -> WalkStep(onNext = { currentStep++ }, strings = strings)
                    8 -> EnvironmentStep(onNext = { currentStep++ }, strings = strings)
                    9 -> TriggerStep(onNext = { currentStep++ }, strings = strings)
                    10 -> JournalStep(onNext = { currentStep++ }, strings = strings)
                    11 -> LastWaitStep(onNext = { currentStep++ }, strings = strings)
                    12 -> CompleteStep(onFinish = { level ->
                        if (level == strings.optionIncreased) currentStep = 1
                        else {
                            viewModel.logUrgeSession(300 - timerSeconds, "Multiple", level != strings.optionIncreased, level)
                            onBack()
                        }
                    }, strings = strings)
                }
            }
        }
    }
}

@Composable
fun StopStep(onNext: () -> Unit, strings: AppStrings) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(strings.stopTitle, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text(strings.stopDesc, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(64.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))) { Text(strings.readyButton, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
    }
}

@Composable
fun BreathingStep(onNext: () -> Unit, strings: AppStrings) {
    var phase by remember { mutableStateOf(strings.breatheIn) }
    val scale = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        repeat(4) {
            phase = strings.breatheIn
            scale.animateTo(1.5f, tween(4000))
            phase = strings.hold
            delay(4000)
            phase = strings.breatheOut
            scale.animateTo(1f, tween(6000))
        }
        onNext()
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(strings.breathingTitle, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(64.dp))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(250.dp)) {
            Box(modifier = Modifier.size(150.dp).scale(scale.value).clip(CircleShape).background(Color(0xFFBFDBFE)))
            Text(phase, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
        }
    }
}

@Composable
fun MotivationStep(streak: Int, onNext: () -> Unit, strings: AppStrings) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(strings.motivationTitle, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(strings.motivationDesc, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(24.dp))
                Text(String.format(strings.streakMessage, streak), fontWeight = FontWeight.Black, color = Color(0xFF16A34A), fontSize = 24.sp)
                Text(strings.halChharben, style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text(strings.nextStep) }
    }
}

@Composable
fun QuranHadithStep(onNext: () -> Unit, strings: AppStrings) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val content = RecoveryContent.quranHadith.random()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(48.dp), tint = Color(0xFFF59E0B))
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(content.content, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                Spacer(modifier = Modifier.height(12.dp))
                Text("— ${content.source}", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = {
                        val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                        val clip = android.content.ClipData.newPlainText("Recovery Motivation", "${content.content} — ${content.source}")
                        clipboard.setPrimaryClip(clip)
                    }) { Icon(Icons.Default.EditNote, contentDescription = "Copy") }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text(strings.nextStep) }
    }
}

@Composable
fun DuaStep(onNext: () -> Unit, strings: AppStrings) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val dua = RecoveryContent.duas[0]
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Dua", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(dua.arabic, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(dua.pronunciation, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Text(dua.translation, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
                Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = {
                        val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                        val clip = android.content.ClipData.newPlainText("Dua", "${dua.arabic}\n${dua.translation}")
                        clipboard.setPrimaryClip(clip)
                    }) { Icon(Icons.Default.EditNote, contentDescription = "Copy") }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text(strings.nextStep) }
    }
}

@Composable
fun WaterStep(onNext: () -> Unit, strings: AppStrings) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.LocalDrink, null, modifier = Modifier.size(100.dp), tint = Color(0xFF3B82F6))
        Spacer(modifier = Modifier.height(24.dp))
        Text(strings.drinkWaterTitle, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(64.dp)) { Text(strings.drankWaterButton) }
    }
}

@Composable
fun WalkStep(onNext: () -> Unit, strings: AppStrings) {
    var walkSeconds by remember { mutableIntStateOf(300) }
    LaunchedEffect(Unit) {
        while (walkSeconds > 0) {
            delay(1000)
            walkSeconds--
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.DirectionsWalk, null, modifier = Modifier.size(100.dp), tint = Color(0xFF10B981))
        Spacer(modifier = Modifier.height(24.dp))
        Text(strings.walkTitle, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Text(String.format("%02d:%02d", walkSeconds / 60, walkSeconds % 60), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(64.dp)) { Text(strings.finishedWalkButton) }
    }
}

@Composable
fun EnvironmentStep(onNext: () -> Unit, strings: AppStrings) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(strings.aloneQuestion, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = onNext, modifier = Modifier.weight(1f).height(64.dp)) { Text("Yes") }
            Button(onClick = onNext, modifier = Modifier.weight(1f).height(64.dp)) { Text("No") }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(strings.changeEnvDesc, textAlign = TextAlign.Center)
    }
}

@Composable
fun TriggerStep(onNext: () -> Unit, strings: AppStrings) {
    var selectedTriggers by remember { mutableStateOf(setOf<String>()) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(strings.triggerTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.height(300.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(RecoveryContent.triggers) { trigger ->
                FilterChip(selected = selectedTriggers.contains(trigger), onClick = { if (selectedTriggers.contains(trigger)) selectedTriggers -= trigger else selectedTriggers += trigger }, label = { Text(trigger) })
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text(strings.nextStep) }
    }
}

@Composable
fun JournalStep(onNext: () -> Unit, strings: AppStrings) {
    var text by remember { mutableStateOf("") }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(strings.journalTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = text, onValueChange = { text = it }, modifier = Modifier.fillMaxWidth().height(200.dp), placeholder = { Text(strings.journalHint) }, shape = RoundedCornerShape(16.dp))
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text(strings.nextStep) }
    }
}

@Composable
fun LastWaitStep(onNext: () -> Unit, strings: AppStrings) {
    var waitTimer by remember { mutableIntStateOf(60) }
    LaunchedEffect(Unit) {
        while (waitTimer > 0) {
            delay(1000)
            waitTimer--
        }
        onNext()
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(strings.stayWithUs, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(24.dp))
        CircularProgressIndicator(progress = waitTimer / 60f, modifier = Modifier.size(100.dp), strokeWidth = 8.dp)
        Spacer(modifier = Modifier.height(24.dp))
        Text("$waitTimer", style = MaterialTheme.typography.displayMedium)
    }
}

@Composable
fun CompleteStep(onFinish: (String) -> Unit, strings: AppStrings) {
    val options = listOf(strings.optionFullyGone, strings.optionDecreased, strings.optionSmallUrge, strings.optionSame, strings.optionIncreased)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(strings.sessionCompleteTitle, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = Color(0xFF16A34A))
        Spacer(modifier = Modifier.height(16.dp))
        Text(strings.proudMessage, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        Text(strings.howFeelingNow, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        options.forEach { option ->
            OutlinedButton(onClick = { onFinish(option) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(12.dp)) { Text(option) }
        }
    }
}
