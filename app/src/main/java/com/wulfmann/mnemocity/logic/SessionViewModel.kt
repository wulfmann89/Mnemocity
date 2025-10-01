package com.wulfmann.mnemocity.logic

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.wulfmann.mnemocity.ui.identity.UserIdentity
import com.wulfmann.mnemocity.logic.input.SpeechInputManager
import com.wulfmann.mnemocity.logic.session.AuthMethod
import com.wulfmann.mnemocity.logic.session.IdentityPhase
import com.wulfmann.mnemocity.logic.session.SessionOrigin
import com.wulfmann.mnemocity.logic.session.UserSession
import com.wulfmann.mnemocity.security.SecureImplementRepository

class SessionViewModel : ViewModel() {

    private fun initializeSession(
        uid: String = "anon",
        origin: SessionOrigin = SessionOrigin.UNKNOWN,
        identityPhase: IdentityPhase = IdentityPhase.GUEST,
        authMethod: AuthMethod = AuthMethod.NONE,
        isVerified: Boolean = false
    ): UserSession {
        return UserSession(
            uid = uid,
            origin = origin,
            identityPhase = identityPhase,
            timestamp = System.currentTimeMillis(),
            authMethod = authMethod,
            isVerified = isVerified
        )
    }

    fun startSession(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        onResult: (String?) -> Unit
    ) {
        val session = initializeSession(
            origin = SessionOrigin.TASK_FLOW,
            identityPhase = IdentityPhase.UNIDENTIFIED
        )
        UserSession.initializeSession(session)
        SpeechInputManager.listenForTaskRequest(context, launcher, onResult)
    }

    fun getStartupRoute(context: Context): String {
        val repo = SecureImplementRepository(context)
        val token: String? = repo.loadSecure("token")
        val identity = repo.loadUserIdentity()
        val hasNeuroType = identity?.neuroType != null

        return when {
            token.isNullOrBlank() && identity == null -> "STARTUP_CHOICE"
            identity != null && !hasNeuroType -> "USER_IDENTITY_SCREEN"
            else -> "HOME_ANON"
        }
    }

    fun getIdentityPhase(identity: UserIdentity?): IdentityPhase {
        return when {
            identity == null -> IdentityPhase.NEW_USER
            identity.preferredName == "Guest" -> IdentityPhase.ANONYMOUS
            identity.preferredName.isNotBlank() -> IdentityPhase.LOGGED_IN
            else -> IdentityPhase.NEW_USER
        }
    }

    fun loadUserIdentity(context: Context): UserIdentity? {
        val repo = SecureImplementRepository(context)
        return repo.loadUserIdentity()
    }

    fun loadToken(context: Context): String? {
        val repo = SecureImplementRepository(context)
        return repo.loadSecure("token")
    }

    fun clearSession(context: Context) {
        val repo = SecureImplementRepository(context)
        repo.clearSecure("identity")
        repo.clearSecure("token")
    }
}