package com.farhanarnob.appscheduler.base

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.clear_all_schedule -> {
                val mainIntent = Intent(Intent.ACTION_MAIN, null)
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                val pkgAppsList: List<ResolveInfo> =
                    this.packageManager.queryIntentActivities(mainIntent, 0)
                android.app.AlertDialog
                    .Builder(this)
                    .setMessage(pkgAppsList.toString())
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ -> }
                    .setNegativeButton("No") { _, _ -> }
                    .create().show()
                true
            }
            else -> super.onOptionsItemSelected(item)
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