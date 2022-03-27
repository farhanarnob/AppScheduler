package com.farhanarnob.appscheduler.adapter

import android.content.Context
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes


class AppInfoArrayAdapter(
    context: Context, @LayoutRes private val layoutResource: Int,
    val appList: List<ResolveInfo>,
) :
    ArrayAdapter<ResolveInfo>(context, layoutResource, appList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(
        position: Int,
        convertView: View?,
        parent: ViewGroup?,
    ): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(
            layoutResource, parent, false
        ) as TextView
        view.text = appList[position].loadLabel(parent?.rootView?.context?.packageManager)
        return view
    }

}