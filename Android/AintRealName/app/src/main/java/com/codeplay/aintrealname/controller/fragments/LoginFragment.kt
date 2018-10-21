package com.codeplay.aintrealname.controller.fragments


import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.PasswordTransformationMethod
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
import com.codeplay.aintrealname.controller.activities.SplashActivity
import com.codeplay.aintrealname.models.User
import com.codeplay.aintrealname.utilities.AppDB
import com.codeplay.aintrealname.utilities.Constants
import com.codeplay.aintrealname.utilities.MyPreferences
import com.codeplay.aintrealname.utilities.MyPreferences.set
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject


class LoginFragment : Fragment() {

    lateinit var prefs: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputPasswordLogin.typeface = Typeface.DEFAULT
        inputPasswordLogin.transformationMethod = PasswordTransformationMethod()

        btnLogin.setOnClickListener { login() }
        tvLinkSignup.setOnClickListener { showSignupFragment() }
    }

    private fun showSignupFragment() {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        val signupFragment = SignUpFragment()
        transaction.setCustomAnimations(R.anim.push_left_in_fast, R.anim.push_left_out_fast)
        transaction.replace(R.id.login_signup_fragment_holder, signupFragment)
        transaction.commit()
    }

    private fun login() {
        val username = inputUsernameLogin.text.toString()
        val password = inputPasswordLogin.text.toString()

        val progressDialog = ProgressDialog(context)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        prefs = MyPreferences.customPrefs(context!!, Constants.MY_SHARED_PREFERENCE)

        AndroidNetworking.post(Constants.LOGIN_URL)
                .addBodyParameter("username", username)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {

                    override fun onResponse(response: JSONObject) {

                        try {

                            val token = response.getString("key")

                            prefs[Constants.KEY_TOKEN] = token

                            Constants.token = token

                        } catch (e: Exception) {
                            Log.d("Response", response.toString())
                        }

                        onLoginSuccess()
                        progressDialog.dismiss()
                    }

                    override fun onError(error: ANError) {
                        Log.e("Error", error.errorBody)
//                        if (error.errorCode != 0) {
//                            val errorResponse = JSONObject(error.errorBody)
//
//                            if (errorResponse.has("username")) {
//                                inputUsernameLoginLayout.error = errorResponse.getJSONArray("username").getString(0)
//                            }
//                            if (errorResponse.has("non_field_errors")) {
//                                inputUsernameLoginLayout.error = "Either the username or the password is incorrect"
//                            }
//                        }
                        // handle error
                        onLoginFailed()
                        progressDialog.dismiss()
                    }
                })
    }


    fun onLoginSuccess() {
        btnLogin.isEnabled = true

        AndroidNetworking.get(Constants.USER_DETAILS_URL)
                .addHeaders(Constants.AUTHORIZATION_KEY, Constants.TOKEN_STRING + Constants.token)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        AppDB.getInstance(context!!).putUserDetails(
                                User(response!!.optString("name"),
                                        response.optString("phoneNumber"),
                                        response.optString("pincode"),
                                        response.optString("gender"),
                                        response.optString("dob"),
                                        response.optString("height"),
                                        response.optString("weight"),
                                        response.optString("bloodGroup"))
                        )
                        activity?.finish()
                        Toast.makeText(context, "Details saved successfully", Toast.LENGTH_LONG).show()
                        startActivity(Intent(context, SplashActivity::class.java))
                    }

                    override fun onError(anError: ANError?) {
                        prefs[Constants.KEY_TOKEN] = Constants.TOKEN_DEFAULT
                    }

                })
    }

    fun onLoginFailed() {
        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
        btnLogin.isEnabled = true
    }


}
