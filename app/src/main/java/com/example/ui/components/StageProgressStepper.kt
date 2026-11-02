package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CinemaAmber
import com.example.ui.theme.GradingTeal
import com.example.ui.theme.StudioGreen
import com.example.ui.viewmodel.WORKFLOW_STAGES

fun getStageIndex(stage: String): Int {
    val idx = WORKFLOW_STAGES.indexOfFirst { it.first == stage }
    return if (idx >= 0) idx else 0
}

@Composable
fun StageProgressStepper(
    currentStage: String,
    onStageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentIndex = getStageIndex(currentStage)
    val scrollState = rememberScrollState()

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PRODUCTION PIPELINE",
                style = MaterialTheme.typography.labelSmall,
                color = CinemaAmber,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Stage ${currentIndex + 1} of ${WORKFLOW_STAGES.size}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Horizontal scrollable stepper
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WORKFLOW_STAGES.forEachIndexed { index, (stageKey, stageLabel) ->
                val isCompleted = index < currentIndex
                val isCurrent = index == currentIndex

                val bubbleColor by animateColorAsState(
                    targetValue = when {
                        isCurrent -> CinemaAmber
                        isCompleted -> StudioGreen
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    label = "bubbleColor"
                )

                val contentColor = when {
                    isCurrent -> Color.Black
                    isCompleted -> Color.White
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onStageSelected(stageKey) }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .testTag("stage_pill_$stageKey")
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(bubbleColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = contentColor,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = contentColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = stageLabel.substringAfter(". "),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCurrent) CinemaAmber else contentColor
                    )
                }

                if (index < WORKFLOW_STAGES.size - 1) {
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(2.dp)
                            .background(
                                if (index < currentIndex) StudioGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                    )
                }
            }
        }
    }
}
