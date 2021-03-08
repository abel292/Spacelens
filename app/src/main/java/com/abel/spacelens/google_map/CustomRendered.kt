package com.abel.spacelens.google_map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.abel.spacelens.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import java.net.URL


internal class CustomRendered(
    var context: Context?, map: GoogleMap?,
    clusterManager: ClusterManager<ProductItemMarker>
) : DefaultClusterRenderer<ProductItemMarker>(context, map, clusterManager) {

    private var shouldCluster = true
    private var mImageView: ImageView = ImageView(context)
    private val mIconGenerator = IconGenerator(context)


    override fun getDescriptorForCluster(cluster: Cluster<ProductItemMarker>?): BitmapDescriptor {
        mIconGenerator.setBackground(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.ic_cart
            )
        })
        val icon: Bitmap = mIconGenerator.makeIcon(cluster?.size.toString())
        return BitmapDescriptorFactory.fromBitmap(icon)

    }

    override fun onBeforeClusterItemRendered(
        item: ProductItemMarker,
        markerOptions: MarkerOptions
    ) {
        /*mImageView.setImageResource(R.drawable.ic_avatar_default_product)
        val icon = mIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
            .title(item.name)
        mIconGenerator.setBackground(null)
        mIconGenerator.setContentView(mImageView)*/
        markerOptions.snippet(item.snippet)
        markerOptions.title(item.title)
        super.onBeforeClusterItemRendered(item, markerOptions)
    }

    override fun onClusterItemRendered(clusterItem: ProductItemMarker?, marker: Marker?) {
        super.onClusterItemRendered(clusterItem, marker)
        try {
            val url = URL(clusterItem?.project?.attachment?.thumbnail)
            Glide.with(mImageView.context)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.ic_avatar_default_product)
                .error(R.drawable.ic_avatar_default_product)
                .override(100, 100)
                .centerCrop()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        marker?.setIcon(BitmapDescriptorFactory.fromBitmap(resource))
                    }

                })

        } catch (ex: Exception) {
            Log.e("map", ex.toString())
        }
    }

    override fun shouldRenderAsCluster(cluster: Cluster<ProductItemMarker>): Boolean {
        return if (shouldCluster) {
            cluster.size > 1
        } else {
            shouldCluster
        }
    }

    fun setMarkersToCluster(toCluster: Boolean) {
        this.shouldCluster = toCluster
    }

}