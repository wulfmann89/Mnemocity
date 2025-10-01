package com.wulfmann.mnemocity.ui.identity.identityflow

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.wulfmann.mnemocity.logic.SessionViewModel
import com.wulfmann.mnemocity.profile.UserProfileManager
import com.wulfmann.mnemocity.security.SecureImplementRepository
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userProfileManager = remember { UserProfileManager(context) }
    val secureRepo = remember { SecureImplementRepository(context) }
    val sessionViewModel = remember { SessionViewModel() }

    LaunchedEffect(Unit) {
        delay(5000)                                                                                         // Ensure splash stays visible

        val route = sessionViewModel.getStartupRoute(context)
        Log.d("NavDebug", "SplashScreen routing to $route")

        navController.navigate(route) {
            popUpTo(0)                                                                                // Clear the back stack
        }
    }

    // Optional: show a loading animation or logo
    Text("Loading your task identity...")
}