package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs ORDER BY isFeatured DESC, postedTimestamp DESC")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE id = :id LIMIT 1")
    fun getJobById(id: Long): Flow<JobEntity?>

    @Query("SELECT COUNT(*) FROM jobs")
    suspend fun getJobCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobs(jobs: List<JobEntity>)

    @Update
    suspend fun updateJob(job: JobEntity)

    @Query("DELETE FROM jobs WHERE id = :id")
    suspend fun deleteJobById(id: Long)

    // Saved jobs queries
    @Query("SELECT jobId FROM saved_jobs ORDER BY savedAt DESC")
    fun getSavedJobIds(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJob(savedJob: SavedJobEntity)

    @Query("DELETE FROM saved_jobs WHERE jobId = :jobId")
    suspend fun unsaveJob(jobId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_jobs WHERE jobId = :jobId)")
    fun isJobSaved(jobId: Long): Flow<Boolean>
}
