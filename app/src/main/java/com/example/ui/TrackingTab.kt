package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun TrackingTab(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isTrackingActive by viewModel.isTrackingActive.collectAsState()
    val isTrackingPaused by viewModel.isTrackingPaused.collectAsState()
    val speed by viewModel.simSpeed.collectAsState()
    val distance by viewModel.simDistance.collectAsState()
    val durationSeconds by viewModel.simDurationSeconds.collectAsState()
    val isSimMode by viewModel.isSimulationMode.collectAsState()
    val showStopDialog by viewModel.showStopConfirmation.collectAsState()

    // Formatted duration
    val minutes = (durationSeconds % 3600) / 60
    val seconds = durationSeconds % 60
    val formattedDuration = String.format("%02d:%02d", minutes, seconds)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBackground)
    ) {
        if (!isTrackingActive) {
            // Not active tracking state - beautiful call to action
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(RacingOrange.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = RacingOrange,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Text(
                        text = "Ready to hit the road?",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextWhite
                    )

                    Text(
                        text = "Connect to your motorcycle and launch real-time telemetry HUD tracking.",
                        color = TextGray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.startRide() },
                        colors = ButtonDefaults.buttonColors(containerColor = RacingOrange),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .height(54.dp)
                            .width(220.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = TextWhite
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "START SESSION",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // HIGH-FIDELITY ACTIVE COCKPIT TRACKER HUD
            // Bottom Simulated Map coordinate Canvas
            MapCoordinateCanvasHUD(isPaused = isTrackingPaused)

            // Top GPS / Sim toggle float panel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Signals
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .clip(CapsuleShape())
                        .background(MidnightSurface.copy(alpha = 0.8f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isTrackingPaused) AmberYellow else Color.Green)
                        )
                        Text(
                            text = if (isTrackingPaused) "PAUSED" else "TRACKING",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                            color = TextWhite
                        )
                    }
                }

                // GPS / SIM trigger toggle
                Row(
                    modifier = Modifier
                        .clip(CapsuleShape())
                        .background(MidnightSurface.copy(alpha = 0.8f))
                        .border(1.dp, BorderColor, CapsuleShape())
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // GPS mode Option
                    Box(
                        modifier = Modifier
                            .clip(CapsuleShape())
                            .background(if (!isSimMode) RacingOrange else Color.Transparent)
                            .clickable { viewModel.setSimulationMode(false) }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "GPS",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                            color = if (!isSimMode) TextWhite else TextGray,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // SIM mode option
                    Box(
                        modifier = Modifier
                            .clip(CapsuleShape())
                            .background(if (isSimMode) RacingOrange else Color.Transparent)
                            .clickable { viewModel.setSimulationMode(true) }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "SIM",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                            color = if (isSimMode) TextWhite else TextGray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Floating Telemetry Display Bento panel overlain
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp, start = 20.dp, end = 20.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderColor, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MidnightSurface.copy(alpha = 0.85f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Current Speed
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "SPEED",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                                color = TextGray
                            )
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = speed.toInt().toString(),
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
                                    color = RacingOrange
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "km/h",
                                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                                    color = TextGray,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                        }

                        // Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(BorderColor)
                        )

                        // Distance accumulated
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "DISTANCE",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                                color = TextGray
                            )
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                val distanceFormatted = String.format("%.1f", distance)
                                Text(
                                    text = distanceFormatted,
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
                                    color = RacingOrange
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "km",
                                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                                    color = TextGray,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                        }

                        // Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(BorderColor)
                        )

                        // Elapsed duration
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "DURATION",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                                color = TextGray
                            )
                            Text(
                                text = formattedDuration,
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                                color = RacingOrange,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Telemetry Mini Info & Controllers
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp, start = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Left telemetry display
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MidnightSurface.copy(alpha = 0.85f))
                            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = RacingOrange,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "14.2V SYSTEM",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                                color = TextWhite
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Thermostat,
                                contentDescription = null,
                                tint = RacingOrange,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "88°C TEMP",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                                color = TextWhite
                            )
                        }
                    }

                    // Floating core play pause stop controls
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pause / Play toggle bubble
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(MidnightSurface.copy(alpha = 0.85f))
                                .border(1.dp, RacingOrange, CircleShape)
                                .clickable { viewModel.togglePauseResume() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isTrackingPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = if (isTrackingPaused) "Resume" else "Pause",
                                tint = RacingOrange,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // Stop Action Button
                        Button(
                            onClick = { viewModel.triggerStopConfirmation(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = RacingOrange),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("stop_ride_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop",
                                tint = TextWhite
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "STOP",
                                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Right float system details
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MidnightSurface.copy(alpha = 0.85f))
                            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "SIGNAL STABLE",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                                color = TextWhite
                            )
                            Icon(
                                imageVector = Icons.Default.SignalCellular4Bar,
                                contentDescription = null,
                                tint = RacingOrange,
                                modifier = Modifier.size(13.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "GPS LOCKED",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                                color = TextWhite
                            )
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = RacingOrange,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        }

        // Animated dialog overlay trigger
        if (showStopDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.triggerStopConfirmation(false) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = RacingOrange,
                        modifier = Modifier.size(40.dp)
                    )
                },
                title = {
                    Text(
                        text = "Finish Ride?",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextWhite
                    )
                },
                text = {
                    Text(
                        text = "Ending this session will save your telemetries and route to your feed. You cannot undo this action.",
                        color = TextGray
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.saveTelemetryAndStop() },
                        colors = ButtonDefaults.buttonColors(containerColor = RacingOrange)
                    ) {
                        Text(text = "FINISH & SAVE", color = TextWhite, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(
                            onClick = { viewModel.discardTelemetryAndStop() }
                        ) {
                            Text(text = "DISCARD RUN", color = Color.Red.copy(alpha = 0.8f))
                        }
                        TextButton(
                            onClick = { viewModel.triggerStopConfirmation(false) }
                        ) {
                            Text(text = "KEEP RIDING", color = TextWhite)
                        }
                    }
                },
                containerColor = MidnightSurface,
                textContentColor = TextGray,
                titleContentColor = TextWhite
            )
        }
    }
}

// Custom vector dynamic canvas to represent active GPS track map!
@Composable
fun MapCoordinateCanvasHUD(isPaused: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "Radar pulse")
    val dotColorAnim by infiniteTransition.animateColor(
        initialValue = RacingOrange.copy(alpha = 0.3f),
        targetValue = RacingOrange,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "DotPulse"
    )

    // Animated dash path progress
    val currentPositionOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "MarkerAnim"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
    ) {
        val width = size.width
        val height = size.height

        // 1. Draw coordinate dot grids
        val dotRadius = 1.3.dp.toPx()
        val spacing = 35.dp.toPx()

        for (x in 0..(width / spacing).toInt()) {
            for (y in 0..(height / spacing).toInt()) {
                drawCircle(
                    color = Color(0xFF1B1B1D),
                    radius = dotRadius,
                    center = Offset(x * spacing, y * spacing)
                )
            }
        }

        // Draw HUD alignment lines
        drawLine(
            color = Color(0xFF202022),
            start = Offset(0f, height / 2f),
            end = Offset(width, height / 2f),
            strokeWidth = 0.8f
        )
        drawLine(
            color = Color(0xFF202022),
            start = Offset(width / 2f, 0f),
            end = Offset(width / 2f, height),
            strokeWidth = 0.8f
        )

        // 2. Beautiful glowing telemetry trace trajectory path
        val path = Path().apply {
            moveTo(width * 0.15f, height * 0.85f)
            quadraticBezierTo(
                width * 0.40f, height * 0.70f,
                width * 0.60f, height * 0.45f
            )
            quadraticBezierTo(
                width * 0.72f, height * 0.30f,
                width * 0.85f, height * 0.20f
            )
        }

        // Draw track vector shadow
        drawPath(
            path = path,
            color = RacingOrange.copy(alpha = 0.12f),
            style = Stroke(width = 12.dp.toPx())
        )

        // Draw main vector segment path
        drawPath(
            path = path,
            color = RacingOrange,
            style = Stroke(width = 4.dp.toPx())
        )

        // 3. Moving motorcycle indicator point
        // Calculate dynamic interpolation on bezier path
        // For visual, let's place it at a steady curve coordinate
        val markerX = width * 0.60f
        val markerY = height * 0.45f

        if (!isPaused) {
            // Radar pulse ring
            drawCircle(
                color = dotColorAnim.copy(alpha = 0.25f),
                radius = 24.dp.toPx(),
                center = Offset(markerX, markerY)
            )
        }

        drawCircle(
            color = RacingOrange,
            radius = 7.dp.toPx(),
            center = Offset(markerX, markerY)
        )

        drawCircle(
            color = TextWhite,
            radius = 3.dp.toPx(),
            center = Offset(markerX, markerY)
        )
    }
}

@Composable
fun MapGraphicIndicator(routeIndex: Int) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Simple abstract paths
        val path = Path().apply {
            when (routeIndex) {
                1 -> {
                    moveTo(width * 0.1f, height * 0.8f)
                    quadraticBezierTo(width * 0.5f, height * 0.2f, width * 0.9f, height * 0.5f)
                }
                2 -> {
                    moveTo(width * 0.2f, height * 0.2f)
                    lineTo(width * 0.4f, height * 0.8f)
                    lineTo(width * 0.6f, height * 0.3f)
                    lineTo(width * 0.8f, height * 0.7f)
                }
                else -> {
                    moveTo(width * 0.1f, height * 0.5f)
                    quadraticBezierTo(width * 0.3f, height * 0.8f, width * 0.5f, height * 0.5f)
                    quadraticBezierTo(width * 0.7f, height * 0.2f, width * 0.9f, height * 0.5f)
                }
            }
        }

        drawPath(
            path = path,
            color = RacingOrange.copy(alpha = 0.5f),
            style = Stroke(width = 3.dp.toPx())
        )

        // End circle
        drawCircle(
            color = RacingOrange,
            radius = 3.dp.toPx(),
            center = Offset(width * 0.85f, height * 0.5f)
        )
    }
}
