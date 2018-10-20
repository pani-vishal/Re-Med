package com.codeplay.aintrealname.controller

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.services.NotifyWorker
import com.codeplay.aintrealname.utilities.AppDB
import com.codeplay.aintrealname.utilities.Constants
import com.codeplay.aintrealname.utilities.MyPreferences
import com.codeplay.aintrealname.utilities.MyPreferences.get
import com.codeplay.aintrealname.utilities.MyPreferences.set
import com.codeplay.aintrealname.utilities.UserDetails
import java.time.DateTimeException
import java.time.Duration
import java.time.LocalTime

class SplashActivity : AppCompatActivity() {

    lateinit var prefs: SharedPreferences
    lateinit var userToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        prefs = MyPreferences.customPrefs(this, Constants.MY_SHARED_PREFERENCE)
        userToken = prefs[Constants.KEY_TOKEN, Constants.TOKEN_DEFAULT]
        Log.v("Token", userToken)
        if(userToken == Constants.TOKEN_DEFAULT){
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }else{
            val user = AppDB.getInstance(this).getUserDetails()

            UserDetails.name = user.name
            UserDetails.birthDate = user.birthDate
            UserDetails.bloodGroup = user.bloodGroup
            UserDetails.gender = user.gender
            UserDetails.height = user.height
            UserDetails.weight = user.weight
            UserDetails.phoneNo = user.phoneNo
            UserDetails.pincode = user.pincode

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }


}
