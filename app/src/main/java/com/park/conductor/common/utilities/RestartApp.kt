package com.park.conductor.common.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent

fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    if (context is Activity) {
        context.finish()
    }
    Runtime.getRuntime().exit(0) // Optional, force process restart
}
