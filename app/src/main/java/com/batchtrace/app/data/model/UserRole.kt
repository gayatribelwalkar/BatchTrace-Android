package com.batchtrace.app.data.model

enum class UserRole {
    ADMIN,
    PRODUCTION,
    QUALITY,
    WAREHOUSE;

    companion object {
        fun fromString(value: String): UserRole? {
            return entries.find {
                it.name.equals(value, ignoreCase = true)
            }
        }
    }
}