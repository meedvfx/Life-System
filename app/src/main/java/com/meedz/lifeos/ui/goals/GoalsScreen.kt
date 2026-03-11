package com.meedz.lifeos.ui.goals

import androidx.compose.foundation.background
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
import com.meedz.lifeos.domain.models.Goal
import com.meedz.lifeos.ui.components.GlassCard
import com.meedz.lifeos.ui.theme.*

@Composable
fun GoalsRoute(viewModel: GoalsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    GoalsScreen(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(uiState: GoalsUiState) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Goals", 
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
                onClick = { /* TODO implement add goal */ },
                containerColor = AccentEmerald,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Goal")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentEmerald)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Active Goals",
                        style = AppTypography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                    )
                }

                if (uiState.activeGoals.isEmpty()) {
                    item { 
                        Text(
                            "No active goals.",
                            style = AppTypography.bodyMedium,
                            color = TextTertiary,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) 
                    }
                } else {
                    items(uiState.activeGoals, key = { it.id }) { goal ->
                        GoalItem(goal = goal)
                    }
                }
                
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun GoalItem(goal: Goal) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = goal.title,
                    style = AppTypography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )
                Text(
                    text = "${goal.progress}%",
                    style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = AccentEmerald
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = goal.description,
                style = AppTypography.bodyMedium,
                color = TextSecondary,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Custom Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceElevated)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(goal.progress.toFloat() / 100f)
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(AccentEmerald)
                )
            }
        }
    }
}
