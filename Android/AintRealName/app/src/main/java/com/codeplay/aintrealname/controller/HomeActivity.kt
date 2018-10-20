package com.codeplay.aintrealname.controller

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.controller.fragments.DiseaseFragment
import com.codeplay.aintrealname.services.NotifyWorker
import com.codeplay.aintrealname.utilities.AppDB
import com.codeplay.aintrealname.utilities.Constants
import com.codeplay.aintrealname.utilities.MyPreferences
import com.codeplay.aintrealname.utilities.MyPreferences.set
import java.time.Duration
import java.time.LocalTime


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        createNotificationChannel()

       WorkManager.getInstance().cancelAllWorkByTag("notificationWork")



        val preferrendTimes = arrayOf(8, 14, 18, 21)

        val time = LocalTime.now().hour
        var differ = 1
        if(time in 11..15){
            differ = 2
        } else if(time in 16..18){
            differ = 3
        } else if(time < 24) {
            differ = 4
        }
        val inputData2 = Data.Builder().putInt("duration", differ).build()

        val notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
                .setInitialDelay(Duration.ofMillis(preferrendTimes[differ-1] - time.toLong()))
                .setInputData(inputData2)
                .addTag("notificationWork")
                .build()

        WorkManager.getInstance().enqueue(notificationWork)


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
            AppDB.getInstance(this).deleteAllData()
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
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
}
