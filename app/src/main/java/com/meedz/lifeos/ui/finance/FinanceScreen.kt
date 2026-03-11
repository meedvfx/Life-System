package com.meedz.lifeos.ui.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
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
import com.meedz.lifeos.domain.models.Transaction
import com.meedz.lifeos.domain.models.TransactionType
import com.meedz.lifeos.ui.components.GlassCard
import com.meedz.lifeos.ui.theme.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FinanceRoute(viewModel: FinanceViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    FinanceScreen(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(uiState: FinanceUiState) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Finance", 
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
                onClick = { /* TODO implement add transaction */ },
                containerColor = AccentEmerald,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "New Transaction")
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
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    BalanceOverviewCard(
                        balance = uiState.totalBalance,
                        expenses = uiState.monthlyExpenses
                    )
                }

                item {
                    Text(
                        text = "Recent Transactions",
                        style = AppTypography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (uiState.transactions.isEmpty()) {
                    item { 
                        Text(
                            "No transactions recorded.",
                            style = AppTypography.bodyMedium,
                            color = TextTertiary
                        ) 
                    }
                } else {
                    items(uiState.transactions, key = { it.id }) { transaction ->
                        TransactionItem(transaction = transaction)
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun BalanceOverviewCard(balance: Double, expenses: Double) {
    val gradient = Brush.linearGradient(
        colors = listOf(AccentEmerald.copy(alpha = 0.8f), AccentBlue.copy(alpha = 0.8f))
    )
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Total Balance",
                    style = AppTypography.labelMedium.copy(letterSpacing = 1.sp),
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatter.format(balance),
                    style = AppTypography.displayLarge.copy(fontSize = 40.sp),
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Monthly Expenses",
                            style = AppTypography.labelMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatter.format(expenses),
                            style = AppTypography.titleLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val isExpense = transaction.type == TransactionType.EXPENSE
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
    val amountString = "${if (isExpense) "-" else "+"}${formatter.format(transaction.amount)}"

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
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isExpense) AccentRose.copy(alpha = 0.2f) else AccentEmerald.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isExpense) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                    contentDescription = null,
                    tint = if (isExpense) AccentRose else AccentEmerald
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.category,
                    style = AppTypography.titleLarge.copy(fontSize = 18.sp),
                    color = TextPrimary
                )
                Text(
                    text = dateFormat.format(Date(transaction.dateMillis)),
                    style = AppTypography.labelMedium,
                    color = TextTertiary
                )
            }
            
            Text(
                text = amountString,
                style = AppTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = if (isExpense) TextPrimary else AccentEmerald
            )
        }
    }
}
