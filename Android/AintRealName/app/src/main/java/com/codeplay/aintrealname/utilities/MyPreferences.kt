package com.codeplay.aintrealname.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object MyPreferences {


    fun defaultPrefs(context: Context): SharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPrefs(context: Context, name: String): SharedPreferences
            = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    operator fun SharedPreferences.set(key: String, value: String) {
        edit { it.putString(key, value) }
    }

    /**
     * finds value on given key.
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    operator fun SharedPreferences.get(key: String, defaultValue: String): String{
        return getString(key, defaultValue)
    }

}