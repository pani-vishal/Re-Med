package com.codeplay.aintrealname.controller.fragments


import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener

import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.utilities.Constants
import com.codeplay.aintrealname.utilities.MyPreferences
import com.codeplay.aintrealname.utilities.MyPreferences.set
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.json.JSONObject


class SignUpFragment : Fragment() {

    lateinit var prefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignup.setOnClickListener { signup() }
        tvLinkLogin.setOnClickListener { showLoginFragment() }
    }

    private fun showLoginFragment() {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        val loginFragment = LoginFragment()
        transaction.setCustomAnimations(R.anim.push_right_out_fast, R.anim.push_right_in_fast)
        transaction.replace(R.id.login_signup_fragment_holder, loginFragment)
        transaction.commit()
    }

    private fun signup() {
        val username = inputUsernameSignup.text.toString()
        val email = inputEmailSignup.text.toString()

        val password = inputPasswordSignup.text.toString()
        val reEnterPassword = inputReEnterPasswordSignup.text.toString()

        btnSignup.isEnabled = false

        val progressDialog = ProgressDialog(context)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Creating Account...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        prefs = MyPreferences.customPrefs(context!!, Constants.MY_SHARED_PREFERENCE)
        Log.v("URL", Constants.SIGNUP_URL + " $email $username $password $reEnterPassword")
        AndroidNetworking.post(Constants.SIGNUP_URL)
                .addBodyParameter("email", email)
                .addBodyParameter("username", username)
                .addBodyParameter("password1", password)
                .addBodyParameter("password2", reEnterPassword)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {

                    override fun onResponse(response: JSONObject) {

                        try {

                            val token = response.getString("key")
                            Log.v("Token received", token)
                            prefs[Constants.KEY_TOKEN] = token

                            Constants.token = token

                        } catch (e: Exception) {
                            Log.d("Response", response.toString())
                        }

                        onSignupSuccess()
                        progressDialog.dismiss()
                        showUserDetailsInputFragment()
                    }

                    override fun onError(error: ANError) {
                        if (error.errorCode != 0) {
                            val errorResponse = JSONObject(error.errorBody)

                            if (errorResponse.has("username")) {
                                inputUsernameSignupLayout.error =
                                        errorResponse.getJSONArray("username").getString(0)
                            }

                            if (errorResponse.has("email")) {
                                inputEmailSignupLayout.error = errorResponse.getJSONArray("email").getString(0)
                            }
                        }
                        Log.v("URL", error.errorBody)
                        onSignupFailed()
                        progressDialog.dismiss()
                    }
                })
    }

    private fun showUserDetailsInputFragment() {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        val userDetailsInputFragment = UserDetailsFragment()
        transaction.setCustomAnimations(R.anim.push_left_in_fast, R.anim.push_left_out_fast)
        transaction.replace(R.id.login_signup_fragment_holder, userDetailsInputFragment)
        transaction.commit()
    }

    fun onSignupSuccess() {
        Toast.makeText(activity, "Registration Successful", Toast.LENGTH_SHORT).show()
        btnSignup.isEnabled = true
    }

    fun onSignupFailed() {
        Toast.makeText(activity, "Sign Up Failed", Toast.LENGTH_SHORT).show()
        btnSignup.isEnabled = true
    }



}
