package com.expensetracker.payload.response

import com.expensetracker.model.Expense
import com.expensetracker.model.ExpenseCategory
import java.math.BigDecimal
import java.time.LocalDate

data class ExpenseResponse(
    val id: Long,
    val title: String,
    val amount: BigDecimal,
    val currency: String,
    val category: ExpenseCategory,
    val date: LocalDate,
    val userId: Long
) {
    companion object {
        fun fromEntity(expense: Expense): ExpenseResponse {
            return ExpenseResponse(
                id = expense.id,
                title = expense.title,
                amount = expense.amount,
                currency = expense.currency,
                category = expense.category,
                date = expense.date,
                userId = expense.user.id
            )
        }
    }
}
