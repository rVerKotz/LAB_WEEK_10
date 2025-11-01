package eu.tutorials.lab_week_10.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Total::class], version = 1)
abstract class TotalDatabase : RoomDatabase() { // 2. Make it an abstract class that extends RoomDatabase

    // 3. Add an abstract function to get your DAO
    abstract fun totalDao(): TotalDao
}