package com.openinapp.task.helper

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale

public fun logThis(msg: String, tag: String = "TAGTAG") {
    Log.d(tag, msg)
}

fun String.formatDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val date = inputFormat.parse(this)
    return date?.let { outputFormat.format(it) } ?: this
}
