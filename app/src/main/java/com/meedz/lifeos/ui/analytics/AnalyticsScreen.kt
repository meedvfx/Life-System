package com.meedz.lifeos.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meedz.lifeos.domain.models.ProductivityMetrics
import com.meedz.lifeos.ui.components.GlassCard
import com.meedz.lifeos.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AnalyticsRoute(viewModel: AnalyticsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    AnalyticsScreen(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(uiState: AnalyticsUiState) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Analytics", 
                        style = AppTypography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    OverviewStatCard(score = uiState.averageScore)
                }

                item {
                    Text(
                        text = "Weekly Productivity",
                        style = AppTypography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                item {
                    WeeklyBarChart(metrics = uiState.weeklyMetrics)
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun OverviewStatCard(score: Int) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 24.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.TrendingUp,
                contentDescription = null,
                tint = AccentEmerald,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Weekly Average Score",
                style = AppTypography.titleLarge,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$score",
                style = AppTypography.displayLarge.copy(fontSize = 64.sp),
                color = AccentEmerald
            )
            Text(
                text = "Great job! You're in the top 10% of your performance.",
                style = AppTypography.bodyMedium,
                color = TextTertiary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun WeeklyBarChart(metrics: List<ProductivityMetrics>) {
    if (metrics.isEmpty()) return
    val sdf = SimpleDateFormat("E", Locale.getDefault())

    GlassCard(
        modifier = Modifier.fillMaxWidth().height(250.dp),
        cornerRadius = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            metrics.forEach { metric ->
                val dayName = sdf.format(Date(metric.dateMillis))
                val heightWeight = (metric.overallScore / 100f).coerceIn(0.1f, 1f)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    // Bar
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(heightWeight)
                            .width(16.dp)
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(AccentBlue)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = dayName,
                        style = AppTypography.labelMedium,
                        color = TextTertiary
                    )
                }
            }
        }
    }
}
