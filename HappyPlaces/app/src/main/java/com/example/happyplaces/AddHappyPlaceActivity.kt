package com.example.happyplaces

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.webkit.PermissionRequest
import android.widget.DatePicker
import android.widget.Toast
import com.example.happyplaces.databinding.ActivityAddHappyPlaceBinding
import com.example.happyplaces.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.text.SimpleDateFormat
import java.util.*

class AddHappyPlaceActivity : AppCompatActivity(), View.OnClickListener {

    private var binding : ActivityAddHappyPlaceBinding? = null
    private var cal = Calendar.getInstance() // this is calendar objects
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener // this is a listener to selected date in calendar dialog
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        // adds the support action bar to go back to home
        setSupportActionBar(binding?.toolbarAddPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarAddPlace?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.etDate?.setOnClickListener(this) // this implies that the local class will take care of processing the click
        // we could've also just processed the click within this listener
        binding?.tvAddImage?.setOnClickListener(this)// this implies that the local class will take care of processing the click
        // this sets the date values based on what has been clicked iwthin the Dialog
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
    }

    override fun onClick(view: View?) {
        when(view){
            binding?.etDate -> {
                DatePickerDialog(this@AddHappyPlaceActivity,
                    dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            } // builds a listener for user selecting a date
            binding?.tvAddImage -> {
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select photo from Gallery",
                    "Capture photo from Camera")
                pictureDialog.setItems(pictureDialogItems){
                    dialog, which ->
                    when (which){
                        0 -> choosePhotoFromGallery()
                        1 -> {
                            Toast.makeText(this, "Camera selection coming soon..", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                pictureDialog.show()
            }
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this).withPermissions(
            WRITE_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE
        ).withListener(object: MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report?.areAllPermissionsGranted() == true){
                    Toast.makeText(this@AddHappyPlaceActivity,"Storage access GRANTED", Toast.LENGTH_LONG).show()
                }
            }
            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                token: PermissionToken?
            ) {
               shouldShowDialogForPermission()
            }
        }).onSameThread().check()
    }

    private fun shouldShowDialogForPermission() {
        AlertDialog.Builder(this).setMessage("It looks like you turned off permission required for this feature. " +
                "It can be enabled under the application settings").setPositiveButton("GO TO SETTING"){_,_ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } catch (e: ActivityNotFoundException){
                e.printStackTrace()
            }
        }.setNegativeButton("Cancel"){dialog, _ ->
            dialog.dismiss()
        }.show()
    }

    private fun updateDateInView(){
        val sdf = SimpleDateFormat("MM.dd.yyyy", Locale.getDefault()) // we extract calendar time with cal.time that we convert to appropriate string
        binding?.etDate?.setText(sdf.format(cal.time).toString())
    }
}