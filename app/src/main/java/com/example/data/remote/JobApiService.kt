package com.example.data.remote

import com.example.data.local.JobEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class RemoteJobDto(
    @Json(name = "id") val id: Long? = null,
    @Json(name = "title") val title: String,
    @Json(name = "company") val company: String,
    @Json(name = "district") val district: String,
    @Json(name = "location_details") val locationDetails: String,
    @Json(name = "category") val category: String,
    @Json(name = "job_type") val jobType: String,
    @Json(name = "salary") val salary: String,
    @Json(name = "min_salary") val minSalary: Int = 0,
    @Json(name = "experience") val experience: String,
    @Json(name = "qualifications") val qualifications: List<String> = emptyList(),
    @Json(name = "description") val description: String,
    @Json(name = "posted_date") val postedDate: String,
    @Json(name = "expiry_date") val expiryDate: String,
    @Json(name = "source_name") val sourceName: String,
    @Json(name = "source_url") val sourceUrl: String,
    @Json(name = "contact_phone") val contactPhone: String? = null,
    @Json(name = "contact_email") val contactEmail: String? = null,
    @Json(name = "is_featured") val isFeatured: Boolean = false
) {
    fun toJobEntity(): JobEntity {
        return JobEntity(
            id = id ?: 0L,
            title = title,
            company = company,
            district = district,
            locationDetails = locationDetails,
            categoryName = category,
            jobTypeName = jobType,
            salary = salary,
            minSalary = minSalary,
            experienceRequired = experience,
            qualificationsSerialized = qualifications.joinToString("\n"),
            description = description,
            postedDate = postedDate,
            postedTimestamp = System.currentTimeMillis(),
            expiryDate = expiryDate,
            sourceName = sourceName,
            sourceUrl = sourceUrl,
            contactPhone = contactPhone,
            contactEmail = contactEmail,
            isFeatured = isFeatured,
            isVerified = true
        )
    }
}

/**
 * Standard legal API interface for future certified / authorized Job Feeds or RSS.
 */
interface JobApiService {
    @GET("api/v1/jobs")
    suspend fun getVerifiedJobs(
        @Query("district") district: String? = null,
        @Query("category") category: String? = null
    ): Response<List<RemoteJobDto>>
}
