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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
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
        days >= 1460 -> Milestone("Master of Recovery", Icons.Default.AutoAwesome, Color(0xFF00E5FF), 1460, "“আল্লাহর কাছে সবচেয়ে প্রিয় আমল হলো যা নিয়মিত করা হয়, যদিও তা অল্প হয়।” (বুখারী)")
        days >= 730 -> Milestone("দৃঢ় ব্যক্তিত্ব", Icons.Default.EmojiEvents, Color(0xFFFFD700), 730, "“প্রকৃত বীর সে নয় যে কুস্তিতে অন্যকে হারিয়ে দেয়, বরং সেই প্রকৃত বীর যে রাগের সময় নিজেকে নিয়ন্ত্রণ করে।” (মুসলিম)")
        days >= 365 -> Milestone("নতুন জীবন", Icons.Default.Park, Color(0xFF4CAF50), 365, "“যে ব্যক্তি আল্লাহকে ভয় করবে, আল্লাহ তার জন্য বের হওয়ার পথ করে দিবেন।” (সূরা তালাক: ২)")
        days >= 180 -> Milestone("অটল যোদ্ধা", Icons.Default.Terrain, Color(0xFF795548), 180, "“নিশ্চয়ই কষ্টের সাথেই স্বস্তি রয়েছে।” (সূরা ইনশিরাহ: ৬)")
        days >= 90 -> Milestone("Recovery Champion", Icons.Default.MilitaryTech, Color(0xFFFF9800), 90, "“নিশ্চয়ই সফল হয়েছে সেই, যে নিজের আত্মাকে শুদ্ধ করেছে।” (সূরা আশ-শামস: ৯)")
        days >= 60 -> Milestone("শক্তিশালী মন", Icons.Default.FitnessCenter, Color(0xFF607D8B), 60, "“আল্লাহর সাহায্য ধৈর্য ও সালাতের মাধ্যমে অন্বেষণ করো।” (সূরা বাকারা: ৪৫)")
        days >= 30 -> Milestone("অভ্যাস ভাঙার শুরু", Icons.Default.Spa, Color(0xFF8BC34A), 30, "“মুমিনের প্রতিটি কাজই বিস্ময়কর; তার জন্য তার সবকিছুতেই কল্যাণ রয়েছে।” (মুসলিম)")
        days >= 14 -> Milestone("আত্মনিয়ন্ত্রণ", Icons.Default.Shield, Color(0xFF2196F3), 14, "“যে ব্যক্তি তার লজ্জাস্থান ও জিহ্বার হেফাজত করবে, আমি তার জান্নাতের দায়িত্ব নিব।” (বুখারী)")
        days >= 7 -> Milestone("দৃঢ় সংকল্প", Icons.Default.Shield, Color(0xFFFFEB3B), 7, "“তোমরা ধৈর্য ও সালাতের মাধ্যমে সাহায্য প্রার্থনা করো।”")
        days >= 3 -> Milestone("প্রথম জয়", Icons.Default.Star, Color(0xFFFFC107), 3, "“আল্লাহর পথে এক সকাল বা এক সন্ধ্যা ব্যয় করা দুনিয়া ও তার মধ্যকার সবকিছুর চেয়ে উত্তম।”")
        days >= 1 -> Milestone("নতুন শুরু", Icons.Default.Shield, Color(0xFFCDDC39), 1, "“প্রতিটি আমল নিয়তের ওপর নির্ভরশীল।”")
        else -> Milestone("সূচনা", Icons.Default.Shield, Color.Gray, 0)
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
}

enum class Screen {
    Dashboard, AddHabit, Settings, CheckInHistory, UrgeEmergency
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

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "ScreenTransition"
    ) { screen ->
        when (screen) {
            Screen.Dashboard -> DashboardScreen(
                habits = habits,
                onAddHabitClick = { currentScreen = Screen.AddHabit },
                onSettingsClick = { currentScreen = Screen.Settings },
                onHistoryClick = { currentScreen = Screen.CheckInHistory },
                onUrgeClick = { currentScreen = Screen.UrgeEmergency },
                onRelapse = { viewModel.relapse(it) },
                onCheckIn = { viewModel.checkIn(it) },
                onDelete = { viewModel.deleteHabit(it) },
                isCheckInDone = viewModel.isTodayCheckInDone.value,
                onPerformCheckIn = { viewModel.performDailyCheckIn(it) },
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
                onBack = { currentScreen = Screen.Dashboard },
                strings = strings
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    habits: List<RecoveryHabit>,
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

            if (!isCheckInDone) {
                item {
                    DailyCheckInCard(onCheckIn = onPerformCheckIn, strings = strings)
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
                RecoveryJourneyCard(
                    habit = habit,
                    onRelapse = { habitToRelapse = habit },
                    onCheckIn = { onCheckIn(habit) },
                    onDelete = { onDelete(habit) },
                    strings = strings
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
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
                    transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(500)) },
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
                                    modifier = Modifier.fillMaxWidth()
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
                                    modifier = Modifier.fillMaxWidth()
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
                                    modifier = Modifier.fillMaxWidth()
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
fun RecoveryJourneyCard(habit: RecoveryHabit, onRelapse: () -> Unit, onCheckIn: () -> Unit, onDelete: () -> Unit, strings: AppStrings) {
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
                    IconButton(onClick = onCheckIn) { Icon(Icons.Default.Check, contentDescription = "Check-in", tint = habit.color) }
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
        "ভালো", "Good" -> "😊"
        "মোটামুটি", "Neutral" -> "😐"
        "খারাপ", "Bad" -> "😞"
        "Urge" -> "😖"
        "Relapse" -> "😔"
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrgeEmergencyScreen(onBack: () -> Unit, strings: AppStrings) {
    var timerSeconds by remember { mutableIntStateOf(60) }
    var breathingPhase by remember { mutableStateOf(strings.breatheIn) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        while (timerSeconds > 0) {
            // Breathing animation logic (approx 4s in, 4s hold, 4s out)
            val phase = (60 - timerSeconds) % 12
            when {
                phase < 4 -> {
                    breathingPhase = strings.breatheIn
                    scale.animateTo(1.5f, tween(4000))
                }
                phase < 8 -> {
                    breathingPhase = strings.hold
                    delay(4000)
                }
                else -> {
                    breathingPhase = strings.breatheOut
                    scale.animateTo(1f, tween(4000))
                }
            }
            delay(1000)
            timerSeconds--
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.emergency, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(strings.urgeTitle, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            }

            item {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                    Box(modifier = Modifier.size(100.dp).scale(scale.value).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(breathingPhase, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text("$timerSeconds", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black)
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(strings.urgeMotivation, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(strings.urgeDua, style = MaterialTheme.typography.bodyMedium, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    UrgeActionCard(modifier = Modifier.weight(1f), icon = Icons.Default.LocalDrink, label = strings.waterReminder)
                    UrgeActionCard(modifier = Modifier.weight(1f), icon = Icons.Default.DirectionsWalk, label = strings.walkReminder)
                }
            }

            item {
                UrgeActionCard(modifier = Modifier.fillMaxWidth(), icon = Icons.Default.EditNote, label = strings.journalReminder)
            }

            item {
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)) {
                    Text(strings.stayStrong, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun UrgeActionCard(modifier: Modifier, icon: ImageVector, label: String) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
        }
    }
}
