package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VerificationLevel

@Composable
fun VerificationBadge(
    level: VerificationLevel,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (level) {
        VerificationLevel.BASIC -> Triple(
            Color(0xFFF1F5F9),
            Color(0xFF64748B),
            Icons.Default.CheckCircle
        )
        VerificationLevel.PHONE_VERIFIED -> Triple(
            Color(0xFFEFF6FF),
            Color(0xFF1D4ED8),
            Icons.Default.Verified
        )
        VerificationLevel.IDENTITY_VERIFIED -> Triple(
            Color(0xFFEEF2FF),
            Color(0xFF4338CA),
            Icons.Default.Security
        )
        VerificationLevel.SKILL_VERIFIED -> Triple(
            Color(0xFFECFDF5),
            Color(0xFF047857),
            Icons.Default.Verified
        )
        VerificationLevel.TRUSTED_WORKER -> Triple(
            Color(0xFFFEF3C7),
            Color(0xFFB45309),
            Icons.Default.Star
        )
    }

    Row(
        modifier = modifier
            .background(color = bgColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = level.title,
            tint = textColor,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = level.title,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
