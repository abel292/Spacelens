package com.abel.spacelens.model.products

import java.io.Serializable

data class Location(
    val latitude: Double,
    val longitude: Double
) : Serializable