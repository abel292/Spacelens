package com.abel.spacelens.model.products

import com.abel.spacelens.google_map.ProductItemMarker
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class Product(
    val address: String,
    val attachment: Attachment,
    val category: String,
    val created: String,
    val createdTime: String,
    val currency: String,
    val description: String,
    val distance: Int,
    val gallery: List<Gallery>,
    val is_private: String,
    val like_user: Boolean,
    val likes: Int,
    val location: Location,
    val offer: Boolean,
    val owner: String,
    val p_condition: String,
    val payment_method: String,
    val price: Int,
    val product_id: Int,
    val rating_amount: Int,
    val rating_product: Int,
    val story_img: Any,
    val story_url: Any,
    val title: String
) : Serializable {

    fun toMarker(): ProductItemMarker {
        return ProductItemMarker(LatLng(location.latitude, location.longitude), title, this)
    }
}