package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ServiceCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkerDao {
    @Query("SELECT * FROM workers ORDER BY rating DESC, completedJobsCount DESC")
    fun getAllWorkers(): Flow<List<WorkerEntity>>

    @Query("SELECT * FROM workers WHERE category = :category ORDER BY rating DESC")
    fun getWorkersByCategory(category: ServiceCategory): Flow<List<WorkerEntity>>

    @Query("SELECT * FROM workers WHERE id = :id LIMIT 1")
    fun getWorkerById(id: Long): Flow<WorkerEntity?>

    @Query("SELECT * FROM workers WHERE name LIKE '%' || :query || '%' OR skills LIKE '%' || :query || '%' OR locationArea LIKE '%' || :query || '%'")
    fun searchWorkers(query: String): Flow<List<WorkerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkers(workers: List<WorkerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorker(worker: WorkerEntity): Long

    @Update
    suspend fun updateWorker(worker: WorkerEntity)

    @Query("SELECT COUNT(*) FROM workers")
    suspend fun getWorkerCount(): Int
}

@Dao
interface WorkRequestDao {
    @Query("SELECT * FROM work_requests ORDER BY createdAt DESC")
    fun getAllRequests(): Flow<List<WorkRequestEntity>>

    @Query("SELECT * FROM work_requests WHERE workerId = :workerId ORDER BY createdAt DESC")
    fun getRequestsForWorker(workerId: Long): Flow<List<WorkRequestEntity>>

    @Query("SELECT * FROM work_requests WHERE id = :id LIMIT 1")
    fun getRequestById(id: Long): Flow<WorkRequestEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: WorkRequestEntity): Long

    @Update
    suspend fun updateRequest(request: WorkRequestEntity)

    @Query("DELETE FROM work_requests WHERE id = :id")
    suspend fun deleteRequestById(id: Long)
}
