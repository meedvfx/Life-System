package com.meedz.lifeos.ui.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meedz.lifeos.domain.models.Transaction
import com.meedz.lifeos.domain.models.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class FinanceUiState(
    val transactions: List<Transaction> = emptyList(),
    val totalBalance: Double = 0.0,
    val monthlyExpenses: Double = 0.0,
    val isLoading: Boolean = false
)

@HiltViewModel
class FinanceViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FinanceUiState())
    val uiState: StateFlow<FinanceUiState> = _uiState.asStateFlow()

    init {
        loadFinances()
    }

    private fun loadFinances() {
        _uiState.update { it.copy(isLoading = true) }
        
        val mockTransactions = listOf(
            Transaction(id = 1, amount = 3500.0, category = "Salary", type = TransactionType.INCOME),
            Transaction(id = 2, amount = 120.50, category = "Groceries", type = TransactionType.EXPENSE, dateMillis = System.currentTimeMillis() - 86400000L),
            Transaction(id = 3, amount = 45.0, category = "Subscriptions", type = TransactionType.EXPENSE, dateMillis = System.currentTimeMillis() - (86400000L * 3))
        )
        
        val income = mockTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val expenses = mockTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        
        _uiState.update { 
            it.copy(
                transactions = mockTransactions,
                totalBalance = income - expenses,
                monthlyExpenses = expenses,
                isLoading = false
            ) 
        }
    }
}
