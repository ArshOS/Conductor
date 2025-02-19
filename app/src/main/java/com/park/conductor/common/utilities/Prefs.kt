package com.park.conductor.common.utilities

import android.content.SharedPreferences
import android.util.Log
import com.park.conductor.data.remote.dto.LoginResponse
import com.google.gson.Gson

object Prefs {

    val PREFS_NAME : String = "prefs"
    private const val PREFS_FILENAME = "com.park.conductor"
    private const val LOGIN_KEY = "login_key"

    private val GSON = Gson()
    val prefs: SharedPreferences = App.getINSTANCE()?.getSharedPreferences(PREFS_FILENAME, 0)!!

    fun saveLogin(resp: LoginResponse){
        Log.d("PREF1: ", resp.toString())
        putObject(LOGIN_KEY, resp)
    }

    fun clear() =  prefs.edit().clear().apply()
    fun getLogin() = getObject(LOGIN_KEY, LoginResponse::class.java)

    private fun putObject(key: String?, `object`: Any?) {
        requireNotNull(`object`) { "Object is null" }
        require(!(key == null || key == "")) { "Key is empty or null" }
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(
            key,
            GSON.toJson(`object`)
        )
        editor.apply()
    }

    fun <T> getObject(key: String, a: Class<T>?): T? {
        val gson: String? = prefs.getString(key, null)
        return if (gson == null) {
            null
        } else {
            try {
                GSON.fromJson(
                    gson,
                    a
                )
            } catch (e: Exception) {
                throw IllegalArgumentException(
                    "Object stored with key "
                            + key + " is instance of other class"
                )
            }
        }
    }
}
