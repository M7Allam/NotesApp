package com.mallam.mynotes.utilities

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.Toast
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*


fun View.makeToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun View.makeSuccessToasty(message: String) {
    Toasty.success(context, message, Toast.LENGTH_SHORT).show()
}

fun View.makeWarningToasty(message: String) {
    Toasty.warning(context, message, Toast.LENGTH_SHORT).show()
}

fun log(message: String) {
    Log.d("myLog", message)
}

@SuppressLint("SimpleDateFormat")
fun convertDateToString(date: Date?, pattern: String?): String {
    var dateStr = ""
    date?.let {
        val sdf = SimpleDateFormat(pattern)
        dateStr = sdf.format(it)
    }

    return dateStr
}

@SuppressLint("SimpleDateFormat")
fun convertDateLongToString(dateLong: Long?, pattern: String?): String {
    var dateStr = ""
    dateLong?.let {
        val date = Date(it)
        dateStr = convertDateToString(date, pattern)
    }
    return dateStr

}

fun getDatePattern(date: Date?): String {
    var pattern = ""
    date?.let {
        val calendarNow = Calendar.getInstance()
        calendarNow.time = Date()
        val calendarDate = Calendar.getInstance()
        calendarDate.time = date

        //YEAR
        if (calendarNow.get(Calendar.YEAR) == calendarDate.get(Calendar.YEAR)) {
            //MONTH
            if (calendarNow.get(Calendar.MONTH) == calendarDate.get(Calendar.MONTH)) {
                //TODAY
                if (calendarNow.get(Calendar.DAY_OF_MONTH) == calendarDate.get(Calendar.DAY_OF_MONTH)) {
                    pattern = "HH:mm"
                } else {
                    //WEEK
                    if (calendarNow.get(Calendar.DAY_OF_WEEK) == calendarDate.get(Calendar.DAY_OF_WEEK)) {
                        pattern = "EEE, HH:mm"
                    } else {
                        pattern = "MMM d, HH:mm"
                    }
                }
            }
            //MONTH
            else {
                pattern = "MMM d, HH:mm"
            }

        }
        //YEAR
        else {
            pattern = "MMM d, yyyy"
        }
    }

    return pattern
}

typealias ItemNotesAdapterOnCLick<T> = (date: T, position: Int) -> Unit

typealias WebUrlNotesAdapterOnCLick<T> = (date: T) -> Unit