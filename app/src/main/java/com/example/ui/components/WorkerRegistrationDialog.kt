package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.example.data.model.ServiceCategory
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

@Composable
fun WorkerRegistrationDialog(
    onDismiss: () -> Unit,
    onSubmit: (
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
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+91 98") }
    var selectedCategory by remember { mutableStateOf(ServiceCategory.MISTRI) }
    var skills by remember { mutableStateOf("Plaster, Brickwork, Foundation") }
    var expYears by remember { mutableIntStateOf(6) }
    var area by remember { mutableStateOf("Sector 18, Central City") }
    var hourlyRate by remember { mutableIntStateOf(450) }
    var visitCharge by remember { mutableIntStateOf(100) }
    var bio by remember { mutableStateOf("Experienced professional craftsman with own tools and guaranteed quality.") }
    var emergency by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Badge,
                    contentDescription = null,
                    tint = AmberAccent,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Text(
                    text = "Karyakarta Registration (कारीगर जुड़ें)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = NavyPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Aapka Poora Naam") },
                    placeholder = { Text("e.g. Ramesh Kumar Mistri") },
                    modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Number (Calling & WhatsApp)") },
                    modifier = Modifier.fillMaxWidth().testTag("reg_phone_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                Text(
                    text = "Aapka Trade / Category:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color(0xFF1E293B)
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(
                        ServiceCategory.MISTRI,
                        ServiceCategory.ELECTRICIAN,
                        ServiceCategory.PLUMBER,
                        ServiceCategory.CARPENTER,
                        ServiceCategory.PAINTER,
                        ServiceCategory.TILE_WORKER
                    ).chunked(2).forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            pair.forEach { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat.displayName, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = skills,
                    onValueChange = { skills = it },
                    label = { Text("Khas Skills (Commas se alag karein)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = hourlyRate.toString(),
                        onValueChange = { hourlyRate = it.toIntOrNull() ?: 400 },
                        label = { Text("Hourly (₹)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    OutlinedTextField(
                        value = visitCharge.toString(),
                        onValueChange = { visitCharge = it.toIntOrNull() ?: 100 },
                        label = { Text("Visit Charge (₹)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text("Service Area / Colony") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Apne Kaam Ke Baare Me") },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    shape = RoundedCornerShape(8.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = emergency, onCheckedChange = { emergency = it })
                    Text(
                        text = "Available for 24x7 Emergency Services",
                        fontSize = 12.sp,
                        color = Color(0xFF1E293B)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSubmit(name, phone, selectedCategory, skills, expYears, area, hourlyRate, visitCharge, bio, emergency)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavyPrimary,
                    contentColor = AmberAccent
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("submit_reg_button")
            ) {
                Text("Join as Worker", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}
