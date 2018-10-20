package com.codeplay.aintrealname.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseListener
import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.controller.fragments.DiseaseFragment
import com.codeplay.aintrealname.utilities.Constants
import kotlinx.android.synthetic.main.activity_add_medication.*
import okhttp3.Response

class MedicationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medication)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getIntExtra("id", 0)
        button2.setOnClickListener {
            AndroidNetworking.post(Constants.MEDICINE_ADD)
                    .addHeaders(Constants.AUTHORIZATION_KEY, Constants.TOKEN_STRING + Constants.token)
                    .addBodyParameter("morning", morningName.text.toString())
                    .addBodyParameter("afternoon", afternoonName.text.toString())
                    .addBodyParameter("evening", eveningName.text.toString())
                    .addBodyParameter("night", nightName.text.toString())
                    .addBodyParameter("medicineName", newName.text.toString())
                    .addBodyParameter("prognosisID", id.toString())
                    .addBodyParameter("")
                    .build()
                    .getAsOkHttpResponse(object: OkHttpResponseListener {
                        override fun onResponse(response: Response?) {
                            DiseaseFragment.isRefreshForceful = true
                            finish()
                        }

                        override fun onError(anError: ANError?) {
                            Log.v("Get", anError!!.errorBody.toString())
                        }
                    })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
