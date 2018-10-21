package com.codeplay.aintrealname.controller.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.controller.interfaces.OnFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_so.*
import android.content.Intent
import android.app.PendingIntent
import android.R.attr.data
import android.net.Uri
import android.telephony.SmsMessage
import com.codeplay.aintrealname.utilities.Constants
import com.codeplay.aintrealname.utilities.MyPreferences
import com.codeplay.aintrealname.utilities.MyPreferences.get
import com.codeplay.aintrealname.utilities.MyPreferences.set
import com.codeplay.aintrealname.R.id.textView



class SOSFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (listener != null) {
            listener!!.setTitleTo("SOS", 3)
        }
        return inflater.inflate(R.layout.fragment_so, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sosButton.setOnClickListener {


            val prefs = MyPreferences.customPrefs(context!!, Constants.MY_SHARED_PREFERENCE)
            val phonenumber = prefs["phone", "8452800051"]

            val smsNumber = String.format("smsto: %s",
                    phonenumber.toString())
            val smsIntent = Intent(Intent.ACTION_SENDTO)
            smsIntent.setData(Uri.parse(smsNumber))
            // Add the message (sms) with the key ("sms_body").
            smsIntent.putExtra("sms_body", "I don't feel good");
            // If package resolves (target app installed), send intent.

                startActivity(smsIntent)
        }

        button3.setOnClickListener {
            val prefs = MyPreferences.customPrefs(context!!, Constants.MY_SHARED_PREFERENCE)
            prefs["phone"] = editText.text.toString()

            editText.setText("")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
