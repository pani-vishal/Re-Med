package com.codeplay.aintrealname.models

class Perscription(val pid: Int, val medicineName: String, val morning: Float, val afternoon: Float,
                   val evening: Float, val night: Float, val diseaseId: Int) {
    constructor(): this(0, "",  0.0f, 0.0f, 0.0f, 0.0f, 0)
}