package com.expensetracker.payload.request

import com.expensetracker.model.ExpenseCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

data class ExpenseRequest(
    @NotBlank
    val title: String,

    @NotNull
    @Positive
    val amount: BigDecimal,

    @NotBlank
    val currency: String,

    @NotNull
    val category: ExpenseCategory,

    @NotNull
    val date: LocalDate
)