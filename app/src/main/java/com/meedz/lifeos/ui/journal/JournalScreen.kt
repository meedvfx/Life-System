package com.meedz.lifeos.ui.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meedz.lifeos.domain.models.JournalEntry
import com.meedz.lifeos.ui.components.GlassCard
import com.meedz.lifeos.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun JournalRoute(viewModel: JournalViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    JournalScreen(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(uiState: JournalUiState) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Journal", 
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
                onClick = { /* TODO start today's entry */ },
                containerColor = AccentPurple,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "New Entry")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentPurple)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (uiState.entries.isEmpty()) {
                    item { 
                        Text(
                            "No journal entries yet. Document your day!",
                            style = AppTypography.bodyMedium,
                            color = TextTertiary,
                            modifier = Modifier.padding(top = 20.dp)
                        ) 
                    }
                } else {
                    items(uiState.entries, key = { it.id }) { entry ->
                        JournalEntryCard(entry = entry)
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun JournalEntryCard(entry: JournalEntry) {
    val dateFormat = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
    val dateString = dateFormat.format(Date(entry.date))

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateString,
                    style = AppTypography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )
                
                // Mood/Energy Mini Indicators
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ScoreIndicator("Mood", entry.moodScore, AccentPurple)
                    ScoreIndicator("Energy", entry.energyScore, AccentEmerald)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = entry.content,
                style = AppTypography.bodyLarge.copy(lineHeight = 26.sp),
                color = TextSecondary,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            
            if (entry.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(entry.tags) { tag ->
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(DarkSurfaceElevated)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "#$tag",
                                style = AppTypography.labelMedium,
                                color = TextTertiary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreIndicator(label: String, score: Int, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "$label:",
            style = AppTypography.labelMedium,
            color = TextTertiary
        )
        Text(
            text = "$score/5",
            style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}
