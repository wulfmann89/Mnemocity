package com.wulfmann.mnemocity.data

import com.wulfmann.mnemocity.security.SecureImplementRepository
import com.wulfmann.mnemocity.ui.identity.UserIdentity


/**
 * Handles secure persistence of user identity, session tokens, and trait data.
 * Modular and clear for onboarding and session flows.
 **/
class UserRepository(val secureRepo: SecureImplementRepository) {
    fun getUserIdentity(): UserIdentity? = secureRepo.loadUserIdentity()
    fun saveUserIdentity(identity: UserIdentity) = secureRepo.saveUserIdentity(identity)
    fun saveToken(token: String) {
        secureRepo.saveSecure("token", token)
    }
// add accessors for other traits as needed
}