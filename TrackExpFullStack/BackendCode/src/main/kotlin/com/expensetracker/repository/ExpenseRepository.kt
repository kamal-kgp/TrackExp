package com.expensetracker.repository

import com.expensetracker.model.Expense
import com.expensetracker.model.ExpenseCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.List
import java.math.BigDecimal

@Repository
interface ExpenseRepository : JpaRepository<Expense, Long> {
    // Find expenses by user id
    fun findByUserId(userId: Long): List<Expense>

    // Find expenses by user id and date between
    fun findByUserIdAndDateBetween(userId: Long, startDate: LocalDate, endDate: LocalDate): List<Expense>

    // Find expenses by user id and category
    fun findByUserIdAndCategory(userId: Long, category: ExpenseCategory): List<Expense>

    // Find expenses by user id, category and date between
    fun findByUserIdAndCategoryAndDateBetween(
        userId: Long,
        category: ExpenseCategory,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Expense>

    // Find expenses for today
    fun findByUserIdAndDate(userId: Long, date: LocalDate): List<Expense>

    // Search expenses by title (case-insensitive)
    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND LOWER(e.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    fun searchByTitle(userId: Long, searchTerm: String): List<Expense>

    // Get total amount by user id and date range
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND e.date BETWEEN :startDate AND :endDate")
    fun getTotalAmountByDateRange(userId: Long, startDate: LocalDate, endDate: LocalDate): BigDecimal?
}