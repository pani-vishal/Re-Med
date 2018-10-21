package com.codeplay.aintrealname.utilities

object Constants {
    var token: String = ""

    const val MY_SHARED_PREFERENCE = "mypreference"

    const val AUTHORIZATION_KEY = "Authorization"
    const val TOKEN_STRING = "Token "
    const val TOKEN_DEFAULT = "000000"
    const val KEY_TOKEN = "token"

    const val NAME_KEY = "name"
    const val GENDER_KEY = "gender"
    const val BLOOD_GROUP_KEY = "bloodGroup"
    const val DATE_OF_BIRTH_KEY = "dob"
    const val MOBILE_NO_KEY = "phoneNumber"
    const val HEIGHT_KEY = "height"
    const val WEIGHT_KEY = "weight"
    const val PINCODE_KEY = "pincode"


    val BASE_URL = "https://3795a609.ngrok.io"

    val SIGNUP_URL = BASE_URL + "/auth/registration/"
    val LOGIN_URL = BASE_URL + "/auth/login/"
    val USER_DETAILS_URL = BASE_URL + "/user_details/"
    val DISEASE_URL = BASE_URL + "/prognosis_list/"
    val DISEASE_ADD = BASE_URL + "/prognosis_disease_add/"
    val MEDICINE_ADD = BASE_URL + "/prognosis_medicine_add/"
}