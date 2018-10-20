package com.codeplay.aintrealname.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.controller.fragments.DiseaseFragment
import com.codeplay.aintrealname.utilities.Constants
import com.codeplay.aintrealname.utilities.MyPreferences
import com.codeplay.aintrealname.utilities.MyPreferences.set


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportFragmentManager.beginTransaction().replace(R.id.homeScreenArea, DiseaseFragment()).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.logout){
            val prefs = MyPreferences.customPrefs(this, Constants.MY_SHARED_PREFERENCE)
            prefs[Constants.KEY_TOKEN] = Constants.TOKEN_DEFAULT
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
