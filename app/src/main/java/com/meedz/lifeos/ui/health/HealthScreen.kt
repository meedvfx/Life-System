package com.meedz.lifeos.ui.health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meedz.lifeos.domain.models.HealthMetric
import com.meedz.lifeos.domain.models.HealthMetricType
import com.meedz.lifeos.ui.components.GlassCard
import com.meedz.lifeos.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HealthRoute(viewModel: HealthViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    HealthScreen(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthScreen(uiState: HealthUiState) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Health", 
                        style = AppTypography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO implement add log */ },
                containerColor = AccentRose,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Log Health Data")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentRose)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Top Biometric Widgets
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BiometricWidget(
                            modifier = Modifier.weight(1f),
                            title = "Avg Sleep",
                            value = "${String.format(Locale.US, "%.1f", uiState.averageSleep)}h",
                            icon = Icons.Filled.Bedtime,
                            color = AccentPurple
                        )
                        BiometricWidget(
                            modifier = Modifier.weight(1f),
                            title = "Weight",
                            value = "${uiState.latestWeight}kg",
                            icon = Icons.Filled.Scale,
                            color = AccentBlue
                        )
                    }
                }

                item {
                    Text(
                        text = "Recent Logs",
                        style = AppTypography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (uiState.recentMetrics.isEmpty()) {
                    item { 
                        Text(
                            "No health data logged.",
                            style = AppTypography.bodyMedium,
                            color = TextTertiary
                        ) 
                    }
                } else {
                    items(uiState.recentMetrics, key = { it.id }) { metric ->
                        HealthMetricItem(metric = metric)
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun BiometricWidget(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    GlassCard(
        modifier = modifier.aspectRatio(1f),
        cornerRadius = 24.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column {
                Text(
                    text = title,
                    style = AppTypography.labelMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = AppTypography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 28.sp),
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
fun HealthMetricItem(metric: HealthMetric) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val (icon, color, label) = when (metric.type) {
        HealthMetricType.WEIGHT -> Triple(Icons.Filled.Scale, AccentBlue, "Weight")
        HealthMetricType.SLEEP_HOURS -> Triple(Icons.Filled.Bedtime, AccentPurple, "Sleep")
        HealthMetricType.WORKOUT_MINUTES -> Triple(Icons.Filled.DirectionsRun, AccentRose, "Workout")
        HealthMetricType.WATER_GLASSES -> Triple(Icons.Filled.LocalDrink, AccentEmerald, "Water")
    }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = AppTypography.titleLarge.copy(fontSize = 18.sp),
                    color = TextPrimary
                )
                Text(
                    text = dateFormat.format(Date(metric.dateMillis)),
                    style = AppTypography.labelMedium,
                    color = TextTertiary
                )
            }
            
            Text(
                text = "${metric.value} ${metric.unit}",
                style = AppTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
    }
}
