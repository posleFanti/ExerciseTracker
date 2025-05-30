package com.exercisetracker.data.repositories

import com.exercisetracker.data.entities.Set
import kotlinx.coroutines.flow.Flow

interface SetRepository {
    fun getSetsByWorkoutId(workoutId: Long): Flow<List<Set>>
    fun getSets(workoutId: Long, exerciseId: Long): Flow<List<Set>?>
    fun getAllSets(exerciseId: Long): Flow<List<Set>>
    suspend fun insertSet(set: Set): Long
    suspend fun deleteSet(set: Set)
    suspend fun updateSet(set: Set)
    suspend fun runInTransaction(block: suspend () -> Unit)
}