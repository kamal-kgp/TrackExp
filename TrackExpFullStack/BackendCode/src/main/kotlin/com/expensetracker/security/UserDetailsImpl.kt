package com.expensetracker.security

import com.expensetracker.model.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    val id: Long,
    private val username: String,
    val email: String,
    @JsonIgnore private val password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    companion object {
        fun build(user: User): UserDetailsImpl {
            val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

            return UserDetailsImpl(
                id = user.id,
                username = user.username,
                email = user.email,
                password = user.password,
                authorities = authorities
            )
        }
    }
}