package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    // Instantiate MainViewModel safely
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Fully edge-to-edge layout execution
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val currentTab by viewModel.currentTab.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBarHeader(currentTab = currentTab)
                    },
                    bottomBar = {
                        BottomNavBarCustom(
                            currentTab = currentTab,
                            onTabSelected = { viewModel.selectTab(it) }
                        )
                    },
                    containerColor = MidnightBackground
                ) { innerPadding ->
                    // Main viewport switching
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding() / 2f)
                    ) {
                        when (currentTab) {
                            BeeRideTab.HOME -> HomeTab(
                                viewModel = viewModel,
                                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                            )
                            BeeRideTab.TRACKING -> TrackingTab(
                                viewModel = viewModel,
                                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                            )
                            BeeRideTab.GARAGE -> GarageTab(
                                viewModel = viewModel,
                                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                            )
                            BeeRideTab.FEED -> FeedTab(
                                viewModel = viewModel,
                                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                            )
                            BeeRideTab.PROFILE -> ProfileTab(
                                viewModel = viewModel,
                                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                            )
                        }
                    }
                }
            }
        }
    }
}

// Custom top app header matching mockup identity
@Composable
fun TopAppBarHeader(currentTab: BeeRideTab) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MidnightBackground.copy(alpha = 0.9f))
            .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Circular mini motorcycle logo icon
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(RacingOrange.copy(alpha = 0.15f))
                        .border(1.dp, RacingOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Motorcycle,
                        contentDescription = null,
                        tint = RacingOrange,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "BeeRide",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = RacingOrange,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }

            // Notification button
            IconButton(onClick = { /* Action handled */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications indicator",
                    tint = TextGray,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

// Custom premium bottom navigation bar
@Composable
fun BottomNavBarCustom(
    currentTab: BeeRideTab,
    onTabSelected: (BeeRideTab) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        color = MidnightSurfaceDim,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(72.dp)
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tabs Row
            BottomTabItem(
                tab = BeeRideTab.HOME,
                labelText = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                currentTab = currentTab,
                onClick = onTabSelected,
                modifier = Modifier.testTag("tab_home")
            )

            BottomTabItem(
                tab = BeeRideTab.TRACKING,
                labelText = "Tracking",
                selectedIcon = Icons.Filled.Speed,
                unselectedIcon = Icons.Outlined.Speed,
                currentTab = currentTab,
                onClick = onTabSelected,
                modifier = Modifier.testTag("tab_tracking")
            )

            BottomTabItem(
                tab = BeeRideTab.GARAGE,
                labelText = "Garage",
                selectedIcon = Icons.Filled.Motorcycle,
                unselectedIcon = Icons.Outlined.Motorcycle,
                currentTab = currentTab,
                onClick = onTabSelected,
                modifier = Modifier.testTag("tab_garage")
            )

            BottomTabItem(
                tab = BeeRideTab.FEED,
                labelText = "Feed",
                selectedIcon = Icons.Filled.RssFeed,
                unselectedIcon = Icons.Outlined.RssFeed,
                currentTab = currentTab,
                onClick = onTabSelected,
                modifier = Modifier.testTag("tab_feed")
            )

            BottomTabItem(
                tab = BeeRideTab.PROFILE,
                labelText = "Profile",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                currentTab = currentTab,
                onClick = onTabSelected,
                modifier = Modifier.testTag("tab_profile")
            )
        }
    }
}

@Composable
fun BottomTabItem(
    tab: BeeRideTab,
    labelText: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    currentTab: BeeRideTab,
    onClick: (BeeRideTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = currentTab == tab

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(tab) }
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Decorative bubble matching the screenshot
        Box(
            modifier = Modifier
                .clip(CapsuleShape())
                .background(
                    if (isSelected) RacingOrange.copy(alpha = 0.12f) else Color.Transparent
                )
                .padding(horizontal = 14.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                contentDescription = labelText,
                tint = if (isSelected) RacingOrange else TextGray,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = labelText,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (isSelected) RacingOrange else TextGray
        )
    }
}
