package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberAccent

@Composable
fun StarRatingBar(
    rating: Int,
    maxRating: Int = 5,
    onRatingChanged: ((Int) -> Unit)? = null,
    starSize: Dp = 24.dp
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..maxRating) {
            val isSelected = i <= rating
            Icon(
                imageVector = if (isSelected) Icons.Default.Star else Icons.Outlined.StarOutline,
                contentDescription = "Star $i",
                tint = if (isSelected) AmberAccent else Color(0xFFCBD5E1),
                modifier = Modifier
                    .size(starSize)
                    .then(
                        if (onRatingChanged != null) {
                            Modifier.clickable { onRatingChanged(i) }
                        } else {
                            Modifier
                        }
                    )
            )
            if (i < maxRating) Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun RatingDisplayBadge(
    rating: Float,
    reviewsCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = AmberAccent,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = String.format("%.1f", rating),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "($reviewsCount)",
            fontSize = 12.sp,
            color = Color(0xFF64748B)
        )
    }
}
