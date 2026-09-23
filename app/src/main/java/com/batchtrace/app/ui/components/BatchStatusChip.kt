package com.batchtrace.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.batchtrace.app.ui.theme.StatusError
import com.batchtrace.app.ui.theme.StatusInfo
import com.batchtrace.app.ui.theme.StatusSuccess
import com.batchtrace.app.ui.theme.StatusWarning

@Composable
fun BatchStatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val statusColor = getStatusColor(status)

    Text(
        text = status.uppercase(),
        modifier = modifier
            .background(
                color = statusColor.copy(alpha = 0.12f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
        color = statusColor,
        style = MaterialTheme.typography.labelMedium
    )
}

private fun getStatusColor(status: String): Color {
    return when (status.uppercase()) {

        "PASSED",
        "COMPLETED",
        "DISPATCHED" -> StatusSuccess

        "ON HOLD",
        "HOLD",
        "PENDING" -> StatusWarning

        "FAILED",
        "REJECTED" -> StatusError

        "CREATED",
        "IN PRODUCTION",
        "PRODUCTION COMPLETED",
        "QUALITY CHECK",
        "PACKAGED",
        "IN WAREHOUSE" -> StatusInfo

        else -> StatusInfo
    }
}