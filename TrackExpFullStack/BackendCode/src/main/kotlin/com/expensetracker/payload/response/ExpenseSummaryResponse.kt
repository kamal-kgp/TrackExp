package com.expensetracker.payload.response

import com.expensetracker.model.ExpenseCategory
import java.math.BigDecimal

data class ExpenseSummaryResponse(
    val total: BigDecimal,
    val categorySummary: Map<ExpenseCategory, BigDecimal>
)