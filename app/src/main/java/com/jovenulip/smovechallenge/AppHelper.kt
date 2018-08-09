package com.jovenulip.smovechallenge

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import java.text.SimpleDateFormat
import java.util.*

class AppHelper {
    companion object Helper {

        fun getCurrentDateToString(date: Date): String {
            val format = SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH)
            return format.format(date)
        }

        fun getCurrentTimeToString(date: Date): String {
            val format = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
            return format.format(date)
        }

        fun getNextDayToString(date: Date): String {
            val format = SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH)
            val c = Calendar.getInstance()

            c.time = date
            c.add(Calendar.DAY_OF_MONTH, 1)

            val currentDatePlusOne = c.time

            return format.format(currentDatePlusOne)
        }

        fun getStringToDate(s: String): String {
            val sdf = SimpleDateFormat("MM dd yyyy", Locale.ENGLISH)
            return getCurrentDateToString(sdf.parse(s))
        }

        fun getStringToTime(s: String): String {
            val sdf = SimpleDateFormat("hh mm aa", Locale.ENGLISH)
            return getCurrentTimeToString(sdf.parse(s))
        }

        fun getUnixTimeFromDate(s: String): String {
            val sdf = SimpleDateFormat("MMM dd yyyy hh:mm aa", Locale.ENGLISH)
            val date = sdf.parse(s)
            val unix = date.time / 1000
            return unix.toString()
        }

        fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
            val canvas = Canvas()
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            canvas.setBitmap(bitmap)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(canvas)

            return bitmap
        }
    }
}