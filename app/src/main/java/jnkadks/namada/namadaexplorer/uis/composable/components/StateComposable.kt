package jnkadks.namada.namadaexplorer.uis.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SuccessComposable() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(4.dp))
            .background(Color.Green)
            .padding(vertical = 2.dp, horizontal = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Success",
            tint = MaterialTheme.colorScheme.scrim,
            modifier = Modifier.size(8.dp)
        )

        Text(
            text = "Success",
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.scrim)
        )
    }
}

@Composable
fun FailedComposable() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(vertical = 2.dp, horizontal = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Close, contentDescription = "Failed",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(8.dp)
        )

        Text(
            text = "Failed",
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error)
        )
    }
}

@Composable
fun BadgeComposable(title: String, backgroundColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(vertical = 2.dp, horizontal = 4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(color = textColor)
        )
    }
}