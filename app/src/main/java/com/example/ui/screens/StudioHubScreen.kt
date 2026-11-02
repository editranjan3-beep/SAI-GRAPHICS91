package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.VideoCameraFront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CinemaAmber
import com.example.ui.theme.GradingTeal
import com.example.ui.theme.StudioGreen
import com.example.ui.viewmodel.SaiGraphicsViewModel

@Composable
fun StudioHubScreen(
    viewModel: SaiGraphicsViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("studio_hub_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Studio Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.studio_suite_banner),
                            contentDescription = "Sai Graphics Suite",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color(0xCC0B0F17),
                                            Color(0xFF0B0F17)
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "SAI GRAPHICS POST PRODUCTION",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "Professional Editing Lab • Bhagalpur, Bihar",
                                style = MaterialTheme.typography.bodyMedium,
                                color = CinemaAmber,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = CinemaAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sahmarket Picture Palace, Bhagalpur, Bihar",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = StudioGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Helpline & Workstation Desk: +91 8809216991",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick Hub Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.dialPhone(context, "8809216991") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CinemaAmber,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call Lab", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    viewModel.openWhatsApp(
                                        context,
                                        "8809216991",
                                        "Hello Sai Graphics Post Production Lab, I need editing & delivery support for my event."
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = StudioGreen,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("WhatsApp", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { viewModel.openStudioMap(context) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GradingTeal,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Visit", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Specialized Post-Production Services
        item {
            Text(
                text = "SPECIALIZED SERVICES (BHAGALPUR REGION)",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = CinemaAmber,
                letterSpacing = 1.sp
            )
        }

        item {
            ServiceItem(
                icon = Icons.Default.Movie,
                title = "Wedding Cinematography",
                description = "Multi-camera 4K Sony/Canon workflows, cinematic storytelling, drone landscape coverage, and traditional highlight films.",
                color = CinemaAmber
            )
        }

        item {
            ServiceItem(
                icon = Icons.Default.CameraAlt,
                title = "Candid Photography & Retouching",
                description = "High-dynamic range color science, emotional candid portraiture, skin tone correction, and high-DPI sheet masters.",
                color = GradingTeal
            )
        }

        item {
            ServiceItem(
                icon = Icons.Default.PhotoAlbum,
                title = "Karigari Velvet Photobooks",
                description = "Premium wedding album designing with bespoke layout spreads (30, 40, 50 sheets) on velvet-touch and metallic papers.",
                color = Color(0xFFEC4899)
            )
        }

        item {
            ServiceItem(
                icon = Icons.Default.VideoCameraFront,
                title = "Commercial Events & Documentaries",
                description = "Specialized post-production for corporate events, industrial silk documentaries, institutional showcases, and digital ads.",
                color = Color(0xFF6366F1)
            )
        }

        // Editing Lab Hardware & Workstations
        item {
            Text(
                text = "LAB WORKSTATION INFRASTRUCTURE",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = CinemaAmber,
                letterSpacing = 1.sp
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    LabSpecRow(
                        title = "Color Grading Bay 1",
                        spec = "DaVinci Resolve Studio 4K HDR • Calibrated 10-bit IPS Displays • Kodak 2383 / Fuji Emulation LUTs"
                    )
                    LabSpecRow(
                        title = "Editing Bay 2",
                        spec = "Adobe Premiere Pro & After Effects • Multicam Syncing • Fast NVMe Scratch Drives"
                    )
                    LabSpecRow(
                        title = "Storage & Archival",
                        spec = "48TB Local NAS RAID + Redundant Google Cloud Storage with encrypted PIN client delivery portals"
                    )
                    LabSpecRow(
                        title = "Location",
                        spec = "Sahmarket Picture Palace, Bhagalpur, Bihar — Walk-ins welcome for offline drive ingestion"
                    )
                }
            }
        }
    }
}

@Composable
private fun ServiceItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun LabSpecRow(title: String, spec: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = CinemaAmber
        )
        Text(
            text = spec,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
