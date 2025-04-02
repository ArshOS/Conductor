package com.park.conductor.data.remote.api

import com.park.conductor.common.utilities.Prefs

object ApiConstant {

    const val USERNAME = "userName"
    const val USERID = "userid"
    const val MOBILE = "mobile"
    const val USERTYPE = "userType"
    const val OS_TYPE = "os_type"
    const val PLACE_ID = "placeid"

    fun getBaseParam(): HashMap<String, Any> {
        val param  = HashMap<String,Any>()
        param[USERNAME] = Prefs.getLogin()?.userInfo?.userName ?:""
        param[USERID] = Prefs.getLogin()?.userInfo?.userId?.toInt() ?: 0
        param[USERTYPE] = Prefs.getLogin()?.userInfo?.userType ?:""
        param[OS_TYPE] = "1"
        param[PLACE_ID] = Prefs.getLogin()?.userInfo?.parkId?.toInt() ?: 0


        return param
    }
}
