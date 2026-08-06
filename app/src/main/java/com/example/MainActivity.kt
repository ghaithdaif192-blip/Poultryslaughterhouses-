package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ColdStorageLocation
import com.example.data.model.PoultryBatch
import com.example.data.model.ProductionLine
import com.example.data.model.QCEntry
import com.example.ui.viewmodel.ApiStatus
import com.example.ui.viewmodel.GreetingViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private val viewModel: GreetingViewModel by viewModels {
        GreetingViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            
            val colors = if (isDarkMode) {
                IndustrialColors(
                    background = Color(0xFF080F1D),
                    surface = Color(0xFF111C2E),
                    surfaceVariant = Color(0xFF1B2A47),
                    primary = Color(0xFF00E5FF),
                    onPrimary = Color(0xFF00363A),
                    secondary = Color(0xFFFFB300),
                    onSecondary = Color(0xFF3E2723),
                    onBackground = Color(0xFFE2F1FF),
                    onSurface = Color(0xFFD4E5FA),
                    border = Color(0xFF223556),
                    success = Color(0xFF00E676),
                    error = Color(0xFFFF5252),
                    info = Color(0xFF29B6F6)
                )
            } else {
                IndustrialColors(
                    background = Color(0xFFF3F6FA),
                    surface = Color(0xFFFFFFFF),
                    surfaceVariant = Color(0xFFE8EFF6),
                    primary = Color(0xFF1E40AF),
                    onPrimary = Color(0xFFFFFFFF),
                    secondary = Color(0xFF10B981),
                    onSecondary = Color(0xFFFFFFFF),
                    onBackground = Color(0xFF1F2937),
                    onSurface = Color(0xFF374151),
                    border = Color(0xFFD1D5DB),
                    success = Color(0xFF059669),
                    error = Color(0xFFDC2626),
                    info = Color(0xFF2563EB)
                )
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colors.background
            ) {
                PoultryProAppContent(viewModel = viewModel, colors = colors)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

data class IndustrialColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val border: Color,
    val success: Color,
    val error: Color,
    val info: Color
)

object TranslationHelper {
    private val en = mapOf(
        "app_title" to "PoultryPro ERP",
        "app_subtitle" to "Smart Slaughterhouse & Cold Chain Management System",
        "login_title" to "Enterprise Security Portal",
        "login_btn" to "Sign In to Terminal",
        "username" to "Username",
        "password" to "Password",
        "lang_switch" to "العربية",
        "theme_toggle" to "Toggle Theme",
        "active_user" to "Duty Supervisor",
        "system_status" to "System Online",
        "tfa_code" to "2FA Verification Code",
        "remember_me" to "Remember Terminal Session",
        "tab_dashboard" to "Dashboard",
        "tab_reception" to "Poultry Reception",
        "tab_tracker" to "Batch Tracker",
        "tab_production" to "Production Lines",
        "tab_qc" to "Quality Control",
        "tab_inventory" to "Cold Storage Map",
        "tab_ai" to "AI Advisor Reports",
        
        "kpi_received" to "Received Today",
        "kpi_avg_temp" to "Avg Cold Room Temp",
        "kpi_active_lines" to "Active Lines",
        "kpi_rejected" to "QC Rejection Rate",
        "live_factory_map" to "Live Factory Workspace Canvas",
        "conveyor_belt" to "Active Evisceration Belt",
        
        "reception_form" to "Chamber Reception Intake Form",
        "supplier" to "Supplier Name",
        "truck_no" to "Truck License Number",
        "driver" to "Driver Full Name",
        "cages" to "Cages Count",
        "birds" to "Birds Count",
        "weight" to "Net Weight (kg)",
        "save_reception" to "Register Shipment & Print QR Batch Card",
        "batch_card" to "Generated QR Batch Tracking Tag",
        "status" to "Initial Inspection Status",
        "passed" to "Passed (HACCP Compliant)",
        "rejected" to "Rejected",
        
        "batch_tracker" to "HACCP Live Batch Timeline Tracker",
        "advance_stage" to "Advance to Next Process",
        "batch_id" to "Batch ID",
        
        "qc_form" to "HACCP Thermal & Water Inspection Form",
        "select_batch" to "Select Active Processing Batch",
        "core_temp" to "Core Temp (°C)",
        "ph_level" to "Water pH Quality",
        "reject_count" to "Condemnation Count (Birds)",
        "reject_reason" to "Primary Condemnation Reason",
        "inspector" to "Inspector Signature Name",
        "save_qc" to "Commit Inspection Log",
        "qc_history" to "Inspections Audit Log",
        
        "cold_storage_map" to "Cold Storage Interactive Capacity Grid",
        "room_temp" to "Room Temp",
        "capacity" to "Capacity Used",
        "add_stock" to "+ Carton",
        "remove_stock" to "- Carton",
        
        "ai_analysis" to "Gemini Operational Optimization Engine",
        "generate_report" to "Generate Live AI Analytical Report",
        "running_ai" to "Querying Gemini API Models..."
    )

    private val ar = mapOf(
        "app_title" to "نظام مسالخ دواجن برو",
        "app_subtitle" to "نظام إدارة المسالخ الذكية وسلسلة التبريد والتجميد",
        "login_title" to "بوابة الأمن والتشغيل للمؤسسة",
        "login_btn" to "تسجيل الدخول للنظام",
        "username" to "اسم المستخدم",
        "password" to "كلمة المرور",
        "lang_switch" to "English",
        "theme_toggle" to "تغيير المظهر",
        "active_user" to "مشرف الوردية",
        "system_status" to "النظام متصل ونشط",
        "tfa_code" to "رمز التحقق الثنائي (2FA)",
        "remember_me" to "حفظ جلسة العمل الحالية",
        "tab_dashboard" to "لوحة التحكم",
        "tab_reception" to "استقبال الدجاج",
        "tab_tracker" to "تتبع الدفعات",
        "tab_production" to "خطوط الإنتاج",
        "tab_qc" to "رقابة الجودة HACCP",
        "tab_inventory" to "مخطط المخازن والبرادات",
        "tab_ai" to "تحليل الأداء بالذكاء الاصطناعي",

        "kpi_received" to "المستلم اليوم",
        "kpi_avg_temp" to "متوسط حرارة الغرف",
        "kpi_active_lines" to "الخطوط النشطة",
        "kpi_rejected" to "معدل استبعاد الجودة",
        "live_factory_map" to "المخطط المباشر لحركة المصنع",
        "conveyor_belt" to "سير النقل والتنظيف النشط",

        "reception_form" to "نموذج استقبال وتسجيل الشحنات الجديد",
        "supplier" to "اسم المورد",
        "truck_no" to "رقم لوحة الشاحنة",
        "driver" to "اسم السائق الكامل",
        "cages" to "عدد الأقفاص",
        "birds" to "عدد الطيور",
        "weight" to "الوزن الصافي (كجم)",
        "save_reception" to "حفظ الشحنة وطباعة بطاقة الدفعة QR",
        "batch_card" to "بطاقة تتبع الدفعة المنتجة (QR)",
        "status" to "حالة الفحص الأولي",
        "passed" to "سليم ومطابق لمواصفات الجودة",
        "rejected" to "مرفوض ومستبعد",

        "batch_tracker" to "لوحة التتبع الحي لدفعات الدواجن",
        "advance_stage" to "ترقية الدفعة للمرحلة التالية",
        "batch_id" to "دفعة رقم",

        "qc_form" to "نموذج الفحص الحراري والكيميائي (HACCP)",
        "select_batch" to "اختر الدفعة النشطة للفحص",
        "core_temp" to "درجة حرارة اللحم الداخلية (°م)",
        "ph_level" to "مستوى حموضة الماء (pH)",
        "reject_count" to "عدد الطيور المستبعدة (النافقة/التالفة)",
        "reject_reason" to "سبب الاستبعاد الرئيسي",
        "inspector" to "اسم وتوقيع فاحص الجودة",
        "save_qc" to "تسجيل تقرير فحص الجودة",
        "qc_history" to "سجل عمليات التدقيق والجودة",

        "cold_storage_map" to "المخطط التفاعلي لمستودعات التبريد والتجميد",
        "room_temp" to "درجة الحرارة",
        "capacity" to "السعة المستخدمة",
        "add_stock" to "+ كرتون",
        "remove_stock" to "- كرتون",

        "ai_analysis" to "مستشار التشغيل المدعوم بالذكاء الاصطناعي (Gemini)",
        "generate_report" to "تحليل أداء المصنع بالذكاء الاصطناعي",
        "running_ai" to "جاري تحليل البيانات عبر نماذج Gemini..."
    )

    fun translate(key: String, isArabic: Boolean): String {
        return if (isArabic) ar[key] ?: key else en[key] ?: key
    }
}

@Composable
fun PoultryProAppContent(viewModel: GreetingViewModel, colors: IndustrialColors) {
    val isArabic by viewModel.isArabic.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    
    var isLoggedIn by remember { mutableStateOf(false) }
    var loginUsername by remember { mutableStateOf("Admin-HACCP") }
    var loginPassword by remember { mutableStateOf("••••••••") }
    var login2FA by remember { mutableStateOf("109432") }
    var rememberSession by remember { mutableStateOf(true) }

    if (!isLoggedIn) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(colors.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = TranslationHelper.translate("app_title", isArabic),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = colors.primary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = TranslationHelper.translate("login_title", isArabic),
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.onSurface.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = loginUsername,
                        onValueChange = { loginUsername = it },
                        label = { Text(TranslationHelper.translate("username", isArabic)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.border,
                            focusedLabelColor = colors.primary
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = loginPassword,
                        onValueChange = { loginPassword = it },
                        label = { Text(TranslationHelper.translate("password", isArabic)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.border,
                            focusedLabelColor = colors.primary
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = login2FA,
                        onValueChange = { login2FA = it },
                        label = { Text(TranslationHelper.translate("tfa_code", isArabic)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.border,
                            focusedLabelColor = colors.primary
                        ),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberSession,
                                onCheckedChange = { rememberSession = it },
                                colors = CheckboxDefaults.colors(checkedColor = colors.primary)
                            )
                            Text(
                                text = TranslationHelper.translate("remember_me", isArabic),
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSurface.copy(alpha = 0.8f)
                            )
                        }

                        TextButton(onClick = { viewModel.toggleLanguage() }) {
                            Icon(Icons.Default.Translate, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(TranslationHelper.translate("lang_switch", isArabic))
                        }
                    }

                    Button(
                        onClick = { isLoggedIn = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = TranslationHelper.translate("login_btn", isArabic),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = colors.onPrimary
                        )
                    }
                }
            }
        }
    } else {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isTablet = maxWidth > 768.dp
            val selectedTab by viewModel.selectedTab.collectAsState()

            Row(modifier = Modifier.fillMaxSize()) {
                if (isTablet) {
                    Column(
                        modifier = Modifier
                            .width(280.dp)
                            .fillMaxHeight()
                            .background(colors.surface)
                            .border(width = 1.dp, color = colors.border, shape = RoundedCornerShape(0.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(bottom = 24.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(colors.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.AcUnit, contentDescription = null, tint = colors.onPrimary)
                                }
                                Column {
                                    Text(
                                        text = TranslationHelper.translate("app_title", isArabic),
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = colors.primary
                                    )
                                    Text(
                                        text = TranslationHelper.translate("system_status", isArabic),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.success
                                    )
                                }
                            }

                            Divider(color = colors.border, thickness = 1.dp, modifier = Modifier.padding(bottom = 16.dp))

                            val navTabs = listOf("Dashboard", "Reception", "Batch Tracker", "Production", "Quality Control", "Inventory", "AI Analytics")
                            navTabs.forEach { tabKey ->
                                val label = when (tabKey) {
                                    "Dashboard" -> "tab_dashboard"
                                    "Reception" -> "tab_reception"
                                    "Batch Tracker" -> "tab_tracker"
                                    "Production" -> "tab_production"
                                    "Quality Control" -> "tab_qc"
                                    "Inventory" -> "tab_inventory"
                                    else -> "tab_ai"
                                }
                                val icon = when (tabKey) {
                                    "Dashboard" -> Icons.Default.Dashboard
                                    "Reception" -> Icons.Default.AssignmentReturned
                                    "Batch Tracker" -> Icons.Default.Timeline
                                    "Production" -> Icons.Default.Settings
                                    "Quality Control" -> Icons.Default.FactCheck
                                    "Inventory" -> Icons.Default.Warehouse
                                    else -> Icons.Default.Memory
                                }

                                val isSelected = selectedTab == tabKey
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) colors.primary.copy(alpha = 0.12f) else Color.Transparent)
                                        .clickable { viewModel.selectTab(tabKey) }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if (isSelected) colors.primary else colors.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = TranslationHelper.translate(label, isArabic),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (isSelected) colors.primary else colors.onSurface
                                    )
                                }
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Divider(color = colors.border, thickness = 1.dp)
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = TranslationHelper.translate("active_user", isArabic),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = colors.onSurface
                                    )
                                    Text(
                                        text = "ID: HACCP-1029",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                                IconButton(onClick = { isLoggedIn = false }) {
                                    Icon(Icons.Default.Logout, contentDescription = "Log out", tint = colors.error)
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(onClick = { viewModel.toggleDarkMode() }) {
                                    Icon(
                                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Toggle Theme",
                                        tint = colors.secondary
                                    )
                                }
                                IconButton(onClick = { viewModel.toggleLanguage() }) {
                                    Icon(Icons.Default.Translate, contentDescription = "Lang", tint = colors.primary)
                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(colors.background)
                ) {
                    if (!isTablet) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp),
                            color = colors.surface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = TranslationHelper.translate("app_title", isArabic),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = colors.primary
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    IconButton(onClick = { viewModel.toggleDarkMode() }) {
                                        Icon(
                                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                            contentDescription = null,
                                            tint = colors.secondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    IconButton(onClick = { viewModel.toggleLanguage() }) {
                                        Icon(Icons.Default.Translate, contentDescription = null, tint = colors.primary, modifier = Modifier.size(20.dp))
                                    }
                                    IconButton(onClick = { isLoggedIn = false }) {
                                        Icon(Icons.Default.Logout, contentDescription = null, tint = colors.error, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        when (selectedTab) {
                            "Dashboard" -> DashboardModule(viewModel, colors, isArabic)
                            "Reception" -> ReceptionModule(viewModel, colors, isArabic)
                            "Batch Tracker" -> TrackerModule(viewModel, colors, isArabic)
                            "Production" -> ProductionModule(viewModel, colors, isArabic)
                            "Quality Control" -> QCModule(viewModel, colors, isArabic)
                            "Inventory" -> InventoryModule(viewModel, colors, isArabic)
                            "AI Analytics" -> AiReportModule(viewModel, colors, isArabic)
                        }
                    }

                    if (!isTablet) {
                        NavigationBar(
                            containerColor = colors.surface,
                            tonalElevation = 8.dp
                        ) {
                            val navTabs = listOf("Dashboard", "Reception", "Batch Tracker", "Production", "Quality Control", "Inventory", "AI Analytics")
                            navTabs.forEach { tabKey ->
                                val label = when (tabKey) {
                                    "Dashboard" -> "tab_dashboard"
                                    "Reception" -> "tab_reception"
                                    "Batch Tracker" -> "tab_tracker"
                                    "Production" -> "tab_production"
                                    "Quality Control" -> "tab_qc"
                                    "Inventory" -> "tab_inventory"
                                    else -> "tab_ai"
                                }
                                val icon = when (tabKey) {
                                    "Dashboard" -> Icons.Default.Dashboard
                                    "Reception" -> Icons.Default.AssignmentReturned
                                    "Batch Tracker" -> Icons.Default.Timeline
                                    "Production" -> Icons.Default.Settings
                                    "Quality Control" -> Icons.Default.FactCheck
                                    "Inventory" -> Icons.Default.Warehouse
                                    else -> Icons.Default.Memory
                                }

                                NavigationBarItem(
                                    selected = selectedTab == tabKey,
                                    onClick = { viewModel.selectTab(tabKey) },
                                    icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp)) },
                                    label = {
                                        Text(
                                            text = TranslationHelper.translate(label, isArabic),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = colors.primary,
                                        selectedTextColor = colors.primary,
                                        indicatorColor = colors.primary.copy(alpha = 0.15f),
                                        unselectedIconColor = colors.onSurface.copy(alpha = 0.5f),
                                        unselectedTextColor = colors.onSurface.copy(alpha = 0.5f)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardModule(viewModel: GreetingViewModel, colors: IndustrialColors, isArabic: Boolean) {
    val batches by viewModel.poultryBatches.collectAsState()
    val qcLogs by viewModel.qcEntries.collectAsState()
    val lines by viewModel.productionLines.collectAsState()
    val coldRooms by viewModel.coldStorageLocations.collectAsState()

    val totalBirds = batches.sumOf { it.birdsCount }
    val totalWeight = batches.sumOf { it.weightKg }
    val avgTemp = if (coldRooms.isNotEmpty()) coldRooms.map { it.temperature }.average() else 0.0
    val activeLinesCount = lines.count { it.isActive }
    val totalQcRejection = qcLogs.sumOf { it.rejectionCount }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = TranslationHelper.translate("app_title", isArabic),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = colors.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = TranslationHelper.translate("app_subtitle", isArabic),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(colors.primary.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = colors.primary, modifier = Modifier.size(16.dp))
                            Text(
                                text = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = colors.primary
                            )
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KPICard(
                    modifier = Modifier.weight(1f),
                    title = TranslationHelper.translate("kpi_received", isArabic),
                    value = "$totalBirds",
                    subtext = "${String.format("%.1f", totalWeight)} kg",
                    icon = Icons.Default.CheckCircle,
                    tint = colors.primary,
                    colors = colors
                )
                KPICard(
                    modifier = Modifier.weight(1f),
                    title = TranslationHelper.translate("kpi_avg_temp", isArabic),
                    value = "${String.format("%.1f", avgTemp)}°C",
                    subtext = "HACCP Safety Safe",
                    icon = Icons.Default.AcUnit,
                    tint = colors.info,
                    colors = colors
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KPICard(
                    modifier = Modifier.weight(1f),
                    title = TranslationHelper.translate("kpi_active_lines", isArabic),
                    value = "$activeLinesCount / ${lines.size}",
                    subtext = "SLA Target Met",
                    icon = Icons.Default.Settings,
                    tint = colors.secondary,
                    colors = colors
                )
                KPICard(
                    modifier = Modifier.weight(1f),
                    title = TranslationHelper.translate("kpi_rejected", isArabic),
                    value = "$totalQcRejection",
                    subtext = "Birds Condemned",
                    icon = Icons.Default.Cancel,
                    tint = colors.error,
                    colors = colors
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = TranslationHelper.translate("live_factory_map", isArabic),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.primary
                        )
                        Box(
                            modifier = Modifier
                                .background(colors.success.copy(alpha = 0.15f), CircleShape)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "LIVE WORKSPACE",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = colors.success
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(colors.background, RoundedCornerShape(12.dp))
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val beltPath = Path().apply {
                                moveTo(50f, 90f)
                                lineTo(size.width - 50f, 90f)
                            }
                            drawPath(
                                path = beltPath,
                                color = colors.border,
                                style = Stroke(width = 12f, miter = 2f)
                            )
                            drawCircle(color = colors.primary, radius = 10f, center = Offset(50f, 90f))
                            drawCircle(color = colors.primary, radius = 10f, center = Offset(size.width - 50f, 90f))

                            val step = size.width / 6f
                            for (i in 1..4) {
                                val xOffset = (step * i + (System.currentTimeMillis() / 40) % step.toInt()) % (size.width - 100f) + 50f
                                drawRect(
                                    color = colors.primary.copy(alpha = 0.8f),
                                    topLeft = Offset(xOffset - 16f, 64f),
                                    size = Size(32f, 26f)
                                )
                                drawCircle(
                                    color = colors.secondary,
                                    radius = 8f,
                                    center = Offset(xOffset, 54f)
                                )
                            }

                            drawRoundRect(
                                color = colors.info,
                                topLeft = Offset(30f, 110f),
                                size = Size(110f, 50f),
                                style = Stroke(width = 4f)
                            )
                            drawRoundRect(
                                color = colors.secondary,
                                topLeft = Offset(size.width / 2f - 60f, 110f),
                                size = Size(120f, 50f),
                                style = Stroke(width = 4f)
                            )
                            drawRoundRect(
                                color = colors.success,
                                topLeft = Offset(size.width - 140f, 110f),
                                size = Size(110f, 50f),
                                style = Stroke(width = 4f)
                            )
                        }

                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "Intake Gate\n(البوابة)",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = colors.onSurface,
                                modifier = Modifier.align(Alignment.BottomStart).padding(start = 12.dp, bottom = 12.dp)
                            )
                            Text(
                                text = "QC Thermal Audit\n(تدقيق الجودة)",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = colors.onSurface,
                                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp)
                            )
                            Text(
                                text = "Blast Freezer\n(التجميد الصاعق)",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = colors.onSurface,
                                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 12.dp, bottom = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KPICard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    colors: IndustrialColors
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.onSurface.copy(alpha = 0.6f)
                )
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.onSurface
            )
            Text(
                text = subtext,
                style = MaterialTheme.typography.labelSmall,
                color = colors.success
            )
        }
    }
}

@Composable
fun ReceptionModule(viewModel: GreetingViewModel, colors: IndustrialColors, isArabic: Boolean) {
    val supplier by viewModel.supplierName.collectAsState()
    val truck by viewModel.truckNumber.collectAsState()
    val driver by viewModel.driverName.collectAsState()
    val cages by viewModel.cagesCount.collectAsState()
    val birds by viewModel.birdsCount.collectAsState()
    val weight by viewModel.weightKg.collectAsState()
    val status by viewModel.receptionResult.collectAsState()

    val batches by viewModel.poultryBatches.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = TranslationHelper.translate("reception_form", isArabic),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colors.primary
                    )

                    OutlinedTextField(
                        value = supplier,
                        onValueChange = { viewModel.onSupplierNameChange(it) },
                        label = { Text(TranslationHelper.translate("supplier", isArabic)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = truck,
                            onValueChange = { viewModel.onTruckNumberChange(it) },
                            label = { Text(TranslationHelper.translate("truck_no", isArabic)) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border)
                        )
                        OutlinedTextField(
                            value = driver,
                            onValueChange = { viewModel.onDriverNameChange(it) },
                            label = { Text(TranslationHelper.translate("driver", isArabic)) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = cages,
                            onValueChange = { viewModel.onCagesCountChange(it) },
                            label = { Text(TranslationHelper.translate("cages", isArabic)) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border)
                        )
                        OutlinedTextField(
                            value = birds,
                            onValueChange = { viewModel.onBirdsCountChange(it) },
                            label = { Text(TranslationHelper.translate("birds", isArabic)) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border)
                        )
                    }

                    OutlinedTextField(
                        value = weight,
                        onValueChange = { viewModel.onWeightKgChange(it) },
                        label = { Text(TranslationHelper.translate("weight", isArabic)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border)
                    )

                    Column {
                        Text(
                            text = TranslationHelper.translate("status", isArabic),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { viewModel.setReceptionResult("Passed") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (status == "Passed") colors.success else colors.surfaceVariant
                                )
                            ) {
                                Text(
                                    text = TranslationHelper.translate("passed", isArabic),
                                    color = if (status == "Passed") Color.White else colors.onSurface
                                )
                            }
                            Button(
                                onClick = { viewModel.setReceptionResult("Rejected") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (status == "Rejected") colors.error else colors.surfaceVariant
                                )
                            ) {
                                Text(
                                    text = TranslationHelper.translate("rejected", isArabic),
                                    color = if (status == "Rejected") Color.White else colors.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.submitPoultryReception() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = TranslationHelper.translate("save_reception", isArabic),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Last Received Shipments (${batches.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(batches) { b ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = b.supplierName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = colors.primary)
                        Text(text = "Truck: ${b.truckNumber} • Driver: ${b.driverName}", style = MaterialTheme.typography.bodySmall, color = colors.onSurface.copy(alpha = 0.6f))
                        Text(text = "Birds: ${b.birdsCount} • Weight: ${b.weightKg} kg", style = MaterialTheme.typography.bodyMedium, color = colors.onSurface)
                        
                        Box(
                            modifier = Modifier
                                .background(colors.primary.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = b.currentStage, style = MaterialTheme.typography.labelSmall, color = colors.primary)
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color.White)
                                .border(1.dp, Color.Black)
                                .padding(4.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val sizePx = size.width
                                drawRect(color = Color.Black, topLeft = Offset(0f, 0f), size = Size(sizePx/3f, sizePx/3f))
                                drawRect(color = Color.Black, topLeft = Offset(sizePx - sizePx/3f, 0f), size = Size(sizePx/3f, sizePx/3f))
                                drawRect(color = Color.Black, topLeft = Offset(0f, sizePx - sizePx/3f), size = Size(sizePx/3f, sizePx/3f))
                                drawRect(color = Color.Black, topLeft = Offset(sizePx/2f - 4f, sizePx/2f - 4f), size = Size(8f, 8f))
                            }
                        }
                        Text(text = "ID: #${b.id}", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = colors.onSurface.copy(alpha = 0.5f))
                    }
                }
            }
        }
    }
}

@Composable
fun TrackerModule(viewModel: GreetingViewModel, colors: IndustrialColors, isArabic: Boolean) {
    val batches by viewModel.poultryBatches.collectAsState()
    val stages = listOf("Reception", "Slaughter", "Cleaning", "Chilling", "Freezing", "Packing", "Inventory", "Shipping")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = TranslationHelper.translate("batch_tracker", isArabic),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = colors.primary
            )
        }

        if (batches.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = colors.surface)) {
                    Text(
                        text = "No active batches in processing. Register poultry under Reception first.",
                        modifier = Modifier.padding(24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(batches) { batch ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    border = BorderStroke(1.dp, colors.border)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${TranslationHelper.translate("batch_id", isArabic)}: #${batch.id}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = colors.primary
                                )
                                Text(
                                    text = "Supplier: ${batch.supplierName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.onSurface.copy(alpha = 0.6f)
                                )
                            }

                            Button(
                                onClick = { viewModel.advanceBatchStage(batch) },
                                colors = ButtonDefaults.buttonColors(containerColor = colors.secondary),
                                shape = RoundedCornerShape(8.dp),
                                enabled = stages.indexOf(batch.currentStage) < stages.lastIndex
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Text(
                                        text = TranslationHelper.translate("advance_stage", isArabic),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }

                        Divider(color = colors.border)

                        val currentIdx = stages.indexOf(batch.currentStage)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            stages.forEachIndexed { idx, stage ->
                                val active = idx <= currentIdx
                                val isCurrent = idx == currentIdx

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isCurrent) colors.secondary
                                                else if (active) colors.primary
                                                else colors.border
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (active && !isCurrent) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                        } else {
                                            Text(
                                                text = "${idx + 1}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = Color.White
                                            )
                                        }
                                    }

                                    Text(
                                        text = stage,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 10.sp
                                        ),
                                        color = if (isCurrent) colors.secondary else if (active) colors.primary else colors.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductionModule(viewModel: GreetingViewModel, colors: IndustrialColors, isArabic: Boolean) {
    val lines by viewModel.productionLines.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = TranslationHelper.translate("tab_production", isArabic),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = colors.primary
            )
        }

        items(lines) { line ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                        Text(
                            text = line.lineName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.primary
                        )
                        Text(
                            text = "Lead Operator: ${line.operatorName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurface.copy(alpha = 0.6f)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (line.isActive) colors.success else colors.error)
                            )
                            Text(
                                text = if (line.isActive) "Running Online" else "Offline / Downtime",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (line.isActive) colors.success else colors.error
                            )
                        }
                        Text(
                            text = "Accumulated Downtime: ${line.downtimeMinutes} min",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurface.copy(alpha = 0.5f)
                        )
                    }

                    Box(
                        modifier = Modifier.size(90.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                color = colors.border,
                                startAngle = 135f,
                                sweepAngle = 270f,
                                useCenter = false,
                                style = Stroke(width = 8f)
                            )
                            val fillAngle = (line.speedBirdsPerMin / 100f) * 270f
                            drawArc(
                                color = colors.primary,
                                startAngle = 135f,
                                sweepAngle = fillAngle,
                                useCenter = false,
                                style = Stroke(width = 8f)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${line.speedBirdsPerMin}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = colors.onSurface
                            )
                            Text(
                                text = "BPM",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                color = colors.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QCModule(viewModel: GreetingViewModel, colors: IndustrialColors, isArabic: Boolean) {
    val batches by viewModel.poultryBatches.collectAsState()
    val qcLogs by viewModel.qcEntries.collectAsState()

    val selectId by viewModel.qcSelectedBatchId.collectAsState()
    val temp by viewModel.qcTemperature.collectAsState()
    val ph by viewModel.qcWaterPh.collectAsState()
    val rejectCount by viewModel.qcRejectionCount.collectAsState()
    val rejectReason by viewModel.qcRejectionReason.collectAsState()
    val inspectorName by viewModel.qcInspectorName.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = TranslationHelper.translate("qc_form", isArabic),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colors.primary
                    )

                    Text(
                        text = TranslationHelper.translate("select_batch", isArabic),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.onSurface.copy(alpha = 0.6f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        batches.forEach { b ->
                            val isSelected = selectId == b.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) colors.primary else colors.surfaceVariant)
                                    .clickable { viewModel.selectQcBatchId(b.id) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "#${b.id} - ${b.supplierName}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) Color.White else colors.onSurface
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = temp,
                            onValueChange = { viewModel.onQcTemperatureChange(it) },
                            label = { Text(TranslationHelper.translate("core_temp", isArabic)) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border)
                        )
                        OutlinedTextField(
                            value = ph,
                            onValueChange = { viewModel.onQcWaterPhChange(it) },
                            label = { Text(TranslationHelper.translate("ph_level", isArabic)) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = rejectCount,
                            onValueChange = { viewModel.onQcRejectionCountChange(it) },
                            label = { Text(TranslationHelper.translate("reject_count", isArabic)) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border)
                        )
                        OutlinedTextField(
                            value = rejectReason,
                            onValueChange = { viewModel.onQcRejectionReasonChange(it) },
                            label = { Text(TranslationHelper.translate("reject_reason", isArabic)) },
                            modifier = Modifier.weight(1.5f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border)
                        )
                    }

                    OutlinedTextField(
                        value = inspectorName,
                        onValueChange = { viewModel.onQcInspectorNameChange(it) },
                        label = { Text(TranslationHelper.translate("inspector", isArabic)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border)
                    )

                    Button(
                        onClick = { viewModel.submitQCEntry() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = TranslationHelper.translate("save_qc", isArabic), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }

        item {
            Text(
                text = TranslationHelper.translate("qc_history", isArabic),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(qcLogs) { entry ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Inspector: ${entry.inspectorName}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.primary
                        )
                        Text(
                            text = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(entry.timestamp)),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.onSurface.copy(alpha = 0.5f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(text = "Temp: ${entry.temperature}°C", style = MaterialTheme.typography.bodySmall, color = colors.onSurface)
                        Text(text = "pH: ${entry.waterPh}", style = MaterialTheme.typography.bodySmall, color = colors.onSurface)
                        Text(text = "Rejections: ${entry.rejectionCount}", style = MaterialTheme.typography.bodySmall, color = colors.error)
                    }

                    if (entry.rejectionReason.isNotEmpty()) {
                        Text(
                            text = "Reason: ${entry.rejectionReason}",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InventoryModule(viewModel: GreetingViewModel, colors: IndustrialColors, isArabic: Boolean) {
    val rooms by viewModel.coldStorageLocations.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = TranslationHelper.translate("cold_storage_map", isArabic),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = colors.primary
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 280.dp),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rooms) { room ->
                val occupancyPercent = (room.currentCartons.toFloat() / room.capacityCartons.toFloat()) * 100
                val temperatureWarning = (room.roomType == "Blast Freezer" && room.temperature > -30.0) ||
                                         (room.roomType == "Frozen Storage" && room.temperature > -15.0)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    border = BorderStroke(1.dp, if (temperatureWarning) colors.error else colors.border)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = room.roomNumber,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = colors.primary
                                )
                                Text(text = room.roomType, style = MaterialTheme.typography.labelSmall, color = colors.onSurface.copy(alpha = 0.5f))
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        if (temperatureWarning) colors.error.copy(alpha = 0.15f) else colors.success.copy(alpha = 0.15f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${room.temperature}°C",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (temperatureWarning) colors.error else colors.success
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = TranslationHelper.translate("capacity", isArabic),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.onSurface.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "${room.currentCartons} / ${room.capacityCartons} Cartons (${occupancyPercent.toInt()}%)",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = colors.onSurface
                                )
                            }
                            LinearProgressIndicator(
                                progress = room.currentCartons.toFloat() / room.capacityCartons.toFloat(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = if (occupancyPercent > 90f) colors.error else colors.secondary,
                                trackColor = colors.border
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.updateColdRoomStorage(room, 10) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceVariant),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = TranslationHelper.translate("add_stock", isArabic), color = colors.onSurface, style = MaterialTheme.typography.labelSmall)
                            }
                            Button(
                                onClick = { viewModel.updateColdRoomStorage(room, -10) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceVariant),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = TranslationHelper.translate("remove_stock", isArabic), color = colors.onSurface, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AiReportModule(viewModel: GreetingViewModel, colors: IndustrialColors, isArabic: Boolean) {
    val reportText by viewModel.generatedReport.collectAsState()
    val apiStatus by viewModel.apiStatus.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(colors.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = TranslationHelper.translate("ai_analysis", isArabic),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = colors.primary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Let Gemini analyze received volumes, condemnation metrics, and chilling telemetry to output intelligent HACCP recommendations.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Button(
                        onClick = { viewModel.generateAiAnalysis() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Text(
                                text = TranslationHelper.translate("generate_report", isArabic),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = apiStatus !is ApiStatus.Idle,
                enter = fadeIn() + expandVertically()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant),
                    border = BorderStroke(1.dp, colors.primary.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        when (val status = apiStatus) {
                            is ApiStatus.Loading -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CircularProgressIndicator(color = colors.primary)
                                    Text(
                                        text = TranslationHelper.translate("running_ai", isArabic),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = colors.primary
                                    )
                                }
                            }
                            is ApiStatus.Success -> {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "GEMINI CORE REPORT",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = colors.primary
                                        )
                                        Icon(Icons.Default.Verified, contentDescription = null, tint = colors.success, modifier = Modifier.size(20.dp))
                                    }

                                    Text(
                                        text = status.response,
                                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                        color = colors.onSurface
                                    )
                                }
                            }
                            is ApiStatus.Error -> {
                                Text(
                                    text = "Error loading intelligence report: ${status.message}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colors.error
                                )
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}
