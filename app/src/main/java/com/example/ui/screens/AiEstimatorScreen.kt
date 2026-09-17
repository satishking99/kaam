package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.JobEstimateResult
import com.example.data.model.ServiceCategory
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberDark
import com.example.ui.theme.EmeraldTrust
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiEstimatorScreen(
    query: String,
    area: String,
    isEstimating: Boolean,
    estimateResult: JobEstimateResult?,
    onQueryChanged: (String) -> Unit,
    onAreaChanged: (String) -> Unit,
    onRequestEstimate: () -> Unit,
    onFindWorkersForCategory: (ServiceCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val quickSuggestions = listOf(
        "Bathroom pipe leak & tap replace",
        "MCB switchboard sparking repair",
        "1 room wall plaster & painting",
        "Door lock replacement & hinge fix",
        "Floor vitrified tile zero-gap fitting",
        "Full 2BHK deep cleaning"
    )

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(AmberAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NavyDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "KaamWala Sahayak (AI Estimator)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "Kaam ka anumanit kharch, material aur time jaanein",
                            color = AmberAccent,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Input Card
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
                    Text(
                        text = "Apne Kaam Ki Details Likhein",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = query,
                        onValueChange = onQueryChanged,
                        placeholder = {
                            Text(
                                "e.g. Bathroom ki tap leak ho rahi hai aur geyser check karwana hai...",
                                fontSize = 13.sp,
                                color = Color(0xFF94A3B8)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("ai_estimator_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Aapka Area / Location",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = area,
                        onValueChange = onAreaChanged,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Quick Idea Chuniye:",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quickSuggestions.forEach { suggestion ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF1F5F9), RoundedCornerShape(6.dp))
                                    .clickable { onQueryChanged(suggestion) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = suggestion,
                                    fontSize = 11.sp,
                                    color = Color(0xFF334155)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onRequestEstimate,
                        enabled = query.isNotBlank() && !isEstimating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("request_ai_estimate_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavyPrimary,
                            contentColor = AmberAccent
                        )
                    ) {
                        if (isEstimating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = AmberAccent,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Calculating Rates with AI...")
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Get Digital Quotation & Advice", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Result Card
        if (estimateResult != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("ai_estimate_result_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = EmeraldTrust,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Estimated Quotation",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = NavyPrimary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFEFF6FF), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = estimateResult.suggestedCategory,
                                    color = Color(0xFF1D4ED8),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Rates Matrix
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Labour Charges", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text(
                                    estimateResult.estimatedLabourCost,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = NavyPrimary
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Material Cost", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text(
                                    estimateResult.estimatedMaterialCost,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF0F766E)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Est. Time", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text(
                                    estimateResult.estimatedTimeRequired,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = AmberDark
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Expert Advice
                        Text(
                            text = "💡 Expert Advice / Sujhav:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = estimateResult.expertAdvice,
                            fontSize = 13.sp,
                            color = Color(0xFF334155),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Breakdown Steps
                        Text(
                            text = "Kaam Kaise Hoga (Steps):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        estimateResult.breakdownSteps.forEachIndexed { index, step ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "${index + 1}. ",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberDark
                                )
                                Text(
                                    text = step,
                                    fontSize = 12.sp,
                                    color = Color(0xFF475569)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // CTA Button: Find matching workers
                        Button(
                            onClick = {
                                val cat = when {
                                    estimateResult.suggestedCategory.contains("Mistri", ignoreCase = true) ||
                                            estimateResult.suggestedCategory.contains("Mason", ignoreCase = true) -> ServiceCategory.MISTRI
                                    estimateResult.suggestedCategory.contains("Electric", ignoreCase = true) -> ServiceCategory.ELECTRICIAN
                                    estimateResult.suggestedCategory.contains("Plumb", ignoreCase = true) -> ServiceCategory.PLUMBER
                                    estimateResult.suggestedCategory.contains("Carpent", ignoreCase = true) -> ServiceCategory.CARPENTER
                                    estimateResult.suggestedCategory.contains("Paint", ignoreCase = true) -> ServiceCategory.PAINTER
                                    estimateResult.suggestedCategory.contains("Tile", ignoreCase = true) -> ServiceCategory.TILE_WORKER
                                    estimateResult.suggestedCategory.contains("Clean", ignoreCase = true) -> ServiceCategory.CLEANER
                                    else -> ServiceCategory.ALL
                                }
                                onFindWorkersForCategory(cat)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("find_matching_workers_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NavyPrimary,
                                contentColor = AmberAccent
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Find ${estimateResult.suggestedCategory} Workers", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
