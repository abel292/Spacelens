package com.abel.spacelens.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.abel.spacelens.model.products.Product
import com.abel.spacelens.repositories.ApiRepository
import com.abel.spacelens.service.ErrorType
import com.abel.spacelens.service.OnResponse
import com.abel.spacelens.service.RemoteErrorEmitter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ApiViewModel(application: Application) :
    BaseViewModel(application), RemoteErrorEmitter {

    private val repository = ApiRepository()
    val allProducts = MutableLiveData<List<Product>>()

    fun getProducts() {
        GlobalScope.launch {
            repository.getProducts(object : OnResponse<Product> {
                override fun onResponse(
                    responseType: OnResponse.ResponseType,
                    object0: Product?,
                    listEntity: List<Product>?
                ) {
                    if (listEntity != null) {
                        allProducts.postValue(listEntity)
                    } else {
                        allProducts.postValue(ArrayList())
                    }
                }

                override fun onError(code: Int, error: String?) {
                }
            })

        }
    }

    override fun onError(msg: String) {
        //por tiempo no pude implementar esto
    }

    override fun onError(errorType: ErrorType) {
        //por tiempo no pude implementar esto
    }
}