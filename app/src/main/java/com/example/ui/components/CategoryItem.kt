package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.JobCategory

@Composable
fun CategoryItem(
    category: JobCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon: ImageVector = when (category) {
        JobCategory.MANUFACTURING -> Icons.Default.PrecisionManufacturing
        JobCategory.FACTORY -> Icons.Default.Factory
        JobCategory.OFFICE -> Icons.Default.BusinessCenter
        JobCategory.IT -> Icons.Default.Computer
        JobCategory.DATA_ENTRY -> Icons.Default.Keyboard
        JobCategory.DRIVER -> Icons.Default.DirectionsCar
        JobCategory.DELIVERY -> Icons.Default.LocalShipping
        JobCategory.SALES -> Icons.Default.PointOfSale
        JobCategory.HOTEL -> Icons.Default.Hotel
        JobCategory.SECURITY -> Icons.Default.Shield
        JobCategory.CONSTRUCTION -> Icons.Default.Construction
        JobCategory.OTHER -> Icons.Default.Category
    }

    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val iconBgColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }

    val iconColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier
            .width(88.dp)
            .clickable(onClick = onClick)
            .testTag("category_chip_${category.name.lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = category.displayName,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.displayName,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                ),
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
