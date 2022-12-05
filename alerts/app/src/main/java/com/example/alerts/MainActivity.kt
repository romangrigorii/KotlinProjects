package com.example.alerts

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var but_toast : Button? = null
    var but_snackbar : Button? = null
    var but_alert : Button? = null
    var but_custom : Button? = null
    var but_loading : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        but_toast = findViewById(R.id.toast_but)
        but_snackbar = findViewById(R.id.snackbar_but)
        but_alert = findViewById(R.id.alert_but)
        but_custom = findViewById(R.id.custom_but)
        but_loading = findViewById(R.id.loading_but)

        but_toast?.setOnClickListener(){
            Toast.makeText(this, "You clicked on a Toast", Toast.LENGTH_LONG).show()
        }
        but_snackbar?.setOnClickListener(){view ->
            Snackbar.make(view, "You have clicked a SnackBar", Snackbar.LENGTH_LONG).show()
        }
        but_alert?.setOnClickListener{ view->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Alert")
            builder.setMessage("This is an Alert dialogue.")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes"){
                dialogInterface, which ->
                Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
                dialogInterface.dismiss()
            }
            builder.setNeutralButton("Cancel"){
                dialogInterface, which ->
                Toast.makeText(applicationContext,"clicked cancel \n operation cancel",Toast.LENGTH_LONG).show()
                dialogInterface.dismiss()
            }
            builder.setNegativeButton("No"){
                    dialogInterface, which ->
                Toast.makeText(applicationContext,"clicked cancel \n operation cancel",Toast.LENGTH_LONG).show()
                dialogInterface.dismiss()
            }
            val alertDialog : AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        but_custom?.setOnClickListener{
            customDialogFunction()
        }

        but_loading?.setOnClickListener{
            customProgressDialogFunction()
        }
    }

    private fun customDialogFunction() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom)
        customDialog.findViewById<TextView>(R.id.tv_submit).setOnClickListener(){
            Toast.makeText(this,"clicked submit",Toast.LENGTH_LONG).show()
            customDialog.dismiss()
        }
        customDialog.findViewById<TextView>(R.id.tv_cancel).setOnClickListener(){
            Toast.makeText(this,"clicked cancel",Toast.LENGTH_LONG).show()
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun customProgressDialogFunction(){
        val customProgressDialog = Dialog(this)
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog.show()
    }

}