package com.wulfmann.mnemocity.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// A button the user taps to start a sesion
@Composable
fun SessionStartButton(onStart: () -> Unit) {
    Button(
        onClick = onStart,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Start Task Session")
    }
}