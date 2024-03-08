package jnkadks.namada.namadaexplorer.uis.composable.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed interface CardVerticalData {
    data class Image(
        @DrawableRes val id: Int,
        val tint: Color = Color.White,
        val backgroundColor: Color,
        val size: Dp = 16.dp,
        val padding: Dp = 4.dp
    ) : CardVerticalData

    data class Title(val text: String, val maxLines: Int? = 1) : CardVerticalData
    data class SubTitle(val text: String, val maxLines: Int? = 1) : CardVerticalData
    data class Content(val text: String, val maxLines: Int? = 1) : CardVerticalData
}

@Composable
fun CardVertical(
    data: List<CardVerticalData>,
    modifier: Modifier = Modifier,
    spacing: Dp = 4.dp
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalAlignment = Alignment.Start
        ) {
            for (item in data) {
                when (item) {
                    is CardVerticalData.Content -> Text(
                        text = item.text,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    is CardVerticalData.Image -> Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(item.backgroundColor)
                            .size(item.size + item.padding)
                    ) {
                        Image(
                            painter = painterResource(id = item.id),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(item.tint),
                            modifier = Modifier
                                .size(item.size)
                                .align(Alignment.Center)
                        )
                    }

                    is CardVerticalData.SubTitle -> Text(
                        text = if (item.maxLines == null) {
                            item.text
                        } else if (item.maxLines <= 1) item.text else "${item.text}${
                            "\n".repeat(
                                item.maxLines
                            )
                        }",
                        maxLines = item.maxLines ?: Int.MAX_VALUE,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.Gray
                        )
                    )

                    is CardVerticalData.Title -> Text(
                        text = if (item.maxLines == null) {
                            item.text
                        } else if (item.maxLines <= 1) item.text else "${item.text}${
                            "\n".repeat(
                                item.maxLines
                            )
                        }",
                        maxLines = item.maxLines ?: Int.MAX_VALUE,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}