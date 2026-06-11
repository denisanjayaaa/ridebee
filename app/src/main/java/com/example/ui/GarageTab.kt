package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Bike
import com.example.ui.theme.*

@Composable
fun GarageTab(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bikes by viewModel.bikes.collectAsState()

    // Maintenance parameters
    val oilStatus by viewModel.engineOilStatus.collectAsState()
    val tireStatus by viewModel.tirePressureStatus.collectAsState()
    val airFilterStatus by viewModel.airFilterStatus.collectAsState()
    val radiatorStatus by viewModel.radiatorFlushStatus.collectAsState()

    var showAddBikeDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Fleet Header section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "ACTIVE GARAGE",
                            style = MaterialTheme.typography.labelLarge,
                            color = RacingOrange,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Your Fleet",
                            style = MaterialTheme.typography.headlineLarge,
                            color = TextWhite
                        )
                    }

                    Text(
                        text = "ADD UNIT",
                        style = MaterialTheme.typography.labelLarge,
                        color = RacingOrange,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { showAddBikeDialog = true }
                            .padding(8.dp)
                    )
                }
            }

            // Bike Horizontal Cards Scroller Slider
            item {
                if (bikes.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No motorcycles in fleet", color = TextGray)
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(bikes) { bike ->
                            BikeCardItem(
                                bike = bike,
                                onActivate = {
                                    viewModel.makeBikeActive(bike.id)
                                    Toast.makeText(context, "${bike.name} made active", Toast.LENGTH_SHORT).show()
                                },
                                onDelete = {
                                    viewModel.deleteBike(bike)
                                    Toast.makeText(context, "${bike.name} deleted", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }

            // Maintenance Header space
            item {
                Column {
                    Text(
                        text = "MAINTENANCE",
                        style = MaterialTheme.typography.labelLarge,
                        color = RacingOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Service Status",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextWhite
                    )
                }
            }

            // Grid Maintenance Status checklist cards
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Maintenance Card 1 - Engine Oil
                    MaintenanceCard(
                        title = "Engine Oil & Filter",
                        subtitle = "REPLACE EVERY 5,000 KM",
                        percentage = oilStatus,
                        lastChecked = "LAST: 10,000 KM",
                        nextEstimate = "NEXT: 15,000 KM",
                        isCritical = oilStatus <= 0.2f,
                        onLogService = {
                            viewModel.logMaintenanceCompleted("OIL")
                            Toast.makeText(context, "Engine Oil & Filter log updated!", Toast.LENGTH_SHORT).show()
                        }
                    )

                    // Maintenance Card 2 - Tire Pressure Check
                    MaintenanceCard(
                        title = "Tire Pressure",
                        subtitle = "CHECK IMMEDIATELY",
                        percentage = tireStatus,
                        lastChecked = "LAST CHECK: 14 DAYS AGO",
                        nextEstimate = "LOG RE-CHECK",
                        isCritical = tireStatus <= 0.2f,
                        onLogService = {
                            viewModel.logMaintenanceCompleted("TIRE")
                            Toast.makeText(context, "Tire pressures logged as stable!", Toast.LENGTH_SHORT).show()
                        }
                    )

                    // Maintenance Card 3 - Air Filter Check
                    MaintenanceCard(
                        title = "Air Filter",
                        subtitle = "REPLACE EVERY 10,000 KM",
                        percentage = airFilterStatus,
                        lastChecked = "CONDITION: FAIR",
                        nextEstimate = "NEXT: 20,000 KM",
                        isCritical = airFilterStatus <= 0.2f,
                        onLogService = {
                            viewModel.logMaintenanceCompleted("FILTER")
                            Toast.makeText(context, "Air filter logged as cleared!", Toast.LENGTH_SHORT).show()
                        }
                    )

                    // Maintenance Card 4 - Radiator Check
                    MaintenanceCard(
                        title = "Radiator Flush",
                        subtitle = "BI-ANNUAL INSPECTION",
                        percentage = radiatorStatus,
                        lastChecked = "LAST CHECK: MAY 2023",
                        nextEstimate = "DUE: MAY 2025",
                        isCritical = radiatorStatus <= 0.2f,
                        onLogService = {
                            viewModel.logMaintenanceCompleted("RADIATOR")
                            Toast.makeText(context, "Radiator flushed successfully!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            // Stats bottom overview list
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "FLEET ANALYTICS",
                        style = MaterialTheme.typography.labelLarge,
                        color = RacingOrange,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Stat 1
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "TOTAL RUN", style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp), color = TextGray)
                                Text(text = "20.5k", style = MaterialTheme.typography.headlineMedium.copy(fontFamily = MetricFamily, fontSize = 20.sp), color = RacingOrange)
                            }
                        }

                        // Stat 2
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "RIDES", style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp), color = TextGray)
                                Text(text = "142", style = MaterialTheme.typography.headlineMedium.copy(fontFamily = MetricFamily, fontSize = 20.sp), color = TextWhite)
                            }
                        }

                        // Stat 3
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "FUEL AVG", style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp), color = TextGray)
                                Text(text = "4.2L", style = MaterialTheme.typography.headlineMedium.copy(fontFamily = MetricFamily, fontSize = 20.sp), color = TextWhite)
                            }
                        }

                        // Stat 4
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "SERVICE", style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp), color = TextGray)
                                Text(text = "!", style = MaterialTheme.typography.headlineMedium.copy(fontFamily = MetricFamily, fontSize = 20.sp), color = AmberYellow)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(110.dp))
            }
        }

        // Circular Add button (+)
        LargeFloatingActionButton(
            onClick = { showAddBikeDialog = true },
            containerColor = RacingOrange,
            contentColor = TextWhite,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 96.dp, end = 20.dp)
                .testTag("add_bike_fab")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add new bike"
            )
        }

        // Compose floating dialog to add vehicles dynamically
        if (showAddBikeDialog) {
            var bikeName by remember { mutableStateOf("") }
            var bikeBrand by remember { mutableStateOf("") }
            var bikeModel by remember { mutableStateOf("") }
            var odometerText by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showAddBikeDialog = false },
                title = {
                    Text(
                        text = "Add Motorcycle",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextWhite
                    )
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = bikeName,
                            onValueChange = { bikeName = it },
                            label = { Text("Display Name (e.g. Nightshade R1)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RacingOrange,
                                unfocusedBorderColor = BorderColor,
                                focusedLabelColor = RacingOrange,
                                unfocusedLabelColor = TextGray,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("add_bike_name_field")
                        )

                        OutlinedTextField(
                            value = bikeBrand,
                            onValueChange = { bikeBrand = it },
                            label = { Text("Brand Name (e.g. DUCATI)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RacingOrange,
                                unfocusedBorderColor = BorderColor,
                                focusedLabelColor = RacingOrange,
                                unfocusedLabelColor = TextGray,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = bikeModel,
                            onValueChange = { bikeModel = it },
                            label = { Text("Model details (e.g. PANIGALE V4)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RacingOrange,
                                unfocusedBorderColor = BorderColor,
                                focusedLabelColor = RacingOrange,
                                unfocusedLabelColor = TextGray,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = odometerText,
                            onValueChange = { odometerText = it },
                            label = { Text("Starting Odometer (KM)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RacingOrange,
                                unfocusedBorderColor = BorderColor,
                                focusedLabelColor = RacingOrange,
                                unfocusedLabelColor = TextGray,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (bikeName.isNotBlank()) {
                                val odo = odometerText.toDoubleOrNull() ?: 0.0
                                viewModel.addNewBike(bikeName, bikeBrand, bikeModel, odo)
                                Toast.makeText(context, "$bikeName registered in active fleet!", Toast.LENGTH_SHORT).show()
                                showAddBikeDialog = false
                            } else {
                                Toast.makeText(context, "Please enter a valid display name", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RacingOrange),
                        modifier = Modifier.testTag("confirm_add_bike")
                    ) {
                        Text(text = "ADD VEHICLE")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddBikeDialog = false }) {
                        Text(text = "CANCEL", color = TextGray)
                    }
                },
                containerColor = MidnightSurface,
                titleContentColor = TextWhite
            )
        }
    }
}

@Composable
fun BikeCardItem(
    bike: Bike,
    onActivate: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(310.dp)
            .height(180.dp)
            .border(
                1.dp,
                if (bike.isActive) RacingOrange.copy(alpha = 0.6f) else BorderColor,
                RoundedCornerShape(20.dp)
            )
            .clickable { if (!bike.isActive) onActivate() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (bike.isActive) MidnightSurfaceContainer else MidnightSurfaceDim
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Delete action button for standby units
            if (!bike.isActive) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = bike.brand.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = RacingOrange,
                        )
                        Text(
                            text = bike.name,
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Active badge icon
                    Box(
                        modifier = Modifier
                            .clip(CapsuleShape())
                            .background(
                                if (bike.isActive) RacingOrange else BorderColor
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (bike.isActive) "ACTIVE" else "STANDBY",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                            color = if (bike.isActive) TextWhite else TextGray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(text = "MODEL CONFIGURATION", style = MaterialTheme.typography.labelLarge.copy(fontSize = 8.sp), color = TextGray)
                        Text(text = bike.modelName.uppercase(), style = MaterialTheme.typography.bodyMedium, color = TextWhite, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        val odoFormatted = String.format("%,d", bike.odometer.toInt())
                        Text(
                            text = odoFormatted,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = MetricFamily,
                                fontSize = 24.sp
                            ),
                            color = RacingOrange
                        )
                        Text(
                            text = "${bike.unit} ODOMETER",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 8.sp),
                            color = TextGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MaintenanceCard(
    title: String,
    subtitle: String,
    percentage: Float,
    lastChecked: String,
    nextEstimate: String,
    isCritical: Boolean,
    onLogService: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isCritical) Color.Red.copy(alpha = 0.3f) else BorderColor,
                RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCritical) Color(0xFF1E0E0E) else MidnightSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isCritical) Color.Red.copy(alpha = 0.15f) else RacingOrange.copy(alpha = 0.1f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCritical) Icons.Default.Warning else Icons.Default.Settings,
                            contentDescription = null,
                            tint = if (isCritical) Color.Red else RacingOrange,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextWhite
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                            color = if (isCritical) Color.Red else TextGray,
                            fontWeight = if (isCritical) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (isCritical) "CRITICAL" else "${(percentage * 100).toInt()}%",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontFamily = MetricFamily,
                            fontSize = 13.sp
                        ),
                        color = if (isCritical) Color.Red else RacingOrange,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // High-fidelity progress indicator status bar
            LinearProgressIndicator(
                progress = { percentage },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = if (isCritical) Color.Red else RacingOrange,
                trackColor = BorderColor,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = lastChecked.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                    color = TextGray
                )

                Text(
                    text = nextEstimate.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                    color = if (isCritical) Color.Red else RacingOrange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLogService() }
                )
            }
        }
    }
}
