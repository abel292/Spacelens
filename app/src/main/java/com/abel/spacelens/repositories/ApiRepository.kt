package com.abel.spacelens.repositories

import com.abel.spacelens.model.products.Product
import com.abel.spacelens.service.OnResponse
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response


class ApiRepository() : BaseRemoteRepository() {

    suspend fun getProducts(
        onResponse: OnResponse<Product>
    ) {
        val result = getResult { spacelensApi.getProducts() }
        onResponse.onResponse(OnResponse.ResponseType.OK, null, result?.products)
    }


}