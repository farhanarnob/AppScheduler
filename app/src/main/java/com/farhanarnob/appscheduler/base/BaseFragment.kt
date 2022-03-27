package com.farhanarnob.appscheduler.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope


open class BaseFragment:Fragment(){
    companion object{
        private var callback: OnBackPressedCallback? = null
        private var _func : (() -> Unit)? = null
        private var activityCallback: ActivityCallback? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            if(requireActivity() is MainActivity){
                activityCallback = requireActivity() as MainActivity
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            _func?.let {
                backPressListenerSetup(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _func = null
        removeCallBack()
    }

    private fun backPressListenerSetup(func :() -> Unit){
        removeCallBack()
        lifecycleScope.launchWhenResumed {
            callback = requireActivity().onBackPressedDispatcher.addCallback {
                if (callback?.isEnabled == true) {
                    func()
                }
            }
        }
    }

    private fun removeCallBack() {
        callback?.isEnabled = false
        callback?.remove()
        callback = null
    }

    internal fun hideToolbarBackIcon(){
        activityCallback?.toolbarIconVisibility(visibility = false)
    }

    internal fun toolbarSetup(func :() -> Unit, title:Int) {
        _func = func
        backPressListenerSetup(func)
        activityCallback?.setupToolbar(title,func)
    }

}

interface ActivityCallback {
    fun setupToolbar(title: Int,func: ()->Unit)
    fun toolbarIconVisibility(visibility:Boolean)
}

