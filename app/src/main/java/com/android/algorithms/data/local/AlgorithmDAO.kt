package com.android.algorithms.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

/**
 * Interface for all room database interactions.
 * @see AlgorithmDatabase
 * @see Dao
 */
@Dao
interface AlgorithmDAO {
    /**
     * Insert [AlgorithmInfoEntity] to the database.
     * @param algorithmInfo Instance of [AlgorithmInfoEntity].
     */
    @Insert
    suspend fun insertAlgorithmInfo(algorithmInfo: AlgorithmInfoEntity)

    /**
     * Insert list of [AlgorithmDataEntity] to the database.
     * @param algorithmDataList List of [AlgorithmDataEntity].
     */
    @Insert
    suspend fun insertAlgorithmData(algorithmDataList: List<AlgorithmDataEntity>)


    /**
     * Update [AlgorithmInfoEntity] in the database.
     * @param algorithmInfo Instance of [AlgorithmInfoEntity].
     */
    @Update
    suspend fun updateAlgorithmInfo(algorithmInfo: AlgorithmInfoEntity)

    /**
     * Update list of [AlgorithmDataEntity] in the database.
     * @param algorithmDataList List of [AlgorithmDataEntity].
     */
    @Update
    suspend fun updateAlgorithmData(algorithmDataList: List<AlgorithmDataEntity>)

    // TODO: update by name

    /**
     * Delete [AlgorithmInfoEntity] from the database.
     * @param algorithmInfo Instance of [AlgorithmInfoEntity].
     */
    @Delete
    suspend fun deleteAlgorithmInfo(algorithmInfo: AlgorithmInfoEntity)

    /**
     * Delete list of [AlgorithmDataEntity] from the database.
     * @param algorithmDataList List of [AlgorithmDataEntity].
     */
    @Delete
    suspend fun deleteAlgorithmData(algorithmDataList: List<AlgorithmDataEntity>)


    /**
     * Get a full list of [AlgorithmInfoEntity] from the database.
     * @return List of [AlgorithmInfoEntity] instances.
     */
    @Query("SELECT * FROM algorithms")
    suspend fun getAlgorithmDeclarations(): List<AlgorithmInfoEntity>

    /**
     * Get all algorithm details by algorithm name.
     * @param algorithmName Name of the algorithm.
     * @return [AlgorithmEntity] - nullable entity of algorithm
     */
    @Transaction // Room runs two queries
    @Query("SELECT * FROM algorithms WHERE algorithm_name = :algorithmName")
    suspend fun getAlgorithmDetails(algorithmName: String): AlgorithmEntity?
}