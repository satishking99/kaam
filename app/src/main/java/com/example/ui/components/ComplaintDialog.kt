package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WorkRequestEntity
import com.example.ui.theme.DangerRed
import com.example.ui.theme.NavyPrimary

@Composable
fun ComplaintDialog(
    request: WorkRequestEntity,
    onDismiss: () -> Unit,
    onSubmitComplaint: (complaintText: String) -> Unit
) {
    var selectedReason by remember { mutableStateOf("Incomplete Work") }
    var detailsText by remember { mutableStateOf("") }

    val reasons = listOf(
        "Incomplete Work",
        "Overcharging",
        "Delay / No Show",
        "Quality Issue",
        "Misbehaviour"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ReportProblem,
                    contentDescription = null,
                    tint = DangerRed,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Text(
                    text = "Report Problem / Complaint",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = DangerRed
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Worker: ${request.workerName} • Task: ${request.taskTitle}",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Text(
                    text = "Problem Category chuniye:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B)
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    reasons.chunked(2).forEach { rowReasons ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            rowReasons.forEach { reason ->
                                FilterChip(
                                    selected = selectedReason == reason,
                                    onClick = { selectedReason = reason },
                                    label = { Text(reason, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = detailsText,
                    onValueChange = { detailsText = it },
                    label = { Text("Problem ki poori jaankari likhein") },
                    placeholder = { Text("e.g. Kaam theek se check nahi kiya, abhi bhi paani tapak raha hai...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("complaint_details_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                Text(
                    text = "TrustX Guarantee: Aapka escrow balance tab tak hold rahega jab tak KaamWala support team problem resolve na kare.",
                    fontSize = 11.sp,
                    color = Color(0xFF475569),
                    lineHeight = 15.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val fullComplaint = "[$selectedReason] ${detailsText.trim()}"
                    onSubmitComplaint(fullComplaint)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DangerRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("submit_complaint_button")
            ) {
                Text("Submit Dispute", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}
