package com.meedz.lifeos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meedz.lifeos.ui.analytics.AnalyticsRoute
import com.meedz.lifeos.ui.calendar.CalendarRoute
import com.meedz.lifeos.ui.dashboard.DashboardRoute
import com.meedz.lifeos.ui.finance.FinanceRoute
import com.meedz.lifeos.ui.goals.GoalsRoute
import com.meedz.lifeos.ui.habits.HabitsRoute
import com.meedz.lifeos.ui.health.HealthRoute
import com.meedz.lifeos.ui.journal.JournalRoute
import com.meedz.lifeos.ui.knowledge.KnowledgeRoute
import com.meedz.lifeos.ui.projects.ProjectsRoute

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
        composable("dashboard") { DashboardRoute() }
        composable("calendar") { CalendarRoute() }
        composable("goals") { GoalsRoute() }
        composable("projects") { ProjectsRoute() }
        composable("habits") { HabitsRoute() }
        composable("journal") { JournalRoute() }
        composable("knowledge") { KnowledgeRoute() }
        composable("finance") { FinanceRoute() }
        composable("health") { HealthRoute() }
        composable("analytics") { AnalyticsRoute() }
    }
}
