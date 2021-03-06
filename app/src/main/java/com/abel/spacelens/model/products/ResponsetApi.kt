package com.abel.spacelens.model.products

data class ResultApi(
    val code: String,
    val message_error: String,
    val products: List<Product>
)