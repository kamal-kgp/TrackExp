package com.expensetracker.controller

import com.expensetracker.model.Expense
import com.expensetracker.model.ExpenseCategory
import com.expensetracker.payload.request.ExpenseRequest
import com.expensetracker.payload.response.ExpenseResponse
import com.expensetracker.payload.response.ExpenseSummaryResponse
import com.expensetracker.payload.response.MessageResponse
import com.expensetracker.security.UserDetailsImpl
import com.expensetracker.service.ExpenseService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/expenses")
class ExpenseController(private val expenseService: ExpenseService) {

    @PostMapping
    fun createExpense(
        @Valid @RequestBody expenseRequest: ExpenseRequest,
        @AuthenticationPrincipal currentUser: UserDetailsImpl
    ): ResponseEntity<ExpenseResponse> {
        val expense = expenseService.createExpense(expenseRequest, currentUser.id)
        return ResponseEntity(ExpenseResponse.fromEntity(expense), HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllExpenses(@AuthenticationPrincipal currentUser: UserDetailsImpl): ResponseEntity<List<ExpenseResponse>> {
        val expenses = expenseService.getAllExpensesByUserId(currentUser.id)
        return ResponseEntity.ok(expenses.map { ExpenseResponse.fromEntity(it) })
    }

    @GetMapping("/{id}")
    fun getExpenseById(
        @PathVariable id: Long,
        @AuthenticationPrincipal currentUser: UserDetailsImpl
    ): ResponseEntity<ExpenseResponse> {
        val expense = expenseService.getExpenseByIdAndUserId(id, currentUser.id)
        return ResponseEntity.ok(ExpenseResponse.fromEntity(expense))
    }

    @PutMapping("/{id}")
    fun updateExpense(
        @PathVariable id: Long,
        @Valid @RequestBody expenseRequest: ExpenseRequest,
        @AuthenticationPrincipal currentUser: UserDetailsImpl
    ): ResponseEntity<ExpenseResponse> {
        val updatedExpense = expenseService.updateExpense(id, expenseRequest, currentUser.id)
        return ResponseEntity.ok(ExpenseResponse.fromEntity(updatedExpense))
    }

    @DeleteMapping("/{id}")
    fun deleteExpense(
        @PathVariable id: Long,
        @AuthenticationPrincipal currentUser: UserDetailsImpl
    ): ResponseEntity<MessageResponse> {
        expenseService.deleteExpense(id, currentUser.id)
        return ResponseEntity.ok(MessageResponse("Expense deleted successfully"))
    }

    @GetMapping("/filter")
    fun filterExpenses(
        @RequestParam(required = false) category: ExpenseCategory?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?,
        @RequestParam(required = false) searchTerm: String?,
        @AuthenticationPrincipal currentUser: UserDetailsImpl
    ): ResponseEntity<List<ExpenseResponse>> {
        val expenses = expenseService.filterExpenses(currentUser.id, category, startDate, endDate, searchTerm)
        return ResponseEntity.ok(expenses.map { ExpenseResponse.fromEntity(it) })
    }

    @GetMapping("/today")
    fun getTodayExpenses(@AuthenticationPrincipal currentUser: UserDetailsImpl): ResponseEntity<List<ExpenseResponse>> {
        val expenses = expenseService.getExpensesByTimeFrame(currentUser.id, "today")
        return ResponseEntity.ok(expenses.map { ExpenseResponse.fromEntity(it) })
    }

    @GetMapping("/week")
    fun getWeekExpenses(@AuthenticationPrincipal currentUser: UserDetailsImpl): ResponseEntity<List<ExpenseResponse>> {
        val expenses = expenseService.getExpensesByTimeFrame(currentUser.id, "week")
        return ResponseEntity.ok(expenses.map { ExpenseResponse.fromEntity(it) })
    }

    @GetMapping("/month")
    fun getMonthExpenses(@AuthenticationPrincipal currentUser: UserDetailsImpl): ResponseEntity<List<ExpenseResponse>> {
        val expenses = expenseService.getExpensesByTimeFrame(currentUser.id, "month")
        return ResponseEntity.ok(expenses.map { ExpenseResponse.fromEntity(it) })
    }

    @GetMapping("/summary")
    fun getExpenseSummary(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate,
        @AuthenticationPrincipal currentUser: UserDetailsImpl
    ): ResponseEntity<ExpenseSummaryResponse> {
        val summary = expenseService.getExpenseSummary(currentUser.id, startDate, endDate)
        return ResponseEntity.ok(summary)
    }
}