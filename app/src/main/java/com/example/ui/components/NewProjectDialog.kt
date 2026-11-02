package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.CinemaAmber
import com.example.ui.theme.GradingTeal
import com.example.ui.viewmodel.SaiGraphicsViewModel

private val EVENT_TYPES = listOf(
    "Wedding Cinematography",
    "Candid Photography",
    "Pre-Wedding Teaser",
    "Commercial Event",
    "Drone / Highlight Reel",
    "Album Designing"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewProjectDialog(
    viewModel: SaiGraphicsViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var selectedEventType by remember { mutableStateOf(EVENT_TYPES[0]) }
    var eventDate by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Bhagalpur, Bihar") }
    var deliverables by remember { mutableStateOf("4K Teaser (3-5m) + Full Traditional Video (2.5h) + 40-Sheet Album + 5 Reels") }
    var rackId by remember { mutableStateOf("HDD-05 (Lab Bay 1)") }
    var footageGb by remember { mutableStateOf("250") }
    var quotation by remember { mutableStateOf("50000") }
    var advance by remember { mutableStateOf("25000") }
    var notes by remember { mutableStateOf("Shot on Sony FX3 / A7IV. Local Bhagalpur client.") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 20.dp)
                .testTag("new_project_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = null,
                            tint = CinemaAmber,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "New Post-Production Project",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Text(
                    text = "Sai Graphics Lab • Sahmarket Picture Palace, Bhagalpur",
                    style = MaterialTheme.typography.bodySmall,
                    color = CinemaAmber
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Event Type Selector Chips
                Text(
                    text = "EVENT CATEGORY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    EVENT_TYPES.forEach { type ->
                        val isSelected = selectedEventType == type
                        Surface(
                            onClick = { selectedEventType = type },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) CinemaAmber else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                            )
                        ) {
                            Text(
                                text = type,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Project Title (e.g. Vikram & Sneha Wedding)") },
                    placeholder = { Text("Leave blank to auto-generate") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_project_title"),
                    singleLine = true,
                    colors = outlinedFieldColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Client Name & Phone
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("Client Name *") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_client_name"),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = clientPhone,
                        onValueChange = { clientPhone = it },
                        label = { Text("Mobile No *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_client_phone"),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Event Date & Location
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = eventDate,
                        onValueChange = { eventDate = it },
                        label = { Text("Event Date (e.g. 28 Nov 2026)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location (Bhagalpur)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Deliverables
                OutlinedTextField(
                    value = deliverables,
                    onValueChange = { deliverables = it },
                    label = { Text("Deliverables Package") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedFieldColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Storage Rack & Raw Footage
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = rackId,
                        onValueChange = { rackId = it },
                        label = { Text("HDD / Storage Rack ID") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = footageGb,
                        onValueChange = { footageGb = it },
                        label = { Text("Raw Size (GB)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quotation & Advance
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = quotation,
                        onValueChange = { quotation = it },
                        label = { Text("Quotation (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = advance,
                        onValueChange = { advance = it },
                        label = { Text("Advance Paid (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = outlinedFieldColors()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Editor Notes & Instructions") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = outlinedFieldColors()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
                            if (clientName.isNotBlank() && clientPhone.isNotBlank()) {
                                viewModel.createProject(
                                    title = title,
                                    clientName = clientName,
                                    clientPhone = clientPhone,
                                    eventType = selectedEventType,
                                    eventDate = eventDate.ifBlank { "TBD" },
                                    location = location,
                                    deliverables = deliverables,
                                    rackId = rackId,
                                    footageGb = footageGb.toDoubleOrNull() ?: 200.0,
                                    quotation = quotation.toDoubleOrNull() ?: 50000.0,
                                    advance = advance.toDoubleOrNull() ?: 0.0,
                                    notes = notes
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CinemaAmber,
                            contentColor = Color.Black
                        ),
                        enabled = clientName.isNotBlank() && clientPhone.isNotBlank(),
                        modifier = Modifier.testTag("submit_create_project_button")
                    ) {
                        Text("Create Project", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CinemaAmber,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    focusedLabelColor = CinemaAmber,
    cursorColor = CinemaAmber
)
