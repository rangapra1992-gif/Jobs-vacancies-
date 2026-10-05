package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.JobCategory
import com.example.model.JobType
import com.example.model.SriLankaDistricts

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    sheetState: SheetState,
    selectedDistrict: String,
    selectedCategory: JobCategory?,
    selectedJobType: JobType?,
    selectedMinSalary: Int,
    onlyFeatured: Boolean,
    onDistrictChange: (String) -> Unit,
    onCategoryChange: (JobCategory?) -> Unit,
    onJobTypeChange: (JobType?) -> Unit,
    onMinSalaryChange: (Int) -> Unit,
    onOnlyFeaturedChange: (Boolean) -> Unit,
    onClearFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    var districtMenuExpanded by remember { mutableStateOf(false) }

    val salaryOptions = listOf(
        0 to "සියලුම වැටුප්",
        50000 to "රු. 50,000+",
        75000 to "රු. 75,000+",
        100000 to "රු. 100,000+",
        150000 to "රු. 150,000+",
        200000 to "රු. 200,000+"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "රැකියා පෙරහන් (Filters)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "වසන්න")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. District Filter (ශ්රී ලංකාවේ සියලුම දිස්ත්රික්ක)
            Text(
                text = "දිස්ත්රික්කය තෝරන්න",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = districtMenuExpanded,
                onExpandedChange = { districtMenuExpanded = !districtMenuExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedDistrict,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtMenuExpanded) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .testTag("district_dropdown_field"),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = districtMenuExpanded,
                    onDismissRequest = { districtMenuExpanded = false }
                ) {
                    SriLankaDistricts.listWithAll.forEach { district ->
                        DropdownMenuItem(
                            text = { Text(district) },
                            onClick = {
                                onDistrictChange(district)
                                districtMenuExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Job Type (රැකියා වර්ගය)
            Text(
                text = "රැකියා වර්ගය (Job Type)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                JobType.entries.forEach { type ->
                    val selected = selectedJobType == type
                    FilterChip(
                        selected = selected,
                        onClick = {
                            onJobTypeChange(if (selected) null else type)
                        },
                        label = { Text(type.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Minimum Salary (අවම වැටුප)
            Text(
                text = "අවම වැටුප තෝරන්න",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                salaryOptions.forEach { (amount, label) ->
                    val selected = selectedMinSalary == amount
                    FilterChip(
                        selected = selected,
                        onClick = { onMinSalaryChange(amount) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Category Filter (ප්රධාන ප්රවර්ගය)
            Text(
                text = "රැකියා ක්ෂේත්රය (Category)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                JobCategory.entries.forEach { cat ->
                    val selected = selectedCategory == cat
                    FilterChip(
                        selected = selected,
                        onClick = {
                            onCategoryChange(if (selected) null else cat)
                        },
                        label = { Text(cat.displayName) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Featured only switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "විශේෂාංග රැකියා පමණක්",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "සත්යාපිත ප්රමුඛ රැකියා අවස්ථා",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = onlyFeatured,
                    onCheckedChange = onOnlyFeaturedChange,
                    modifier = Modifier.testTag("featured_switch")
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons: Reset & Apply
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onClearFilters,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("clear_filters_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ඉවත් කරන්න")
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("apply_filters_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("යොදන්න (Apply)")
                }
            }
        }
    }
}
