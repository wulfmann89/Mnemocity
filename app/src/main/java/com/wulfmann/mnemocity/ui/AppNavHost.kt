package com.wulfmann.mnemocity.ui

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.Choreographer
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wulfmann.mnemocity.TaskRepository
import com.wulfmann.mnemocity.data.AnalyticsService
import com.wulfmann.mnemocity.data.UserRepository
import com.wulfmann.mnemocity.diagnostics.DebugOverlayManager
import com.wulfmann.mnemocity.diagnostics.LogcatReader
import com.wulfmann.mnemocity.logic.session.AccountFlowViewModel
import com.wulfmann.mnemocity.logic.session.AuthMethod
import com.wulfmann.mnemocity.logic.session.IdentityPhase
import com.wulfmann.mnemocity.logic.session.SessionOrigin
import com.wulfmann.mnemocity.logic.session.UserSession
import com.wulfmann.mnemocity.logic.session.provideAccountFlowViewModelFactory
import com.wulfmann.mnemocity.navigation.AppRoutes
import com.wulfmann.mnemocity.profile.UserProfileManager
import com.wulfmann.mnemocity.security.SecureImplementRepository
import com.wulfmann.mnemocity.ui.calendar.CalendarScreen
import com.wulfmann.mnemocity.ui.calendar.ScheduleDayScreen
import com.wulfmann.mnemocity.ui.identity.identityflow.AccountCreationFlowScreen
import com.wulfmann.mnemocity.ui.identity.identityflow.SplashScreen
import com.wulfmann.mnemocity.ui.identity.identityflow.StartupChoiceScreen
import com.wulfmann.mnemocity.ui.identity.intakescreens.IntakeFlowViewModel
import com.wulfmann.mnemocity.ui.identity.intakescreens.IntakeFlowViewModelFactory
import com.wulfmann.mnemocity.ui.identity.toState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    speechLauncher: ActivityResultLauncher<Intent>
) {
    val context = LocalContext.current
    val secureRepo = remember { SecureImplementRepository(context) }
    val userRepository = remember { UserRepository(secureRepo) }
    val analyticsService = remember { AnalyticsService() }

    val intakeViewModelFactory = remember {
        IntakeFlowViewModelFactory(context, userRepository, analyticsService)
    }

    val viewModel: IntakeFlowViewModel = viewModel(factory = intakeViewModelFactory)


    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {
        composable(AppRoutes.STARTUP_CHOICE) {
            StartupChoiceScreen(
                onCreateAccount = { navController.navigate(AppRoutes.ACCOUNT_CREATION_FLOW) },
                onLogin = { navController.navigate(AppRoutes.LOGIN_SCREEN) },
                onExploreAsGuest = { navController.navigate(AppRoutes.ANONYMOUS_APP_SCREEN) }
            )
        }

        composable(AppRoutes.SPLASH) {
            SplashScreen(onReady = { hasBaseline ->
                val next = if (hasBaseline) AppRoutes.HOME else AppRoutes.ARCHETYPE_INTAKE_SCREEN
                navController.navigate(next)
            }
            )
            Log.d("NavDebug", "Splash registered")
        }

        composable(AppRoutes.HOME_AUTH) {
            MainScreen(
                onCalendarTap = { selectedDate ->
                    navController.navigate("${AppRoutes.CALENDAR_SCREEN}/$selectedDate")
                },
                taskMap = TaskRepository.taskMap,
                activeTasks = TaskRepository.activeTasks,
                onStartSession = { navController.navigate(AppRoutes.SESSION_SCREEN) },
                onTaskCheck = { /* TODO */ }
            )
            Log.d("NavDebug", "MainScreen (auth) registered")
        }

        composable(AppRoutes.HOME_ANON) {
            MainScreen(
                onCalendarTap = { selectedDate ->
                    navController.navigate("${AppRoutes.CALENDAR_SCREEN}/$selectedDate")
                },
                taskMap = TaskRepository.taskMap,
                activeTasks = TaskRepository.activeTasks,
                onStartSession = { navController.navigate(AppRoutes.SESSION_SCREEN) },
                onTaskCheck = { /* TODO */ }
            )
            Log.d("NavDebug", "MainScreen (anon) registered")
        }

        composable("${AppRoutes.CALENDAR_SCREEN}/{date}") { backStackEntry ->                        // Populates the Calendar Screen
            val context = LocalContext.current
            val dateArg = backStackEntry.arguments?.getString("date")
            val parsedDate = dateArg?.let { LocalDate.parse(it) } ?: LocalDate.now()
            val alarmManager = remember {
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
                CalendarScreen(
                    date = parsedDate,
                    taskMap = TaskRepository.taskMap,
                    onDateSelected = { selectedDate ->
                        navController.navigate("${AppRoutes.CALENDAR_SCREEN}/${selectedDate}")
                    }
                )
            } else {
                // Optional fallback UI or log
                Log.w("AppNavHost", "Exact alarms not permitted.  CalendarScreen not displayed.")
            }
            Log.d("NavDebug", "CalendarScreen registered")
        }

        composable(AppRoutes.SESSION_SCREEN) {
            SessionScreen(
                speechLauncher = speechLauncher,
                onResult = { result ->
                    Log.d("AppNavHost", "Spoken result: $result")
                }
            )
            Log.d("NavDebug", "SessionScreen registered")
        }

        composable("${AppRoutes.SCHEDULE_DAY_SCREEN}/{date}") { backStackEntry ->
            val dateArg = backStackEntry.arguments?.getString("date")
            val parsedDate = dateArg?.let { LocalDate.parse(it) } ?: LocalDate.now()

            ScheduleDayScreen(date = parsedDate)

            Log.d("NavDebug", "ScheduleDayScreen registered")
        }

        composable(AppRoutes.USER_IDENTITY_SCREEN) {
            UserIdentityScreen(
                onContinueAsGuest = {
                    navController.navigate(AppRoutes.HOME)
                },
                onCreateAccount = { identity ->
                    viewModel.updateUserIdentity(identity.toState())
                    viewModel.finalizeUserIdentity()
                    navController.navigate(AppRoutes.ACCOUNT_CREATION_FLOW)
                },
                onLogin = {
                    navController.navigate(AppRoutes.LOGIN_SCREEN)
                }
            )
        }

        composable(AppRoutes.ANONYMOUS_APP_SCREEN) {
            AnonymousAppScreen(
                userRepository = userRepository,
                secureRepo = secureRepo,
                onContinue = {
                    navController.navigate(AppRoutes.HOME)
                },
                onCreateAccount = {
                    navController.navigate(AppRoutes.USER_IDENTITY_SCREEN)
                })
        }

        composable(AppRoutes.ACCOUNT_CREATION_FLOW) {
            val context = LocalContext.current
            val factory = provideAccountFlowViewModelFactory(context)
            val viewModel: AccountFlowViewModel = viewModel(factory = factory)

            AccountCreationFlowScreen(
                viewModel = viewModel,
                onComplete = { navController.navigate(AppRoutes.HOME_AUTH) }
            )
        }
        // Future modules can be added here:
        // composable("ReminderManager") { ReminderManagerScreen(...) }
        // composable("TaskScheduler") { TaskSchedulerScreen(...) }
    }

    LaunchedEffect(Unit) {
        DebugOverlayManager.isEnabled = true
        Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                val now = System.nanoTime()
                val skippedFrames = ((now - frameTimeNanos) / 16_666_666L).toInt()

                DebugOverlayManager.logFrameSkip(skippedFrames)

                val durationMs = (now - frameTimeNanos) / 1_000_000L
                DebugOverlayManager.logDavey(durationMs)

                //Re-register for next frame
                Choreographer.getInstance().postFrameCallback(this)
            }
        })

        CoroutineScope(Dispatchers.Default).launch {
            while (DebugOverlayManager.isEnabled) {
                val usedKb =
                    (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024
                val maxKb = Runtime.getRuntime().maxMemory() / 1024
                val usagePercent = (usedKb.toFloat() / maxKb.toFloat()) * 100

                if (usagePercent > 70) {
                    DebugOverlayManager.logMemoryPressure(usedKb.toInt())
                }

                delay(5000)

                Log.d("DebugOverlay", "Overlay enabled. Frame and memory hooks active.")
            }
        }

        DebugOverlayManager.isEnabled = true
        LogcatReader.start()


        delay(100)                                                                           // delay to ensure state is updated

        val nextRoute = viewModel.getNextRoute(secureRepo, UserProfileManager(context))
        Log.d("NavDebug", "Routing to: $nextRoute")

        navController.navigate(nextRoute) {
            popUpTo(0) { inclusive = true }
        }

        // Initialize session AFTER routing
        val token = secureRepo.loadSecure<String>("token")
        val session = UserSession(
            uid = token ?: "anon",
            origin = SessionOrigin.LAUNCH,
            identityPhase = if (token != null) IdentityPhase.LOGGED_IN else IdentityPhase.ANONYMOUS,
            timestamp = System.currentTimeMillis(),
            authMethod = if (token != null) AuthMethod.TOKEN else AuthMethod.NONE,
            isVerified = token != null
        )

        UserSession.initializeSession(session)
    }
}