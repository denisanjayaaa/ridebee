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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Ride
import com.example.ui.theme.*

@Composable
fun HomeTab(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val rides by viewModel.rides.collectAsState()
    val bikes by viewModel.bikes.collectAsState()
    val name by viewModel.userName.collectAsState()

    // Aggregate metrics
    val totalDistanceRun = rides.sumOf { it.distance }
    val formattedTotalDistance = String.format("%,d", (12482 + totalDistanceRun).toInt())
    val activeBikesCount = String.format("%02d", bikes.size)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Welcoming space
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WELCOME BACK, ${name.uppercase()}",
                        style = MaterialTheme.typography.labelLarge,
                        color = RacingOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Ready for a ride?",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextWhite
                    )
                }

                // Weather Card
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MidnightSurface)
                        .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = "Weather indicator",
                            tint = AmberYellow,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "72°F • Clear",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Perfect conditions",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                                color = TextGray
                            )
                        }
                    }
                }
            }
        }

        // Quick Stats Bento Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Stat 1 - Total Distance
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(115.dp)
                        .border(1.dp, BorderColor, RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Watermark icon
                        Icon(
                            imageVector = Icons.Default.Route,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.03f),
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = 12.dp, y = 12.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Distance",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                                color = TextGray
                            )
                            Text(
                                text = formattedTotalDistance,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontFamily = MetricFamily,
                                    fontSize = 28.sp
                                ),
                                color = TextWhite
                            )
                            Text(
                                text = "MILES",
                                style = MaterialTheme.typography.labelLarge,
                                color = RacingOrange,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Stat 2 - Active Bikes
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(115.dp)
                        .border(1.dp, BorderColor, RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Watermark icon
                        Icon(
                            imageVector = Icons.Default.Motorcycle,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.03f),
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = 12.dp, y = 12.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Active Bikes",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                                color = TextGray
                            )
                            Text(
                                text = activeBikesCount,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontFamily = MetricFamily,
                                    fontSize = 28.sp
                                ),
                                color = TextWhite
                            )
                            Text(
                                text = "GARAGE",
                                style = MaterialTheme.typography.labelLarge,
                                color = RacingOrange,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Start Ride (CTA Button)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.startRide() }
                    .testTag("start_ride_cta")
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                RacingOrange.copy(alpha = 0.8f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = RacingOrange)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = "Start action indicator",
                            tint = TextWhite,
                            modifier = Modifier.size(44.dp)
                        )
                        Box(
                            modifier = Modifier
                                .clip(CapsuleShape())
                                .background(TextWhite.copy(alpha = 0.15f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "LIVE TRACKING",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "START RIDE",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite,
                        letterSpacing = (-0.5).sp
                    )

                    val activeBike = bikes.find { it.isActive }
                    Text(
                        text = "READY • CONNECTED TO ${activeBike?.name?.uppercase() ?: "DUCATI V4"}",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                        color = TextWhite.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Service Reminder Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, RacingOrange.copy(alpha = 0.2f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = RacingOrange.copy(alpha = 0.05f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(RacingOrange.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "Wrench icon",
                                tint = RacingOrange,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Service Due Soon",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextWhite
                            )
                            val activeBike = bikes.find { it.isActive }
                            Text(
                                text = "Oil change in 240 miles • ${activeBike?.name ?: "Ducati V4"}",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                                color = TextGray
                            )
                        }
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "Maintenance scheduled!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MidnightSurfaceContainerHigh
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "SCHEDULE",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                            color = RacingOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Recent Rides Title Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Rides",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextWhite
                )

                Text(
                    text = "VIEW ALL",
                    style = MaterialTheme.typography.labelLarge,
                    color = RacingOrange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        viewModel.selectTab(BeeRideTab.FEED)
                    }
                )
            }
        }

        // Recent Activity List elements (renders last 2 activity rows dynamically!)
        if (rides.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No rides completed yet", color = TextGray)
                }
            }
        } else {
            items(rides.take(4)) { ride ->
                RecentRideItem(ride = ride, onClick = {
                    Toast.makeText(context, "Telemetry detail: ${ride.title}", Toast.LENGTH_SHORT).show()
                })
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun RecentRideItem(
    ride: Ride,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(1.dp, BorderColor, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightSurfaceDim),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stylized micro graphic mimicking image mapping of track route
            Box(
                modifier = Modifier
                    .size(width = 86.dp, height = 56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1F1F1F))
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            ) {
                // Subtle overlay pattern simulating track path
                MapGraphicIndicator(routeIndex = ride.routeIndex)
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${ride.title} • ${ride.location}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextWhite,
                    maxLines = 1
                )
                Text(
                    text = "${ride.date} • ${ride.distance} ${ride.unit.lowercase()}",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                    color = TextGray
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${ride.topSpeed.toInt()} MPH",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFamily = MetricFamily,
                        fontSize = 13.sp
                    ),
                    color = AmberYellow,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "MAX",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                    color = TextGray
                )
            }
        }
    }
}

@Composable
fun CapsuleShape(): RoundedCornerShape = RoundedCornerShape(50)
