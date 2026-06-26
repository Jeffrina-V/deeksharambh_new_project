package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainPortalApp(viewModel: AppViewModel) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (currentUser == null) {
            LoginScreen(
                onLogin = { username, password ->
                    viewModel.login(username, password) { success ->
                        if (success) {
                            Toast.makeText(context, "Logged in as ${username.uppercase()}!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                loginError = viewModel.loginError.collectAsStateWithLifecycle().value
            )
        } else {
            PortalDashboard(viewModel = viewModel)
        }
    }
}

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    loginError: String?
) {
    var username by remember { mutableStateOf("admin") }
    var password by remember { mutableStateOf("admin123") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F5132),
                        Color(0xFF198754),
                        Color(0xFF111C15)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .widthIn(max = 450.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // College Header Logo Mockup
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF0F5132)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "College Logo",
                        tint = Color(0xFFC59B27),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "SANKARA COLLEGE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F5132),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "Science and Commerce (Autonomous)",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Department of CSDA",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFFD700),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Deeksharambh Bridge Course",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Text(
                    text = "Management System",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("username_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0F5132),
                        focusedLabelColor = Color(0xFF0F5132)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("password_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0F5132),
                        focusedLabelColor = Color(0xFF0F5132)
                    )
                )

                if (loginError != null) {
                    Text(
                        text = loginError,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onLogin(username, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("login_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F5132)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Sign In",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Credentials Cheat-Sheet to facilitate testing
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
                    border = BorderStroke(1.dp, Color(0xFFFFF59D))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "💡 Quick Access Accounts:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFF5D4037)
                        )
                        Text("• Admin: admin / admin123 (Full control)", fontSize = 10.sp, color = Color.DarkGray)
                        Text("• Faculty: faculty / faculty123 (Attendance, Assessment, Gallery)", fontSize = 10.sp, color = Color.DarkGray)
                        Text("• Viewer: viewer / viewer123 (View archives)", fontSize = 10.sp, color = Color.DarkGray)
                    }
                }
            }
        }
    }
}

enum class NavigationItem(val title: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Default.Dashboard),
    NEW_BATCH("New Batch", Icons.Default.AddBox),
    SYLLABUS("Syllabus", Icons.Default.Book),
    SCHEDULE("Schedule", Icons.Default.CalendarToday),
    STUDENTS("Students", Icons.Default.People),
    ATTENDANCE("Attendance", Icons.Default.CheckCircle),
    ASSESSMENT("Assessment", Icons.Default.Quiz),
    RESULTS("Results Analysis", Icons.Default.BarChart),
    SIP_REPORT("SIP Report", Icons.Default.Description),
    PHOTO_GALLERY("Gallery", Icons.Default.PhotoLibrary),
    CHATBOT("AI Doubts Chat", Icons.Default.QuestionAnswer)
}

@Composable
fun PortalDashboard(viewModel: AppViewModel) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allBatches by viewModel.allBatches.collectAsStateWithLifecycle()
    val selectedBatchId by viewModel.selectedBatchId.collectAsStateWithLifecycle()
    val selectedBatch by viewModel.selectedBatch.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf(NavigationItem.DASHBOARD) }
    var expandedDrawer by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                "Sankara College",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                "Department of CSDA",
                                fontSize = 11.sp,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { expandedDrawer = !expandedDrawer }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                },
                actions = {
                    // Quick Batch Selector in Top Bar
                    if (allBatches.isNotEmpty()) {
                        var showDropdown by remember { mutableStateOf(false) }
                        Box {
                            TextButton(
                                onClick = { showDropdown = true },
                                colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = selectedBatch?.deeksharambhVersion?.let { "Deeksharambh $it" } ?: "Select Batch",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            }
                            DropdownMenu(
                                expanded = showDropdown,
                                onDismissRequest = { showDropdown = false }
                            ) {
                                allBatches.forEach { batch ->
                                    DropdownMenuItem(
                                        text = { Text("Deeksharambh ${batch.deeksharambhVersion} (${batch.batchYearRange})") },
                                        onClick = {
                                            viewModel.selectedBatchId.value = batch.id
                                            showDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // User Profile & Logout
                    IconButton(onClick = {
                        viewModel.logout()
                        Toast.makeText(context, "Logged out!", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F5132)
                )
            )
        }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Sidebar Navigation (Responsive Drawer representation)
            AnimatedVisibility(
                visible = expandedDrawer,
                enter = slideInHorizontally() + fadeIn(),
                exit = slideOutHorizontally() + fadeOut()
            ) {
                NavigationDrawerContent(
                    selectedItem = currentScreen,
                    userRole = currentUser?.role ?: "Viewer",
                    onItemSelected = {
                        currentScreen = it
                        expandedDrawer = false
                    },
                    modifier = Modifier
                        .width(260.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f))
                )
            }

            // Main Content Area with state transitions
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFAFAFA))
            ) {
                when (currentScreen) {
                    NavigationItem.DASHBOARD -> DashboardHomeScreen(viewModel, onNavigate = { currentScreen = it })
                    NavigationItem.NEW_BATCH -> {
                        if (currentUser?.role == "Admin") {
                            NewBatchSetupScreen(viewModel)
                        } else {
                            RoleRestrictionView()
                        }
                    }
                    NavigationItem.SYLLABUS -> SyllabusManagementScreen(viewModel)
                    NavigationItem.SCHEDULE -> ScheduleManagementScreen(viewModel)
                    NavigationItem.STUDENTS -> StudentMasterScreen(viewModel)
                    NavigationItem.ATTENDANCE -> AttendanceModuleScreen(viewModel)
                    NavigationItem.ASSESSMENT -> AssessmentModuleScreen(viewModel)
                    NavigationItem.RESULTS -> ResultAnalysisScreen(viewModel)
                    NavigationItem.SIP_REPORT -> SipReportGeneratorScreen(viewModel)
                    NavigationItem.PHOTO_GALLERY -> PhotoGalleryScreen(viewModel)
                    NavigationItem.CHATBOT -> ChatbotDoubtsScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun NavigationDrawerContent(
    selectedItem: NavigationItem,
    userRole: String,
    onItemSelected: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(12.dp)
    ) {
        // User Meta Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F5132).copy(alpha = 0.08f))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color(0xFF0F5132),
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = userRole.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F5132)
                    )
                    Text(
                        text = "Authorized Access",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Divider(color = Color.LightGray.copy(alpha = 0.5f), modifier = Modifier.padding(bottom = 8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(NavigationItem.values()) { item ->
                // Check permissions for setup screen
                val isEnabled = !(item == NavigationItem.NEW_BATCH && userRole != "Admin")

                if (isEnabled) {
                    val isSelected = selectedItem == item
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null, tint = if (isSelected) Color(0xFFC59B27) else Color.DarkGray) },
                        label = { Text(item.title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        selected = isSelected,
                        onClick = { onItemSelected(item) },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color(0xFF0F5132),
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.DarkGray,
                            unselectedIconColor = Color.DarkGray
                        ),
                        modifier = Modifier.height(48.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sankara CSDA Bridge Course Portal v1.0",
            fontSize = 9.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun RoleRestrictionView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Admin Role Required!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Red)
            Text("This action is restricted to administrators only.", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

// ---------------- DASHBOARD HOME ----------------
@Composable
fun DashboardHomeScreen(
    viewModel: AppViewModel,
    onNavigate: (NavigationItem) -> Unit
) {
    val selectedBatch by viewModel.selectedBatch.collectAsStateWithLifecycle()
    val students by viewModel.students.collectAsStateWithLifecycle()
    val attendanceList by viewModel.attendance.collectAsStateWithLifecycle()
    val resultsList by viewModel.results.collectAsStateWithLifecycle()

    val syncLoading by viewModel.syncLoading.collectAsStateWithLifecycle()
    val syncSuccess by viewModel.syncSuccess.collectAsStateWithLifecycle()
    val syncError by viewModel.syncError.collectAsStateWithLifecycle()
    var backendUrl by remember { mutableStateOf("http://10.0.2.2:5000") }

    // Compute stats
    val totalStudents = students.size
    val totalAttendanceRecords = attendanceList.size
    val presentCount = attendanceList.count { it.status == "P" }
    val attendancePercentage = if (totalAttendanceRecords > 0) {
        (presentCount.toDouble() / totalAttendanceRecords) * 100.0
    } else 0.0

    val submittedCount = resultsList.count { !it.isAbsent && (it.tamil != "AB" || it.english != "AB") }
    val advancedLearners = resultsList.count { !it.isAbsent && it.percentage >= 70.0 }
    val slowLearners = resultsList.count { !it.isAbsent && it.percentage < 70.0 }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Hero College Branding Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F5132)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "DEEKSHARAMBH BRIDGE COURSE PORTAL",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        "Sankara College of Science and Commerce (Autonomous)",
                        fontSize = 13.sp,
                        color = Color(0xFFC59B27)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Current Active Batch: ${selectedBatch?.className ?: "N/A"} (${selectedBatch?.academicYear ?: "N/A"}) - Version ${selectedBatch?.deeksharambhVersion ?: "N/A"}",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        item {
            // Official Invitation Circular Card for Deeksharambh 7.0
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF0)), // Light elegant cream tint
                border = BorderStroke(1.dp, Color(0xFFC59B27)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFF0F5132),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "OFFICIAL CIRCULAR",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text("Date: 26.06.2026", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "DEEKSHARAMBH 7.0",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F5132)
                    )
                    Text(
                        text = "Six Day Student Induction Programme & Bridge Course (AY 2026-2027)",
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "There will be a SIX DAY STUDENTS INDUCTION PROGRAMME of the department of Computer Science with Data Analytics for FIRST YEAR students. Students are requested to attend the programme from 26.06.2026 to 03.07.2026 without fail.",
                        fontSize = 12.sp,
                        color = Color(0xFF1B2E24),
                        lineHeight = 18.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Dr. R. Sasikala", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F5132))
                            Text("Head of Department", fontSize = 10.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Dr. V. Radhika", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F5132))
                            Text("Principal", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        item {
            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Total Students",
                    value = "$totalStudents",
                    icon = Icons.Default.People,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Avg Attendance",
                    value = String.format("%.1f%%", attendancePercentage),
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Advanced Learners",
                    value = "$advancedLearners",
                    icon = Icons.Default.Star,
                    color = Color(0xFFFF9800),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Slow Learners",
                    value = "$slowLearners",
                    icon = Icons.Default.MenuBook,
                    color = Color(0xFFE91E63),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            // Quick Actions Block
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Quick Workflows", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { onNavigate(NavigationItem.ATTENDANCE) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5132)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mark Attendance", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onNavigate(NavigationItem.RESULTS) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF198754)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Results", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item {
            // Batch Information Table Overview
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Active Program Insights", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    selectedBatch?.insights?.forEach { insight ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(insight, fontSize = 13.sp, color = Color.DarkGray)
                        }
                    }
                }
            }
        }

        item {
            // MongoDB Atlas Integration Overview
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudQueue,
                                contentDescription = "Cloud Status",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Cloud Sync & Database Status",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Database Type", fontSize = 11.sp, color = Color.Gray)
                            Text("MongoDB Atlas Cloud Database", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                        ) {
                            Text(
                                "CONFIGURED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Atlas Target Connection Endpoint:", fontSize = 11.sp, color = Color.Gray)
                    Text(
                        text = com.example.BuildConfig.MONGO_URI.take(45) + "...",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFECEFF1), RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Sync Server URL (Express Backend):", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = backendUrl,
                        onValueChange = { backendUrl = it },
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, fontFamily = FontFamily.Monospace),
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. http://10.0.2.2:5000") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = { viewModel.syncToCloud(backendUrl) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !syncLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (syncLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Synchronizing data...", fontSize = 13.sp)
                        } else {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = "Sync",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sync SQLite to MongoDB Atlas", fontSize = 13.sp)
                        }
                    }

                    if (syncSuccess != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE8F5E9), RoundedCornerShape(4.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = syncSuccess ?: "Sync Completed!",
                                fontSize = 11.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (syncError != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFEBEE), RoundedCornerShape(4.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = Color.Red,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = syncError ?: "Sync Failed!",
                                fontSize = 11.sp,
                                color = Color.Red,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Synchronizing backups your local Room SQLite database onto MongoDB Cloud securely via the REST backend server. Access your server dashboard to view collections.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontSize = 11.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            }
        }
    }
}

// ---------------- NEW BATCH SETUP ----------------
@Composable
fun NewBatchSetupScreen(viewModel: AppViewModel) {
    var range by remember { mutableStateOf("2026-2029") }
    var acYear by remember { mutableStateOf("2026-2027") }
    var ver by remember { mutableStateOf("7.0") }
    var start by remember { mutableStateOf("26.06.2026") }
    var end by remember { mutableStateOf("03.07.2026") }
    var hod by remember { mutableStateOf("Dr. R. Sasikala") }
    var principal by remember { mutableStateOf("Dr. V. Radhika") }
    var cls by remember { mutableStateOf("I B.Sc. CSDA") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Setup New Bridge Course Batch",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F5132)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = range, onValueChange = { range = it }, label = { Text("Batch Year Range (e.g. 2026-2029)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = acYear, onValueChange = { acYear = it }, label = { Text("Academic Year (e.g. 2026-2027)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ver, onValueChange = { ver = it }, label = { Text("Deeksharambh Version (e.g. 7.0)") }, modifier = Modifier.fillMaxWidth())
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("Start Date") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("End Date") }, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(value = hod, onValueChange = { hod = it }, label = { Text("HoD Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = principal, onValueChange = { principal = it }, label = { Text("Principal Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = cls, onValueChange = { cls = it }, label = { Text("Class Name (e.g. I B.Sc. CSDA)") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.createBatch(
                            batchYearRange = range,
                            academicYear = acYear,
                            deeksharambhVersion = ver,
                            startDate = start,
                            endDate = end,
                            hodName = hod,
                            principalName = principal,
                            className = cls,
                            tamilMax = 15,
                            englishMax = 15,
                            mathsMax = 15,
                            coreMax = 55,
                            totalMax = 100,
                            insights = listOf(
                                "Motivational Talks",
                                "Gender Sensitivity Programmes",
                                "Placement & Life Skill Orientation",
                                "Clubs & Committees Orientation",
                                "Physical Education",
                                "Fun Events"
                            )
                        )
                        Toast.makeText(context, "New Batch created successfully!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
                ) {
                    Text("Initialize Batch & Auto-Generate Documents")
                }
            }
        }
    }
}

// ---------------- SYLLABUS MANAGEMENT ----------------
@Composable
fun SyllabusManagementScreen(viewModel: AppViewModel) {
    val syllabiList by viewModel.syllabi.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Syllabus Management", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Subject")
            }
        }

        if (syllabiList.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No syllabus defined for this batch.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(syllabiList) { syllabus ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = syllabus.subjectName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF1A237E)
                                )
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD700).copy(alpha = 0.2f))
                                ) {
                                    Text(
                                        text = when(syllabus.mathsStream) {
                                            "M" -> "Maths Stream"
                                            "NM" -> "Non-Maths Stream"
                                            else -> "Common"
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF5D4037)
                                    )
                                }
                            }
                            Text(syllabus.departmentName, fontSize = 12.sp, color = Color.Gray)
                            Text("Hours: ${syllabus.hours}", fontSize = 12.sp, color = Color.Gray)

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Objectives:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            syllabus.objectives.forEach { obj ->
                                Text("• $obj", fontSize = 12.sp, color = Color.DarkGray)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Units Description:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            syllabus.units.forEach { unit ->
                                Text("${unit.unitNumber}: ${unit.description}", fontSize = 12.sp, color = Color.DarkGray)
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Faculty Incharge: ${syllabus.staffIncharge}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text("Subject Expert: ${syllabus.subjectExpertName} (${syllabus.subjectExpertDesignation}, ${syllabus.subjectExpertInstitution})", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var subName by remember { mutableStateOf("") }
        var deptName by remember { mutableStateOf("Department of Computer Science with Data Analytics") }
        var hours by remember { mutableStateOf("3") }
        var mathsStream by remember { mutableStateOf("All") }
        var objective by remember { mutableStateOf("") }
        var staffIncharge by remember { mutableStateOf("") }
        var expertName by remember { mutableStateOf("") }
        var expertDesig by remember { mutableStateOf("") }
        var expertInst by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Subject Syllabus") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = subName, onValueChange = { subName = it }, label = { Text("Subject Name") })
                    OutlinedTextField(value = deptName, onValueChange = { deptName = it }, label = { Text("Department") })
                    OutlinedTextField(value = hours, onValueChange = { hours = it }, label = { Text("Hours") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    
                    Text("Mathematics Stream separation:")
                    Row {
                        RadioButton(selected = mathsStream == "All", onClick = { mathsStream = "All" })
                        Text("Common", modifier = Modifier.align(Alignment.CenterVertically))
                        Spacer(modifier = Modifier.width(8.dp))
                        RadioButton(selected = mathsStream == "M", onClick = { mathsStream = "M" })
                        Text("Maths (M)", modifier = Modifier.align(Alignment.CenterVertically))
                        Spacer(modifier = Modifier.width(8.dp))
                        RadioButton(selected = mathsStream == "NM", onClick = { mathsStream = "NM" })
                        Text("Non-Maths (NM)", modifier = Modifier.align(Alignment.CenterVertically))
                    }

                    OutlinedTextField(value = objective, onValueChange = { objective = it }, label = { Text("Objectives") })
                    OutlinedTextField(value = staffIncharge, onValueChange = { staffIncharge = it }, label = { Text("Staff Incharge") })
                    OutlinedTextField(value = expertName, onValueChange = { expertName = it }, label = { Text("Expert Name") })
                    OutlinedTextField(value = expertDesig, onValueChange = { expertDesig = it }, label = { Text("Expert Designation") })
                    OutlinedTextField(value = expertInst, onValueChange = { expertInst = it }, label = { Text("Expert Institution") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addSyllabus(
                        subjectName = subName,
                        departmentName = deptName,
                        hours = hours.toIntOrNull() ?: 3,
                        stream = mathsStream,
                        objectives = listOf(objective),
                        units = listOf(
                            SyllabusUnit("Unit I", "Introduction and Basic Concepts"),
                            SyllabusUnit("Unit II", "Core fundamentals and Applications")
                        ),
                        referenceBooks = listOf("Standard Reference Book"),
                        staffIncharge = staffIncharge,
                        expertName = expertName,
                        expertDesignation = expertDesig,
                        expertInstitution = expertInst,
                        hodName = viewModel.selectedBatch.value?.hodName ?: ""
                    )
                    showAddDialog = false
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// ---------------- SCHEDULE MANAGEMENT ----------------
@Composable
fun ScheduleManagementScreen(viewModel: AppViewModel) {
    val scheduleList by viewModel.schedule.collectAsStateWithLifecycle()
    val abbreviations by viewModel.abbreviations.collectAsStateWithLifecycle()

    val totalHours = abbreviations.sumOf { it.noOfHours }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Schedule & Timetable Management", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))

        // running total verification header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (totalHours == 36) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Scheduled Hours: $totalHours / 36 Hours", fontWeight = FontWeight.Bold, color = if (totalHours == 36) Color(0xFF2E7D32) else Color(0xFFC62828))
                    if (totalHours != 36) {
                        Text("🚨 Alert: Total schedule must equal exactly 36 hours!", fontSize = 12.sp, color = Color(0xFFC62828))
                    } else {
                        Text("✅ Verified: Perfect 36-hour schedule compliance.", fontSize = 12.sp, color = Color(0xFF2E7D32))
                    }
                }
            }
        }

        // Timetable display
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Timetable (Day Orders)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
            }

            items(scheduleList) { slot ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Day Order: ${slot.dayOrder}", fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
                            Text("Date: ${slot.date}", color = Color.Gray, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PeriodCell("I", slot.period1, Modifier.weight(1f))
                            PeriodCell("II", slot.period2, Modifier.weight(1f))
                            PeriodCell("III", slot.period3, Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PeriodCell("IV", slot.period4, Modifier.weight(1f))
                            PeriodCell("V", slot.period5, Modifier.weight(1f))
                            PeriodCell("VI", slot.period6, Modifier.weight(1f))
                        }
                    }
                }
            }

            item {
                Text("Abbreviation Legend & Faculty Assignment", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray, modifier = Modifier.padding(top = 16.dp))
            }

            items(abbreviations) { abbr ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1A237E).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(abbr.abbreviation, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E), fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(abbr.particulars, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            Text("Faculty: ${abbr.facultyName}", fontSize = 11.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${abbr.noOfHours} Hrs", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1A237E))
                    }
                }
            }
        }
    }
}

@Composable
fun PeriodCell(period: String, subject: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("P $period", fontSize = 10.sp, color = Color.Gray)
            Text(subject.ifEmpty { "-" }, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

// ---------------- STUDENT MASTER ----------------
@Composable
fun StudentMasterScreen(viewModel: AppViewModel) {
    val studentList by viewModel.students.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(0) } // 0: All, 1: Maths, 2: Non-Maths
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredStudents = remember(studentList, selectedTab) {
        when(selectedTab) {
            1 -> studentList.filter { it.mathsStream == "M" }
            2 -> studentList.filter { it.mathsStream == "NM" }
            else -> studentList
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Student Master", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Student")
            }
        }

        // Tab Selector for Maths/Non-Maths streams
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("All (${studentList.size})") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Maths (${studentList.count { it.mathsStream == "M" }})") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Non-Maths (${studentList.count { it.mathsStream == "NM" }})") })
        }

        if (filteredStudents.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No students added yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredStudents) { student ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0xFF1A237E)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${student.sNo}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(student.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    Text(
                                        text = if (student.mathsStream == "M") "Maths Stream" else "Non-Maths Stream (NM Marking)",
                                        fontSize = 11.sp,
                                        color = if (student.mathsStream == "M") Color.DarkGray else Color.Red
                                    )
                                }
                            }

                            IconButton(onClick = { viewModel.deleteStudent(student) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        var isMaths by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Student") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Student Name") }, modifier = Modifier.fillMaxWidth())
                    Text("Mathematics Stream Status:")
                    Row {
                        RadioButton(selected = isMaths, onClick = { isMaths = true })
                        Text("Maths (M)", modifier = Modifier.align(Alignment.CenterVertically))
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(selected = !isMaths, onClick = { isMaths = false })
                        Text("Non-Maths (NM)", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (name.isNotEmpty()) {
                        viewModel.addStudent(name, if (isMaths) "M" else "NM")
                        showAddDialog = false
                    }
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// ---------------- ATTENDANCE MODULE ----------------
@Composable
fun AttendanceModuleScreen(viewModel: AppViewModel) {
    val selectedBatch by viewModel.selectedBatch.collectAsStateWithLifecycle()
    val students by viewModel.students.collectAsStateWithLifecycle()
    val attendanceList by viewModel.attendance.collectAsStateWithLifecycle()

    var selectedDate by remember { mutableStateOf("") }

    // Preload dates based on batch range (or use default ones)
    val dates = remember(selectedBatch) {
        if (selectedBatch?.academicYear == "2024-2025") {
            listOf("02.07.2024", "03.07.2024", "04.07.2024", "05.07.2024", "08.07.2024", "09.07.2024")
        } else {
            listOf("26.06.2025", "27.06.2025", "30.06.2025", "01.07.2025", "02.07.2025", "03.07.2025")
        }
    }

    LaunchedEffect(dates) {
        if (selectedDate.isEmpty() && dates.isNotEmpty()) {
            selectedDate = dates.first()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Daily Attendance Marking", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))

        // Date Selector Row
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dates.forEach { date ->
                FilterChip(
                    selected = selectedDate == date,
                    onClick = { selectedDate = date },
                    label = { Text(date) }
                )
            }
        }

        if (students.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Please add students in Student Master first.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(students) { student ->
                    val record = attendanceList.find { it.studentId == student.id && it.date == selectedDate }
                    val status = record?.status ?: "P" // default present

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(student.name, fontWeight = FontWeight.SemiBold)

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { viewModel.saveAttendance(student.id, selectedDate, "P") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (status == "P") Color(0xFF198754) else Color(0xFFE2E8F0)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("P", color = if (status == "P") Color.White else Color.DarkGray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                Button(
                                    onClick = { viewModel.saveAttendance(student.id, selectedDate, "A") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (status == "A") Color(0xFFDC3545) else Color(0xFFE2E8F0)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("A", color = if (status == "A") Color.White else Color.DarkGray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                Button(
                                    onClick = { viewModel.saveAttendance(student.id, selectedDate, "OD") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (status == "OD") Color(0xFF0D6EFD) else Color(0xFFE2E8F0)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text("OD", color = if (status == "OD") Color.White else Color.DarkGray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- ASSESSMENT MODULE ----------------
@Composable
fun AssessmentModuleScreen(viewModel: AppViewModel) {
    val students by viewModel.students.collectAsStateWithLifecycle()
    val questionsList by viewModel.questions.collectAsStateWithLifecycle()

    var quizMode by remember { mutableStateOf(false) }
    var selectedStudent by remember { mutableStateOf<Student?>(null) }
    var responsesMap by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    var viewOneByOne by remember { mutableStateOf(false) }
    var currentQuestionIdx by remember { mutableStateOf(0) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!quizMode) {
            Text("Student Bridge Course Assessment", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Select Student to start test:", fontWeight = FontWeight.Bold, fontSize = 14.sp)

                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                            Text(selectedStudent?.name ?: "Select Name from Dropdown")
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            students.forEach { s ->
                                DropdownMenuItem(
                                    text = { Text(s.name) },
                                    onClick = {
                                        selectedStudent = s
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Question Display Mode:")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("All at Once", fontSize = 12.sp, color = if (!viewOneByOne) Color(0xFF1A237E) else Color.Gray)
                            Switch(checked = viewOneByOne, onCheckedChange = { viewOneByOne = it })
                            Text("One by One", fontSize = 12.sp, color = if (viewOneByOne) Color(0xFF1A237E) else Color.Gray)
                        }
                    }

                    Button(
                        onClick = {
                            if (selectedStudent != null && questionsList.isNotEmpty()) {
                                quizMode = true
                                responsesMap = emptyMap()
                                currentQuestionIdx = 0
                            } else {
                                Toast.makeText(context, "Please select student and ensure questions exist!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
                    ) {
                        Text("Launch Google Form Style Assessment")
                    }
                }
            }

            // Tamil input helper section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("📝 Faculty Note for Tamil Questions:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF7F5F00))
                    Text("Tamil font questions must be typed manually in Tamil Unicode.", fontSize = 11.sp, color = Color.DarkGray)
                }
            }
        } else {
            // Assessment Form Mode
            val currentStudent = selectedStudent ?: return
            Text(
                text = "Taking Quiz: ${currentStudent.name} (${if (currentStudent.mathsStream == "M") "Maths Stream" else "Non-Maths"})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E)
            )

            // Filter questions based on student stream
            val filteredQs = remember(questionsList, currentStudent) {
                questionsList.filter { it.mathsStream == "All" || it.mathsStream == currentStudent.mathsStream }
            }

            if (filteredQs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No questions found for this stream.")
                }
            } else {
                if (viewOneByOne) {
                    // One by one mode
                    val q = filteredQs[currentQuestionIdx]
                    Card(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Question ${currentQuestionIdx + 1} of ${filteredQs.size}", color = Color.Gray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(q.questionText, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(16.dp))
                            listOf(
                                "A" to q.optionA,
                                "B" to q.optionB,
                                "C" to q.optionC,
                                "D" to q.optionD
                            ).forEach { (opt, text) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { responsesMap = responsesMap + (q.id to opt) }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = responsesMap[q.id] == opt, onClick = { responsesMap = responsesMap + (q.id to opt) })
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("$opt. $text", fontSize = 14.sp)
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { if (currentQuestionIdx > 0) currentQuestionIdx-- },
                            enabled = currentQuestionIdx > 0
                        ) {
                            Text("Previous")
                        }

                        if (currentQuestionIdx < filteredQs.size - 1) {
                            Button(onClick = { currentQuestionIdx++ }) {
                                Text("Next")
                            }
                        } else {
                            Button(
                                onClick = {
                                    viewModel.submitAssessment(currentStudent, responsesMap)
                                    Toast.makeText(context, "Assessment submitted successfully!", Toast.LENGTH_LONG).show()
                                    quizMode = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Text("Submit")
                            }
                        }
                    }
                } else {
                    // All at once mode
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredQs.size) { idx ->
                            val q = filteredQs[idx]
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Question ${idx + 1}: ${q.questionText}", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    listOf(
                                        "A" to q.optionA,
                                        "B" to q.optionB,
                                        "C" to q.optionC,
                                        "D" to q.optionD
                                    ).forEach { (opt, text) ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { responsesMap = responsesMap + (q.id to opt) }
                                                .padding(vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(selected = responsesMap[q.id] == opt, onClick = { responsesMap = responsesMap + (q.id to opt) })
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("$opt. $text")
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = {
                                    viewModel.submitAssessment(currentStudent, responsesMap)
                                    Toast.makeText(context, "Assessment Submitted!", Toast.LENGTH_SHORT).show()
                                    quizMode = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Text("Submit All Answers")
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- RESULTS ANALYSIS & CHARTS ----------------
@Composable
fun ResultAnalysisScreen(viewModel: AppViewModel) {
    val resultsList by viewModel.results.collectAsStateWithLifecycle()
    val students by viewModel.students.collectAsStateWithLifecycle()
    val batch by viewModel.selectedBatch.collectAsStateWithLifecycle()

    var sortByPercentage by remember { mutableStateOf(false) }

    val sortedResults = remember(resultsList, students, sortByPercentage) {
        val mapped = resultsList.map { res ->
            val name = students.find { it.id == res.studentId }?.name ?: "Unknown"
            res to name
        }
        if (sortByPercentage) {
            mapped.sortedByDescending { it.first.percentage }
        } else {
            mapped.sortedBy { it.first.studentId }
        }
    }

    // Ranges Summary stats
    val totalCount = resultsList.count { !it.isAbsent }
    val above60 = resultsList.count { !it.isAbsent && it.percentage >= 60.0 }
    val between50_59 = resultsList.count { !it.isAbsent && it.percentage in 50.0..59.9 }
    val below50 = resultsList.count { !it.isAbsent && it.percentage < 50.0 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Result Analysis", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Sort by %", fontSize = 12.sp)
                Checkbox(checked = sortByPercentage, onCheckedChange = { sortByPercentage = it })
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Custom drawn bar chart displaying series: count and percent
                Card(
                    modifier = Modifier.fillMaxWidth().height(260.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Score Range Analytics", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Custom Canvas Bar Chart Drawing
                        Canvas(modifier = Modifier.fillMaxSize().weight(1f)) {
                            val maxVal = maxOf(totalCount.toFloat(), 100f)
                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            
                            val ranges = listOf(
                                Triple("60 & Above", above60, if (totalCount > 0) (above60.toFloat() / totalCount) * 100f else 0f),
                                Triple("50 - 59", between50_59, if (totalCount > 0) (between50_59.toFloat() / totalCount) * 100f else 0f),
                                Triple("Below 50", below50, if (totalCount > 0) (below50.toFloat() / totalCount) * 100f else 0f)
                            )
                            
                            val spacing = canvasWidth / 4f
                            
                            // Draw Grid Lines
                            for (i in 1..4) {
                                val y = canvasHeight - (canvasHeight * (i / 4f))
                                drawLine(
                                    color = Color.LightGray.copy(alpha = 0.5f),
                                    start = Offset(0f, y),
                                    end = Offset(canvasWidth, y),
                                    strokeWidth = 1f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                )
                            }
                            
                            ranges.forEachIndexed { idx, (label, count, pct) ->
                                val x = (idx + 1) * spacing - 40f
                                
                                // Count Bar (Navy)
                                val countBarHeight = (count.toFloat() / maxVal) * canvasHeight
                                drawRoundRect(
                                    color = Color(0xFF1A237E),
                                    topLeft = Offset(x, canvasHeight - countBarHeight),
                                    size = Size(24f, countBarHeight),
                                    cornerRadius = CornerRadius(4f, 4f)
                                )
                                
                                // Percentage Bar (Gold)
                                val pctBarHeight = (pct / maxVal) * canvasHeight
                                drawRoundRect(
                                    color = Color(0xFFFFD700),
                                    topLeft = Offset(x + 28f, canvasHeight - pctBarHeight),
                                    size = Size(24f, pctBarHeight),
                                    cornerRadius = CornerRadius(4f, 4f)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(12.dp).background(Color(0xFF1A237E)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Students Count", fontSize = 10.sp)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(12.dp).background(Color(0xFFFFD700)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Percentage (%)", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            item {
                // Table Footer Disclaimer
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Text(
                        text = "💡 THOSE WHO GOT 70 AND ABOVE ARE THE ADVANCED LEARNERS AND OTHERS ARE SLOW LEARNERS.",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            items(sortedResults) { (res, name) ->
                val highlightColor = when {
                    res.percentage >= 70.0 -> Color(0xFFE8F5E9) // Green
                    res.percentage >= 50.0 -> Color(0xFFFFFDE7) // Yellow
                    else -> Color(0xFFFFEBEE) // Red
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = highlightColor)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(name, fontWeight = FontWeight.Bold)
                            Text("Total: ${res.total} / ${batch?.totalMax ?: 100}", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Tamil: ${res.tamil}", fontSize = 11.sp, modifier = Modifier.weight(1f))
                            Text("English: ${res.english}", fontSize = 11.sp, modifier = Modifier.weight(1f))
                            Text("Maths: ${res.maths}", fontSize = 11.sp, modifier = Modifier.weight(1f))
                            Text("Core: ${res.core}", fontSize = 11.sp, modifier = Modifier.weight(1f))
                            Text("Percent: ${String.format("%.1f%%", res.percentage)}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

// ---------------- SIP REPORT GENERATOR ----------------
@Composable
fun SipReportGeneratorScreen(viewModel: AppViewModel) {
    val sipReport by viewModel.sipReport.collectAsStateWithLifecycle()
    val batch by viewModel.selectedBatch.collectAsStateWithLifecycle()

    var narrativeText by remember { mutableStateOf("") }

    LaunchedEffect(sipReport) {
        sipReport?.let {
            narrativeText = it.narrative
        }
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("SIP Narrative Report Generator", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "SANKARA COLLEGE OF SCIENCE AND COMMERCE (Autonomous)\nDepartment of CSDA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Report on Deeksharambh ${batch?.deeksharambhVersion ?: "5.0"}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = narrativeText,
                    onValueChange = { narrativeText = it },
                    label = { Text("Standard Narrative Paragraph") },
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    maxLines = 10
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.updateSipReport(narrativeText, sipReport?.objectives ?: emptyList())
                        Toast.makeText(context, "SIP Report Saved Successfully!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
                ) {
                    Text("Save & Update Report")
                }
            }
        }
    }
}

// ---------------- PHOTO GALLERY MODULE ----------------
@Composable
fun PhotoGalleryScreen(viewModel: AppViewModel) {
    val photosList by viewModel.photos.collectAsStateWithLifecycle()
    var showUploadDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Fleeting Views / Photo Gallery", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
            Button(
                onClick = { showUploadDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Photo")
            }
        }

        if (photosList.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No photos added in this batch.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(photosList) { photo ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column {
                            // Mocking GPS Timestamp Overlay Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.DarkGray, Color.Black)
                                        )
                                    ),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                // Camera icon in background
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.1f),
                                    modifier = Modifier.fillMaxSize().padding(32.dp)
                                )

                                // GPS Camera Style overlay text
                                Card(
                                    modifier = Modifier.padding(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f)),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = photo.gpsOverlayText,
                                        color = Color.Green,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(photo.caption, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Date captured: ${photo.photoDate}", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showUploadDialog) {
        var caption by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("26.06.2025") }
        var gpsText by remember { mutableStateOf("Saravanampatty, Coimbatore, Tamil Nadu, India\nLat 11.085°, Long 76.984°\n26/06/25 09:51 AM GMT +05:30") }

        AlertDialog(
            onDismissRequest = { showUploadDialog = false },
            title = { Text("Add Photo with GPS Stamp") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = caption, onValueChange = { caption = it }, label = { Text("Caption") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Capture Date") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(
                        value = gpsText,
                        onValueChange = { gpsText = it },
                        label = { Text("GPS Overlay Text (Simulate GPS Camera)") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (caption.isNotEmpty()) {
                        viewModel.addPhoto(caption, date, gpsText)
                        showUploadDialog = false
                    }
                }) {
                    Text("Add to Gallery")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ChatbotDoubtsScreen(viewModel: AppViewModel) {
    val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isLoading by viewModel.chatLoading.collectAsStateWithLifecycle()

    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Chat Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F5132))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🏫 AI Academic Doubts Chatbot",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Sankara CSDA Department - Ask any question about Tamil, English, Maths, AI or Computer Core syllabus!",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.sp
                )
            }
        }

        // Message Feed Area
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                val bubbleColor = if (msg.isUser) Color(0xFFE2E8F0) else Color(0xFFE8F5E9)
                val textColor = if (msg.isUser) Color.Black else Color(0xFF0F5132)
                val alignment = if (msg.isUser) Alignment.End else Alignment.Start

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = alignment
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = if (msg.isUser) 12.dp else 0.dp,
                            bottomEnd = if (msg.isUser) 0.dp else 12.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = bubbleColor),
                        modifier = Modifier.widthIn(max = 300.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.text,
                                color = textColor,
                                fontSize = 14.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                    Text(
                        text = if (msg.isUser) "Student" else "AI CSDA Tutor",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color(0xFF0F5132),
                            strokeWidth = 2.dp
                        )
                        Text("AI Tutor is formulating response...", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }

        // Chat Input Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Ask your doubt here...") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0F5132),
                    focusedLabelColor = Color(0xFF0F5132)
                )
            )

            IconButton(
                onClick = {
                    if (textInput.isNotBlank()) {
                        viewModel.sendChatMessage(textInput)
                        textInput = ""
                    }
                },
                modifier = Modifier
                    .background(Color(0xFF0F5132), shape = CircleShape)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}
