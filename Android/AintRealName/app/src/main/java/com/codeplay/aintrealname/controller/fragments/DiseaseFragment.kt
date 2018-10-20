package com.codeplay.aintrealname.controller.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v4.util.Pair

import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.adapters.DiseaseAdapter
import com.codeplay.aintrealname.controller.PrescriptionDetailActivity
import com.codeplay.aintrealname.models.Disease
import com.codeplay.aintrealname.utilities.AppDB
import kotlinx.android.synthetic.main.fragment_disease.*
import android.support.v4.app.ActivityOptionsCompat
import android.util.Log
import android.view.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.codeplay.aintrealname.controller.AddPrognosisActivity
import com.codeplay.aintrealname.models.Perscription
import com.codeplay.aintrealname.utilities.Constants
import com.sergiocasero.revealfab.RevealFAB
import org.json.JSONArray
import org.json.JSONObject




class DiseaseFragment : Fragment() {
    companion object {
        var isRefreshForceful = false
    }
    private var allDiseases = ArrayList<Disease>()
    lateinit var adapter: DiseaseAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_disease, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(activity!!, AddPrognosisActivity::class.java)
        reveal_fab.intent = intent
        reveal_fab.setOnClickListener { button, _ ->
            button!!.startActivityWithAnimation()
        }

        adapter = DiseaseAdapter(activity!!){a, b ->
            val intent = Intent(activity!!, PrescriptionDetailActivity::class.java)
            intent.putExtra("id", a.id)
            intent.putExtra("name", a.name)
            intent.putExtra("startDate", a.startDate)
            intent.putExtra("endDate", a.endDate)

            val p1 = Pair.create(b.findViewById(R.id.prescriptionNameTextView) as View, "title")
            val p2 = Pair.create(b.findViewById(R.id.startDateTextView) as View, "start")
            val p3 = Pair.create(b.findViewById(R.id.endDateTextView) as View, "end")

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, p1, p2, p3)
            startActivity(intent, options.toBundle())
        }
        diseaseRecyclerView.adapter = adapter
        diseaseRecyclerView.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)

    }






    private fun getDiseases() {
        allDiseases.clear()
        allDiseases.addAll(AppDB.getInstance(context!!).getAllDiseases())

        if(allDiseases.isEmpty() || isRefreshForceful) {

            isRefreshForceful = false
            //Toast.makeText(context!!, "I am empty", Toast.LENGTH_SHORT).show()
            AndroidNetworking.get(Constants.DISEASE_URL)
                    .addHeaders(Constants.AUTHORIZATION_KEY, Constants.TOKEN_STRING + Constants.token)
                    .build()
                    .getAsJSONArray(object: JSONArrayRequestListener{
                        override fun onResponse(response: JSONArray?) {
                            val len = response!!.length()

                            Log.e("JSON", response.toString())
                            for(i in 0..len-1){
                                val obj = response[i] as JSONObject
                                allDiseases.add(Disease(
                                        obj.optInt("id"),
                                        obj.optString("diseaseName"),
                                        obj.optString("startTime"),
                                        obj.optString("endTime"),
                                        obj.optBoolean("isActive")
                                ))

                                val prescription = obj.optJSONArray("prescription")
                                for(j in 0 until prescription.length()){
                                    val obj2 = prescription[j] as JSONObject

                                    AppDB.getInstance(context!!).putPrescription(Perscription(
                                            obj2.optInt("id"),
                                            obj2.optString("medicineName"),
                                            obj2.optDouble("morning").toFloat(),
                                            obj2.optDouble("afternoon").toFloat(),
                                            obj2.optDouble("evening").toFloat(),
                                            obj2.optDouble("night").toFloat(),
                                            obj.optInt("id")
                                    ))
                                }

                            }

                            AppDB.getInstance(context!!).putAllDiseases(allDiseases)


                            adapter.swapList(allDiseases)

                        }

                        override fun onError(anError: ANError?) {
                            Log.v("Error", anError!!.message)
                        }

                    })

        } else{
            adapter.swapList(allDiseases)
        }
    }

    override fun onResume() {
        super.onResume()
        reveal_fab.onResume()

        getDiseases()
    }
}
