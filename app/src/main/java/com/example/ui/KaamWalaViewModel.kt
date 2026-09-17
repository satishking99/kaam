package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiEstimatorService
import com.example.data.ai.JobEstimateResult
import com.example.data.local.WorkerEntity
import com.example.data.local.WorkRequestEntity
import com.example.data.model.EscrowStatus
import com.example.data.model.RequestStatus
import com.example.data.model.ServiceCategory
import com.example.data.model.VerificationLevel
import com.example.data.repository.KaamWalaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val label: String, val hindiLabel: String) {
    EXPLORE("Explore", "कारीगर खोजें"),
    BOOKINGS("My Bookings", "मेरे ऑर्डर्स"),
    AI_ESTIMATOR("AI Estimator", "काम का खर्च"),
    WORKER_PORTAL("Worker Mode", "काम वाला मोड")
}

data class UiState(
    val currentTab: AppTab = AppTab.EXPLORE,
    val selectedCategory: ServiceCategory = ServiceCategory.ALL,
    val searchQuery: String = "",
    val emergencyOnly: Boolean = false,
    val selectedWorker: WorkerEntity? = null,
    val bookingSheetWorker: WorkerEntity? = null,
    val ratingDialogRequest: WorkRequestEntity? = null,
    val complaintDialogRequest: WorkRequestEntity? = null,
    // AI Estimator
    val aiQuery: String = "",
    val aiArea: String = "Sector 18, Central City",
    val isEstimating: Boolean = false,
    val aiEstimateResult: JobEstimateResult? = null,
    // Worker portal mode
    val currentWorkerProfileId: Long = 2L, // Suraj Sharma by default for worker demonstration
    val showWorkerRegistration: Boolean = false
)

class KaamWalaViewModel(
    private val repository: KaamWalaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
        }
    }

    val workers: StateFlow<List<WorkerEntity>> = combine(
        repository.allWorkers,
        _uiState
    ) { allWorkers, state ->
        allWorkers.filter { worker ->
            val matchesCategory = (state.selectedCategory == ServiceCategory.ALL || worker.category == state.selectedCategory)
            val matchesEmergency = (!state.emergencyOnly || worker.isEmergencyServiceAvailable)
            val matchesQuery = if (state.searchQuery.isBlank()) {
                true
            } else {
                val q = state.searchQuery.trim().lowercase()
                worker.name.lowercase().contains(q) ||
                        worker.skills.lowercase().contains(q) ||
                        worker.locationArea.lowercase().contains(q) ||
                        worker.category.displayName.lowercase().contains(q)
            }
            matchesCategory && matchesEmergency && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workRequests: StateFlow<List<WorkRequestEntity>> = repository.allRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: AppTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab)
    }

    fun selectCategory(category: ServiceCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun toggleEmergencyOnly() {
        _uiState.value = _uiState.value.copy(emergencyOnly = !_uiState.value.emergencyOnly)
    }

    fun openWorkerDetail(worker: WorkerEntity) {
        _uiState.value = _uiState.value.copy(selectedWorker = worker)
    }

    fun closeWorkerDetail() {
        _uiState.value = _uiState.value.copy(selectedWorker = null)
    }

    fun openBookingSheet(worker: WorkerEntity) {
        _uiState.value = _uiState.value.copy(bookingSheetWorker = worker)
    }

    fun closeBookingSheet() {
        _uiState.value = _uiState.value.copy(bookingSheetWorker = null)
    }

    fun openRatingDialog(request: WorkRequestEntity) {
        _uiState.value = _uiState.value.copy(ratingDialogRequest = request)
    }

    fun closeRatingDialog() {
        _uiState.value = _uiState.value.copy(ratingDialogRequest = null)
    }

    fun openComplaintDialog(request: WorkRequestEntity) {
        _uiState.value = _uiState.value.copy(complaintDialogRequest = request)
    }

    fun closeComplaintDialog() {
        _uiState.value = _uiState.value.copy(complaintDialogRequest = null)
    }

    fun submitBooking(
        taskTitle: String,
        taskDescription: String,
        jobType: String,
        scheduledDate: String,
        scheduledTimeSlot: String,
        amount: Int,
        customerName: String,
        customerPhone: String,
        customerAddress: String
    ) {
        val worker = _uiState.value.bookingSheetWorker ?: return
        viewModelScope.launch {
            val request = WorkRequestEntity(
                workerId = worker.id,
                workerName = worker.name,
                workerCategory = worker.category,
                workerPhone = worker.phone,
                customerName = customerName.ifBlank { "Rahul Verma" },
                customerPhone = customerPhone.ifBlank { "+91 98980 12345" },
                customerAddress = customerAddress.ifBlank { "Flat 402, Shivalik Heights, Sector 18" },
                taskTitle = taskTitle,
                taskDescription = taskDescription,
                jobType = jobType,
                scheduledDate = scheduledDate,
                scheduledTimeSlot = scheduledTimeSlot,
                estimatedAmount = amount,
                finalAmount = amount,
                requestStatus = RequestStatus.REQUESTED,
                escrowStatus = EscrowStatus.SECURED_IN_ESCROW
            )
            repository.createWorkRequest(request)
            closeBookingSheet()
            // Automatically switch to bookings tab to show real-time progress
            selectTab(AppTab.BOOKINGS)
        }
    }

    // Customer confirms completion & releases TrustX Escrow payment to worker
    fun releaseEscrowPayment(request: WorkRequestEntity) {
        viewModelScope.launch {
            val updated = request.copy(
                requestStatus = RequestStatus.PAID_RELEASED,
                escrowStatus = EscrowStatus.RELEASED_TO_WORKER
            )
            repository.updateRequest(updated)
            // Open rating dialog right after releasing payment
            openRatingDialog(updated)
        }
    }

    // Submit detailed rating
    fun submitRating(
        request: WorkRequestEntity,
        quality: Int,
        behaviour: Int,
        time: Int,
        price: Int,
        overall: Int,
        comment: String
    ) {
        viewModelScope.launch {
            val updatedRequest = request.copy(
                qualityRating = quality,
                behaviourRating = behaviour,
                timeRating = time,
                priceRating = price,
                overallRating = overall,
                customerReviewComment = comment
            )
            repository.updateRequest(updatedRequest)
            closeRatingDialog()
        }
    }

    // Customer submits a complaint
    fun submitComplaint(request: WorkRequestEntity, complaintText: String) {
        viewModelScope.launch {
            val updated = request.copy(
                complaintText = complaintText,
                complaintStatus = "OPEN"
            )
            repository.updateRequest(updated)
            closeComplaintDialog()
        }
    }

    // Worker mode: toggle availability
    fun toggleWorkerAvailability(worker: WorkerEntity) {
        viewModelScope.launch {
            val updated = worker.copy(isAvailable = !worker.isAvailable)
            repository.updateWorker(updated)
        }
    }

    // Worker mode: change request status (Accept, Start In-Progress, Mark Completed)
    fun updateRequestStatusByWorker(request: WorkRequestEntity, newStatus: RequestStatus) {
        viewModelScope.launch {
            val updated = request.copy(requestStatus = newStatus)
            repository.updateRequest(updated)
        }
    }

    // AI Estimator actions
    fun setAiQuery(query: String) {
        _uiState.value = _uiState.value.copy(aiQuery = query)
    }

    fun setAiArea(area: String) {
        _uiState.value = _uiState.value.copy(aiArea = area)
    }

    fun requestAiEstimate() {
        val query = _uiState.value.aiQuery.trim()
        if (query.isBlank()) return

        _uiState.value = _uiState.value.copy(isEstimating = true)
        viewModelScope.launch {
            val result = GeminiEstimatorService.getJobEstimate(query, _uiState.value.aiArea)
            result.onSuccess { estimate ->
                _uiState.value = _uiState.value.copy(
                    isEstimating = false,
                    aiEstimateResult = estimate
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isEstimating = false
                )
            }
        }
    }

    fun openWorkerRegistration(show: Boolean) {
        _uiState.value = _uiState.value.copy(showWorkerRegistration = show)
    }

    fun registerNewWorker(
        name: String,
        phone: String,
        category: ServiceCategory,
        skills: String,
        experienceYears: Int,
        area: String,
        hourlyRate: Int,
        visitCharge: Int,
        bio: String,
        emergency: Boolean
    ) {
        viewModelScope.launch {
            val initials = name.split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .map { it.first().uppercase() }
                .joinToString("")
                .ifEmpty { "KW" }

            val newWorker = WorkerEntity(
                name = name,
                phone = phone,
                category = category,
                skills = skills,
                experienceYears = experienceYears,
                rating = 5.0f,
                totalReviews = 1,
                qualityRating = 5.0f,
                behaviourRating = 5.0f,
                timeRating = 5.0f,
                priceRating = 5.0f,
                locationArea = area,
                distanceKm = 0.5f,
                hourlyRate = hourlyRate,
                visitCharge = visitCharge,
                isAvailable = true,
                verificationLevel = VerificationLevel.SKILL_VERIFIED,
                completedJobsCount = 0,
                bio = bio,
                isEmergencyServiceAvailable = emergency,
                avatarInitials = initials,
                toolsProvided = "Complete professional toolkit"
            )
            val newId = repository.registerWorker(newWorker)
            _uiState.value = _uiState.value.copy(
                currentWorkerProfileId = newId,
                showWorkerRegistration = false
            )
        }
    }
}

class KaamWalaViewModelFactory(
    private val repository: KaamWalaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KaamWalaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KaamWalaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
