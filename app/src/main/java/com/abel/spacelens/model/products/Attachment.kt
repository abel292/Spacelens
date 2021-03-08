package com.abel.spacelens.model.products

import java.io.Serializable

data class Attachment(
    val thumbnail: String,
    val type: String,
    val url: String
) : Serializable