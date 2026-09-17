package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.data.local.WorkRequestEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

@Composable
fun RatingDialog(
    request: WorkRequestEntity,
    onDismiss: () -> Unit,
    onSubmit: (quality: Int, behaviour: Int, time: Int, price: Int, overall: Int, comment: String) -> Unit
) {
    var qualityRating by remember { mutableIntStateOf(5) }
    var behaviourRating by remember { mutableIntStateOf(5) }
    var timeRating by remember { mutableIntStateOf(5) }
    var priceRating by remember { mutableIntStateOf(5) }
    var overallRating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AmberAccent,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = "Rate Kaam & Worker",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )
                }
                Text(
                    text = "Worker: ${request.workerName} (${request.workerCategory.displayName})",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Aapka feedback worker ki reputation badhane me madad karega:",
                    fontSize = 12.sp,
                    color = Color(0xFF475569)
                )

                // 1. Quality
                RatingRow(
                    label = "⭐ Quality of Work (काम की गुणवत्ता)",
                    rating = qualityRating,
                    onRatingChanged = { qualityRating = it }
                )

                // 2. Behaviour
                RatingRow(
                    label = "⭐ Behaviour (व्यवहार व आचरण)",
                    rating = behaviourRating,
                    onRatingChanged = { behaviourRating = it }
                )

                // 3. Time
                RatingRow(
                    label = "⭐ Time & Punctuality (समय पर आगमन)",
                    rating = timeRating,
                    onRatingChanged = { timeRating = it }
                )

                // 4. Price
                RatingRow(
                    label = "⭐ Price & Fairness (उचित मूल्य)",
                    rating = priceRating,
                    onRatingChanged = { priceRating = it }
                )

                // 5. Overall Experience
                RatingRow(
                    label = "⭐ Overall Experience (कुल अनुभव)",
                    rating = overallRating,
                    onRatingChanged = { overallRating = it }
                )

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Apna anubhav likhein (Optional)") },
                    placeholder = { Text("e.g. Kaam bahut safai se kiya aur time par aaye...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("rating_comment_input"),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(qualityRating, behaviourRating, timeRating, priceRating, overallRating, comment)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavyPrimary,
                    contentColor = AmberAccent
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("submit_rating_button")
            ) {
                Text("Submit Review", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancel", color = Color(0xFF64748B))
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}

@Composable
private fun RatingRow(
    label: String,
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(4.dp))
        StarRatingBar(
            rating = rating,
            onRatingChanged = onRatingChanged,
            starSize = 22.dp
        )
    }
}
