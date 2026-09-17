package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WorkRequestEntity
import com.example.data.model.EscrowStatus
import com.example.data.model.RequestStatus
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldTrust
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

@Composable
fun BookingsScreen(
    requests: List<WorkRequestEntity>,
    onReleasePayment: (WorkRequestEntity) -> Unit,
    onRateWorker: (WorkRequestEntity) -> Unit,
    onReportComplaint: (WorkRequestEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyPrimary)
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Text(
                text = "My Service Orders & TrustX",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Kaam ki live status track karein aur completion confirm karein",
                color = Color(0xFFCBD5E1),
                fontSize = 12.sp
            )
        }

        if (requests.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = Color(0xFF94A3B8)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Koi booking abhi active nahi hai",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Explore tab se kisi bhi verified mistri ya worker ko book karein.",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp, top = 16.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(requests, key = { it.id }) { request ->
                    BookingItemCard(
                        request = request,
                        onReleasePayment = { onReleasePayment(request) },
                        onRateWorker = { onRateWorker(request) },
                        onReportComplaint = { onReportComplaint(request) },
                        onCallWorker = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${request.workerPhone}")
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BookingItemCard(
    request: WorkRequestEntity,
    onReleasePayment: () -> Unit,
    onRateWorker: () -> Unit,
    onReportComplaint: () -> Unit,
    onCallWorker: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("booking_card_${request.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row: Worker Name & Request Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = request.workerName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = NavyPrimary
                    )
                    Text(
                        text = "${request.workerCategory.displayName} • ${request.jobType}",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }

                // Status Badge
                val (statusBg, statusText, statusColor) = when (request.requestStatus) {
                    RequestStatus.REQUESTED -> Triple(Color(0xFFFEF3C7), "Requested ⏳", Color(0xFFB45309))
                    RequestStatus.ACCEPTED -> Triple(Color(0xFFEFF6FF), "Accepted 👍", Color(0xFF1D4ED8))
                    RequestStatus.IN_PROGRESS -> Triple(Color(0xFFEEF2FF), "In Progress 🛠️", Color(0xFF4338CA))
                    RequestStatus.COMPLETED -> Triple(Color(0xFFECFDF5), "Work Done ✅", Color(0xFF047857))
                    RequestStatus.PAID_RELEASED -> Triple(Color(0xFFF1F5F9), "Paid & Closed 💵", Color(0xFF334155))
                    RequestStatus.CANCELLED -> Triple(Color(0xFFFEE2E2), "Cancelled", Color(0xFFDC2626))
                }

                Box(
                    modifier = Modifier
                        .background(statusBg, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Task Details Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = request.taskTitle,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = request.taskDescription,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Slot: ${request.scheduledDate}, ${request.scheduledTimeSlot}",
                            fontSize = 11.sp,
                            color = Color(0xFF475569)
                        )
                        Text(
                            text = "PIN: ${request.completionCode}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // TrustX Escrow Status Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (request.escrowStatus == EscrowStatus.SECURED_IN_ESCROW)
                            Color(0xFFECFDF5)
                        else
                            Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        0.5.dp,
                        if (request.escrowStatus == EscrowStatus.SECURED_IN_ESCROW)
                            Color(0xFF10B981)
                        else
                            Color(0xFFCBD5E1),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (request.escrowStatus == EscrowStatus.SECURED_IN_ESCROW)
                            Icons.Default.Lock
                        else
                            Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (request.escrowStatus == EscrowStatus.SECURED_IN_ESCROW)
                            EmeraldTrust
                        else
                            Color(0xFF64748B),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (request.escrowStatus) {
                            EscrowStatus.SECURED_IN_ESCROW -> "TrustX Escrow: ₹${request.estimatedAmount} Secure"
                            EscrowStatus.RELEASED_TO_WORKER -> "TrustX: ₹${request.finalAmount} Released to Worker"
                            EscrowStatus.REFUNDED -> "TrustX: Refunded to Customer"
                            else -> "TrustX Escrow: Pending"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (request.escrowStatus == EscrowStatus.SECURED_IN_ESCROW)
                            Color(0xFF065F46)
                        else
                            Color(0xFF334155)
                    )
                }

                Text(
                    text = "₹${request.finalAmount}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = NavyPrimary
                )
            }

            // If a complaint was reported
            if (request.complaintStatus == "OPEN" && request.complaintText != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFEF2F2), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ReportProblem,
                            contentDescription = null,
                            tint = DangerRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Dispute Open: Support Team investigating",
                                color = DangerRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = request.complaintText,
                                color = Color(0xFF991B1B),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // If rating was given
            if (request.overallRating > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Aapki Rating: ",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "⭐ ${request.overallRating}/5",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = AmberAccent
                    )
                    if (request.customerReviewComment.isNotBlank()) {
                        Text(
                            text = " • \"${request.customerReviewComment}\"",
                            fontSize = 11.sp,
                            color = Color(0xFF475569)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onCallWorker,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        modifier = Modifier.size(15.dp),
                        tint = NavyPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call", fontSize = 12.sp, color = NavyPrimary)
                }

                if (request.requestStatus == RequestStatus.COMPLETED || request.requestStatus == RequestStatus.IN_PROGRESS) {
                    Button(
                        onClick = onReleasePayment,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldTrust,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).testTag("release_payment_${request.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Release Payment",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                if (request.requestStatus == RequestStatus.PAID_RELEASED && request.overallRating == 0) {
                    Button(
                        onClick = onRateWorker,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavyPrimary,
                            contentColor = AmberAccent
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).testTag("rate_worker_${request.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rate Worker", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                // Complaint option
                OutlinedButton(
                    onClick = onReportComplaint,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("report_dispute_${request.id}")
                ) {
                    Text("Report Problem", color = DangerRed, fontSize = 11.sp)
                }
            }
        }
    }
}
