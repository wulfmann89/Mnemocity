package com.wulfmann.mnemocity.ui.identity.intakescreens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wulfmann.mnemocity.core.intake.IntakeModule
import com.wulfmann.mnemocity.core.savestate.model.SaveState
import com.wulfmann.mnemocity.data.AnalyticsService
import com.wulfmann.mnemocity.data.UserRepository
import com.wulfmann.mnemocity.navigation.AppRoutes
import com.wulfmann.mnemocity.profile.UserProfileManager
import com.wulfmann.mnemocity.security.SecureImplementRepository
import com.wulfmann.mnemocity.ui.identity.UserIdentity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class IntakeFlowViewModelFactory(
    val context: Context,
    val userRepository: UserRepository,
    val analyticsService: AnalyticsService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IntakeFlowViewModel::class.java)) {
            return IntakeFlowViewModel(context.applicationContext, userRepository, analyticsService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
@Suppress("unused")
class IntakeFlowViewModel(
    context: Context,
    private val userRepository: UserRepository,
    private val analyticsService: AnalyticsService
) : ViewModel() {
    private val secureRepo = SecureImplementRepository(context.applicationContext)
    // Builder

    private inline fun <reified T : Any> persistFinalizedModule(key: String, data: T) {
        secureRepo.saveSecure(key, data)
    }

    fun saveUserIdentity(identity: IntakeModule.UserIdentity) {
        secureRepo.saveSecure("identity", identity)
    }

    fun updateUserIdentity(state: SaveState) {
        // Save to repo or local state
    }

    fun finalizeUserIdentity() {
        // Trigger any finalization logic or navigation
    }
    suspend fun getNextRoute(
        secureRepo: SecureImplementRepository,
        userProfileManager: UserProfileManager
    ): String {
        val token = secureRepo.loadSecure<String>("token")
        val identity = secureRepo.loadUserIdentity()
        val hasArchetype = userProfileManager.hasArchetypeProfile()

        return withContext(Dispatchers.IO) {
            when {
                token == null && identity == null -> AppRoutes.STARTUP_CHOICE
                identity != null && identity.neuroType == null -> AppRoutes.USER_IDENTITY_SCREEN
                !hasArchetype -> AppRoutes.TRAIT_SEEDING_SCREEN
                else -> if (token != null) AppRoutes.HOME_AUTH else AppRoutes.HOME_ANON
            }
        }
    }
}