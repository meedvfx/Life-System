package com.meedz.lifeos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meedz.lifeos.ui.dashboard.DashboardRoute

@Composable
fun LifeOSNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "dashboard"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("dashboard") {
            DashboardRoute()
        }
        // Placeholders for future MVP features
        composable("calendar") {
            // CalendarRoute()
        }
        composable("tasks") {
            // TasksRoute()
        }
    }
}
