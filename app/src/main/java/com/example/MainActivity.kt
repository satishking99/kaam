package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.KaamWalaDatabase
import com.example.data.repository.KaamWalaRepository
import com.example.ui.AppTab
import com.example.ui.KaamWalaViewModel
import com.example.ui.KaamWalaViewModelFactory
import com.example.ui.components.BookingSheet
import com.example.ui.components.ComplaintDialog
import com.example.ui.components.RatingDialog
import com.example.ui.components.WorkerDetailSheet
import com.example.ui.components.WorkerRegistrationDialog
import com.example.ui.screens.AiEstimatorScreen
import com.example.ui.screens.BookingsScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.WorkerPortalScreen
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.KaamWalaTheme
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

class MainActivity : ComponentActivity() {

    private val viewModel: KaamWalaViewModel by viewModels {
        val database = KaamWalaDatabase.getDatabase(applicationContext)
        val repository = KaamWalaRepository(database.workerDao(), database.workRequestDao())
        KaamWalaViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KaamWalaTheme {
                KaamWalaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun KaamWalaApp(viewModel: KaamWalaViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val workers by viewModel.workers.collectAsState()
    val workRequests by viewModel.workRequests.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = NavyPrimary,
                contentColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                AppTab.values().forEach { tab ->
                    val isSelected = uiState.currentTab == tab
                    val icon = when (tab) {
                        AppTab.EXPLORE -> Icons.Default.Explore
                        AppTab.BOOKINGS -> Icons.Default.ReceiptLong
                        AppTab.AI_ESTIMATOR -> Icons.Default.AutoAwesome
                        AppTab.WORKER_PORTAL -> Icons.Default.Engineering
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(tab) },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = tab.label,
                                tint = if (isSelected) AmberAccent else Color(0xFF94A3B8)
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AmberAccent else Color(0xFF94A3B8)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color(0xFF1E293B)
                        ),
                        modifier = Modifier.testTag("nav_item_${tab.name}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                AppTab.EXPLORE -> {
                    ExploreScreen(
                        workers = workers,
                        selectedCategory = uiState.selectedCategory,
                        searchQuery = uiState.searchQuery,
                        emergencyOnly = uiState.emergencyOnly,
                        onCategorySelected = { viewModel.selectCategory(it) },
                        onSearchQueryChanged = { viewModel.setSearchQuery(it) },
                        onToggleEmergency = { viewModel.toggleEmergencyOnly() },
                        onWorkerClicked = { viewModel.openWorkerDetail(it) },
                        onBookWorkerClicked = { viewModel.openBookingSheet(it) }
                    )
                }

                AppTab.BOOKINGS -> {
                    BookingsScreen(
                        requests = workRequests,
                        onReleasePayment = { viewModel.releaseEscrowPayment(it) },
                        onRateWorker = { viewModel.openRatingDialog(it) },
                        onReportComplaint = { viewModel.openComplaintDialog(it) }
                    )
                }

                AppTab.AI_ESTIMATOR -> {
                    AiEstimatorScreen(
                        query = uiState.aiQuery,
                        area = uiState.aiArea,
                        isEstimating = uiState.isEstimating,
                        estimateResult = uiState.aiEstimateResult,
                        onQueryChanged = { viewModel.setAiQuery(it) },
                        onAreaChanged = { viewModel.setAiArea(it) },
                        onRequestEstimate = { viewModel.requestAiEstimate() },
                        onFindWorkersForCategory = { cat ->
                            viewModel.selectCategory(cat)
                            viewModel.selectTab(AppTab.EXPLORE)
                        }
                    )
                }

                AppTab.WORKER_PORTAL -> {
                    val currentWorker = workers.firstOrNull { it.id == uiState.currentWorkerProfileId }
                        ?: workers.firstOrNull()

                    WorkerPortalScreen(
                        currentWorker = currentWorker,
                        requests = workRequests,
                        onToggleAvailability = { viewModel.toggleWorkerAvailability(it) },
                        onUpdateRequestStatus = { req, status -> viewModel.updateRequestStatusByWorker(req, status) },
                        onOpenRegisterWorker = { viewModel.openWorkerRegistration(true) }
                    )
                }
            }

            // Worker Detail Modal
            uiState.selectedWorker?.let { worker ->
                WorkerDetailSheet(
                    worker = worker,
                    onDismiss = { viewModel.closeWorkerDetail() },
                    onBookClick = {
                        viewModel.closeWorkerDetail()
                        viewModel.openBookingSheet(worker)
                    }
                )
            }

            // Booking Modal
            uiState.bookingSheetWorker?.let { worker ->
                BookingSheet(
                    worker = worker,
                    onDismiss = { viewModel.closeBookingSheet() },
                    onConfirmBooking = { title, desc, jobType, date, slot, amount, name, phone, addr ->
                        viewModel.submitBooking(title, desc, jobType, date, slot, amount, name, phone, addr)
                    }
                )
            }

            // Rating & Review Dialog (5 Dimensions)
            uiState.ratingDialogRequest?.let { req ->
                RatingDialog(
                    request = req,
                    onDismiss = { viewModel.closeRatingDialog() },
                    onSubmit = { quality, behaviour, time, price, overall, comment ->
                        viewModel.submitRating(req, quality, behaviour, time, price, overall, comment)
                    }
                )
            }

            // Complaint / Dispute Dialog
            uiState.complaintDialogRequest?.let { req ->
                ComplaintDialog(
                    request = req,
                    onDismiss = { viewModel.closeComplaintDialog() },
                    onSubmitComplaint = { complaintText ->
                        viewModel.submitComplaint(req, complaintText)
                    }
                )
            }

            // Worker Registration Dialog
            if (uiState.showWorkerRegistration) {
                WorkerRegistrationDialog(
                    onDismiss = { viewModel.openWorkerRegistration(false) },
                    onSubmit = { name, phone, cat, skills, exp, area, hourly, visit, bio, emergency ->
                        viewModel.registerNewWorker(name, phone, cat, skills, exp, area, hourly, visit, bio, emergency)
                    }
                )
            }
        }
    }
}
