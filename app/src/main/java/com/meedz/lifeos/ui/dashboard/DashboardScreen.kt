package com.meedz.lifeos.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meedz.lifeos.domain.models.Habit
import com.meedz.lifeos.domain.models.Task
import com.meedz.lifeos.ui.components.GlassCard
import com.meedz.lifeos.ui.theme.*

@Composable
fun DashboardRoute(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    DashboardScreen(
        uiState = uiState,
        onHabitToggled = viewModel::onHabitToggled,
        onTaskToggled = { /* TODO implement task toggle in VM */ }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onHabitToggled: (Habit, Boolean) -> Unit,
    onTaskToggled: (Task) -> Unit
) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Life OS", 
                        style = AppTypography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
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
                    Spacer(modifier = Modifier.height(8.dp))
                    ProductivityCard(score = uiState.productivityScore)
                }

                item {
                    SectionHeader("Today's Focus")
                }
                
                if (uiState.todayTasks.isEmpty()) {
                    item { EmptyStateText("No tasks scheduled for today.") }
                } else {
                    items(uiState.todayTasks, key = { it.id }) { task ->
                        TaskItem(task = task, onToggle = { onTaskToggled(task) })
                    }
                }

                item {
                    SectionHeader("Daily Habits")
                }

                if (uiState.habits.isEmpty()) {
                    item { EmptyStateText("No habits tracked yet.") }
                } else {
                    items(uiState.habits, key = { it.id }) { habit ->
                        HabitItem(habit = habit, onToggle = { isChecked -> onHabitToggled(habit, isChecked) })
                    }
                }
                
                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = AppTypography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        color = TextPrimary,
        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
    )
}

@Composable
fun EmptyStateText(message: String) {
    Text(
        text = message,
        style = AppTypography.bodyMedium,
        color = TextTertiary,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun ProductivityCard(score: Int) {
    val gradient = Brush.linearGradient(
        colors = listOf(AccentBlue.copy(alpha = 0.8f), AccentPurple.copy(alpha = 0.8f))
    )
    
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Productivity Score",
                    style = AppTypography.labelMedium.copy(letterSpacing = 1.sp),
                    color = TextPrimary.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$score",
                        style = AppTypography.displayLarge.copy(fontSize = 48.sp),
                        color = Color.White
                    )
                    Text(
                        text = "/ 100",
                        style = AppTypography.titleLarge,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onToggle: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onToggle,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (task.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = "Toggle Task",
                    tint = if (task.isCompleted) AccentEmerald else TextSecondary,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = task.title,
                style = AppTypography.bodyLarge,
                color = if (task.isCompleted) TextSecondary else TextPrimary
            )
        }
    }
}

@Composable
fun HabitItem(habit: Habit, onToggle: (Boolean) -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = habit.streak.toString(),
                    style = AppTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = AccentRose
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = TextPrimary
                )
                Text(
                    text = "Current Streak",
                    style = AppTypography.labelMedium,
                    color = TextTertiary
                )
            }
            Switch(
                checked = habit.streak > 0, // Simplified for UI representation 
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AccentRose,
                    uncheckedThumbColor = TextSecondary,
                    uncheckedTrackColor = DarkSurfaceElevated
                )
            )
        }
    }
}
