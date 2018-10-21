package com.codeplay.aintrealname.controller.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.controller.fragments.LoginFragment

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        loadLogInFragment()
    }

    private fun loadLogInFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val loginFragment = LoginFragment()
        transaction.replace(R.id.login_signup_fragment_holder, loginFragment)
        transaction.commit()
    }
}
