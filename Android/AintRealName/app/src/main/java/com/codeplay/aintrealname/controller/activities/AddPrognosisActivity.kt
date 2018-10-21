package com.codeplay.aintrealname.controller.activities

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseListener
import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.controller.fragments.DiseaseFragment
import com.codeplay.aintrealname.utilities.Constants
import kotlinx.android.synthetic.main.activity_add_prognosis.*
import okhttp3.Response
import java.time.LocalDate

class AddPrognosisActivity : AppCompatActivity() {

    var date: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prognosis)

        button.setOnClickListener {
            val name = newName.text.toString()
            Log.v("Date", "$name $date")
            AndroidNetworking.post(Constants.DISEASE_ADD)
                    .addHeaders(Constants.AUTHORIZATION_KEY, Constants.TOKEN_STRING + Constants.token)
                    .addBodyParameter("startTime", date)
                    .addBodyParameter("diseaseName", name)
                    .build()
                    .getAsOkHttpResponse(object: OkHttpResponseListener{
                        override fun onResponse(response: Response?) {
                            DiseaseFragment.isRefreshForceful = true
                            finish()
                        }

                        override fun onError(anError: ANError?) {
                            Log.v("Get", anError!!.errorBody.toString())
                        }

                    })
        }

        newMonth.setOnClickListener {
            val timeNow = LocalDate.now()
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDayOfMonth ->
                date = formatDate(mYear, mMonth+1, mDayOfMonth)
                newMonth.text = date
            }, timeNow.year, timeNow.monthValue, timeNow.dayOfMonth)

            datePickerDialog.show()

        }
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val yearString = year.toString()
        val monthString =
                if (month < 10)
                    "0$month"
                else
                    month.toString()
        val dayString =
                if (day < 10)
                    "0$day"
                else
                    day.toString()

        return "$yearString-$monthString-$dayString"

    }
}
