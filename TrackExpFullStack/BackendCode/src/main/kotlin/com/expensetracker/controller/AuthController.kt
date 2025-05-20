package com.expensetracker.controller

import com.expensetracker.model.User
import com.expensetracker.payload.request.LoginRequest
import com.expensetracker.payload.request.SignupRequest
import com.expensetracker.payload.response.JwtResponse
import com.expensetracker.payload.response.MessageResponse
import com.expensetracker.repository.UserRepository
import com.expensetracker.security.JwtTokenProvider
import com.expensetracker.security.UserDetailsImpl
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600) // Fordev, adjust as needed for production
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignupRequest): ResponseEntity<*> {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Username is already taken!"))
        }

        if (userRepository.existsByEmail(signUpRequest.email)) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Email is already in use!"))
        }

        // Create new user's account
        val user = User(
            username = signUpRequest.username,
            email = signUpRequest.email,
            password = passwordEncoder.encode(signUpRequest.password)
            // createdAt and updatedAt will be set by @CreationTimestamp and @UpdateTimestamp
        )

        userRepository.save(user)

        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }

    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtTokenProvider.generateToken(authentication)

        val userDetails = authentication.principal as UserDetailsImpl

        // If you add roles, you would get them here:
        // val roles = userDetails.authorities.map { item -> item.authority }

        return ResponseEntity.ok(
            JwtResponse(
                token = jwt,
                id = userDetails.id,
                username = userDetails.username,
                email = userDetails.email
                // roles = roles // if you have roles
            )
        )
    }
}