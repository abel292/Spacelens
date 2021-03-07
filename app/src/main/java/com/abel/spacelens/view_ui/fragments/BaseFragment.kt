package com.abel.spacelens.view_ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.abel.spacelens.utils.BasicMethodView


abstract class BaseFragment : Fragment() , BasicMethodView{

    companion object {
        val EXTRAS_VIEW_PRODUCT = "EXTRAS_VIEW_PRODUCT"
    }

    protected val TAG = this.javaClass.simpleName
    protected var mActivity: AppCompatActivity? = null
    protected var mContext: Context? = null

    //views
    protected lateinit var fragmentView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OPEN FRAGMENT $TAG")
        super.onCreate(savedInstanceState)
        mActivity = activity as AppCompatActivity?
    }

    fun View.goTo(action: Int) {
        findNavController().navigate(action)

    }

    //cuando tengamos el entity Remito cambiamos el "string" por el entity (tiene que ser serialisable para pasarlo)
    fun View.goToDetailProduct(action: Int, remito: String) {
        val bundle = Bundle()
        bundle.putSerializable(EXTRAS_VIEW_PRODUCT, remito)
        findNavController().navigate(action, bundle)
    }


}