package com.wulfmann.mnemocity.session

import android.content.Context
import android.util.Log
import com.wulfmann.mnemocity.logic.session.AuthMethod
import com.wulfmann.mnemocity.logic.session.IdentityPhase
import com.wulfmann.mnemocity.logic.session.SessionOrigin
import com.wulfmann.mnemocity.logic.session.UserSession
import com.wulfmann.mnemocity.security.SecureImplementRepository
import com.wulfmann.mnemocity.ui.identity.UserIdentity

class AccountFlowManager(private val context: Context) {

    private val secureRepo = SecureImplementRepository(context)

    fun createAccount(email: String, password: String): Boolean {
        // Simulate account creation logic
        val success = simulateAccountCreation(email, password)

        if (success) {
            val token = generateToken(email)
            secureRepo.saveSecure("token", token)
            Log.d("AccountFlowManager", "Token saved: $token")
        } else {
            Log.d("AccountFlowManager", "Account creation failed")
        }

        return success
    }

    private fun simulateAccountCreation(email: String, password: String): Boolean {
        // Replace with real network call or Firebase logic
        return email.isNotBlank() && password.length >= 6
    }

    private fun generateToken(email: String?): String {
        // Replace with secure token generation logic
        val seed = email ?: "anon"
        return "token_${email.hashCode()}_${System.currentTimeMillis()}"
    }

    fun registerIdentity(identity: UserIdentity) {
        secureRepo.saveUserIdentity(identity)
        val token = generateToken(identity.email)
        secureRepo.saveSecure("token", token)

        UserSession.initializeSession(
            UserSession(
                uid = token,
                origin = SessionOrigin.SIGNUP,
                identityPhase = IdentityPhase.LOGGED_IN,
                timestamp = System.currentTimeMillis(),
                authMethod = AuthMethod.TOKEN,
                isVerified = true
            )
        )
    }
}