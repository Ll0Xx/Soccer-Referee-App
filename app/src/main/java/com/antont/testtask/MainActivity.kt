package com.antont.testtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
val CardColor = Color(0xFF18447E)

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

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MainBackgroundColor)
        ) {
            // Test text that will be visible under the blurred navigation bar
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { }

            // Navigation content
            Column(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Calendar.route,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(Screen.Calendar.route) { CalendarScreen() }
                    composable(Screen.Results.route) { ResultsScreen() }
                    composable(Screen.Add.route) { AddScreen() }
                    composable(Screen.Statistic.route) { StatisticScreen() }
                    composable(Screen.Settings.route) { SettingsScreen() }
                }
            }
            
            // Blurred background for navigation bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .align(Alignment.BottomCenter)
                    .blur(20.dp)
                    .background(BottomBarColor)
            )
            
            // Clear buttons on top of the blur
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
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
                                .clickable { 
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }, 
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
    // Sample data for the list
    val calendarItems = List(30) { index -> "Calendar Item ${index + 1}" }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Calendar",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(calendarItems) { item ->
                CalendarItem(text = item)
            }
            // Add some extra space at the bottom for the navigation bar
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun CalendarItem(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ },
        colors = CardDefaults.cardColors(containerColor = CardColor),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ResultsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Results Screen",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Composable
fun AddScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Screen",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Composable
fun StatisticScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Statistic Screen",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings Screen",
            color = Color.White,
            fontSize = 24.sp
        )
    }
} 