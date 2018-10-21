package com.codeplay.aintrealname.controller.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.controller.interfaces.OnFragmentInteractionListener
import com.codeplay.aintrealname.models.User
import com.codeplay.aintrealname.utilities.UserDetails
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (listener != null) {
            listener!!.setTitleTo(resources.getString(R.string.app_name), 1)
        }
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userNameTextView.text = UserDetails.name
        messageTextView.text = "Congrats! You have been healthy for last 96 days. Stay healthy and live longer :)"

        phoneNoTextView.text = UserDetails.phoneNo
        heightTextView.text = UserDetails.height + " cm"
        weightTextView.text = UserDetails.weight + " kg"
        zipCodeTextView.text = UserDetails.pincode
        bloodGroupTextView.text = UserDetails.bloodGroup
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
