package com.wulfmann.mnemocity.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wulfmann.mnemocity.core.intake.IntakeModule
import com.wulfmann.mnemocity.security.SecureImplementRepository
import com.wulfmann.mnemocity.data.UserRepository

import java.util.UUID

@Composable
fun AnonymousAppScreen(
    userRepository: UserRepository,
    secureRepo: SecureImplementRepository,
    onContinue: () -> Unit,
    onCreateAccount: () -> Unit,
) {
    var hasSaved by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Welcome to Mnemocity!",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "You're welcome to explore without sharing personal info.  Your data will be stored locally and never synced.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val anonymousIdentity = IntakeModule.UserIdentity(
                preferredName = "Guest",
                pronouns = null,
                currentFocus = "Exploring",
                neuroType = null
            )
            userRepository.saveUserIdentity(anonymousIdentity)

            val uuid = UUID.randomUUID().toString()
            secureRepo.saveSecure(key = "anon_id", data = uuid)
            hasSaved = true
            onContinue()
        }) {
            Text("Continue as Guest")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onCreateAccount) {
            Text("Create Account")
        }

        if (hasSaved) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Anonymous identity saved locally.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}