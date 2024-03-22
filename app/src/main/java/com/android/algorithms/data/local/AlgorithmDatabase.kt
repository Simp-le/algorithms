package com.android.algorithms.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Represents a Room database for storing algorithm-related entities.
 *
 * @property algorithmDAO Data Access Object for accessing algorithm-related entities.
 * @constructor Creates an instance of AlgorithmDatabase.
 * @see RoomDatabase
 */
@Database(
    entities = [AlgorithmInfoEntity::class, AlgorithmDataEntity::class],
    version = 1
)
abstract class AlgorithmDatabase : RoomDatabase() {
    abstract val algorithmDAO: AlgorithmDAO
}

