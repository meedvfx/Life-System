package com.meedz.lifeos.ui.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.meedz.lifeos.domain.models.Habit
import com.meedz.lifeos.ui.components.GlassCard
import com.meedz.lifeos.ui.theme.*
import java.util.*

@Composable
fun HabitsRoute(viewModel: HabitsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    HabitsScreen(
        uiState = uiState,
        onHabitToggled = viewModel::toggleHabit
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    uiState: HabitsUiState,
    onHabitToggled: (Habit, Boolean) -> Unit
) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Habits", 
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
                onClick = { /* TODO implement add habit */ },
                containerColor = AccentRose,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "New Habit")
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (uiState.activeHabits.isEmpty()) {
                    item { 
                        Text(
                            "No habits tracked yet. Start small!",
                            style = AppTypography.bodyMedium,
                            color = TextTertiary,
                            modifier = Modifier.padding(top = 20.dp)
                        ) 
                    }
                } else {
                    items(uiState.activeHabits, key = { it.id }) { habit ->
                        HabitDetailCard(
                            habit = habit, 
                            onToggle = { isCompleted -> onHabitToggled(habit, isCompleted) }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun HabitDetailCard(habit: Habit, onToggle: (Boolean) -> Unit) {
    val isCompletedToday = isCompletedToday(habit.lastCompletedDate)

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.name,
                        style = AppTypography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = habit.frequency,
                        style = AppTypography.labelMedium,
                        color = AccentRose
                    )
                }

                // Custom Check Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isCompletedToday) AccentRose else DarkSurfaceElevated)
                        .clickable { onToggle(!isCompletedToday) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompletedToday) {
                        Text("✓", color = Color.White, style = AppTypography.titleLarge)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Streak Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.Add, // Placeholder for fire
                    contentDescription = "Streak",
                    tint = if (habit.streak > 0) AccentRose else TextTertiary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${habit.streak} Day Streak",
                    style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (habit.streak > 0) TextPrimary else TextTertiary
                )
            }
        }
    }
}

private fun isCompletedToday(lastCompletedDate: Long?): Boolean {
    if (lastCompletedDate == null) return false
    val cal1 = Calendar.getInstance().apply { timeInMillis = lastCompletedDate }
    val cal2 = Calendar.getInstance()
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
           cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
