package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var decpressed : Boolean = false
    private var lastnumeric : Boolean = false
    private var tvInput : TextView? = null
    private var btnvalue : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.TextField)
    }

    fun onDigit(view: View) { // view is a button in this case, but we need to cast it to ensure
        btnvalue = (view as Button).text as String?
        Toast.makeText(this, "Button $btnvalue clicked", Toast.LENGTH_LONG).show()
        if (!((tvInput?.text)?.isEmpty() == true && btnvalue == "0")){
            tvInput?.append(btnvalue)
            lastnumeric = true
        }
    }

    fun onClear(view: View){
        tvInput?.text = ""
        decpressed = false
    }

    fun onDecimalPoint(view: View){
        if (!decpressed){
            decpressed = true
            if ((tvInput?.text)?.isEmpty() == true){
                tvInput?.append("0")
            }
            tvInput?.append(".")
            lastnumeric = false
        }
    }

    fun onOperator(view: View) {
        tvInput?.text?.let { // will only run if this is a string and not a null
            if (lastnumeric && !isOperatorAdded(it.toString())) { // it in this case is tied to the let statement
                tvInput?.append((view as Button).text)
                lastnumeric = false
                decpressed = false
            }
        }
    }

    private fun isOperatorAdded(value : String): Boolean {
        return if(value.startsWith("-")){
            false
        } else {
            return (value.endsWith("/") ||
                    value.endsWith("-") ||
                    value.endsWith("*") ||
                    value.endsWith("+"))
        }
    }

    private fun removeZeroAfterDot(result: String) : String{
        var value = result
        while (!value.isEmpty() && (value.endsWith("0") || value.endsWith("."))){
            value = value.subSequence(0,value.length - 1) as String
        }
        return value
    }

    fun onEqual(view: View){
        if (lastnumeric){
            var tvValue = tvInput?.text.toString()
            var mul = 1

            try {

                if(tvValue.startsWith("-")){
                    mul = -1
                    tvValue = tvValue.substring(1)
                }

                val splitValue = tvValue.split("-", "*", "+", "/")
                if (tvValue.contains('-')) {
                    tvInput?.text =
                        removeZeroAfterDot((mul*splitValue[0].toDouble() - splitValue[1].toDouble()).toString())
                } else if (tvValue.contains('+')) {
                    tvInput?.text =
                        removeZeroAfterDot((mul*splitValue[0].toDouble() + splitValue[1].toDouble()).toString())
                } else if (tvValue.contains('*')) {
                    tvInput?.text =
                        removeZeroAfterDot((mul*splitValue[0].toDouble() * splitValue[1].toDouble()).toString())
                } else if (tvValue.contains('/')) {
                    tvInput?.text =
                        removeZeroAfterDot((mul*splitValue[0].toDouble() / splitValue[1].toDouble()).toString())
                }
            } catch (e: java.lang.ArithmeticException){
                e.printStackTrace()
            }
        }
    }
}
