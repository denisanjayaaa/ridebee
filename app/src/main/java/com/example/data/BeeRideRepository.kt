package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class BeeRideRepository(private val db: AppDatabase) {
    val rides: Flow<List<Ride>> = db.rideDao.getAllRidesFlow()
    val bikes: Flow<List<Bike>> = db.bikeDao.getAllBikesFlow()

    suspend fun insertRide(ride: Ride) = db.rideDao.insertRide(ride)
    suspend fun deleteRide(ride: Ride) = db.rideDao.deleteRide(ride)

    suspend fun insertBike(bike: Bike) = db.bikeDao.insertBike(bike)
    suspend fun updateBike(bike: Bike) = db.bikeDao.updateBike(bike)
    suspend fun deleteBike(bike: Bike) = db.bikeDao.deleteBike(bike)

    suspend fun selectActiveBike(bikeId: Int) {
        db.bikeDao.deactivateAll()
        db.bikeDao.activateBike(bikeId)
    }

    suspend fun populateInitialDataIfEmpty() {
        // Check if bikes is empty
        val currentBikes = db.bikeDao.getAllBikesFlow().first()
        if (currentBikes.isEmpty()) {
            val initialBikes = listOf(
                Bike(
                    name = "Nightshade R1",
                    brand = "DUCATI",
                    modelName = "PANIGALE V4",
                    odometer = 12450.0,
                    isActive = true,
                    lastServiceDate = "Oct 12, 2023",
                    tirePressure = "34 / 42 PSI"
                ),
                Bike(
                    name = "Desert Storm",
                    brand = "BMW",
                    modelName = "R 1250 GS",
                    odometer = 8120.0,
                    isActive = false,
                    lastServiceDate = "May 20, 2024",
                    tirePressure = "36 / 40 PSI"
                ),
                Bike(
                    name = "Street Triple RS",
                    brand = "TRIUMPH",
                    modelName = "STREET TRIPLE RS",
                    odometer = 11200.0,
                    isActive = false,
                    lastServiceDate = "Oct 12, 2023",
                    tirePressure = "34 / 42 PSI"
                )
            )
            for (bike in initialBikes) {
                db.bikeDao.insertBike(bike)
            }
        }

        // Check if rides is empty
        val currentRides = db.rideDao.getAllRidesFlow().first()
        if (currentRides.isEmpty()) {
            val initialRides = listOf(
                Ride(
                    title = "Mountain Twisties",
                    location = "Malibu Canyon",
                    distance = 124.5,
                    unit = "KM",
                    avgSpeed = 86.2,
                    topSpeed = 168.0,
                    date = "SAT, OCT 26 • 09:15 AM",
                    duration = "01:24:45",
                    routeIndex = 1
                ),
                Ride(
                    title = "Sunset Coastal Run",
                    location = "Pacific Coast Hwy",
                    distance = 42.8,
                    avgSpeed = 62.0,
                    topSpeed = 112.0,
                    unit = "KM",
                    date = "FRI, OCT 25 • 05:40 PM",
                    duration = "00:48:12",
                    routeIndex = 2
                ),
                Ride(
                    title = "City Commute",
                    location = "Downtown Loop",
                    distance = 8.2,
                    avgSpeed = 24.5,
                    topSpeed = 42.0,
                    unit = "KM",
                    fuel = 0.6,
                    date = "WED, OCT 23 • 12:15 PM",
                    duration = "00:18:42",
                    routeIndex = 3
                ),
                Ride(
                    title = "Canyon Run",
                    location = "Malibu Hills",
                    distance = 42.5,
                    avgSpeed = 64.0,
                    topSpeed = 84.0,
                    unit = "MILES",
                    date = "2 days ago",
                    duration = "00:52:10",
                    routeIndex = 1
                ),
                Ride(
                    title = "Night Loop",
                    location = "Downtown Freeway",
                    distance = 18.2,
                    avgSpeed = 32.0,
                    topSpeed = 42.0,
                    unit = "MILES",
                    date = "Last Week",
                    duration = "00:28:15",
                    routeIndex = 2
                )
            )
            for (ride in initialRides) {
                db.rideDao.insertRide(ride)
            }
        }
    }
}
