package com.abel.spacelens.service.network

import com.abel.spacelens.model.products.ResponsetApi
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

interface SpacelensApi {

    @GET("test/products_list.php?latitude=1&longitude=1")
    suspend fun getProducts(): Response<ResponsetApi>
}