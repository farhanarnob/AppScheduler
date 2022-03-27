package com.farhanarnob.appscheduler.base

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.farhanarnob.appscheduler.R
import com.farhanarnob.appscheduler.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), ActivityCallback {

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
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
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