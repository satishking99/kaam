package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WorkerEntity
import com.example.data.model.VerificationLevel
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.EmeraldTrust
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WorkerDetailSheet(
    worker: WorkerEntity,
    onDismiss: () -> Unit,
    onBookClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(NavyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = worker.avatarInitials,
                            color = AmberAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = worker.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = NavyPrimary
                        )
                        Text(
                            text = "${worker.category.displayName} • ${worker.experienceYears} Saal Anubhav",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        VerificationBadge(level = worker.verificationLevel)
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Verification Level Progress Journey
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "KaamWala Trust Verification",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NavyPrimary
                        )
                        Text(
                            text = "Step ${worker.verificationLevel.stepNumber}/5",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EmeraldTrust
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { worker.verificationLevel.stepNumber / 5f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = EmeraldTrust,
                        trackColor = Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Basic Profile → Phone Verified → Identity Verified → Skill/Work Verified → Trusted Worker",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bio / Anubhav
            Text(
                text = "Karyakarta Ke Bare Me (About)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = NavyPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = worker.bio,
                fontSize = 13.sp,
                color = Color(0xFF334155),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Detailed Rating Breakdown (Quality, Behaviour, Time, Price)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Customer Rating Record",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NavyPrimary
                        )
                        RatingDisplayBadge(
                            rating = worker.rating,
                            reviewsCount = worker.totalReviews
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    RatingProgressRow("⭐ Quality of Work", worker.qualityRating)
                    RatingProgressRow("⭐ Behaviour & Conduct", worker.behaviourRating)
                    RatingProgressRow("⭐ Punctuality & Time", worker.timeRating)
                    RatingProgressRow("⭐ Fair Price", worker.priceRating)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Skills & Specializations
            Text(
                text = "Khas Hunar (Skills & Trade)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = NavyPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                worker.skills.split(",").forEach { skill ->
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = skill.trim(),
                            fontSize = 12.sp,
                            color = Color(0xFF1E293B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tools provided
            Text(
                text = "Tools & Equipment",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = NavyPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = worker.toolsProvided,
                    fontSize = 13.sp,
                    color = Color(0xFF475569)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Rates Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Visit Charge", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text("₹${worker.visitCharge}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NavyPrimary)
                }
                Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color(0xFFCBD5E1)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Hourly Rate", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text("₹${worker.hourlyRate}/hr", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NavyPrimary)
                }
                Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color(0xFFCBD5E1)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Jobs Completed", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text("${worker.completedJobsCount}+", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = EmeraldTrust)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${worker.phone}")
                        }
                        context.startActivity(intent)
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = NavyPrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Direct Call", color = NavyPrimary, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        onDismiss()
                        onBookClick()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyPrimary,
                        contentColor = AmberAccent
                    ),
                    modifier = Modifier.weight(1.5f).height(48.dp).testTag("detail_book_button")
                ) {
                    Text("Book This Worker", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RatingProgressRow(label: String, score: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF334155),
            modifier = Modifier.weight(1.8f)
        )
        LinearProgressIndicator(
            progress = { score / 5.0f },
            modifier = Modifier
                .weight(2f)
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = AmberAccent,
            trackColor = Color(0xFFF1F5F9)
        )
        Text(
            text = String.format("%.1f", score),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
