package com.farhanarnob.appscheduler.model

import android.content.Context
import android.content.pm.ResolveInfo

data class PackageInfo(
    val appName:String,
    val packageName:String,
    val name:String,
)

fun ResolveInfo.asPackage(context: Context): PackageInfo{
    return PackageInfo(
        this.loadLabel(context.packageManager).toString(),
        this.activityInfo.packageName,
        this.activityInfo.name
    )
}
