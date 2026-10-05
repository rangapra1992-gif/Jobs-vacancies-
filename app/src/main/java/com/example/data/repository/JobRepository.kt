package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.JobDao
import com.example.data.local.JobEntity
import com.example.data.local.SavedJobEntity
import com.example.data.remote.JobApiService
import com.example.data.seed.SeedJobData
import com.example.model.Job
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobRepository(
    private val jobDao: JobDao,
    private val apiService: JobApiService? = null
) {
    init {
        // Seed default jobs if database is empty on first launch
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (jobDao.getJobCount() == 0) {
                    jobDao.insertJobs(SeedJobData.initialJobs)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val allJobsFlow: Flow<List<Job>> = combine(
        jobDao.getAllJobs(),
        jobDao.getSavedJobIds()
    ) { jobEntities, savedIds ->
        val savedSet = savedIds.toSet()
        jobEntities.map { entity ->
            entity.toJob(isSaved = savedSet.contains(entity.id))
        }
    }.flowOn(Dispatchers.IO)

    val savedJobsFlow: Flow<List<Job>> = combine(
        jobDao.getAllJobs(),
        jobDao.getSavedJobIds()
    ) { jobEntities, savedIds ->
        val savedSet = savedIds.toSet()
        jobEntities.filter { savedSet.contains(it.id) }
            .map { it.toJob(isSaved = true) }
    }.flowOn(Dispatchers.IO)

    fun getJobById(id: Long): Flow<Job?> = combine(
        jobDao.getJobById(id),
        jobDao.getSavedJobIds()
    ) { entity, savedIds ->
        entity?.toJob(isSaved = savedIds.contains(id))
    }.flowOn(Dispatchers.IO)

    suspend fun toggleSave(jobId: Long, currentlySaved: Boolean) = withContext(Dispatchers.IO) {
        if (currentlySaved) {
            jobDao.unsaveJob(jobId)
        } else {
            jobDao.saveJob(SavedJobEntity(jobId = jobId))
        }
    }

    suspend fun insertJob(job: Job): Long = withContext(Dispatchers.IO) {
        val entity = JobEntity.fromJob(job)
        jobDao.insertJob(entity)
    }

    suspend fun updateJob(job: Job) = withContext(Dispatchers.IO) {
        val entity = JobEntity.fromJob(job)
        jobDao.updateJob(entity)
    }

    suspend fun deleteJob(jobId: Long) = withContext(Dispatchers.IO) {
        jobDao.deleteJobById(jobId)
        jobDao.unsaveJob(jobId)
    }

    suspend fun refreshRemoteJobs(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            if (apiService == null) {
                return@withContext Result.success(0)
            }
            val response = apiService.getVerifiedJobs()
            if (response.isSuccessful) {
                val remoteJobs = response.body() ?: emptyList()
                if (remoteJobs.isNotEmpty()) {
                    jobDao.insertJobs(remoteJobs.map { it.toJobEntity() })
                }
                Result.success(remoteJobs.size)
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: JobRepository? = null

        fun getInstance(database: AppDatabase): JobRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = JobRepository(database.jobDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
