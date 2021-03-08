package com.abel.spacelens.view_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.abel.spacelens.R
import com.abel.spacelens.view_model.ApiViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    var drawerLayout: DrawerLayout? = null
    lateinit var navView: NavigationView
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //configuro
        navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation?.setOnNavigationItemSelectedListener(this)
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        // get the reference of the bottomNavigationView and set the visibility.
        bottom_navigation.visibility = visibility
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_product -> {
                navController.navigate(R.id.actionListProductsFragment)
            }
            R.id.menu_mapa -> {
                navController.navigate(R.id.actionMapFragment)
            }
        }
        //close navigation drawer
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

}