package com.pxinxs.paintview.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 *Created by AndroidDev on 04.01.2018.
 */
class Permissions {

    fun isStoragePermissions(activity: Activity, WRITE_REQUEST: Int): Boolean {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_REQUEST)
                false
            } else {
                true
            }
        } else {    //for Android lover then 6.0
            true
        }
    }
}