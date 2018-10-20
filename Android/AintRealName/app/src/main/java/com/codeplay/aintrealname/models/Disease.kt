package com.codeplay.aintrealname.models


class Disease(val id: Int, val name: String, val startDate: String, val endDate: String, val isActive: Boolean){
    constructor(): this(0, "Hello", "1", "2", false)
}