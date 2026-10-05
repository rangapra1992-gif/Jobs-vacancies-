package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Job
import com.example.model.SriLankaDistricts
import com.example.ui.components.FilterBottomSheet
import com.example.ui.components.JobCard
import com.example.ui.viewmodel.JobViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JobsScreen(
    viewModel: JobViewModel,
    onNavigateToJobDetail: (Job) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedDistrict by viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedJobType by viewModel.selectedJobType.collectAsStateWithLifecycle()
    val selectedMinSalary by viewModel.selectedMinSalary.collectAsStateWithLifecycle()
    val onlyFeatured by viewModel.onlyFeatured.collectAsStateWithLifecycle()
    val activeFilterCount by viewModel.activeFilterCount.collectAsStateWithLifecycle()

    val filteredJobs by viewModel.filteredJobs.collectAsStateWithLifecycle()

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("jobs_screen")
    ) {
        // Top Search & Filter Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("රැකියාවක් සොයන්න...") },
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
                                        contentDescription = "මකන්න"
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("jobs_search_field"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    // Filter Button with Badge
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { showFilterSheet = true }
                            .testTag("open_filter_button"),
                        color = if (activeFilterCount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (activeFilterCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = MaterialTheme.colorScheme.tertiary) {
                                            Text("$activeFilterCount")
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "පෙරහන්",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = "පෙරහන්",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Active Filter Chips
                if (activeFilterCount > 0) {
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (selectedDistrict != SriLankaDistricts.ALL_DISTRICTS) {
                            InputChip(
                                selected = true,
                                onClick = { viewModel.setSelectedDistrict(SriLankaDistricts.ALL_DISTRICTS) },
                                label = { Text(selectedDistrict) },
                                trailingIcon = {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            )
                        }

                        if (selectedCategory != null) {
                            InputChip(
                                selected = true,
                                onClick = { viewModel.setSelectedCategory(null) },
                                label = { Text(selectedCategory!!.displayName) },
                                trailingIcon = {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            )
                        }

                        if (selectedJobType != null) {
                            InputChip(
                                selected = true,
                                onClick = { viewModel.setSelectedJobType(null) },
                                label = { Text(selectedJobType!!.displayName) },
                                trailingIcon = {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            )
                        }

                        if (selectedMinSalary > 0) {
                            InputChip(
                                selected = true,
                                onClick = { viewModel.setSelectedMinSalary(0) },
                                label = { Text("රු. ${selectedMinSalary / 1000}k+") },
                                trailingIcon = {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            )
                        }

                        if (onlyFeatured) {
                            InputChip(
                                selected = true,
                                onClick = { viewModel.setOnlyFeatured(false) },
                                label = { Text("විශේෂාංග") },
                                trailingIcon = {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            )
                        }

                        TextButton(
                            onClick = { viewModel.clearFilters() },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text("සියල්ල ඉවත් කරන්න", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Results Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "රැකියා ${filteredJobs.size} ක් හමුවිය",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (activeFilterCount > 0) {
                Text(
                    text = "පෙරහන් සක්රියයි",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Job Cards List or Empty State
        if (filteredJobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "ගැලපෙන රැකියා හමු නොවීය",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "ඔබ ලබා දුන් සෙවුම් පද හෝ පෙරහන් වෙනස් කර නැවත උත්සාහ කරන්න.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { viewModel.clearFilters() }) {
                        Text("පෙරහන් ඉවත් කර සියල්ල පෙන්වන්න")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredJobs, key = { it.id }) { job ->
                    JobCard(
                        job = job,
                        onClick = { onNavigateToJobDetail(job) },
                        onToggleSave = { viewModel.toggleSaveJob(job) }
                    )
                }
            }
        }
    }
}
