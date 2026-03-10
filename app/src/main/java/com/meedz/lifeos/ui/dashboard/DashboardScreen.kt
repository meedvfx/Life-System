package com.meedz.lifeos.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meedz.lifeos.domain.models.Habit
import com.meedz.lifeos.domain.models.Task

@Composable
fun DashboardRoute(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    DashboardScreen(
        uiState = uiState,
        onHabitToggled = viewModel::onHabitToggled
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onHabitToggled: (Habit, Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ProductivityCard(score = uiState.productivityScore)
                }

                item {
                    Text("Today's Tasks", style = MaterialTheme.typography.titleMedium)
                }
                
                if (uiState.todayTasks.isEmpty()) {
                    item { Text("No tasks for today. Relax!", style = MaterialTheme.typography.bodyMedium) }
                } else {
                    items(uiState.todayTasks, key = { it.id }) { task ->
                        TaskItem(task = task)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Habits", style = MaterialTheme.typography.titleMedium)
                }

                if (uiState.habits.isEmpty()) {
                    item { Text("No active habits.", style = MaterialTheme.typography.bodyMedium) }
                } else {
                    items(uiState.habits, key = { it.id }) { habit ->
                        HabitItem(habit = habit, onToggle = { isChecked -> onHabitToggled(habit, isChecked) })
                    }
                }
            }
        }
    }
}

@Composable
fun ProductivityCard(score: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Productivity Score", style = MaterialTheme.typography.labelMedium)
            Text("$score", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isCompleted, onCheckedChange = { /* TODO Use case to update completion */ })
            Spacer(modifier = Modifier.width(8.dp))
            Text(task.title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun HabitItem(habit: Habit, onToggle: (Boolean) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(habit.name, style = MaterialTheme.typography.bodyLarge)
                Text("Streak: ${habit.streak}", style = MaterialTheme.typography.bodySmall)
            }
            Switch(checked = habit.streak > 0, onCheckedChange = onToggle)
        }
    }
}
