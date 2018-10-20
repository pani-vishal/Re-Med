package com.codeplay.aintrealname.utilities

import android.content.Context
import android.util.Log
import com.codeplay.aintrealname.models.User
import net.rehacktive.waspdb.WaspDb
import net.rehacktive.waspdb.WaspFactory

class AppDB private constructor(context: Context) {
    private val waspDB: WaspDb = WaspFactory.openOrCreateDatabase(
            context.filesDir.path,
            "medicalDb",
            "codeplay"
    )

    private val medicationHash = waspDB.openOrCreateHash("medication")

    private val userHash = waspDB.openOrCreateHash("user")

    companion object : SingletonHolder<AppDB, Context>(::AppDB)

    fun putUserDetails(user: User) = userHash.put(1, user)

    fun getUserDetails() : User = userHash.get(1)



//    fun putAllMedication(medications: List<Medication>) = medications.forEach {
//        medicationHash.put("${it.duration}${it.medicine_id}${it.disease_id}", it)
//        Log.v("Data", "${it.duration}${it.medicine_id}${it.disease_id}")
//    }
//
//    fun getAllMedication(duration: Int, diseaseId: Long): List<Medication>? {
//        val data = medicationHash.getAllValues<Medication>()
//
//        if(data == null)
//            return null
//        else{
//            return data.filter { it.duration == duration && it.disease_id == diseaseId  }
//        }
//    }
}