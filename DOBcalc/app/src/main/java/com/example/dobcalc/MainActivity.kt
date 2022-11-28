package com.example.dobcalc

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var tvSelectedDate : TextView? = null
    private var tvAgeInMinutes : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnDatePicker : Button = findViewById(R.id.btnDatePicker)
        tvSelectedDate = findViewById(R.id.selectedDateText) // we create a new object associated with the text field
        btnDatePicker.setOnClickListener {
            clickDatePicker()
        }
    }

    private fun clickDatePicker(){
        val myCalendar = Calendar.getInstance() // opens the calendar with current time zone and location of the system
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        // at this point, the variables just takes today's values
        val dpd = DatePickerDialog(this, // opens up the calendar interface
            { view, year, month, day ->
                Toast.makeText(this,"Y: $year M: ${month+1} D: $day", Toast.LENGTH_LONG).show()
                val selectedDate = "${month+1}/${day}/$year"
                tvSelectedDate?.setText(selectedDate)
                val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                val theDate = sdf.parse(selectedDate)
                theDate?.let{ // this will ensure that the code in {} runs only if theDate is not null
                    val currentData = sdf.parse(sdf.format(System.currentTimeMillis()))
                    currentData?.let { // this will ensure that the code in {} runs only if currentDate has been selected
                        val Minutes = (currentData.time - theDate.time) / 60000
                        tvAgeInMinutes = findViewById(R.id.Minutes)
                        tvAgeInMinutes?.setText(Minutes.toString())
                    }
            }
            },year,month,day)
        dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
        dpd.show()
        Toast.makeText(this, "btnDatePicker pressed", Toast.LENGTH_LONG).show()
    }

}

