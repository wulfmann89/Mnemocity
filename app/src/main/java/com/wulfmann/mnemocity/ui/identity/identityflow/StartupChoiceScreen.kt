package com.wulfmann.mnemocity.ui.identity.identityflow

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun StartupChoiceScreen(
    onCreateAccount: () -> Unit,
    onLogin: () -> Unit,
    onExploreAsGuest: () -> Unit
) {
    Column {
        Button(onClick = onCreateAccount) { Text("Create Account") }
        Button(onClick = onLogin) { Text("Login") }
        Button(onClick = onExploreAsGuest) { Text("Explore as a Guest") }
    }
}