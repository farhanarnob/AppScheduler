package com.farhanarnob.appscheduler.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.os.Build
import android.os.PowerManager
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.farhanarnob.appscheduler.base.MainActivity

object UIUtility {
    private const val NOTIFICATION_TAG_CALL = "Call"
    private const val NOTIFICATION_TAG_APPLICATION = "Application"


    fun onKeyboardAdjustLayoutToOverflowKeyboard(activity: Activity) {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    fun onKeyboardAdjustLayoutToEnableScroll(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        } else {
            activity.window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }


    /**
     * @param context context of the application
     */
    fun showKeyBoard(context: Context) {
        try {
            val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * @param context        context of the application
     * @param contextualView contextual view of the fragment/activity
     */
    fun hideKeyBoard(context: Context, contextualView: View) {
        try {
            val imm: InputMethodManager = context.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(contextualView.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showFullScreen(activity: Activity) {
        (activity as MainActivity).binding
            .waitingPageLayout.waitingPageMother.visibility = View.VISIBLE
    }

    fun exitFullScreen(activity: Activity) {
        (activity as MainActivity).binding
            .waitingPageLayout.waitingPageMother.visibility = View.GONE
    }


    fun showWaitingScreen(activity: Activity) {
        (activity as MainActivity).binding.waitingPageLayout.waitingPageMother.visibility = View.VISIBLE
    }

    fun exitWaitingScreen(activity: Activity) {
        (activity as MainActivity).binding.waitingPageLayout.waitingPageMother.visibility = View.GONE
    }

    fun makeScreenWakeUp(context: Context) {
        val pm = context.getSystemService(POWER_SERVICE) as PowerManager
        val isScreenOn = pm.isInteractive
        if (!isScreenOn) {
            val wl = pm.newWakeLock(
                PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
                "app:HelloDoc24"
            )
            wl.acquire(10000)
            val wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "app:HelloDoc24")
            wl_cpu.acquire(10000)
        }
    }



    /**
     * Create the required notification channel for O+ devices.
     */
    @TargetApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(
        notificationString: NotificationString,
        notificationManager: NotificationManager,
    ) {
        NotificationChannel(
            notificationString.channelId,
            notificationString.name,
            NotificationManager.IMPORTANCE_HIGH
        ).also { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }


    data class NotificationString(
        val channelId: String,
        val title: String,
        val actionButton: String,
        val name: String,
    )

}