package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ProfileTab(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Gather profile states
    val name by viewModel.userName.collectAsState()
    val motorcycle by viewModel.userMotorcycle.collectAsState()
    val location by viewModel.userLocation.collectAsState()
    val isPro by viewModel.isProUser.collectAsState()

    // Gather statistics from database
    val rides by viewModel.rides.collectAsState()
    val bikes by viewModel.bikes.collectAsState()

    // Preferences
    val isNotificationOn by viewModel.notificationEnabled.collectAsState()
    val isTrackingOn by viewModel.rideTrackingEnabled.collectAsState()
    val isPublicProfileOn by viewModel.publicProfileEnabled.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Hero Profile section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                shape = RoundedCornerShape(24.dp)
              ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(90.dp)) {
                        // Profile circular badge
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(RacingOrange.copy(alpha = 0.1f))
                                .border(2.dp, RacingOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = RacingOrange,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        // Pro badge
                        if (isPro) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .clip(CapsuleShape())
                                    .background(RacingOrange)
                                    .border(1.dp, MidnightSurface, CapsuleShape())
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "PRO",
                                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 8.sp),
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Text(
                        text = "$motorcycle • $location",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showEditProfileDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = RacingOrange),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("edit_profile_button")
                    ) {
                        Text(text = "EDIT PROFILE", color = TextWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Stats aggregates row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Stat 1: Distance
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "DISTANCE", style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp), color = TextGray)
                        Text(text = "12,482", style = MaterialTheme.typography.headlineMedium.copy(fontFamily = MetricFamily, fontSize = 20.sp), color = RacingOrange)
                        Text(text = "TOTAL KM", style = MaterialTheme.typography.labelLarge.copy(fontSize = 8.sp), color = TextGray, modifier = Modifier.padding(top = 2.dp))
                    }
                }

                // Stat 2: Bikes
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "MY BIKES", style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp), color = TextGray)
                        Text(text = String.format("%02d", bikes.size), style = MaterialTheme.typography.headlineMedium.copy(fontFamily = MetricFamily, fontSize = 20.sp), color = AmberYellow)
                        Text(text = "UNITS", style = MaterialTheme.typography.labelLarge.copy(fontSize = 8.sp), color = TextGray, modifier = Modifier.padding(top = 2.dp))
                    }
                }

                // Stat 3: Total Rides
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "TOTAL RIDES", style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp), color = TextGray)
                        Text(text = "156", style = MaterialTheme.typography.headlineMedium.copy(fontFamily = MetricFamily, fontSize = 20.sp), color = TextWhite)
                        Text(text = "TRIPS COMPLETE", style = MaterialTheme.typography.labelLarge.copy(fontSize = 8.sp), color = TextGray, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
        }

        // Service History List logger
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Service Log",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextWhite
                )

                Text(
                    text = "EXPORT PDF",
                    style = MaterialTheme.typography.labelLarge,
                    color = RacingOrange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Telemetry history PDF downloaded!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // Custom Static service history records
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Record 1
                ServiceLogItem(
                    title = "Full Seasonal Service",
                    date = "OCT 12, 2023 • OD: 11,200 KM",
                    price = "£420.00",
                    vendor = "Triumph London West",
                    details = "Oil & filter change, brake fluid flush, chain tensioned and lubed. Rear brake pads replaced (Brembo). Visual inspection of all seals.",
                    tags = listOf("ENGINE OIL", "BRAKES")
                )

                // Record 2
                ServiceLogItem(
                    title = "Tire Replacement (Set)",
                    date = "AUG 05, 2023 • OD: 9,800 KM",
                    price = "£310.00",
                    vendor = "ProTyre Experts",
                    details = "New Pirelli Supercorsa SP V3 tires installed. Balanced and valve stems replaced. Adjusted suspension sag for new rubber.",
                    tags = listOf("TIRE")
                )

                // Record 3
                ServiceLogItem(
                    title = "Minor Electrical Check",
                    date = "MAY 21, 2023 • OD: 8,450 KM",
                    price = "£85.00",
                    vendor = "Self Service",
                    details = "Fixed loose indicator connection in tail tidy. Replaced blown fuse for auxiliary lights.",
                    tags = listOf("ELECTRICAL")
                )
            }
        }

        // Preference Settings toggle headers list
        item {
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.headlineMedium,
                color = TextWhite
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Notifications toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, tint = RacingOrange)
                            Text(text = "Notifications", color = TextWhite)
                        }
                        Switch(
                            checked = isNotificationOn,
                            onCheckedChange = { viewModel.notificationEnabled.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextWhite,
                                checkedTrackColor = RacingOrange,
                                uncheckedThumbColor = TextGray,
                                uncheckedTrackColor = MidnightSurfaceDim
                            )
                        )
                    }

                    Divider(color = BorderColor)

                    // Trackings toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = RacingOrange)
                            Text(text = "Ride Tracking", color = TextWhite)
                        }
                        Switch(
                            checked = isTrackingOn,
                            onCheckedChange = { viewModel.rideTrackingEnabled.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextWhite,
                                checkedTrackColor = RacingOrange,
                                uncheckedThumbColor = TextGray,
                                uncheckedTrackColor = MidnightSurfaceDim
                            )
                        )
                    }

                    Divider(color = BorderColor)

                    // Public Profiles toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Public, contentDescription = null, tint = RacingOrange)
                            Text(text = "Public Profile", color = TextWhite)
                        }
                        Switch(
                            checked = isPublicProfileOn,
                            onCheckedChange = { viewModel.publicProfileEnabled.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextWhite,
                                checkedTrackColor = RacingOrange,
                                uncheckedThumbColor = TextGray,
                                uncheckedTrackColor = MidnightSurfaceDim
                            )
                        )
                    }
                }
            }
        }

        // Logout Action trigger button
        item {
            Button(
                onClick = {
                    Toast.makeText(context, "Session logged out!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, Color.Red.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "LOGOUT FROM ACCOUNT", color = Color.Red, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    // Dynamic layout Dialog box for updates
    if (showEditProfileDialog) {
        var tempName by remember { mutableStateOf(name) }
        var tempMoto by remember { mutableStateOf(motorcycle) }
        var tempLoc by remember { mutableStateOf(location) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = {
                Text(
                    text = "Edit Profile Details",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextWhite
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Your Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RacingOrange,
                            unfocusedBorderColor = BorderColor,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_name_field")
                    )

                    OutlinedTextField(
                        value = tempMoto,
                        onValueChange = { tempMoto = it },
                        label = { Text("Motorcycle Model") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RacingOrange,
                            unfocusedBorderColor = BorderColor,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = tempLoc,
                        onValueChange = { tempLoc = it },
                        label = { Text("Location City") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RacingOrange,
                            unfocusedBorderColor = BorderColor,
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
                        viewModel.updateProfile(tempName, tempMoto, tempLoc)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RacingOrange),
                    modifier = Modifier.testTag("confirm_edit_profile")
                ) {
                    Text(text = "SAVE DETAILS")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text(text = "CANCEL", color = TextGray)
                }
            },
            containerColor = MidnightSurface,
            titleContentColor = TextWhite
        )
    }
}

@Composable
fun ServiceLogItem(
    title: String,
    date: String,
    price: String,
    vendor: String,
    details: String,
    tags: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextWhite
                    )
                    Text(
                        text = date,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                        color = TextGray
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = price,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = MetricFamily
                        ),
                        color = TextWhite
                    )
                    Text(
                        text = vendor,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                        color = TextGray
                    )
                }
            }

            Text(
                text = details,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = TextGray,
                lineHeight = 18.sp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                for (tag in tags) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MidnightSurfaceContainerHigh)
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 8.sp),
                            color = TextGray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
