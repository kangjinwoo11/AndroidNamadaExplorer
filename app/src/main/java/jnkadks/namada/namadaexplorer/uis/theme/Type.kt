package jnkadks.namada.namadaexplorer.uis.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight

val Typography = Typography().let {
    it.copy(
        titleLarge = it.titleLarge.copy(
            fontWeight = FontWeight.Bold
        ),
        titleMedium = it.titleMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        titleSmall = it.titleSmall.copy(
            fontWeight = FontWeight.Bold
        ),
    )
}