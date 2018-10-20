package com.codeplay.aintrealname.models

class User(val name: String, val phoneNo: String, val pincode: String, val gender: String,
           val birthDate: String, val height: String, val weight: String, val bloodGroup: String) {
    constructor(): this("", "", "1", "M", "", "0", "0", "")
}