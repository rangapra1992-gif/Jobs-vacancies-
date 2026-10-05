package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Job
import com.example.model.JobCategory
import com.example.model.JobType
import com.example.model.SriLankaDistricts
import com.example.ui.components.CategoryItem
import com.example.ui.components.FilterBottomSheet
import com.example.ui.components.JobCard
import com.example.ui.viewmodel.JobViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: JobViewModel,
    onNavigateToJobDetail: (Job) -> Unit,
    onNavigateToJobsTab: () -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedDistrict by viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedJobType by viewModel.selectedJobType.collectAsStateWithLifecycle()
    val selectedMinSalary by viewModel.selectedMinSalary.collectAsStateWithLifecycle()
    val onlyFeatured by viewModel.onlyFeatured.collectAsStateWithLifecycle()

    val featuredJobs by viewModel.featuredJobs.collectAsStateWithLifecycle()
    val recentJobs by viewModel.recentJobs.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showFilterSheet by remember { mutableStateOf(false) }

    if (showFilterSheet) {
        FilterBottomSheet(
            sheetState = sheetState,
            selectedDistrict = selectedDistrict,
            selectedCategory = selectedCategory,
            selectedJobType = selectedJobType,
            selectedMinSalary = selectedMinSalary,
            onlyFeatured = onlyFeatured,
            onDistrictChange = { viewModel.setSelectedDistrict(it) },
            onCategoryChange = { viewModel.setSelectedCategory(it) },
            onJobTypeChange = { viewModel.setSelectedJobType(it) },
            onMinSalaryChange = { viewModel.setSelectedMinSalary(it) },
            onOnlyFeaturedChange = { viewModel.setOnlyFeatured(it) },
            onClearFilters = { viewModel.clearFilters() },
            onDismiss = {
                coroutineScope.launch {
                    sheetState.hide()
                    showFilterSheet = false
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Header Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "රැකියා සොයමු",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 28.sp
                                ),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                text = "ශ්රී ලංකාවේ සියලුම රැකියා එකම තැනකින්",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                            )
                        }

                        // Sri Lanka badge / decorative icon
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "🇱🇰",
                                    fontSize = 22.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Search box: "රැකියාවක් සොයන්න..."
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = {
                            Text(
                                text = "රැකියාවක් සොයන්න...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "සොයන්න",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "මකන්න",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_search_input"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            focusManager.clearFocus()
                            onNavigateToJobsTab()
                        })
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Selectors:
                    // "ස්ථානය තෝරන්න", "රැකියා වර්ගය තෝරන්න", "වැටුප තෝරන්න"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Quick 1: Location / District
                        QuickFilterButton(
                            title = if (selectedDistrict == SriLankaDistricts.ALL_DISTRICTS) "ස්ථානය තෝරන්න" else selectedDistrict,
                            icon = Icons.Default.LocationOn,
                            isActive = selectedDistrict != SriLankaDistricts.ALL_DISTRICTS,
                            onClick = { showFilterSheet = true },
                            modifier = Modifier.weight(1f),
                            testTag = "quick_location_btn"
                        )

                        // Quick 2: Job Type
                        QuickFilterButton(
                            title = selectedJobType?.displayName ?: "රැකියා වර්ගය",
                            icon = Icons.Default.Work,
                            isActive = selectedJobType != null,
                            onClick = { showFilterSheet = true },
                            modifier = Modifier.weight(1f),
                            testTag = "quick_type_btn"
                        )

                        // Quick 3: Salary
                        QuickFilterButton(
                            title = if (selectedMinSalary > 0) "රු. ${selectedMinSalary / 1000}k+" else "වැටුප තෝරන්න",
                            icon = Icons.Default.Payments,
                            isActive = selectedMinSalary > 0,
                            onClick = { showFilterSheet = true },
                            modifier = Modifier.weight(1f),
                            testTag = "quick_salary_btn"
                        )
                    }
                }
            }
        }

        // Category Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ප්රධාන ප්රවර්ග (Categories)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                    if (selectedCategory != null) {
                        TextButton(onClick = { viewModel.setSelectedCategory(null) }) {
                            Text("සියල්ල පෙන්වන්න")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(JobCategory.entries) { category ->
                        CategoryItem(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = {
                                if (selectedCategory == category) {
                                    viewModel.setSelectedCategory(null)
                                } else {
                                    viewModel.setSelectedCategory(category)
                                    onNavigateToJobsTab()
                                }
                            }
                        )
                    }
                }
            }
        }

        // Featured Jobs Section
        if (featuredJobs.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "විශේෂාංග රැකියා ⭐",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                        }

                        TextButton(onClick = {
                            viewModel.setOnlyFeatured(true)
                            onNavigateToJobsTab()
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("සියල්ල")
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(featuredJobs, key = { it.id }) { job ->
                            JobCard(
                                job = job,
                                onClick = { onNavigateToJobDetail(job) },
                                onToggleSave = { viewModel.toggleSaveJob(job) },
                                modifier = Modifier.width(310.dp)
                            )
                        }
                    }
                }
            }
        }

        // Recent Jobs Section Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "නවතම රැකියා අවස්ථා",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                    TextButton(onClick = onNavigateToJobsTab) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("තවත් රැකියා")
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Recent Jobs Items
        items(recentJobs, key = { it.id }) { job ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                JobCard(
                    job = job,
                    onClick = { onNavigateToJobDetail(job) },
                    onToggleSave = { viewModel.toggleSaveJob(job) }
                )
            }
        }

        // Bottom CTA banner to explore more
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "තවත් රැකියා සොයනවාද?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "සියලු දිස්ත්රික්ක වල ඇති සියලුම රැකියා පිරික්සන්න",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onNavigateToJobsTab,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("පිරික්සන්න")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickFilterButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        color = if (isActive) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isActive) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = if (isActive) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                fontSize = 11.sp
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
