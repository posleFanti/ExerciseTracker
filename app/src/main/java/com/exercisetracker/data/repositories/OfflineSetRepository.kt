package com.exercisetracker.data.repositories

import com.exercisetracker.data.dao.SetDao
import com.exercisetracker.data.entities.Set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OfflineSetRepository(
    private val setDao: SetDao
) : SetRepository {
    override fun getSetsByWorkoutId(workoutId: Long): Flow<List<Set>> =
        setDao.getSetsByWorkoutId(workoutId)

    override fun getSets(
        workoutId: Long,
        exerciseId: Long
    ): Flow<List<Set>?> =
        setDao.getSets(workoutId = workoutId, exerciseId = exerciseId)

    override fun getAllSets(exerciseId: Long): Flow<List<Set>> =
        setDao.getSetsByExerciseId(exerciseId)

    override suspend fun insertSet(set: Set): Long =
        setDao.insert(set)

    override suspend fun deleteSet(set: Set) =
        setDao.delete(set)

    override suspend fun updateSet(set: Set) =
        setDao.update(set)

    override suspend fun runInTransaction(block: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            setDao.runInTransaction(block)
        }
    }
}