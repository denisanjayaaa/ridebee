package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Ride
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun FeedTab(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val rides by viewModel.rides.collectAsState()

    var selectedShareRide by remember { mutableStateOf<Ride?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Text(
                        text = "Activity Feed",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextWhite
                    )
                    Text(
                        text = "Review your performance telemetry",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextGray
                    )
                }
            }

            // Rides list
            if (rides.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No rides completed yet", color = TextGray)
                    }
                }
            } else {
                items(rides) { ride ->
                    RideFeedCard(
                        ride = ride,
                        onShare = { selectedShareRide = ride },
                        onDelete = {
                            scope.launch {
                                viewModel.repository.deleteRide(ride)
                            }
                            Toast.makeText(context, "Ride deleted", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(110.dp))
            }
        }

        // Tactical Custom Share Dialog sheet overlay
        selectedShareRide?.let { ride ->
            ShareInteractiveModal(
                ride = ride,
                onDismiss = { selectedShareRide = null }
            )
        }
    }
}

// Coroutine helpers can be removed safely

@Composable
fun RideFeedCard(
    ride: Ride,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header titles row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = ride.date.uppercase(),
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                        color = RacingOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ride.title,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ride.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MidnightSurfaceContainerHigh)
                            .size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share ride details",
                            tint = TextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MidnightSurfaceContainerHigh)
                            .size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete ride log",
                            tint = Color.Red.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Animated stylized GPS map trace box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MidnightSurfaceDim)
                    .border(1.dp, BorderColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                MapGraphicIndicator(routeIndex = ride.routeIndex)

                // Route saved capsule badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(RacingOrange.copy(alpha = 0.15f))
                        .border(1.dp, RacingOrange.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ROUTE SAVED",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 8.sp),
                        color = RacingOrange,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Performance spec attributes row bento list
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Dist
                Column {
                    Text(
                        text = "DISTANCE",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                        color = TextGray
                    )
                    Text(
                        text = "${ride.distance} ${ride.unit}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextWhite
                    )
                }

                // Avg speed
                Column {
                    Text(
                        text = "AVG SPEED",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                        color = TextGray
                    )
                    Text(
                        text = "${ride.avgSpeed} KM/H",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextWhite
                    )
                }

                // Top Speed
                Column {
                    Text(
                        text = "TOP SPEED",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                        color = TextGray
                    )
                    Text(
                        text = "${ride.topSpeed.toInt()} KM/H",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = AmberYellow
                    )
                }

                // Fuel or Duration if fuel is missing
                Column(horizontalAlignment = Alignment.End) {
                    if (ride.fuel != null) {
                        Text(
                            text = "FUEL CONSUMED",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                            color = TextGray
                        )
                        Text(
                            text = "${ride.fuel} L",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextWhite
                        )
                    } else {
                        Text(
                            text = "DURATION",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                            color = TextGray
                        )
                        Text(
                            text = ride.duration,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = MetricFamily
                            ),
                            color = TextWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShareInteractiveModal(
    ride: Ride,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Share Ride Route",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextWhite
                )
                Text(
                    text = "${ride.distance} ${ride.unit.uppercase()} TRACKED",
                    style = MaterialTheme.typography.labelLarge,
                    color = RacingOrange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Faux QR Code box with Motorcycle in the middle!
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // QR Code matrix rendering simulated using Canvas dot grids
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val sizeX = 9
                        val sizeY = 9
                        val blockW = this.size.width / sizeX
                        val blockH = this.size.height / sizeY

                        for (i in 0 until sizeX) {
                            for (j in 0 until sizeY) {
                                // Draw random block elements
                                if ((i + j) % 2 == 0 || (i * j % 3 == 1 && (i > 1 && j > 1)) || (i < 3 && j < 3) || (i > 5 && j < 3) || (i < 3 && j > 5)) {
                                    drawRect(
                                        color = Color.Black,
                                        topLeft = Offset(i * blockW + 2f, j * blockH + 2f),
                                        size = Size(blockW - 4f, blockH - 4f)
                                    )
                                }
                            }
                        }
                    }

                    // Centered bike icon badge
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .border(1.dp, BorderColor, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Motorcycle,
                            contentDescription = null,
                            tint = RacingOrange,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // Copy URL card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = MidnightSurfaceDim),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "beeride.app/r/7x8k2q",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextGray,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "COPY",
                            style = MaterialTheme.typography.labelLarge,
                            color = RacingOrange,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    Toast.makeText(context, "Route link copied to dashboard!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    Toast.makeText(context, "Instagram shared preview prepared!", Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = RacingOrange),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = TextWhite
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "SHARE TO INSTAGRAM", color = TextWhite, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().height(44.dp)
            ) {
                Text(text = "CLOSE PREVIEW", color = TextGray)
            }
        },
        containerColor = MidnightSurface,
        titleContentColor = TextWhite
    )
}
