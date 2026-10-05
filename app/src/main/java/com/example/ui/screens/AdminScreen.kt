package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Job
import com.example.model.JobCategory
import com.example.model.JobType
import com.example.model.SriLankaDistricts
import com.example.ui.viewmodel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: JobViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allJobs by viewModel.allJobs.collectAsStateWithLifecycle()

    var showJobFormDialog by remember { mutableStateOf(false) }
    var jobToEdit by remember { mutableStateOf<Job?>(null) }
    var jobToDelete by remember { mutableStateOf<Job?>(null) }

    BackHandler {
        onBack()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = "පරිපාලන පුවරුව (Admin Panel)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "ආපසු")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    jobToEdit = null
                    showJobFormDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_job_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "නව රැකියාවක්")
                    Text("නව රැකියාවක්", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Admin stats card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "රැකියා කළමනාකරණය",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "මුළු රැකියා සංඛ්යාව: ${allJobs.size} | විශේෂාංග: ${allJobs.count { it.isFeatured }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Jobs list for admin
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(allJobs, key = { it.id }) { job ->
                    AdminJobCard(
                        job = job,
                        onEdit = {
                            jobToEdit = job
                            showJobFormDialog = true
                        },
                        onDelete = {
                            jobToDelete = job
                        },
                        onToggleFeatured = {
                            viewModel.updateJob(job.copy(isFeatured = !job.isFeatured)) {}
                        }
                    )
                }
            }
        }
    }

    // Add / Edit Job Dialog Form
    if (showJobFormDialog) {
        JobFormDialog(
            jobToEdit = jobToEdit,
            onDismiss = { showJobFormDialog = false },
            onSave = { job ->
                if (jobToEdit == null) {
                    viewModel.addJob(job) {
                        showJobFormDialog = false
                    }
                } else {
                    viewModel.updateJob(job) {
                        showJobFormDialog = false
                    }
                }
            }
        )
    }

    // Delete Confirmation Dialog
    jobToDelete?.let { job ->
        AlertDialog(
            onDismissRequest = { jobToDelete = null },
            title = { Text("රැකියාව මකා දැමීම") },
            text = { Text("\"${job.title}\" රැකියාව පද්ධතියෙන් ස්ථිරවම ඉවත් කිරීමට ඔබට සහතිකද?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteJob(job.id) {
                            jobToDelete = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("මකා දමන්න")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { jobToDelete = null }) {
                    Text("අවලංගු කරන්න")
                }
            }
        )
    }
}

@Composable
private fun AdminJobCard(
    job: Job,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleFeatured: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = job.category.displayName,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleFeatured) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "විශේෂාංග තත්ත්වය",
                            tint = if (job.isFeatured) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline
                        )
                    }
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "සංස්කරණය",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "මකා දමන්න",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Text(
                text = job.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${job.company} • ${job.district}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = job.salary,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "කල් ඉකුත්වීම: ${job.expiryDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JobFormDialog(
    jobToEdit: Job?,
    onDismiss: () -> Unit,
    onSave: (Job) -> Unit
) {
    var title by remember { mutableStateOf(jobToEdit?.title ?: "") }
    var company by remember { mutableStateOf(jobToEdit?.company ?: "") }
    var district by remember { mutableStateOf(jobToEdit?.district ?: SriLankaDistricts.list.first()) }
    var locationDetails by remember { mutableStateOf(jobToEdit?.locationDetails ?: "") }
    var category by remember { mutableStateOf(jobToEdit?.category ?: JobCategory.OFFICE) }
    var jobType by remember { mutableStateOf(jobToEdit?.jobType ?: JobType.FULL_TIME) }
    var salary by remember { mutableStateOf(jobToEdit?.salary ?: "රු. 75,000 - 90,000") }
    var minSalaryStr by remember { mutableStateOf(jobToEdit?.minSalary?.toString() ?: "75000") }
    var experience by remember { mutableStateOf(jobToEdit?.experienceRequired ?: "වසර 1 - 2") }
    var qualificationsText by remember {
        mutableStateOf(jobToEdit?.qualifications?.joinToString("\n") ?: "අ.පො.ස. උසස් පෙළ සමත් වීම\nඅදාළ ක්ෂේත්රයේ පළපුරුද්ද")
    }
    var description by remember { mutableStateOf(jobToEdit?.description ?: "") }
    var sourceName by remember { mutableStateOf(jobToEdit?.sourceName ?: "නිල මූලාශ්රය") }
    var sourceUrl by remember { mutableStateOf(jobToEdit?.sourceUrl ?: "https://www.topjobs.lk") }
    var expiryDate by remember { mutableStateOf(jobToEdit?.expiryDate ?: "2026-11-30") }
    var contactPhone by remember { mutableStateOf(jobToEdit?.contactPhone ?: "") }
    var contactEmail by remember { mutableStateOf(jobToEdit?.contactEmail ?: "") }
    var isFeatured by remember { mutableStateOf(jobToEdit?.isFeatured ?: false) }

    var districtExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var jobTypeExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (jobToEdit == null) "නව රැකියාවක් එක් කරන්න" else "රැකියාව සංස්කරණය කරන්න",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("රැකියා නාමය (Job Title)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Company
                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("සමාගමේ නම (Company Name)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // District Dropdown
                ExposedDropdownMenuBox(
                    expanded = districtExpanded,
                    onExpandedChange = { districtExpanded = !districtExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = district,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("දිස්ත්රික්කය") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = districtExpanded,
                        onDismissRequest = { districtExpanded = false }
                    ) {
                        SriLankaDistricts.list.forEach { d ->
                            DropdownMenuItem(
                                text = { Text(d) },
                                onClick = {
                                    district = d
                                    districtExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Location Details
                OutlinedTextField(
                    value = locationDetails,
                    onValueChange = { locationDetails = it },
                    label = { Text("නිශ්චිත ස්ථානය (Location Details)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = category.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("ප්රවර්ගය (Category)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        JobCategory.entries.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.displayName) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Job Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = jobTypeExpanded,
                    onExpandedChange = { jobTypeExpanded = !jobTypeExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = jobType.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("රැකියා වර්ගය (Job Type)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = jobTypeExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = jobTypeExpanded,
                        onDismissRequest = { jobTypeExpanded = false }
                    ) {
                        JobType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    jobType = type
                                    jobTypeExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Salary & Min Salary
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = salary,
                        onValueChange = { salary = it },
                        label = { Text("වැටුප") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = minSalaryStr,
                        onValueChange = { minSalaryStr = it },
                        label = { Text("අවම වැටුප (LKR)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Experience
                OutlinedTextField(
                    value = experience,
                    onValueChange = { experience = it },
                    label = { Text("අවශ්ය පළපුරුද්ද") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Qualifications
                OutlinedTextField(
                    value = qualificationsText,
                    onValueChange = { qualificationsText = it },
                    label = { Text("අවශ්ය සුදුසුකම් (පේළියකට එකක් බැගින්)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("සම්පූර්ණ රැකියා විස්තරය") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Source URL & Source Name
                OutlinedTextField(
                    value = sourceName,
                    onValueChange = { sourceName = it },
                    label = { Text("මූලාශ්රයේ නම (e.g. නිල වෙබ් අඩවිය)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = sourceUrl,
                    onValueChange = { sourceUrl = it },
                    label = { Text("අයදුම්පත් වෙබ් අඩවි URL (Source URL)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Expiry Date
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = { Text("කල් ඉකුත්වන දිනය (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Contact Phone & Email
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = contactPhone,
                        onValueChange = { contactPhone = it },
                        label = { Text("දුරකථනය") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = contactEmail,
                        onValueChange = { contactEmail = it },
                        label = { Text("ඊමේල්") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Featured Checkbox
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isFeatured,
                        onCheckedChange = { isFeatured = it }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("විශේෂාංග රැකියාවක් ලෙස සලකුණු කරන්න (Featured Job)")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("අවලංගු කරන්න")
                    }

                    Button(
                        onClick = {
                            if (title.isNotBlank() && company.isNotBlank()) {
                                val qualList = qualificationsText.split("\n")
                                    .map { it.trim() }
                                    .filter { it.isNotBlank() }

                                val finalJob = Job(
                                    id = jobToEdit?.id ?: 0L,
                                    title = title,
                                    company = company,
                                    district = district,
                                    locationDetails = locationDetails.ifBlank { district },
                                    category = category,
                                    jobType = jobType,
                                    salary = salary,
                                    minSalary = minSalaryStr.toIntOrNull() ?: 0,
                                    experienceRequired = experience,
                                    qualifications = qualList,
                                    description = description.ifBlank { "අප ආයතනය සඳහා සුදුසු සේවකයින් අවශ්යයි." },
                                    postedDate = jobToEdit?.postedDate ?: "අද",
                                    postedTimestamp = jobToEdit?.postedTimestamp ?: System.currentTimeMillis(),
                                    expiryDate = expiryDate,
                                    sourceName = sourceName,
                                    sourceUrl = sourceUrl,
                                    contactPhone = contactPhone.ifBlank { null },
                                    contactEmail = contactEmail.ifBlank { null },
                                    isFeatured = isFeatured,
                                    isVerified = true,
                                    isSaved = jobToEdit?.isSaved ?: false
                                )
                                onSave(finalJob)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = title.isNotBlank() && company.isNotBlank()
                    ) {
                        Text("සුරකින්න")
                    }
                }
            }
        }
    }
}
