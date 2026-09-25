package com.batchtrace.app.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val employeeId: String = "",
    val role: String = "",
    val department: String = "",
    val active: Boolean = true
) {
    fun getUserRole(): UserRole? {
        return UserRole.fromString(role)
    }
}