package com.none.flow.data.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.none.flow.data.model.User
import com.none.flow.data.service.FlowPreferencesDataSource.Companion
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAccountService @Inject constructor(
    private val auth: FirebaseAuth
) {
    val authenticatedUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    fun fetchUserProfile(): User {
        return auth.currentUser.toUser()
    }

    suspend fun signInAnonymously() {
        try {
            auth.signInAnonymously().await()
        } catch (e: Exception){
            Log.e(LOG_TAG, "Error creating user: ${e.message}", e)
        }
    }

    suspend fun removeUserAccount() {
        try {
            auth.currentUser?.delete()?.await()
        } catch (e: Exception){
            Log.e(LOG_TAG, "Error deleting user: ${e.message}", e)
        }
    }

    private fun FirebaseUser?.toUser(): User {
        return if (this == null) User() else User(
            uid = this.uid,
            displayName = this.displayName.orEmpty(),
            isAnonymous = this.isAnonymous
        )
    }

    companion object {
        private const val LOG_TAG = "UserAccountService"
    }
}