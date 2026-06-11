package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// --- entities ---

@Entity(tableName = "rides")
data class Ride(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val location: String,
    val distance: Double, // in km or miles
    val unit: String = "KM", // "KM" or "MI"
    val avgSpeed: Double,
    val topSpeed: Double,
    val date: String,
    val fuel: Double? = null,
    val duration: String = "00:00",
    val routeIndex: Int = 1 // index to draw different route graphics
)

@Entity(tableName = "bikes")
data class Bike(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val brand: String,
    val modelName: String,
    val odometer: Double,
    val unit: String = "KM", // "KM" or "MI"
    val isActive: Boolean = false,
    val lastServiceDate: String = "Oct 12, 2023",
    val tirePressure: String = "34 / 42 PSI"
)

// --- DAOs ---

@Dao
interface RideDao {
    @Query("SELECT * FROM rides ORDER BY id DESC")
    fun getAllRidesFlow(): Flow<List<Ride>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRide(ride: Ride)

    @Delete
    suspend fun deleteRide(ride: Ride)
}

@Dao
interface BikeDao {
    @Query("SELECT * FROM bikes ORDER BY id DESC")
    fun getAllBikesFlow(): Flow<List<Bike>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBike(bike: Bike)

    @Update
    suspend fun updateBike(bike: Bike)

    @Query("UPDATE bikes SET isActive = 0")
    suspend fun deactivateAll()

    @Query("UPDATE bikes SET isActive = 1 WHERE id = :bikeId")
    suspend fun activateBike(bikeId: Int)

    @Delete
    suspend fun deleteBike(bike: Bike)
}

// --- Database ---

@Database(entities = [Ride::class, Bike::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val rideDao: RideDao
    abstract val bikeDao: BikeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "beeride_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
