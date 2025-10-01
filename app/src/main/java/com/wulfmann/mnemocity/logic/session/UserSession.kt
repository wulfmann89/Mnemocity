package com.wulfmann.mnemocity.logic.session

data class UserSession(
    val uid: String,
    val origin: SessionOrigin,
    val identityPhase: IdentityPhase,
    val timestamp: Long = System.currentTimeMillis(),
    val authMethod: AuthMethod,
    val isVerified: Boolean
) {
    companion object {
        private var currentSession: UserSession? = null

        fun initializeSession(session: UserSession) {
            currentSession = session
            // Optional log or trigger analytics
        }

        fun getCurrentSession(): UserSession? = currentSession
    }
}
