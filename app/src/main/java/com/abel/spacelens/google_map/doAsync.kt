package com.abel.spacelens.google_map

import android.graphics.Bitmap
import android.os.AsyncTask
import android.graphics.BitmapFactory
import java.lang.Exception
import java.net.URL

class doAsync(var urlProduct: String, val handler: (result: Bitmap) -> Unit) :
    AsyncTask<Void, Void, Void>() {

    var resultBitmap: Bitmap? = null
    override fun doInBackground(vararg params: Void?): Void? {

        val url: URL
        try {
            url = URL(urlProduct)
            val string=url.openConnection().getInputStream()
            resultBitmap = BitmapFactory.decodeStream(string)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
        resultBitmap?.let { handler(it) }
        super.onPostExecute(result)

    }
}