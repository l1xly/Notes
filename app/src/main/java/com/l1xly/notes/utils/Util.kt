package com.l1xly.notes.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

@SuppressLint("SimpleDateFormat")
fun getDateFormat(timeMillis: Long): String {
    val date = Date(timeMillis)
    val simpleDateFormat = SimpleDateFormat("MMM, dd")
    return simpleDateFormat.format(date)
}

fun getDateFormatFull(timeMillis: Long): String {
    val date = Date(timeMillis)
    val simpleDateFormat = SimpleDateFormat("MMM, dd, yyyy, HH:mm")
    return simpleDateFormat.format(date)
}

fun getCurrentTime(): Long {
    return System.currentTimeMillis()
}