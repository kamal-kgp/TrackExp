package com.expensetracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages =["com.expensetracker.repository"])
class ExpenseTrackerApplication

fun main(args: Array<String>) {
    runApplication<ExpenseTrackerApplication>(*args)
}