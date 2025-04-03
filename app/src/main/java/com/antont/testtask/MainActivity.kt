package com.antont.testtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.ripple
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.antont.testtask.data.repository.TeamsRepository
import com.antont.testtask.viewmodel.TeamsViewModel

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
                        .padding(top = 16.dp)
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
                                                        Color(0xFFFE8D3B), Color(0xFFF01515)
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
fun AddScreen() {
    val context = LocalContext.current
    val repository = remember { TeamsRepository(context) }
    val viewModel: TeamsViewModel = viewModel(
        factory = TeamsViewModel.Factory(repository)
    )

    var selectedCountry by remember { mutableStateOf("Select country") }
    var selectedLeague by remember { mutableStateOf("Select league") }
    var selectedTeam1 by remember { mutableStateOf("Select first team") }
    var selectedTeam2 by remember { mutableStateOf("Select second team") }
    var selectedDate by remember { mutableStateOf("Select date") }

    val countries by viewModel.countries.collectAsState()
    val leagues by viewModel.leagues.collectAsState()
    val teams by viewModel.teams.collectAsState()
    val dates = listOf("Today", "Tomorrow", "Next Week", "Next Month")

    // Load countries when screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadCountries()
    }

    // Load leagues when country is selected
    LaunchedEffect(selectedCountry) {
        if (selectedCountry != "Select country") {
            viewModel.loadLeaguesByCountry(selectedCountry)
            selectedLeague = "Select league" // Reset league when country changes
            selectedTeam1 = "Select first team" // Reset teams when country changes
            selectedTeam2 = "Select second team"
        }
    }

    // Load teams when league is selected
    LaunchedEffect(selectedLeague) {
        if (selectedLeague != "Select league") {
            viewModel.loadTeamsByLeague(selectedLeague)
        }
    }

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
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Country",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            InputSelectionBottomSheet(
                "Select a country from the list",
                selectedValue = selectedCountry,
                options = countries,
                onOptionSelected = { selectedCountry = it }
            )
        }

        // Leagues Selection
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Leagues",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            InputSelectionBottomSheet(
                "Select a league from the list",
                selectedValue = selectedLeague,
                options = leagues,
                onOptionSelected = { selectedLeague = it },
                enabled = selectedCountry != "Select country"
            )
        }

        // Teams Selection
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
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
                    // First Team Selection
                    InputSelectionBottomSheet(
                        "Select first team from the list",
                        selectedValue = selectedTeam1,
                        options = teams,
                        onOptionSelected = { selectedTeam1 = it },
                        enabled = selectedLeague != "Select league"
                    )

                    // Second Team Selection
                    InputSelectionBottomSheet(
                        "Select second team from the list",
                        selectedValue = selectedTeam2,
                        options = teams.filter { it != selectedTeam1 },
                        onOptionSelected = { selectedTeam2 = it },
                        enabled = selectedLeague != "Select league"
                    )
                }

                // VS Icon overlapping the selections
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
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Date",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            InputSelectionBottomSheet(
                "Select date from the list",
                selectedValue = selectedDate,
                options = dates,
                onOptionSelected = { selectedDate = it }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save Button
        Image(
            painter = painterResource(id = R.drawable.save_button_background),
            contentDescription = "Start",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 90.dp)
                .clickable(onClick = {
                    // TODO: Implement save functionality
                })
        )
    }
}

@Composable
private fun CustomCheckbox(
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(20.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = 2.dp,
                color = if (selected) Color(0xFFFFFFFF) else Color(0xFF699AD0),
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "Selected",
                tint = Color(0xFFF54524),
                modifier = Modifier.size(9.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSelectionBottomSheet(
    title: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    enabled: Boolean = true
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var tempSelectedValue by remember { mutableStateOf(selectedValue) }
    
    val filteredOptions = remember(searchQuery, options) {
        if (searchQuery.isEmpty()) {
            options
        } else {
            options.filter { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = enabled,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = {
                        tempSelectedValue = selectedValue
                        showBottomSheet = true
                    })
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (enabled) {
                            DropdownGradientColors
                        } else {
                            listOf(
                                Color(0xFF003B7C).copy(alpha = 0.5f),
                                Color(0xFF043872).copy(alpha = 0.5f)
                            )
                        }
                    ),
                    shape = RoundedCornerShape(DropdownCornerRadius)
                )
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            // Label and selected value
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = 24.dp)
            ) {
                Text(
                    text = selectedValue,
                    color = if (enabled) Color.White else Color.White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // Dropdown icon
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select",
                tint = if (enabled) Color.White else Color.White.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF013A78),
            dragHandle = null,
            shape = RoundedCornerShape(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Title bar with close button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF002B5A))
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Close",
                        tint = Color(0xFF699AD0),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(24.dp)
                            .clickable {
                                scope.launch {
                                    sheetState.hide()
                                    showBottomSheet = false
                                }
                            }
                    )
                }

                // Search Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF002B5A),
                                shape = RoundedCornerShape(32.dp)
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = Color.White,
                                fontSize = 16.sp
                            ),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Type it in manually, for example \"${if (options.isNotEmpty()) options.first() else ""}\"",
                                            color = Color(0xFFFFFFFF),
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search",
                            tint = Color(0xFFDFE3EA),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Options list
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filteredOptions.forEach { option ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (option == tempSelectedValue) Color(0xFF002B5A) else Color.Transparent)
                                .clickable {
                                    tempSelectedValue = option
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CustomCheckbox(
                                    selected = option == tempSelectedValue
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = option,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                // Apply Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.apply_button_background),
                        contentDescription = "Start",
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .clickable(onClick = {
                                onOptionSelected(tempSelectedValue)
                                scope.launch {
                                    sheetState.hide()
                                    showBottomSheet = false
                                }
                            })
                    )
                }
            }
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