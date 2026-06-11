package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.BeeRideRepository
import com.example.data.Bike
import com.example.data.Ride
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class BeeRideTab {
    HOME, TRACKING, GARAGE, FEED, PROFILE
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    val repository = BeeRideRepository(database)

    // Current screen state
    private val _currentTab = MutableStateFlow(BeeRideTab.HOME)
    val currentTab: StateFlow<BeeRideTab> = _currentTab.asStateFlow()

    // Room DB lists
    val rides: StateFlow<List<Ride>> = repository.rides.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bikes: StateFlow<List<Bike>> = repository.bikes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Current tracking attributes
    private val _isTrackingActive = MutableStateFlow(false)
    val isTrackingActive = _isTrackingActive.asStateFlow()

    private val _isTrackingPaused = MutableStateFlow(false)
    val isTrackingPaused = _isTrackingPaused.asStateFlow()

    private val _isSimulationMode = MutableStateFlow(true)
    val isSimulationMode = _isSimulationMode.asStateFlow()

    private val _simSpeed = MutableStateFlow(84f)
    val simSpeed = _simSpeed.asStateFlow()

    private val _simDistance = MutableStateFlow(12.4f)
    val simDistance = _simDistance.asStateFlow()

    private val _simDurationSeconds = MutableStateFlow(1122L) // 18m 42s initially
    val simDurationSeconds = _simDurationSeconds.asStateFlow()

    // Live Tracking Dialog
    private val _showStopConfirmation = MutableStateFlow(false)
    val showStopConfirmation = _showStopConfirmation.asStateFlow()

    // User Profile in-memory states (with defaults, editable in Profile)
    private val _userName = MutableStateFlow("Alex Mercer")
    val userName = _userName.asStateFlow()

    private val _userMotorcycle = MutableStateFlow("Triumph Street Triple RS")
    val userMotorcycle = _userMotorcycle.asStateFlow()

    private val _userLocation = MutableStateFlow("London, UK")
    val userLocation = _userLocation.asStateFlow()

    private val _isProUser = MutableStateFlow(true)
    val isProUser = _isProUser.asStateFlow()

    // Preferences toggles
    val notificationEnabled = MutableStateFlow(true)
    val rideTrackingEnabled = MutableStateFlow(true)
    val publicProfileEnabled = MutableStateFlow(false)

    // Maintenance logs (0.0 to 1.0)
    private val _engineOilStatus = MutableStateFlow(0.85f)
    val engineOilStatus = _engineOilStatus.asStateFlow()

    private val _tirePressureStatus = MutableStateFlow(0.15f) // critical
    val tirePressureStatus = _tirePressureStatus.asStateFlow()

    private val _airFilterStatus = MutableStateFlow(0.42f)
    val airFilterStatus = _airFilterStatus.asStateFlow()

    private val _radiatorFlushStatus = MutableStateFlow(0.60f)
    val radiatorFlushStatus = _radiatorFlushStatus.asStateFlow()

    private var trackingJob: Job? = null

    init {
        // Hydrate default values
        viewModelScope.launch {
            repository.populateInitialDataIfEmpty()
        }
    }

    fun selectTab(tab: BeeRideTab) {
        _currentTab.value = tab
    }

    // Toggle simulated GPS / SIM mode
    fun setSimulationMode(isSim: Boolean) {
        _isSimulationMode.value = isSim
    }

    // Start/Resume the simulated tracking loop
    fun startRide() {
        _isTrackingActive.value = true
        _isTrackingPaused.value = false
        _simSpeed.value = 84f
        _simDistance.value = 12.4f
        _simDurationSeconds.value = 1122L // start with 18m 42s as the mockup

        trackingJob?.cancel()
        trackingJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (!_isTrackingPaused.value) {
                    // Inject realistic variance with speed jitter
                    val speedJitter = (Math.random() - 0.5) * 4.4
                    var newSpeed = _simSpeed.value + speedJitter.toFloat()
                    if (newSpeed < 0f) newSpeed = 0f
                    if (newSpeed > 180f) newSpeed = 180f
                    _simSpeed.value = newSpeed

                    // Distance accrues based on current speed
                    // distance accrued in km = (speed km/h) * (1 hour / 3600 seconds)
                    val distIncrement = newSpeed / 3600f
                    _simDistance.value += distIncrement

                    _simDurationSeconds.value += 1
                }
            }
        }
        selectTab(BeeRideTab.TRACKING)
    }

    fun togglePauseResume() {
        _isTrackingPaused.value = !_isTrackingPaused.value
    }

    fun triggerStopConfirmation(show: Boolean) {
        _showStopConfirmation.value = show
    }

    // Finish, compile stats, insert in DB, and clean up active tracking
    fun saveTelemetryAndStop() {
        trackingJob?.cancel()
        trackingJob = null
        _isTrackingActive.value = false
        _showStopConfirmation.value = false

        // Compile and write Ride
        val sdf = SimpleDateFormat("EEE, MMM dd • hh:mm a", Locale.ENGLISH)
        val currentDateStr = sdf.format(Date()).uppercase()

        val hours = _simDurationSeconds.value / 3600
        val minutes = (_simDurationSeconds.value % 3600) / 60
        val seconds = _simDurationSeconds.value % 60
        val durationFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        val ride = Ride(
            title = if (currentDateStr.contains("AM")) "Morning Coastal Cruise" else "Night Sunset Cruise",
            location = "London Loop",
            distance = Math.round(_simDistance.value * 10.0) / 10.0,
            unit = "KM",
            avgSpeed = 74.5,
            topSpeed = 114.0,
            date = currentDateStr,
            duration = durationFormatted,
            fuel = 1.1,
            routeIndex = (1..3).random()
        )

        viewModelScope.launch {
            repository.insertRide(ride)
            // Add mileage to active bike
            bikes.value.firstOrNull { it.isActive }?.let { activeBike ->
                val updatedBike = activeBike.copy(
                    odometer = activeBike.odometer + ride.distance
                )
                repository.updateBike(updatedBike)
            }
        }

        // Navigate directly to Feed to show saved log!
        selectTab(BeeRideTab.FEED)
    }

    fun discardTelemetryAndStop() {
        trackingJob?.cancel()
        trackingJob = null
        _isTrackingActive.value = false
        _showStopConfirmation.value = false
        selectTab(BeeRideTab.HOME)
    }

    // Profile updates
    fun updateProfile(name: String, moto: String, loc: String) {
        _userName.value = name
        _userMotorcycle.value = moto
        _userLocation.value = loc
    }

    // Fleet management
    fun addNewBike(name: String, brand: String, model: String, startingOdo: Double) {
        viewModelScope.launch {
            val newBike = Bike(
                name = name,
                brand = brand.uppercase(),
                modelName = model.uppercase(),
                odometer = startingOdo,
                isActive = false
            )
            repository.insertBike(newBike)
        }
    }

    fun makeBikeActive(bikeId: Int) {
        viewModelScope.launch {
            repository.selectActiveBike(bikeId)
        }
    }

    fun deleteBike(bike: Bike) {
        viewModelScope.launch {
            repository.deleteBike(bike)
        }
    }

    // Maintenance Logs click actions
    fun logMaintenanceCompleted(type: String) {
        when (type) {
            "OIL" -> _engineOilStatus.value = 1.0f
            "TIRE" -> _tirePressureStatus.value = 1.0f
            "FILTER" -> _airFilterStatus.value = 1.0f
            "RADIATOR" -> _radiatorFlushStatus.value = 1.0f
        }
    }
}
