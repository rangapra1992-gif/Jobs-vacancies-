package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Job
import com.example.model.JobCategory
import com.example.model.JobType

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val company: String,
    val district: String,
    val locationDetails: String,
    val categoryName: String,
    val jobTypeName: String,
    val salary: String,
    val minSalary: Int = 0,
    val experienceRequired: String,
    val qualificationsSerialized: String, // newline separated
    val description: String,
    val postedDate: String,
    val postedTimestamp: Long = System.currentTimeMillis(),
    val expiryDate: String,
    val sourceName: String = "නිල මූලාශ්රය",
    val sourceUrl: String,
    val contactPhone: String? = null,
    val contactEmail: String? = null,
    val isFeatured: Boolean = false,
    val isVerified: Boolean = true
) {
    fun toJob(isSaved: Boolean = false): Job {
        val qualList = if (qualificationsSerialized.isBlank()) {
            emptyList()
        } else {
            qualificationsSerialized.split("\n").map { it.trim() }.filter { it.isNotBlank() }
        }
        return Job(
            id = id,
            title = title,
            company = company,
            district = district,
            locationDetails = locationDetails,
            category = JobCategory.fromDisplayName(categoryName),
            jobType = JobType.fromDisplayName(jobTypeName),
            salary = salary,
            minSalary = minSalary,
            experienceRequired = experienceRequired,
            qualifications = qualList,
            description = description,
            postedDate = postedDate,
            postedTimestamp = postedTimestamp,
            expiryDate = expiryDate,
            sourceName = sourceName,
            sourceUrl = sourceUrl,
            contactPhone = contactPhone,
            contactEmail = contactEmail,
            isFeatured = isFeatured,
            isVerified = isVerified,
            isSaved = isSaved
        )
    }

    companion object {
        fun fromJob(job: Job): JobEntity {
            return JobEntity(
                id = job.id,
                title = job.title,
                company = job.company,
                district = job.district,
                locationDetails = job.locationDetails,
                categoryName = job.category.displayName,
                jobTypeName = job.jobType.displayName,
                salary = job.salary,
                minSalary = job.minSalary,
                experienceRequired = job.experienceRequired,
                qualificationsSerialized = job.qualifications.joinToString("\n"),
                description = job.description,
                postedDate = job.postedDate,
                postedTimestamp = job.postedTimestamp,
                expiryDate = job.expiryDate,
                sourceName = job.sourceName,
                sourceUrl = job.sourceUrl,
                contactPhone = job.contactPhone,
                contactEmail = job.contactEmail,
                isFeatured = job.isFeatured,
                isVerified = job.isVerified
            )
        }
    }
}
