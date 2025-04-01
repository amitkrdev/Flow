package com.none.flow.data.repository.impl

import com.none.flow.data.model.User
import com.none.flow.data.repository.UserAccountRepository
import com.none.flow.data.service.UserAccountService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserAccountRepositoryImpl @Inject constructor(
    private val accountService: UserAccountService,
) : UserAccountRepository {

    override val currentUserId: String
        get() = accountService.authenticatedUserId

    override fun isUserAuthenticated(): Boolean {
        return accountService.isUserAuthenticated()
    }

    override fun fetchUserProfile(): User {
        return accountService.fetchUserProfile()
    }

    override suspend fun signInAnonymously() {
        accountService.signInAnonymously()
    }

    override suspend fun removeUserAccount() {
        accountService.removeUserAccount()
    }
}