package com.wulfmann.mnemocity.ui.identity.identityflow

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wulfmann.mnemocity.logic.session.AccountFlowViewModel

@Composable
fun AccountCreationFlowScreen(
    viewModel: AccountFlowViewModel,
    onComplete: () -> Unit
) {
    val preferredName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(24.dp)) {
        Text("Let's create your account", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = preferredName.value,
            onValueChange = { preferredName.value = it },
            label = { Text("Preferred Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.createAccount(preferredName.value, email.value)
                onComplete()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finish Setup")
        }
    }
}