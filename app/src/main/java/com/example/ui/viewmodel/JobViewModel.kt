package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.repository.JobRepository
import com.example.model.Job
import com.example.model.JobCategory
import com.example.model.JobType
import com.example.model.SriLankaDistricts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class JobFilters(
    val query: String = "",
    val district: String = SriLankaDistricts.ALL_DISTRICTS,
    val category: JobCategory? = null,
    val jobType: JobType? = null,
    val minSalary: Int = 0,
    val onlyFeatured: Boolean = false
)

class JobViewModel(
    application: Application,
    private val repository: JobRepository
) : AndroidViewModel(application) {

    private val _filters = MutableStateFlow(JobFilters())
    val filters: StateFlow<JobFilters> = _filters.asStateFlow()

    val searchQuery: StateFlow<String> = _filters
        .map { it.query }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val selectedDistrict: StateFlow<String> = _filters
        .map { it.district }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SriLankaDistricts.ALL_DISTRICTS)

    val selectedCategory: StateFlow<JobCategory?> = _filters
        .map { it.category }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedJobType: StateFlow<JobType?> = _filters
        .map { it.jobType }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedMinSalary: StateFlow<Int> = _filters
        .map { it.minSalary }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val onlyFeatured: StateFlow<Boolean> = _filters
        .map { it.onlyFeatured }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val activeFilterCount: StateFlow<Int> = _filters
        .map { f ->
            var count = 0
            if (f.district != SriLankaDistricts.ALL_DISTRICTS) count++
            if (f.category != null) count++
            if (f.jobType != null) count++
            if (f.minSalary > 0) count++
            if (f.onlyFeatured) count++
            count
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    private val _selectedJob = MutableStateFlow<Job?>(null)
    val selectedJob: StateFlow<Job?> = _selectedJob.asStateFlow()

    val allJobs: StateFlow<List<Job>> = repository.allJobsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedJobs: StateFlow<List<Job>> = repository.savedJobsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredJobs: StateFlow<List<Job>> = combine(allJobs, _filters) { jobs, f ->
        jobs.filter { job ->
            val matchesQuery = f.query.isBlank() ||
                    job.title.contains(f.query, ignoreCase = true) ||
                    job.company.contains(f.query, ignoreCase = true) ||
                    job.locationDetails.contains(f.query, ignoreCase = true) ||
                    job.district.contains(f.query, ignoreCase = true) ||
                    job.description.contains(f.query, ignoreCase = true)

            val matchesDistrict = f.district == SriLankaDistricts.ALL_DISTRICTS ||
                    job.district.equals(f.district, ignoreCase = true)

            val matchesCategory = f.category == null || job.category == f.category

            val matchesJobType = f.jobType == null || job.jobType == f.jobType

            val matchesSalary = f.minSalary == 0 || job.minSalary >= f.minSalary

            val matchesFeatured = !f.onlyFeatured || job.isFeatured

            matchesQuery && matchesDistrict && matchesCategory && matchesJobType && matchesSalary && matchesFeatured
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredJobs: StateFlow<List<Job>> = allJobs
        .map { jobs -> jobs.filter { it.isFeatured } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentJobs: StateFlow<List<Job>> = allJobs
        .map { jobs -> jobs.take(6) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _filters.update { it.copy(query = query) }
    }

    fun setSelectedDistrict(district: String) {
        _filters.update { it.copy(district = district) }
    }

    fun setSelectedCategory(category: JobCategory?) {
        _filters.update { it.copy(category = category) }
    }

    fun setSelectedJobType(jobType: JobType?) {
        _filters.update { it.copy(jobType = jobType) }
    }

    fun setSelectedMinSalary(salary: Int) {
        _filters.update { it.copy(minSalary = salary) }
    }

    fun setOnlyFeatured(onlyFeatured: Boolean) {
        _filters.update { it.copy(onlyFeatured = onlyFeatured) }
    }

    fun clearFilters() {
        _filters.value = JobFilters()
    }

    fun selectJob(job: Job?) {
        _selectedJob.value = job
    }

    fun toggleSaveJob(job: Job) {
        viewModelScope.launch {
            val willBeSaved = !job.isSaved
            repository.toggleSave(job.id, job.isSaved)
            if (_selectedJob.value?.id == job.id) {
                _selectedJob.value = _selectedJob.value?.copy(isSaved = willBeSaved)
            }
            _userMessage.value = if (willBeSaved) {
                "රැකියාව සාර්ථකව සුරැකිණි"
            } else {
                "සුරැකි ලැයිස්තුවෙන් ඉවත් කරන ලදී"
            }
        }
    }

    fun addJob(job: Job, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.insertJob(job)
            _userMessage.value = "නව රැකියාව සාර්ථකව පද්ධතියට එක් කරන ලදී!"
            onComplete()
        }
    }

    fun updateJob(job: Job, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.updateJob(job)
            if (_selectedJob.value?.id == job.id) {
                _selectedJob.value = job
            }
            _userMessage.value = "රැකියා විස්තර සාර්ථකව යාවත්කාලීන විය"
            onComplete()
        }
    }

    fun deleteJob(jobId: Long, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteJob(jobId)
            if (_selectedJob.value?.id == jobId) {
                _selectedJob.value = null
            }
            _userMessage.value = "රැකියාව සාර්ථකව මකා දමන ලදී"
            onComplete()
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobViewModel::class.java)) {
                val db = AppDatabase.getInstance(application)
                val repo = JobRepository.getInstance(db)
                return JobViewModel(application, repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
