package com.abel.spacelens.view_ui.fragments.map

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.abel.spacelens.R
import com.abel.spacelens.google_map.CustomRendered
import com.abel.spacelens.google_map.ProductItemMarker
import com.abel.spacelens.model.products.Product
import com.abel.spacelens.view_model.ApiViewModel
import com.abel.spacelens.view_ui.fragments.BaseFragment
import com.abel.spacelens.view_ui.fragments.productList.ProductListFragmentDirections
import com.abel.spacelens.view_ui.fragments.productList.ProductsListAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.launch


class MapFragment : BaseFragment(), OnMapReadyCallback,
    ClusterManager.OnClusterClickListener<ProductItemMarker>,
    ClusterManager.OnClusterInfoWindowClickListener<ProductItemMarker>,
    ClusterManager.OnClusterItemClickListener<ProductItemMarker>,
    ClusterManager.OnClusterItemInfoWindowClickListener<ProductItemMarker> {

    private val viewModel by lazy { ViewModelProviders.of(this).get(ApiViewModel::class.java) }
    private var mMap: GoogleMap? = null
    private lateinit var mClusterManager: ClusterManager<ProductItemMarker>
    private var renderMap: CustomRendered? = null
    private val markersProjects: MutableList<ProductItemMarker> = ArrayList()
    private var loadedMarkers = false
    private var allProducts: List<Product>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_map, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!loadedMarkers) {
            initObservables()
            init()
            initListener()
        }
    }


    override fun init() {

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        if (checkIfLocationOpened()) {
            //initMyCity()
        } else {
            Toast.makeText(
                mActivity,
                getString(R.string.activar_ubicacion),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun initObservables() {

        viewModel.allProducts.observe(viewLifecycleOwner, Observer {
            try {
                if (mMap != null) {
                    if (it != null) {
                        allProducts = it

                        if (it.isNotEmpty() && !loadedMarkers) {
                            populateMap()
                            addItems(it)
                            loadedMarkers = true
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "No se pudo ordenar los modificados primeros")
            }

        })

    }

    override fun initListener() {

    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map
        mMap?.setOnCameraMoveListener {
            if (mMap != null) {
                val zoom: Float? = mMap?.cameraPosition?.zoom
                if (zoom != null) {
                    if (zoom > 5) {
                        renderMap?.setMarkersToCluster(false)
                    } else {
                        renderMap?.setMarkersToCluster(true)
                    }
                }
            }
        }
        getProducts()
    }

    override fun onClusterClick(cluster: Cluster<ProductItemMarker>): Boolean {

        val firstName: String = cluster.items.iterator().next().name
        Toast.makeText(
            mActivity,
            cluster.size.toString() + " (including " + firstName + ")",
            Toast.LENGTH_SHORT
        ).show()

        val builder = LatLngBounds.builder()
        for (item in cluster.items) {
            builder.include(item.position)
        }
        // Get the LatLngBounds
        val bounds = builder.build()

        // Animate camera to the bounds
        try {
            mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<ProductItemMarker?>?) {
        // Does nothing, but you could go to a list of the users.
        Log.e("TAGmapa", "CLICK 3")

    }

    override fun onClusterItemClick(item: ProductItemMarker?): Boolean {
        Log.e("TAGmapa", "CLICK")
        return false
    }

    override fun onClusterItemInfoWindowClick(item: ProductItemMarker?) {
        val direction: NavDirections =
            MapFragmentDirections.actionMapFragmentToDetailProductFragment2(
                item?.project!!
            )
        /*val extras = FragmentNavigatorExtras(
            textView to product.title
        )*/
        findNavController().navigate(direction)

    }

    private fun populateMap() {
        mClusterManager = ClusterManager(mActivity, mMap)
        renderMap = CustomRendered(mActivity, mMap, mClusterManager)
        mClusterManager.renderer = renderMap
        mMap?.setOnCameraIdleListener(mClusterManager)
        mMap?.setOnMarkerClickListener(mClusterManager)
        mMap?.setOnInfoWindowClickListener(mClusterManager)
        mClusterManager.setOnClusterClickListener(this)
        mClusterManager.setOnClusterInfoWindowClickListener(this)
        mClusterManager.setOnClusterItemClickListener(this)
        mClusterManager.setOnClusterItemInfoWindowClickListener(this)
        //allProjects?.let { addItems(it) }
        mClusterManager.cluster()

    }


    //verificamos si la localizacion del telefono esta activado
    private fun checkIfLocationOpened(): Boolean {
        val provider = Settings.Secure.getString(
            mActivity?.contentResolver,
            Settings.Secure.LOCATION_PROVIDERS_ALLOWED
        )
        if (provider.contains("gps") || provider.contains("network")) {
            return true
        }
        return false
    }


    private fun getProducts() {
        viewModel.viewModelScope.launch { viewModel.getProducts() }
    }

    private fun addItems(list: List<Product>) {
        list.forEach { p ->
            val item = p.toMarker()
            mClusterManager.addItem(item)
            markersProjects.add(item)
        }
    }
}
