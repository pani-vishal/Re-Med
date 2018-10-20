package com.codeplay.aintrealname.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.adapters.PrescriptionAdapter
import com.codeplay.aintrealname.utilities.AppDB
import kotlinx.android.synthetic.main.activity_prescription_detail.*

class PrescriptionDetailActivity : AppCompatActivity() {

    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescription_detail)

        id = intent.getIntExtra("id", 0)
        prescriptionNameTextView.text = intent.getStringExtra("name")
        startDateTextView.text = intent.getStringExtra("startDate")
        endDateTextView.text = intent.getStringExtra("endDate")

        val perscription = AppDB.getInstance(this).getAllPrescription(id)


        val adapter = PrescriptionAdapter(this){_,_ ->

        }

        prescriptionRecyclerView.adapter = adapter
        prescriptionRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        if(perscription.isNotEmpty()){
            adapter.swapList(perscription)
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }
}
