package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.EscrowStatus
import com.example.data.model.RequestStatus
import com.example.data.model.ServiceCategory
import com.example.data.model.VerificationLevel

@Entity(tableName = "workers")
data class WorkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val category: ServiceCategory,
    val skills: String, // comma separated tags
    val experienceYears: Int,
    val rating: Float,
    val totalReviews: Int,
    val qualityRating: Float,
    val behaviourRating: Float,
    val timeRating: Float,
    val priceRating: Float,
    val locationArea: String,
    val distanceKm: Float,
    val hourlyRate: Int,
    val visitCharge: Int,
    val isAvailable: Boolean,
    val verificationLevel: VerificationLevel,
    val completedJobsCount: Int,
    val bio: String,
    val isEmergencyServiceAvailable: Boolean,
    val avatarInitials: String,
    val toolsProvided: String
)

@Entity(tableName = "work_requests")
data class WorkRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workerId: Long,
    val workerName: String,
    val workerCategory: ServiceCategory,
    val workerPhone: String,
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String,
    val taskTitle: String,
    val taskDescription: String,
    val jobType: String = "Labour Only",
    val scheduledDate: String,
    val scheduledTimeSlot: String,
    val estimatedAmount: Int,
    val finalAmount: Int,
    val requestStatus: RequestStatus = RequestStatus.REQUESTED,
    val escrowStatus: EscrowStatus = EscrowStatus.SECURED_IN_ESCROW,
    val createdAt: Long = System.currentTimeMillis(),
    val completionCode: String = "KW-" + (1000..9999).random(),
    val qualityRating: Int = 0,
    val behaviourRating: Int = 0,
    val timeRating: Int = 0,
    val priceRating: Int = 0,
    val overallRating: Int = 0,
    val customerReviewComment: String = "",
    val complaintText: String? = null,
    val complaintStatus: String = "NONE"
)
