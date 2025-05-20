package com.expensetracker.service

import com.expensetracker.exception.ResourceNotFoundException
import com.expensetracker.model.Expense
import com.expensetracker.model.ExpenseCategory
import com.expensetracker.payload.request.ExpenseRequest
import com.expensetracker.payload.response.ExpenseSummaryResponse
import com.expensetracker.repository.ExpenseRepository
import com.expensetracker.repository.UserRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.util.List
import java.time.temporal.TemporalAdjusters.firstDayOfMonth
import java.time.temporal.TemporalAdjusters.lastDayOfMonth

@Service
class ExpenseService(
    private val expenseRepository: ExpenseRepository,
    private val userRepository: UserRepository
) {

    fun createExpense(expenseRequest: ExpenseRequest, userId: Long): Expense {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found with id: $userId") }

        val expense = Expense(
            title = expenseRequest.title,
            amount = expenseRequest.amount,
            currency = expenseRequest.currency,
            category = expenseRequest.category,
            date = expenseRequest.date,
            user = user
        )

        return expenseRepository.save(expense)
    }

    fun getAllExpensesByUserId(userId: Long): List<Expense> {
        return expenseRepository.findByUserId(userId)
    }

    fun getExpenseByIdAndUserId(expenseId: Long, userId: Long): Expense {
        val expense = expenseRepository.findById(expenseId)
            .orElseThrow { ResourceNotFoundException("Expense not found with id: $expenseId") }

        if (expense.user.id != userId) {
            throw AccessDeniedException("You don't have permission to access this expense")
        }

        return expense
    }

    fun updateExpense(expenseId: Long, expenseRequest: ExpenseRequest, userId: Long): Expense {
        val expense = getExpenseByIdAndUserId(expenseId, userId)

        val updatedExpense = expense.copy(
            title = expenseRequest.title,
            amount = expenseRequest.amount,
            category = expenseRequest.category,
            date = expenseRequest.date
        )

        return expenseRepository.save(updatedExpense)
    }

    fun deleteExpense(expenseId: Long, userId: Long) {
        val expense = getExpenseByIdAndUserId(expenseId, userId)
        expenseRepository.delete(expense)
    }

    fun filterExpenses(
        userId: Long,
        category: ExpenseCategory?,
        startDate: LocalDate?,
        endDate: LocalDate?,
        searchTerm: String?
    ): List<Expense> {
        // If search term is provided, search by title
        if (!searchTerm.isNullOrBlank()) {
            return expenseRepository.searchByTitle(userId, searchTerm)
        }

        // Filter by category and date range
        if (category != null && startDate != null && endDate != null) {
            return expenseRepository.findByUserIdAndCategoryAndDateBetween(userId, category, startDate, endDate)
        }

        // Filter by date range only
        if (startDate != null && endDate != null) {
            return expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate)
        }

        // Filter by category only
        if (category != null) {
            return expenseRepository.findByUserIdAndCategory(userId, category)
        }

        // Return all expenses if no filters provided
        return expenseRepository.findByUserId(userId)
    }

    fun getExpensesByTimeFrame(userId: Long, timeFrame: String): List<Expense> {
        val today = LocalDate.now()

        return when (timeFrame.lowercase()) {
            "today" -> {
                expenseRepository.findByUserIdAndDate(userId, today)
            }
            "week" -> {
                val startOfWeek = today.minusDays(today.dayOfWeek.value - 1L)
                val endOfWeek = startOfWeek.plusDays(6)
                expenseRepository.findByUserIdAndDateBetween(userId, startOfWeek, endOfWeek)
            }
            "month" -> {
                val startOfMonth = today.with(firstDayOfMonth())
                val endOfMonth = today.with(lastDayOfMonth())
                expenseRepository.findByUserIdAndDateBetween(userId, startOfMonth, endOfMonth)
            }
            else -> {
                throw IllegalArgumentException("Invalid time frame: $timeFrame. Supported values are 'today', 'week', 'month'")
            }
        }
    }

    fun getExpenseSummary(userId: Long, startDate: LocalDate, endDate: LocalDate): ExpenseSummaryResponse {
        val expenses = expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate)

        if (expenses.isEmpty()) {
            return ExpenseSummaryResponse(BigDecimal.ZERO, emptyMap())
        }

        val total = expenses.fold(BigDecimal.ZERO) { acc, expense -> acc.add(expense.amount) }

        val categorySummary = expenses
            .groupBy { it.category }
            .mapValues { entry ->
                entry.value.fold(BigDecimal.ZERO) { acc, expense -> acc.add(expense.amount) }
            }

        return ExpenseSummaryResponse(total, categorySummary)
    }
}