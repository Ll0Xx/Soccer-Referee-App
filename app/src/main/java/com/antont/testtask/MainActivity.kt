package com.antont.testtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.antont.testtask.ui.theme.TestTaskTheme

// Define custom colors
val MainBackgroundColor = Color(0xFF002B5A)
val BottomBarColor = Color(0xFF1D60AA)
val BottomBarItemColor = Color(0xFF002956)
val SelectedIconColor = Color(0xFFFFE111)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTaskTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Calendar, Screen.Results, Screen.Add, Screen.Statistic, Screen.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = MainBackgroundColor,
        bottomBar = {
            CustomNavigationBar(
                items = items, currentRoute = currentRoute, onItemSelected = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Calendar.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Calendar.route) { CalendarScreen() }
            composable(Screen.Results.route) { ResultsScreen() }
            composable(Screen.Add.route) { AddScreen() }
            composable(Screen.Statistic.route) { StatisticScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}

@Composable
fun CustomNavigationBar(
    items: List<Screen>, currentRoute: String?, onItemSelected: (Screen) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BottomBarColor,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { screen ->
                val isSelected = currentRoute == screen.route
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(BottomBarItemColor)
                        .clickable { onItemSelected(screen) }, 
                    contentAlignment = Alignment.Center
                ) {
                    screen.resourceIcon?.let {
                        Icon(
                            painter = painterResource(id = it),
                            contentDescription = null,
                            tint = if (isSelected) SelectedIconColor
                                  else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } ?: screen.icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            tint = if (isSelected) SelectedIconColor
                                  else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

sealed class Screen(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    val resourceIcon: Int? = null
) {
    object Calendar : Screen("calendar", resourceIcon = R.drawable.ic_calendar)
    object Results : Screen("results", resourceIcon = R.drawable.ic_archive)
    object Add : Screen("add", resourceIcon = R.drawable.ic_add_24)
    object Statistic : Screen("statistic", resourceIcon = R.drawable.ic_statistic)
    object Settings : Screen("settings", resourceIcon = R.drawable.ic_settings)
}

@Composable
fun CalendarScreen() {
    Text(text = "Calendar Screen")
}

@Composable
fun ResultsScreen() {
    Text(text = "Results Screen")
}

@Composable
fun AddScreen() {
    Text(text = "Add Screen")
}

@Composable
fun StatisticScreen() {
    Text(text = "Statistic Screen")
}

@Composable
fun SettingsScreen() {
    Text(text = "Settings Screen")
} 