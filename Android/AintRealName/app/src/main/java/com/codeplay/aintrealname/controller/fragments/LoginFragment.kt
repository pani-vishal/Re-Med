package com.codeplay.aintrealname.controller.fragments


import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.codeplay.aintrealname.R
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
