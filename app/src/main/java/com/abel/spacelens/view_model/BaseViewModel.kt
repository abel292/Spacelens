package com.abel.spacelens.view_model

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    var ERROR_SOCKET_CLOSED =
        "socket closed" //este error tambien ocurre cuando el usuario cancela una solicitud de alguna api

    var loading = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()
    var liveViewEncima = MutableLiveData<View?>()

    fun setViewEncima(view: View?) {
        if (view?.visibility == View.VISIBLE) {
            liveViewEncima.postValue(view)
        } else {
            liveViewEncima.postValue(view)
        }
    }

    /*
    open fun setLoading(loading: String) {
        this.loading.postValue(loading)
    }*/

    /*open fun getError(): MutableLiveData<String>? {
        return error
    }

    open fun setError(msgError: String) {
        if (msgError.trim { it <= ' ' }.toLowerCase() == ERROR_SOCKET_CLOSED) return
        error.postValue(msgError)
    }*/

    open fun cancelRequestData() {}

}