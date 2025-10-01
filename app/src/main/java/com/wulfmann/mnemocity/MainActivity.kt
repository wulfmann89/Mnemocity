package com.wulfmann.mnemocity

import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.jakewharton.threetenabp.AndroidThreeTen
import com.wulfmann.mnemocity.logic.input.SpeechInputManager
import com.wulfmann.mnemocity.ui.AppNavHost
import com.wulfmann.mnemocity.ui.SessionScreenPreviewOnly
import com.wulfmann.mnemocity.ui.theme.MnemocityTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)

        val speechLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (spokenText != null) {
                SpeechInputManager.handleSpokenText(spokenText, context = this)
            }
        }


        enableEdgeToEdge()
        setContent {
            MnemocityTheme {
                val navController = rememberNavController()

                AppNavHost(
                    navController = navController,
                    speechLauncher = speechLauncher
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSessionScreen() {
    SessionScreenPreviewOnly()
}