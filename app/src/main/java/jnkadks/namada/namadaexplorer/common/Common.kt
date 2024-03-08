package jnkadks.namada.namadaexplorer.common

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object Common {
    const val limitPage: Int = 15
    const val BASE_URL = "https://indexer.gangnepan.pro/"
    const val OUTER_BASE_URL = "https://namada-explorer-api.stakepool.dev.br/node/"
    const val RED_BASE_URL = "https://it.api.namada.red/api/v1/chain/"
    const val STAKEPOOL_BASE_URL = "https://namada-explorer-api.stakepool.dev.br/"

    private val numberFormat = DecimalFormat("#,###")
    private val dateFormatter = SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault())
    private val dateFormatters = listOf(
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    )

    fun formattedWithCommas(number: Number): String {
        return numberFormat.format(number)
    }

    fun formatDecimal(decimal: Double, decimalPlaces: Int): String {
        val pattern = StringBuilder("#,##0.")
        repeat(decimalPlaces) { pattern.append("#") }
        val df = DecimalFormat(pattern.toString())
        return df.format(decimal)
    }

    fun stringToDate(date: String): Date? {
        for (format in dateFormatters) {
            try {
                return format.parse(date)
            } catch (e: Exception) {
                continue
            }
        }
        return null
    }

    fun dateToString(date: Date): String = dateFormatter.format(date)

    fun timeAgoString(now: Date = Date(), date: Date): String {
        val timeDifference = TimeUnit.MILLISECONDS.toSeconds(now.time - date.time)
        return when {
            timeDifference < 60 -> "Just now"
            timeDifference < 120 -> "1 minute ago"
            timeDifference < 3600 -> "${timeDifference / 60} minutes ago"
            timeDifference < 7200 -> "1 hour ago"
            timeDifference < 86400 -> "${timeDifference / 3600} hours ago"
            timeDifference < 172800 -> "1 day ago"
            timeDifference < 2592000 -> "${timeDifference / 86400} days ago" // 30 days
            else -> dateToString(date = date)
        }
    }
}