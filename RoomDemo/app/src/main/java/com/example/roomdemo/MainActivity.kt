package com.example.roomdemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.databinding.EditLayoutBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val employeeDao = (application as EmployeeApp).db.employeeDao()
        binding?.btnAdd?.setOnClickListener{
            addRecord(employeeDao)
        }
        lifecycleScope.launch{
            employeeDao.fetchAllEmployees().collect{ // collect acts on the Flow
                setUpListOfDataIntoRecyclerView( it as ArrayList, employeeDao)
            }
        }
    }

    private fun addRecord(employeeDao : EmployeeDao){ // we add a record when name and email fields have text
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmail?.text.toString()
        if (email.isNotEmpty() && name.isNotEmpty()){
            lifecycleScope.launch { // this launches the coroutine
                employeeDao.insert(EmployeeEntity(name=name, email = email))
                Toast.makeText(this@MainActivity, " Record Saved ", Toast.LENGTH_LONG)
                binding?.etName?.text?.clear()
                binding?.etEmail?.text?.clear()
            }
        } else {
            Toast.makeText(this@MainActivity, " Enter valid name and email", Toast.LENGTH_LONG)
        }
    }

    private fun setUpListOfDataIntoRecyclerView(employeeList : ArrayList<EmployeeEntity>, employeeDao: EmployeeDao){
        if (employeeList.isNotEmpty()){
            val itemAdapter = ItemAdapter(employeeList,{ // these items are passed into the adapted as function s
                updateId->
                updateRecordDialog(updateId,employeeDao)
            },
            {
                deleteId->
                deleteRecordAlertDialog(deleteId,employeeDao)
            })
            binding?.rvItemList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemList?.adapter = itemAdapter // connection to the list view
            binding?.rvItemList?.visibility = View.VISIBLE
            binding?.tvNoRecords?.visibility = View.GONE
        } else {
            binding?.rvItemList?.visibility = View.GONE
            binding?.tvNoRecords?.visibility = View.VISIBLE
        }
    }

    private fun updateRecordDialog(id:Int, employeeDao: EmployeeDao){
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding = EditLayoutBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch { // we populate the fields with the text that wa saved
            employeeDao.fetchEmployeeById(id).collect {
                if (it!=null) {
                    binding.etNameUR.setText(it.name)
                    binding.etEmailUR.setText(it.email)
                }
            }
        }

        binding.tvUpdate.setOnClickListener{
            val name = binding.etNameUR.text.toString()
            val email = binding.etEmailUR.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()){
                lifecycleScope.launch {
                    employeeDao.update(EmployeeEntity(id = id, name = name, email = email))
                    Toast.makeText(applicationContext, "Record Updated", Toast.LENGTH_LONG).show()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext, "Name/Email cannot be blank", Toast.LENGTH_LONG).show()
            }
        }

        binding.tvCancel.setOnClickListener{
            updateDialog.dismiss()
        }

        updateDialog.show()
    }

     private fun deleteRecordAlertDialog(id : Int, employeeDao: EmployeeDao){
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes"){dialogInterface, _ ->
            lifecycleScope.launch{
                employeeDao.delete(EmployeeEntity(id))
                Toast.makeText(applicationContext, "Record deleted successfully", Toast.LENGTH_LONG).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: android.app.AlertDialog? = builder.create()
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }
}