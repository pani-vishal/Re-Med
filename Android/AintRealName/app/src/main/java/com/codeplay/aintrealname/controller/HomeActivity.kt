package com.codeplay.aintrealname.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.controller.fragments.HomeFragment
import com.codeplay.aintrealname.utilities.UserDetails
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportFragmentManager.beginTransaction().replace(R.id.homeScreenArea, HomeFragment()).commit()

        //textVIew.text = "Hello ${UserDetails.name} \n You are ${UserDetails.height} and ${UserDetails.weight}.\nYour bloodgroup is ${UserDetails.bloodGroup}"
    }
}
