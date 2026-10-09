package com.emilly.s35678658.medtrack.presentation.screens

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.emilly.s35678658.medtrack.R
import com.emilly.s35678658.medtrack.ui.theme.MedBlue
import com.emilly.s35678658.medtrack.ui.theme.MedGreen
import com.emilly.s35678658.medtrack.ui.theme.MedLightBlue
import com.emilly.s35678658.medtrack.ui.theme.MedLightGreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import com.emilly.s35678658.medtrack.data.entities.PatientEntity
import com.emilly.s35678658.medtrack.presentation.viewmodels.PatientViewModel
import com.emilly.s35678658.medtrack.presentation.viewmodels.MedicationViewModel
import com.emilly.s35678658.medtrack.utils.AuthManager
import com.emilly.s35678658.medtrack.data.entities.MedicationEntity
import com.emilly.s35678658.medtrack.presentation.navigation.Routes
import com.emilly.s35678658.medtrack.presentation.viewmodels.TakenStatusViewModel
import com.emilly.s35678658.medtrack.data.entities.TakenStatusEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "MedTrack",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    AuthManager.logout(context)
                    navController.navigate(Routes.Welcome) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.logout),
                    contentDescription = "Logout",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MedLightBlue
        )
    )
}

@Composable
fun BottomAppBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    data class BottomNavItem(
        val label: String,
        val route: String
    )

    val items = listOf(
        BottomNavItem("Home", Routes.Home),
        BottomNavItem("Symptoms", Routes.Symptoms),
        BottomNavItem("MedCoach", Routes.MedCoach),
        BottomNavItem("Settings", Routes.Settings))

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    when (item.route) {
                        Routes.Home -> Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                        Routes.Symptoms -> Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Symptoms",
                            modifier = Modifier.size(24.dp)
                        )
                        Routes.MedCoach -> Icon(
                            imageVector = Icons.Filled.Face,
                            contentDescription = "MedCoach",
                            modifier = Modifier.size(24.dp)
                        )
                        Routes.Settings -> Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(24.dp)
                        )

                    }
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MedBlue,
                    selectedTextColor = MedBlue,
                    indicatorColor = MedLightBlue,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

@Composable
fun HomeScreen(rootNavController: NavHostController) {
    val homeNavController = rememberNavController()
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = MedLightBlue,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentRoute == Routes.Home) {
                TopBar(rootNavController)
            }
        },
        bottomBar = {
            BottomAppBar(homeNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = homeNavController,
            startDestination = Routes.Home,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Routes.Home) {
                HomeContent(homeNavController)
            }
            composable(Routes.Symptoms) {
                SymptomsScreen(homeNavController)
            }
            composable(Routes.MedCoach) {
                MedCoachScreen(homeNavController)
            }
            composable(Routes.Settings) {
                SettingsScreen(
                    homeNavController = homeNavController,
                    rootNavController = rootNavController
                )
            }
            composable(Routes.AddMedication) {
                AddMedicationScreen(homeNavController)
            }

        }
    }
}

@Composable
fun HomeContent(navController: NavHostController) {
    val context = LocalContext.current
    val patientId = AuthManager.getPatientId() ?: ""
    val patientViewModel: PatientViewModel = viewModel(
        factory = PatientViewModel.PatientViewModelFactory(context)
    )

    val medicationViewModel: MedicationViewModel = viewModel(
        factory = MedicationViewModel.MedicationViewModelFactory(context)
    )

    val takenStatusViewModel: TakenStatusViewModel = viewModel(
        factory = TakenStatusViewModel.TakenStatusViewModelFactory(context)
    )

    val medications by medicationViewModel
        .getMedicationsForPatient(patientId)
        .collectAsState(initial = emptyList())


    var patient by remember { mutableStateOf<PatientEntity?>(null) }

    LaunchedEffect(patientId) {
        if (patientId.isNotBlank()) {
            patient = patientViewModel.getPatientById(patientId)
        }
    }

    val today = LocalDate.now().toString()

    val takenStatuses by takenStatusViewModel
        .getTakenStatusForDate(patientId, today)
        .collectAsState(initial = emptyList())
    val takenCount = medications.count { medication ->
        takenStatuses.any {
            it.medicationName == medication.medicationName &&
                    it.scheduledTime == medication.scheduledTime
        }
    }

    val todayDate = remember {
        LocalDate.now().format(
            DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH)
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Hello, ${patient?.name ?: "Patient"} 👋",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Patient ID: $patientId",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )


        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = todayDate,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MedLightBlue
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Today's Progress",
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$takenCount / ${medications.size} medications taken",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall
                )

                LinearProgressIndicator(
                    progress = takenCount.toFloat() / medications.size.coerceAtLeast(1),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50)),
                    color = MedGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { navController.navigate(Routes.AddMedication) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MedBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Add Medication")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (medications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("No medications scheduled.")
            }
        } else {
            Text(
                text = "Today's Medications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                itemsIndexed(medications) { index, medication ->
                    val isTaken = takenStatuses.any {
                        it.medicationName == medication.medicationName &&
                                it.scheduledTime == medication.scheduledTime
                    }

                    MedicationCard(
                        medication = medication,
                        isTaken = isTaken,
                        onTakenChange = { checked ->
                            if (checked) {
                                takenStatusViewModel.insertTakenStatus(
                                    TakenStatusEntity(
                                        patientId = patientId,
                                        medicationName = medication.medicationName,
                                        scheduledTime = medication.scheduledTime,
                                        date = today,
                                        isTaken = true
                                    )
                                )
                            } else {
                                takenStatusViewModel.deleteTakenStatus(
                                    patientId = patientId,
                                    medicationName = medication.medicationName,
                                    scheduledTime = medication.scheduledTime,
                                    date = today
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun MedicationCard(
    medication: MedicationEntity,
    isTaken: Boolean,
    onTakenChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isTaken) MedLightGreen
            else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = medication.medicationName,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (isTaken) TextDecoration.LineThrough else TextDecoration.None
                )

                if (isTaken) {
                    Text(
                        text = "✓ Taken",
                        color = MedGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Dosage: ${medication.dosage}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isTaken) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                else MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Frequency: ${medication.frequency}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isTaken) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                else MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Scheduled Time: ${medication.scheduledTime}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isTaken) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                else MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Checkbox(
                    checked = isTaken,
                    onCheckedChange = onTakenChange,
                    colors = CheckboxDefaults.colors(checkedColor = MedGreen)
                )

                Text(
                    text = if (isTaken) "Completed" else "Mark as taken",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
