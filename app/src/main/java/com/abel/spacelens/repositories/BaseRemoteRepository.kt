package com.abel.spacelens.repositories

import android.util.Log
import com.abel.spacelens.BuildConfig
import com.abel.spacelens.service.network.SpacelensApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


abstract class BaseRemoteRepository {
    companion object {
        private const val TAG = "BaseRemoteRepository"
        private const val MESSAGE_KEY = "message"
        private const val ERROR_KEY = "error"

        val client = OkHttpClient.Builder()
            .addInterceptor(
                BasicAuthInterceptor(
                    BuildConfig.API_KEY_USER,
                    BuildConfig.API_KEY_PASS
                )
            )
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create();

        val spacelensApi: SpacelensApi = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(SpacelensApi::class.java)
    }


    suspend fun <T> getResult(call: suspend () -> Response<T>): T? {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val gson = Gson()
                    val jsonStr = gson.toJson(body)
                    Log.d("Response ", jsonStr)
                    return body

                }

            }
        } catch (e: Exception) {
            Log.e(ERROR_KEY, e.message.toString())
        }
        return null
    }

    fun <T> error(message: String) {
        Log.e("lista", message.toString())

    }

    class BasicAuthInterceptor(username: String, password: String) : Interceptor {
        private var credentials: String = Credentials.basic(username, password)

        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            var request = chain.request()
            request = request.newBuilder().header("Authorization", credentials).build()
            return chain.proceed(request)
        }
    }
}