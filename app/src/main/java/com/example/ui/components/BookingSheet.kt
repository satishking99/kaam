package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.local.WorkerEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.EmeraldTrust
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookingSheet(
    worker: WorkerEntity,
    onDismiss: () -> Unit,
    onConfirmBooking: (
        taskTitle: String,
        taskDescription: String,
        jobType: String,
        scheduledDate: String,
        scheduledTimeSlot: String,
        amount: Int,
        customerName: String,
        customerPhone: String,
        customerAddress: String
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var selectedJobType by remember { mutableStateOf("Labour Only") }
    var selectedDate by remember { mutableStateOf("Today") }
    var selectedTimeSlot by remember { mutableStateOf("Morning (9 AM - 12 PM)") }
    var estimatedPrice by remember { mutableIntStateOf(worker.hourlyRate) }
    var customerName by remember { mutableStateOf("Rahul Verma") }
    var customerPhone by remember { mutableStateOf("+91 98980 12345") }
    var customerAddress by remember { mutableStateOf("Flat 402, Shivalik Heights, Sector 18") }

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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Book Kaam (काम की रिक्वेस्ट)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )
                    Text(
                        text = "Worker: ${worker.name} • ${worker.category.displayName}",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // TrustX Notice Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = EmeraldTrust,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "TrustX Escrow Safe Payment",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Aapka payment secure status me rahega. Kaam pura hone par hi worker ko milega.",
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Task Title
            Text(
                text = "Kaam Ka Naam (Task Title)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                placeholder = { Text("e.g. Bathroom pipe leak repair, Switchboard fix...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("booking_task_title"),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Task Description
            Text(
                text = "Kaam Ki Poori Details (Description)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                placeholder = { Text("Kya problem hai, kitne points hain, koi material chahiye wagairah...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .testTag("booking_task_desc"),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Service Type
            Text(
                text = "Service Type",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Labour Only", "Labour + Material", "Emergency Service").forEach { type ->
                    FilterChip(
                        selected = selectedJobType == type,
                        onClick = { selectedJobType = type },
                        label = { Text(type, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Date Selection
            Text(
                text = "Kab Chahiye? (Date)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Today", "Tomorrow", "In 2 Days").forEach { date ->
                    FilterChip(
                        selected = selectedDate == date,
                        onClick = { selectedDate = date },
                        label = { Text(date, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Time Slot
            Text(
                text = "Time Slot",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    "Morning (9:00 AM - 12:00 PM)",
                    "Afternoon (12:00 PM - 03:00 PM)",
                    "Evening (03:00 PM - 07:00 PM)",
                    "Emergency (As Soon As Possible)"
                ).forEach { slot ->
                    FilterChip(
                        selected = selectedTimeSlot == slot,
                        onClick = { selectedTimeSlot = slot },
                        label = { Text(slot, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Customer Location
            Text(
                text = "Service Address (Aapka Pata)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = customerAddress,
                onValueChange = { customerAddress = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("booking_address"),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Estimated Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Estimated Amount to Lock",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "Base rate: ₹${worker.hourlyRate}/hr + Visit: ₹${worker.visitCharge}",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Text(
                    text = "₹$estimatedPrice",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Button
            Button(
                onClick = {
                    val finalTitle = taskTitle.ifBlank { "General ${worker.category.displayName} Service" }
                    val finalDesc = taskDescription.ifBlank { "Required repair/service at home." }
                    onConfirmBooking(
                        finalTitle,
                        finalDesc,
                        selectedJobType,
                        selectedDate,
                        selectedTimeSlot,
                        estimatedPrice,
                        customerName,
                        customerPhone,
                        customerAddress
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("confirm_booking_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavyPrimary,
                    contentColor = AmberAccent
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Confirm & Lock in TrustX (₹$estimatedPrice)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
