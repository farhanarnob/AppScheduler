package com.farhanarnob.appscheduler.base

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.farhanarnob.appscheduler.R
import com.farhanarnob.appscheduler.databinding.ActivityMainBinding
import com.farhanarnob.appscheduler.util.UIUtility
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), ActivityCallback {

    private var navigatetoFragment: ((id: Int) -> Unit)? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fabAdd.setOnClickListener { view ->
            navigatetoFragment?.let {
                UIUtility.addButtonVisibility(this@MainActivity,View.GONE)
                it(R.id.CreateOrUpdateScheduleFragment)
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun navigateTo(func: (id: Int) -> Unit) {
        lifecycleScope.launchWhenResumed {
            UIUtility.addButtonVisibility(this@MainActivity,View.VISIBLE)
            navigatetoFragment = func
        }
    }

    override fun setupToolbar(title: Int, func: () -> Unit) {
        binding.toolbar.title = getString(title)
        binding.toolbar.setNavigationOnClickListener {
            func()
        }
    }

    override fun toolbarIconVisibility(visibility: Boolean) {
        binding.toolbar.navigationIcon?.setVisible(visibility,false)
    }
}