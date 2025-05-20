package com.expensetracker.exception

import com.expensetracker.payload.response.MessageResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException, request: WebRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity(MessageResponse(ex.message ?: "Resource not found"), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException, request: WebRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity(MessageResponse(ex.message ?: "Access denied"), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException, request: WebRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity(MessageResponse("Invalid username or password"), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<MessageResponse> {
        val errors = ex.bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity(MessageResponse("Validation error: $errors"), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception, request: WebRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity(MessageResponse("An unexpected error occurred: ${ex.message}"), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}