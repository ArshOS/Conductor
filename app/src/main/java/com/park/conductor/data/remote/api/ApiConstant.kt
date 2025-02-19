package com.park.conductor.data.remote.api

import com.park.conductor.common.utilities.Prefs

object ApiConstant {

    const val USERNAME = "userName"
    const val USERID = "userId"
    const val MOBILE = "mobile"
    const val USERTYPE = "userType"
    const val OS_TYPE = "os_type"

    fun getBaseParam(): HashMap<String, Any> {
        val param  = HashMap<String,Any>()
        param[USERNAME] = Prefs.getLogin()?.userInfo?.userName ?:""
        param[USERID] = Prefs.getLogin()?.userInfo?.userId ?:""
        param[USERTYPE] = Prefs.getLogin()?.userInfo?.userType ?:""
        param[OS_TYPE] = "1"

        return param
    }
}
