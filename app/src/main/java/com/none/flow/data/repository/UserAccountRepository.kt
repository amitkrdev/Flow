package com.none.flow.data.repository

import com.none.flow.data.model.User

interface UserAccountRepository {
    val currentUserId: String
    fun isUserAuthenticated(): Boolean
    fun fetchUserProfile(): User
    suspend fun signInAnonymously()
    suspend fun removeUserAccount()
}