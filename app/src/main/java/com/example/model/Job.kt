package com.example.model

data class Job(
    val id: Long = 0,
    val title: String,
    val company: String,
    val district: String,
    val locationDetails: String,
    val category: JobCategory,
    val jobType: JobType,
    val salary: String,
    val minSalary: Int = 0,
    val experienceRequired: String,
    val qualifications: List<String>,
    val description: String,
    val postedDate: String,
    val postedTimestamp: Long = System.currentTimeMillis(),
    val expiryDate: String,
    val sourceName: String = "නිල මූලාශ්රය",
    val sourceUrl: String,
    val contactPhone: String? = null,
    val contactEmail: String? = null,
    val isFeatured: Boolean = false,
    val isVerified: Boolean = true,
    val isSaved: Boolean = false
)
