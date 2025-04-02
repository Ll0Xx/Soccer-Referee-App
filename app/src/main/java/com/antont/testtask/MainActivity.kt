package com.antont.testtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

// Define custom colors
val MainBackgroundColor = Color(0xFF002B5A)
val BottomBarColor = Color(0xFF1D60AA)
val BottomBarItemColor = Color(0xFF002956)
val SelectedIconColor = Color(0xFFFFE111)
val CardColor = Color(0xFF18447E)
val TitleDecoratorColor = Color(0xFFFA4D01)

// Define common values
val DropdownCornerRadius = 12.dp
val DropdownGradientColors = listOf(
    Color(0xFF003B7C),
    Color(0xFF043872)
)

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
            // Navigation content
            Column(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Calendar.route,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp)
                ) {
                    composable(Screen.Calendar.route) { CalendarScreen() }
                    composable(Screen.Results.route) { ResultsScreen() }
                    composable(Screen.Add.route) { AddScreen() }
                    composable(Screen.Statistic.route) { StatisticScreen() }
                    composable(Screen.Settings.route) { SettingsScreen() }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .align(Alignment.BottomCenter)
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
                        val isAddButton = screen == Screen.Add
                        val isCalendar = screen == Screen.Calendar

                        if (isCalendar) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = Color(0xFFFFE111),
                                        contentColor = Color(0xFF002956),
                                        modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
                                    ) {
                                        Text("2")
                                    }
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
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
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(BottomBarItemColor)
                                    )
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
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
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
                                if (isAddButton) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color(0xFFFE8D3B),
                                                        Color(0xFFF01515)
                                                    )
                                                )
                                            )
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(BottomBarItemColor)
                                    )
                                }
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calendar Screen",
            color = Color.White,
            fontSize = 24.sp
        )
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
fun MyReadOnlyTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(
                    text = label,
                    color = Color.White
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(
                    onClick = onClick,
                ),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetSelectionDemo(
    items: List<String>,
    selectedValue: String,
    onValueSelected: (String) -> Unit,
    label: String
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val modalBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    val toggleModalBottomSheetState = {
        coroutineScope.launch {
            if (modalBottomSheetState.currentValue == modalBottomSheetState.targetValue) {
                if (modalBottomSheetState.isVisible) {
                    modalBottomSheetState.hide()
                } else {
                    modalBottomSheetState.show()
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            LazyColumn {
                items(items) { item ->
                    Text(
                        text = item,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onValueSelected(item)
                                toggleModalBottomSheetState()
                            }
                            .padding(
                                horizontal = 16.dp,
                                vertical = 12.dp,
                            ),
                    )
                }
            }
        },
        sheetBackgroundColor = CardColor
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.wrapContentSize(),
        ) {
            MyReadOnlyTextField(
                value = selectedValue,
                label = label,
                onClick = {
                    toggleModalBottomSheetState()
                },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 4.dp,
                    ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen() {
    var selectedCountry by remember { mutableStateOf("") }
    var selectedLeague by remember { mutableStateOf("") }
    var selectedTeam1 by remember { mutableStateOf("") }
    var selectedTeam2 by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }

    val countries = listOf("England", "Spain", "Germany", "France", "Italy")
    val leagues = listOf("Premier League", "La Liga", "Bundesliga", "Ligue 1", "Serie A")
    val teams = listOf("Team 1", "Team 2", "Team 3", "Team 4", "Team 5")
    val dates = listOf("Today", "Tomorrow", "Next Week", "Next Month")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Title with icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_title_decorator),
                contentDescription = null,
                tint = TitleDecoratorColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Add New Matches",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )
        }

        // Country Selection
        BottomSheetSelectionDemo(
            items = countries,
            selectedValue = selectedCountry,
            onValueSelected = { selectedCountry = it },
            label = "Select country"
        )

        // League Selection
        BottomSheetSelectionDemo(
            items = leagues,
            selectedValue = selectedLeague,
            onValueSelected = { selectedLeague = it },
            label = "Select league"
        )

        // Teams Selection
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Teams",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // First Team
                    BottomSheetSelectionDemo(
                        items = teams,
                        selectedValue = selectedTeam1,
                        onValueSelected = { selectedTeam1 = it },
                        label = "Select first team"
                    )

                    // Second Team
                    BottomSheetSelectionDemo(
                        items = teams,
                        selectedValue = selectedTeam2,
                        onValueSelected = { selectedTeam2 = it },
                        label = "Select second team"
                    )
                }

                // VS Icon overlapping the dropdowns
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd)
                        .offset(x = (-48).dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF002853)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_teams_vs),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Date Selection
        BottomSheetSelectionDemo(
            items = dates,
            selectedValue = selectedDate,
            onValueSelected = { selectedDate = it },
            label = "Select date"
        )

        Spacer(modifier = Modifier.weight(1f))

        // Save Button
        Button(
            onClick = { /* Handle save action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFE111)
            )
        ) {
            Text(
                text = "Save",
                color = Color(0xFF002956),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
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