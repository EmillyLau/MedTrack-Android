package com.emilly.s35678658.medtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emilly.s35678658.medtrack.data.database.MedTrackDatabase
import com.emilly.s35678658.medtrack.presentation.screens.ClaimAccountScreen
import com.emilly.s35678658.medtrack.presentation.screens.ClinicianDashboardScreen
import com.emilly.s35678658.medtrack.presentation.screens.ClinicianLoginScreen
import com.emilly.s35678658.medtrack.presentation.screens.HomeScreen
import com.emilly.s35678658.medtrack.presentation.screens.LoginScreen
import com.emilly.s35678658.medtrack.presentation.screens.SignUpScreen
import com.emilly.s35678658.medtrack.presentation.screens.WelcomeScreen
import com.emilly.s35678658.medtrack.ui.theme.MedTrackTheme
import com.emilly.s35678658.medtrack.utils.AuthManager
import com.emilly.s35678658.medtrack.utils.CsvSeeder
import com.emilly.s35678658.medtrack.presentation.navigation.Routes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AuthManager.initialise(this)

        setContent {
            val context = LocalContext.current
            val database = remember { MedTrackDatabase.getDatabase(context) }

            LaunchedEffect(Unit) {
                CsvSeeder(context).seedDatabaseIfNeeded(database)
            }

            MedTrackTheme {
                MedTrackApp()
            }
        }
    }

    @Composable
    fun MedTrackApp() {
        val context = LocalContext.current
        val navController = rememberNavController()
        val startDestination = if (AuthManager.isLoggedIn()) Routes.Home
        else Routes.Welcome

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(Routes.Welcome) {
                WelcomeScreen(navController = navController)
            }
            composable(Routes.Login) {
                LoginScreen(navController = navController)
            }
            composable(Routes.SignUp) {
                SignUpScreen(navController = navController)
            }
            composable(Routes.ClaimAccount) {
                ClaimAccountScreen(navController = navController)
            }
            composable(Routes.Home) {
                HomeScreen(navController)
            }
            composable(Routes.ClinicianLogin) {
                ClinicianLoginScreen(navController = navController)
            }
            composable(Routes.ClinicianDashboard) {
                ClinicianDashboardScreen(navController = navController)
            }
        }
    }

}