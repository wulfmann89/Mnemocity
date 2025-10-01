package com.wulfmann.mnemocity.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.wulfmann.mnemocity.core.intake.IntakeModule


@Composable
fun UserIdentityScreen(
    onContinueAsGuest: () -> Unit,
    onCreateAccount: (IntakeModule.UserIdentity) -> Unit,
    onLogin: () -> Unit
) {
    var preferredName by remember { mutableStateOf("") }
    var pronouns by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Welcome!  Let's personalize your experience!")

        TextField(
            value = preferredName,
            onValueChange = {
                preferredName = it
                if (showError && preferredName.isNotBlank()) showError = false
            },
            label = { Text("Preferred Name") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { /* move to next field */ })
        )


        TextField(
            value = pronouns,
            onValueChange = { pronouns = it },
            label = { Text("Pronouns (Optional)") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /* hide keyboard */ })
        )

        if (showError) {
            Text(
                text = "Preferred name is required.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            keyboardController?.hide()
            if (preferredName.isBlank()) {
                showError = true
            } else {
                val identity = IntakeModule.UserIdentity(
                    preferredName = preferredName,
                    pronouns = pronouns.ifBlank { null },
                    currentFocus = "Launching a project",
                    neuroType = null
                )
                onCreateAccount(identity)
            }
        }) {
            Text("CreateAccount")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onContinueAsGuest) {
            Text("Continue as Guest")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onLogin) {
            Text("Login")
        }
    }
}