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

    companion object {
        private const val API_URL = "/api/v1"
    }

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
                    "$API_URL/security/csrf-token",

                    "$API_URL/identities/create",

                    "$API_URL/images/by-id", "$API_URL/images/create", "$API_URL/images/change", "$API_URL/images/delete",

                    "$API_URL/stores", "$API_URL/stores/by-id",

                    "$API_URL/categories", "$API_URL/categories/by-id",

                    "$API_URL/products", "$API_URL/products/by-id",
                )
                    .permitAll()

                    .requestMatchers(
                        "$API_URL/identities/by-id", "$API_URL/identities/update",
                        "$API_URL/identities/delete", "$API_URL/identities/change-password",
                        "$API_URL/identities/create-image", "$API_URL/identities/change-image",
                        "$API_URL/identities/delete-image",

                        "$API_URL/buckets/by-id", "$API_URL/buckets/add-product", "$API_URL/buckets/remove-product",
                    )
                    .hasAnyAuthority(Authority.ADMIN.name, Authority.USER.name)

                    .requestMatchers(
                        "$API_URL/identities", "$API_URL/identities/enable-disable",

                        "$API_URL/stores/create-update", "$API_URL/stores/delete",

                        "$API_URL/categories/create-update", "$API_URL/categories/delete",
                        "$API_URL/categories/create-image", "$API_URL/categories/change-image", "$API_URL/categories/delete-image",

                        "$API_URL/products/create-update", "$API_URL/products/delete",
                        "$API_URL/products/create-image", "$API_URL/products/change-image", "$API_URL/products/delete-image",

                        "$API_URL/store-product/add", "$API_URL/store-product/delete",
                    )
                    .hasAnyAuthority(Authority.ADMIN.name)
            }
            .build()
    }
}