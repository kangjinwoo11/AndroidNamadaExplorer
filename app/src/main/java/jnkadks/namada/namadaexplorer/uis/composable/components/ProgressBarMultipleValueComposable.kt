package jnkadks.namada.namadaexplorer.uis.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import jnkadks.namada.namadaexplorer.common.Common

class ProgressBarConfig(
    val value: Long,
    val backgroundColor: Color,
    val textColor: Color
)

@Composable
fun ProgressBarMultipleValueComposable(
    values: List<ProgressBarConfig>,
    modifier: Modifier = Modifier
) {
    val sum = values.sumOf { it.value }
    Row(modifier = Modifier.fillMaxHeight() then modifier) {
        for (value in values) {
            val weight = value.value.toDouble() / sum.toDouble()
            if (weight > 0) {
                Text(
                    text = Common.formattedWithCommas(value.value),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = value.textColor
                    ),
                    maxLines = 1,
                    modifier = Modifier
                        .weight(weight.toFloat())
                        .background(value.backgroundColor)
                        .fillMaxHeight()
                )
            }
        }
    }
}