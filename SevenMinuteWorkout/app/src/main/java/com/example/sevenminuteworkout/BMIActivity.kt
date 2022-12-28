package com.example.sevenminuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.sevenminuteworkout.databinding.ActivityBmiBinding
import com.example.sevenminuteworkout.databinding.ActivityExerciseBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val IMPERIAL_UNITS_VIEW = "IMPERIAL_UNITS_VIEW"
    }

    private var binding : ActivityBmiBinding? = null
    private var currentVisibleView : String = METRIC_UNITS_VIEW
    private var height : Float = 0.0f
    private var weight : Float = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.BMItoolbar)
        if (supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.BMItoolbar?.setNavigationOnClickListener{
            onBackPressed() // performs the back actions that can be performed with the button on the action bar of the phone
        }

        binding?.calcButton?.setOnClickListener{
            if (validate()){
                if (currentVisibleView == METRIC_UNITS_VIEW) {
                    height = binding?.etMetricUnitHeight?.text.toString().toFloat()
                    weight = binding?.etMetricUnitWeight?.text.toString().toFloat()
                } else {
                    height = binding?.etImperialUnitHeightFt?.text.toString().toFloat()*12 + binding?.etImperialUnitHeightIn?.text.toString().toFloat()
                    weight = binding?.etImperialUnitWeight?.text.toString().toFloat()*703
                }
                if (height>0.0f && weight>0.0f) {
                    displayBMIResults(weight/height/height)
                }
            } else {
                Toast.makeText(this@BMIActivity, "Please enter valid values", Toast.LENGTH_SHORT).show()
            }
        }
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == binding?.metUnits?.id){
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleImperialUnitsView()
            }
        }
    }

    private fun validate() : Boolean {
        var isValid = true
        if (currentVisibleView== METRIC_UNITS_VIEW) {
            if (binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
                isValid = false
            }
            if (binding?.etMetricUnitWeight?.text.toString().isEmpty()) {
                isValid = false
            }
        } else {
            if (binding?.etImperialUnitHeightFt?.text.toString().isEmpty()) {
                isValid = false
            }
            if (binding?.etImperialUnitHeightIn?.text.toString().isEmpty()) {
                isValid = false
            }
            if (binding?.etImperialUnitWeight?.text.toString().isEmpty()) {
                isValid = false
            }
        }

        return isValid
    }

    private fun displayBMIResults(bmi : Float){
        val bmiLabel : String
        val bmiDescription : String
        if (bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself"
        }
        else if (bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself"
        }
        else if (bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in good shape."
        }
        else if (bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take better care of yourself"
        }
        else {
            bmiLabel = "Obese"
            bmiDescription = "Oops! You really need to take better care of yourself"
        }
        binding?.BMInum?.text = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
        binding?.BMItext1?.text = bmiLabel
        binding?.BMItext2?.text = bmiDescription
    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilImperialUnitWeight?.visibility = View.INVISIBLE
        binding?.tilImperialUnitHeightFt?.visibility = View.INVISIBLE
        binding?.tilImperialUnitHeightIn?.visibility = View.INVISIBLE
        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()
    }

    private fun makeVisibleImperialUnitsView() {
        currentVisibleView = IMPERIAL_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilImperialUnitWeight?.visibility = View.VISIBLE
        binding?.tilImperialUnitHeightFt?.visibility = View.VISIBLE
        binding?.tilImperialUnitHeightIn?.visibility = View.VISIBLE
        binding?.etImperialUnitHeightFt?.text!!.clear()
        binding?.etImperialUnitHeightIn?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()
    }



}