package com.abel.spacelens.model.products

data class ResponsetApi(
    val code: String,
    val message_error: String,
    val products: List<Product>
)