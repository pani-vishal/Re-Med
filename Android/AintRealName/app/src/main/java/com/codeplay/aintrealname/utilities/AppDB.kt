package com.codeplay.aintrealname.utilities

import android.content.Context
import android.util.Log
import com.codeplay.aintrealname.models.Disease
import com.codeplay.aintrealname.models.Perscription
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

    private val diseaseHash = waspDB.openOrCreateHash("disease")

    private val prescriptionHash = waspDB.openOrCreateHash("prescription")

    companion object : SingletonHolder<AppDB, Context>(::AppDB)

    fun putUserDetails(user: User) = userHash.put(1, user)

    fun getUserDetails() : User = userHash.getAllValues<User>().get(0)

    fun putAllDiseases(diseases: List<Disease>) = diseases.forEach {
        diseaseHash.put(it.id, it)
    }

    fun putPrescription(prescription: Perscription) {
        prescriptionHash.put("${prescription.pid}${prescription.diseaseId}", prescription)
    }

    fun getActivePrescription(type: Int): List<Perscription> {
        return when (type) {
            1 -> prescriptionHash.getAllValues<Perscription>().filter { it.isActive == true && it.morning > 0.0}
            2 -> prescriptionHash.getAllValues<Perscription>().filter { it.isActive == true && it.afternoon > 0.0}
            3 -> prescriptionHash.getAllValues<Perscription>().filter { it.isActive == true && it.evening > 0.0 }
            else -> prescriptionHash.getAllValues<Perscription>().filter { it.isActive == true && it.night > 0.0 }
        }
    }

    fun getAllPrescription(diseaseId: Int): List<Perscription> {
        return prescriptionHash.getAllValues<Perscription>().filter { it.diseaseId == diseaseId }
    }

    fun getAllDiseases(): List<Disease> = diseaseHash.getAllValues<Disease>().sortedBy { it.startDate }


    fun deleteAllData() {
        waspDB.removeHash("prescription")
        waspDB.removeHash("disease")
        waspDB.removeHash("user")
        waspDB.removeHash("medication")
    }
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