package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WorkerEntity
import com.example.data.local.WorkRequestEntity
import com.example.data.model.RequestStatus
import com.example.data.model.ServiceCategory
import com.example.data.model.VerificationLevel
import com.example.ui.components.VerificationBadge
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldTrust
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

@Composable
fun WorkerPortalScreen(
    currentWorker: WorkerEntity?,
    requests: List<WorkRequestEntity>,
    onToggleAvailability: (WorkerEntity) -> Unit,
    onUpdateRequestStatus: (WorkRequestEntity, RequestStatus) -> Unit,
    onOpenRegisterWorker: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyPrimary)
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "KaamWala Worker Portal",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Apni profile, kaam requests aur earnings manage karein",
                            color = AmberAccent,
                            fontSize = 11.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onOpenRegisterWorker,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AmberAccent)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = AmberAccent, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Profile", fontSize = 11.sp, color = AmberAccent)
                    }
                }
            }
        }

        if (currentWorker != null) {
            // Worker Profile Status & Availability Toggle Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(NavyPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentWorker.avatarInitials,
                                    color = AmberAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentWorker.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = NavyPrimary
                                )
                                Text(
                                    text = "${currentWorker.category.displayName} • ${currentWorker.locationArea}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                VerificationBadge(level = currentWorker.verificationLevel)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Availability Toggle Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (currentWorker.isAvailable) Color(0xFFECFDF5) else Color(0xFFFEF2F2),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (currentWorker.isAvailable) "🟢 Available for Work (Online)" else "🔴 Unavailable / Busy (Offline)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (currentWorker.isAvailable) Color(0xFF047857) else Color(0xFFDC2626)
                                )
                                Text(
                                    text = if (currentWorker.isAvailable)
                                        "Customer aapko direct call aur order bhej sakte hain."
                                    else
                                        "Aap offline hain, naye orders pause hain.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }

                            Switch(
                                checked = currentWorker.isAvailable,
                                onCheckedChange = { onToggleAvailability(currentWorker) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = EmeraldTrust
                                ),
                                modifier = Modifier.testTag("worker_availability_switch")
                            )
                        }
                    }
                }
            }

            // Stats & Earnings Grid
            item {
                val completedCount = currentWorker.completedJobsCount + requests.count { it.requestStatus == RequestStatus.PAID_RELEASED }
                val totalEarned = requests.filter { it.requestStatus == RequestStatus.PAID_RELEASED }.sumOf { it.finalAmount } + (currentWorker.completedJobsCount * 450)
                val inEscrow = requests.filter { it.requestStatus == RequestStatus.IN_PROGRESS || it.requestStatus == RequestStatus.ACCEPTED }.sumOf { it.estimatedAmount }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Total Earnings", fontSize = 11.sp, color = Color(0xFF64748B))
                            Text(
                                "₹$totalEarned",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = NavyPrimary
                            )
                            Text("TrustX Settled", fontSize = 10.sp, color = EmeraldTrust)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("In Escrow", fontSize = 11.sp, color = Color(0xFF64748B))
                            Text(
                                "₹$inEscrow",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color(0xFFD97706)
                            )
                            Text("Pending Release", fontSize = 10.sp, color = Color(0xFFD97706))
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Rating & Jobs", fontSize = 11.sp, color = Color(0xFF64748B))
                            Text(
                                "⭐ ${currentWorker.rating}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = AmberAccent
                            )
                            Text("$completedCount Jobs Done", fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                    }
                }
            }

            // Trust Verification Journey
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Verification Badge Level",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NavyPrimary
                            )
                            Text(
                                text = "${currentWorker.verificationLevel.title}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldTrust
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { currentWorker.verificationLevel.stepNumber / 5f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = EmeraldTrust,
                            trackColor = Color(0xFFE2E8F0)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Next level: Identity & Skill Verification badhne se customer direct trust karenge aur 2x zyada booking milti hai.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            // Received Customer Work Requests
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Customer Work Requests",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = NavyPrimary
                    )
                    Text(
                        text = "${requests.size} total orders",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            if (requests.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Abhi koi naya request nahi hai",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "Customer jab aapke area me service book karenge toh yahan aayega.",
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            } else {
                items(requests) { req ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        WorkerOrderCard(
                            request = req,
                            onStatusUpdate = { newStatus -> onUpdateRequestStatus(req, newStatus) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkerOrderCard(
    request: WorkRequestEntity,
    onStatusUpdate: (RequestStatus) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = request.customerName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = NavyPrimary
                    )
                    Text(
                        text = request.customerAddress,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Text(
                    text = "₹${request.finalAmount}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = EmeraldTrust
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = request.taskTitle,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = request.taskDescription,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Time: ${request.scheduledDate}, ${request.scheduledTimeSlot}",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons for Worker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (request.requestStatus) {
                    RequestStatus.REQUESTED -> {
                        Button(
                            onClick = { onStatusUpdate(RequestStatus.ACCEPTED) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NavyPrimary,
                                contentColor = AmberAccent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Accept Request")
                        }
                        OutlinedButton(
                            onClick = { onStatusUpdate(RequestStatus.CANCELLED) },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Decline", color = DangerRed)
                        }
                    }
                    RequestStatus.ACCEPTED -> {
                        Button(
                            onClick = { onStatusUpdate(RequestStatus.IN_PROGRESS) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2563EB),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Start Work (In Progress)")
                        }
                    }
                    RequestStatus.IN_PROGRESS -> {
                        Button(
                            onClick = { onStatusUpdate(RequestStatus.COMPLETED) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldTrust,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mark Work Completed")
                        }
                    }
                    RequestStatus.COMPLETED -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFECFDF5), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Work Marked Done! Awaiting Customer Payment Release (PIN: ${request.completionCode})",
                                fontSize = 11.sp,
                                color = Color(0xFF047857),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    RequestStatus.PAID_RELEASED -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Payment of ₹${request.finalAmount} received in TrustX account!",
                                fontSize = 11.sp,
                                color = Color(0xFF334155),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
