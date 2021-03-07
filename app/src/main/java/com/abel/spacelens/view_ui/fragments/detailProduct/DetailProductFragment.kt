package com.abel.spacelens.view_ui.fragments.detailProduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionInflater
import com.abel.spacelens.R
import com.abel.spacelens.model.products.Product
import com.abel.spacelens.view_ui.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_detail_product.*
import java.util.concurrent.TimeUnit

class DetailProductFragment : BaseFragment() {

    lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = requireArguments()
        product = DetailProductFragmentArgs.fromBundle(args).ArgProductDetail
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_detail_product, container, false)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservables()
        init()
        initListener()
    }

    override fun init() {
        val titleRemittances = product.title
        textViewTitleDetail.text = titleRemittances
        textViewTitleDetail.transitionName = titleRemittances
    }

    override fun initObservables() {
    }

    override fun initListener() {
    }
}