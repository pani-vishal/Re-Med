package com.codeplay.aintrealname.services

import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import androidx.work.*
import com.codeplay.aintrealname.models.Perscription
import com.codeplay.aintrealname.utilities.AppDB
import java.time.Duration
import java.time.LocalTime

class NotifyWorker(val context : Context, val params : WorkerParameters)
    : Worker(context, params) {

    override fun doWork(): Result {
        triggerNotification()

        return Result.SUCCESS

    }

    private fun triggerNotification() {

        val duration = params.inputData.getInt("duration", 0)

        Log.e("Data", "$duration")
        val time = when(duration){
            1-> "Morning"
            2-> "Afternoon"
            3-> "Evening"
            else -> "Night"
        }
        val preferrendTimes =   arrayOf(21, 8, 14, 18, 21)

        val medications = ArrayList<Perscription>()
        medications.addAll(AppDB.getInstance(context).getActivePrescription (duration))

        var medicationDisplay = ""
        if(medications != null && medications.isNotEmpty()){

            for(medication in medications){
                medicationDisplay+="\n${medication.medicineName}"
            }

            var mBuilder = NotificationCompat.Builder(context, "123")
                    .setSmallIcon(android.R.drawable.alert_light_frame)
                    .setContentTitle("Don't forget your medicines")
                    .setContentText("It's time for your $time medication...")
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText("Here's your $time medication $medicationDisplay"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)


            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify((duration* 1234), mBuilder.build())
            }
        }

        val inputData2 = Data.Builder().putInt("duration", (duration % 4) + 1).build()

        val hour = LocalTime.now().hour
        lateinit var notificationWork: WorkRequest

        if(hour < preferrendTimes[duration%4 + 1]){
            notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
                    .setInitialDelay(Duration.ofHours( preferrendTimes[duration%4 + 1]- hour.toLong()))
                    .setInputData(inputData2)
                    .addTag("notificationWork")
                    .build()
        }
        else if(hour > 21 && hour < 24 && preferrendTimes[duration % 4 + 1] == 8) {
            notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
                    .setInitialDelay(Duration.ofHours( 32 - hour.toLong() ))
                    .setInputData(inputData2)
                    .addTag("notificationWork")
                    .build()
        }
        else{
            notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
                    .setInputData(inputData2)
                    .addTag("notificationWork")
                    .build()
        }

        WorkManager.getInstance().enqueue(notificationWork)
    }


}