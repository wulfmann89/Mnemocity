package com.wulfmann.mnemocity.logic.session

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wulfmann.mnemocity.core.intake.IntakeModule
import com.wulfmann.mnemocity.security.SecureImplementRepository
import com.wulfmann.mnemocity.data.UserRepository
import com.wulfmann.mnemocity.session.AccountFlowManager
import kotlinx.coroutines.launch

fun provideAccountFlowViewModelFactory(appContext: Context): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val secureRepo = SecureImplementRepository(appContext)
            val userRepo = UserRepository(secureRepo)
            return AccountFlowViewModel(appContext, userRepo) as T
        }
    }
}
class AccountFlowViewModel(
    private val appContext: Context,
    private val userRepository: UserRepository
) : ViewModel() {

    fun createAccount(preferredName: String, email: String?) {
        val identity = IntakeModule.UserIdentity(
            preferredName = preferredName,
            email = email ?: "",
            neuroType = "Undefined",
            currentFocus = "Getting Started"
        )

        viewModelScope.launch {
            AccountFlowManager(appContext).registerIdentity(identity)
        }
    }
}