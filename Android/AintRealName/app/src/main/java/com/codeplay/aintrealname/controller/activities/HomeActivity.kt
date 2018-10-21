package com.codeplay.aintrealname.controller.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.builders.footer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.controller.fragments.DiseaseFragment
import com.codeplay.aintrealname.controller.fragments.HomeFragment
import com.codeplay.aintrealname.controller.fragments.SOSFragment
import com.codeplay.aintrealname.controller.interfaces.OnFragmentInteractionListener
import com.codeplay.aintrealname.services.NotifyWorker
import com.codeplay.aintrealname.utilities.AppDB
import com.codeplay.aintrealname.utilities.Constants
import com.codeplay.aintrealname.utilities.MyPreferences
import com.codeplay.aintrealname.utilities.MyPreferences.set
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import java.time.Duration
import java.time.LocalTime


class HomeActivity : AppCompatActivity(), OnFragmentInteractionListener {
    private var fragment: Fragment? = null
    private var fragmentClass = arrayOf(HomeFragment::class.java, DiseaseFragment::class.java, SOSFragment::class.java)
    private var currentPage = 1
    private lateinit var result : Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        createNotificationChannel()
        setSupportActionBar(toolbar)
       WorkManager.getInstance().cancelAllWorkByTag("notificationWork")



        val preferrendTimes = arrayOf(8, 14, 18, 21)

        val time = LocalTime.now().hour
        var differ = 1
        when (time) {
            in 11..15 -> differ = 2
            in 16..18 -> differ = 3
            in 18..24 -> differ = 4
        }
        val inputData2 = Data.Builder().putInt("duration", differ).build()

        val notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
                .setInitialDelay(Duration.ofMillis(preferrendTimes[differ-1] - time.toLong()))
                .setInputData(inputData2)
                .addTag("notificationWork")
                .build()

        WorkManager.getInstance().enqueue(notificationWork)


        result = drawer {

            headerViewRes = R.layout.nav_header_home
            headerHeightDp = 180
            primaryItem("Home") {
                iicon = GoogleMaterial.Icon.gmd_home
                onClick { _ ->
                    switchToFragment(1)
                    false
                }
            }
            primaryItem("All Prognosis") {
                iicon = FontAwesome.Icon.faw_medkit
                onClick { _ ->
                    switchToFragment(2)
                    false
                }
            }

            primaryItem("Emergency") {
                iicon = GoogleMaterial.Icon.gmd_announcement
                onClick { _ ->
                    switchToFragment(3)
                    false
                }
            }



            footer {
                secondaryItem("Logout and Exit") {
                    onClick { _->
                        val prefs = MyPreferences.customPrefs(this@HomeActivity, Constants.MY_SHARED_PREFERENCE)
                        prefs[Constants.KEY_TOKEN] = Constants.TOKEN_DEFAULT
                        AppDB.getInstance(this@HomeActivity).deleteAllData()
                        finish()
                        false
                    }

                }
            }

        }

        val toggle = ActionBarDrawerToggle(
                this, result.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        result.actionBarDrawerToggle = toggle
        toggle.syncState()

        supportFragmentManager.beginTransaction().replace(R.id.homeScreenArea, HomeFragment()).commit()
    }

    private fun switchToFragment(fragmentNo: Int) {
        try {
            fragment = fragmentClass[fragmentNo - 1].newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (currentPage != fragmentNo) {

            var startAnimation = R.anim.push_left_in
            var endAnimation = R.anim.push_left_out
            if (currentPage > fragmentNo) {
                startAnimation = R.anim.push_right_out
                endAnimation = R.anim.push_right_in
            }

            supportFragmentManager.beginTransaction().setCustomAnimations(startAnimation, endAnimation).replace(R.id.homeScreenArea, fragment!!).commit()

        }

    }

    override fun onBackPressed() {
        when{
            result.isDrawerOpen -> result.closeDrawer()
            else -> super.onBackPressed()
        }
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Medication"
            val descriptionText = "Get notified of all the notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("123", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun setTitleTo(title: String, pos: Int) {
        supportActionBar!!.title = title
        currentPage = pos
    }
}
