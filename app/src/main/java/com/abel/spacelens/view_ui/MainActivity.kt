package com.abel.spacelens.view_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.abel.spacelens.R
import com.abel.spacelens.google_map.geolocalizacion.GeocoderUtil
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_ubic.*

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
        populateToolbarDefault()
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        bottom_navigation.visibility = visibility
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_product -> {
                if (!item.isChecked) {
                    navController.navigate(R.id.actionListProductsFragment)
                }
            }
            R.id.menu_mapa -> {
                if (!item.isChecked) {
                    navController.navigate(R.id.actionMapFragment)
                }
            }
        }
        //close navigation drawer
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    fun populateToolbarDefault() {
        try {
            val geocoderUtil = GeocoderUtil(this)
            geocoderUtil.getLastKnownLocation {
                val address = geocoderUtil.getCityName(it)
                textViewToolbar?.text = address
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    fun populateToolbarSearched(latLng: LatLng) {
        try {
            val geocoderUtil = GeocoderUtil(this)
            val address = geocoderUtil.getCityName(latLng)
            textViewToolbar?.text = address
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

}