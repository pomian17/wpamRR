package com.example.restaurantreservation.ui.widget

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.*

class DateTimePicker {

    private var datePicker: DatePickerDialog? = null
    private var timePicker: TimePickerDialog? = null

    fun showDateTimePicker(
        context: Context,
        timeInMillis: Long,
        minDate: Long = System.currentTimeMillis(),
        onSetListener: ((Long) -> Unit)
    ) {
        val time = Calendar.getInstance().apply {
            setTimeInMillis(timeInMillis)
        }

        showDatePicker(context, minDate, onSetListener, time)
    }

    private fun showDatePicker(
        context: Context,
        minDate: Long,
        onSetListener: (Long) -> Unit,
        time: Calendar
    ) {
        dismissDialog()
        datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                showTimePicker(context, year, month, dayOfMonth, onSetListener, time)
            },
            time.get(Calendar.YEAR),
            time.get(Calendar.MONTH),
            time.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = minDate
            show()
        }
    }

    private fun showTimePicker(
        context: Context,
        year: Int,
        month: Int,
        dayOfMonth: Int,
        onSetListener: (Long) -> Unit,
        time: Calendar
    ) {

        timePicker = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val selectedTime = getTimeInMillis(year, month, dayOfMonth, hourOfDay, minute)
                onSetListener.invoke(selectedTime)
            },
            time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE),
            true
        ).apply { show() }
    }

    private fun getTimeInMillis(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minute: Int
    ): Long =
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }.timeInMillis

    fun dismissDialog() {
        datePicker?.dismiss()
        timePicker?.dismiss()
    }

}
