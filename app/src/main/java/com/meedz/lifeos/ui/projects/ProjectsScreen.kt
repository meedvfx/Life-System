package com.meedz.lifeos.ui.projects

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
import com.meedz.lifeos.domain.models.Project
import com.meedz.lifeos.ui.components.GlassCard
import com.meedz.lifeos.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProjectsRoute(viewModel: ProjectsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    ProjectsScreen(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(uiState: ProjectsUiState) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Projects", 
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
                onClick = { /* TODO implement add project */ },
                containerColor = AccentBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "New Project")
            }
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (uiState.activeProjects.isEmpty()) {
                    item { 
                        Text(
                            "No active projects.",
                            style = AppTypography.bodyMedium,
                            color = TextTertiary,
                            modifier = Modifier.padding(top = 20.dp)
                        ) 
                    }
                } else {
                    items(uiState.activeProjects, key = { it.id }) { project ->
                        ProjectItem(project = project)
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun ProjectItem(project: Project) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.title,
                    style = AppTypography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )
                
                // Progress Chip
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(AccentBlue.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${project.progress}%",
                        style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AccentBlue
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = project.description,
                style = AppTypography.bodyMedium,
                color = TextSecondary,
                maxLines = 2
            )
            
            if (project.deadLine != null) {
                Spacer(modifier = Modifier.height(12.dp))
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                Text(
                    text = "Due: ${dateFormat.format(Date(project.deadLine))}",
                    style = AppTypography.labelMedium,
                    color = AccentRose
                )
            }
        }
    }
}
