package com.codeplay.aintrealname.controller.fragments


import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.codeplay.aintrealname.R
import android.widget.ArrayAdapter
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.codeplay.aintrealname.controller.SplashActivity
import com.codeplay.aintrealname.models.User
import com.codeplay.aintrealname.utilities.AppDB
import com.codeplay.aintrealname.utilities.Constants
import kotlinx.android.synthetic.main.fragment_user_details.*
import org.json.JSONObject


class UserDetailsFragment : Fragment() {

    var dobString: String = ""
    lateinit var mContext: Context
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val items = arrayOf("A+", "B+", "AB+", "O+", "A-", "B-", "AB-", "O-")

        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, items)
        bloodGroupSpinner.adapter = adapter

        dobTextView.setOnClickListener { showDatePickerDialog() }
        userDetailsSubmitButton.setOnClickListener { submit() }

        mContext = context!!
    }

    fun submit() {

        val progressDialog = ProgressDialog(context)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Saving Information..")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()


        val name = nameEditTextView.text.toString()
        val gender = getSelectedGender()
        val mobileNo = mobileNoEditTextView.text.toString()
        val height = heightEditTextView.text.toString()
        val weight = weightEditTextView.text.toString()
        val bloodGroup = bloodGroupSpinner.selectedItem.toString()
        val pincode = pincodeEditTextView.text.toString()

        Toast.makeText(activity, "$gender $bloodGroup", Toast.LENGTH_SHORT).show()


        AndroidNetworking.post(Constants.USER_DETAILS_URL)
                .addHeaders(Constants.AUTHORIZATION_KEY, Constants.TOKEN_STRING + Constants.token)
                .addBodyParameter(Constants.NAME_KEY, name)
                .addBodyParameter(Constants.PINCODE_KEY, pincode)
                .addBodyParameter(Constants.DATE_OF_BIRTH_KEY, dobString)
                .addBodyParameter(Constants.GENDER_KEY, getSelectedGender())
                .addBodyParameter(Constants.MOBILE_NO_KEY, mobileNo)
                .addBodyParameter(Constants.WEIGHT_KEY, weight)
                .addBodyParameter(Constants.HEIGHT_KEY, height)
                .addBodyParameter(Constants.BLOOD_GROUP_KEY, bloodGroup)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        progressDialog.dismiss()

                        AppDB.getInstance(mContext).putUserDetails(
                                User(name, mobileNo, pincode, getSelectedGender(), dobString, height, weight, bloodGroup)
                        )
                        activity?.finish()
                        Toast.makeText(context, "Details saved successfully", Toast.LENGTH_LONG).show()
                        startActivity(Intent(context, SplashActivity::class.java))
                    }
                    override fun onError(error: ANError) {
                        if (error.errorCode != 0) {

                            val errorResponse = JSONObject(error.errorBody)

                            if (errorResponse.has("phone")) {
                                mobileNoEditTextViewLayout.error = "Not a valid mobile no"
                            }
                            if (errorResponse.has("fb_id")) {
                                weightEditTextViewLayout.error = "The profile entered is incorrect"
                            }
                            if (errorResponse.has("suggested_referral")) {
                                heightEditTextViewLayout.error = "Enter correct Referral Code"
                            }
                        }
                        Log.e("UserDetailsInput", error.errorBody)
                        progressDialog.dismiss()
                    }
                })
    }

    private fun validate(name: String, collegeName: String, mobileNo: String, fbProfileIdLink: String): Boolean{
        var valid = true

        if (name.isEmpty()) {
            nameEditTextViewLayout.error = "This field should not be empty"
            valid = false
        } else {
            nameEditTextViewLayout.error = null
        }


        if(mobileNo.isEmpty()){
            mobileNoEditTextViewLayout.error = "This field should not be empty"
            valid = false
        } else if(mobileNo.length != 10) {
            mobileNoEditTextViewLayout.error = "Not a valid mobile no."
            valid = false
        } else {
            mobileNoEditTextViewLayout.error = null
        }

        if(fbProfileIdLink.isEmpty()){
            weightEditTextViewLayout.error = "This field should not be empty"
            valid = false
        } else {
            weightEditTextViewLayout.error = null
        }

        return valid
    }

    private fun showDatePickerDialog() {
        val year = 1999
        val month = 0
        val day = 1

        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDayOfMonth ->
            dobString = formatDate(mYear, mMonth+1, mDayOfMonth)
            dobTextView.text = dobString
        }, year, month, day)
        datePickerDialog.datePicker.maxDate = 1136053800000

        datePickerDialog.show()
    }

    private fun getSelectedGender(): String {
        val selectedGenderId = genderRadioGroup.checkedRadioButtonId

        return when(selectedGenderId) {
            R.id.femaleRadioButton -> "F"
            R.id.otherRadioButton -> "O"
            else -> "M"
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


