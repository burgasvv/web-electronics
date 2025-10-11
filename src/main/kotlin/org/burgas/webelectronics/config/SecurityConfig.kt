package org.burgas.webelectronics.config

import org.burgas.webelectronics.entity.identity.Authority
import org.burgas.webelectronics.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {

    private val passwordEncoder: PasswordEncoder
    private val customUserDetailsService: CustomUserDetailsService

    constructor(passwordEncoder: PasswordEncoder, customUserDetailsService: CustomUserDetailsService) {
        this.passwordEncoder = passwordEncoder
        this.customUserDetailsService = customUserDetailsService
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val daoAuthenticationProvider = DaoAuthenticationProvider(this.customUserDetailsService)
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder)
        return ProviderManager(daoAuthenticationProvider)
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { csrfConfigurer -> csrfConfigurer.csrfTokenRequestHandler(XorCsrfTokenRequestAttributeHandler()) }
            .cors { corsConfigurer -> corsConfigurer.configurationSource(UrlBasedCorsConfigurationSource()) }
            .httpBasic { httpBasicConfigurer ->
                httpBasicConfigurer.securityContextRepository(
                    RequestAttributeSecurityContextRepository()
                )
            }
            .authenticationManager(this.authenticationManager())
            .authorizeHttpRequests { requestMatcherRegistry ->

                requestMatcherRegistry.requestMatchers(
                    "/api/v1/security/csrf-token",

                    "/api/v1/identities/create",

                    "/api/v1/images/by-id", "/api/v1/images/create", "/api/v1/images/change", "/api/v1/images/delete"
                )
                    .permitAll()

                    .requestMatchers(
                        "/api/v1/identities/by-id", "/api/v1/identities/update",
                        "/api/v1/identities/delete", "/api/v1/identities/change-password",
                        "/api/v1/identities/create-image", "/api/v1/identities/change-image",
                        "/api/v1/identities/delete-image"
                    )
                    .hasAnyAuthority(Authority.ADMIN.name, Authority.USER.name)

                    .requestMatchers(
                        "/api/v1/identities", "/api/v1/identities/enable-disable"
                    )
                    .hasAnyAuthority(Authority.ADMIN.name)
            }
            .build()
    }
}