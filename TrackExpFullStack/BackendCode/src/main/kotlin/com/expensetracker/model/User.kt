package com.expensetracker.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "users",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["username"]),
        UniqueConstraint(columnNames = ["email"])
    ])
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long=0,

    @NotBlank
    @Size(max=20)
    val username: String,

    @NotBlank
    @Size(max=100)
    @Email
    val email: String,

    @NotBlank
    @Size(max=50)
    val password: String,

    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)