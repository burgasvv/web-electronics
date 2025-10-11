package org.burgas.webelectronics.entity.identity

import org.springframework.security.core.GrantedAuthority

enum class Authority : GrantedAuthority {

    ADMIN, USER;

    override fun getAuthority(): String? {
        return this.name
    }
}