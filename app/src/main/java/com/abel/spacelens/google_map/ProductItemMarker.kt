package com.abel.spacelens.google_map

import com.abel.spacelens.model.products.Product
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ProductItemMarker(
    val mPosition: LatLng,
    val name: String,
    val project: Product,
    ) : ClusterItem {
    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String {
        return name
    }

}