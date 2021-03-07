package com.abel.spacelens.view_ui.fragments.productList

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.abel.spacelens.R
import com.abel.spacelens.model.products.Product
import com.abel.spacelens.utils.OnLoadMoreListener
import com.abel.spacelens.view_model.ApiViewModel
import com.abel.spacelens.view_ui.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class ProductListFragment : BaseFragment() {

    val viewModel by lazy { ViewModelProviders.of(this).get(ApiViewModel::class.java) }

    private var mAdapter: ProductsListAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var loadedList: Boolean = false

    private lateinit var productsLoadeds: ArrayList<Product?>
    private lateinit var allProducts: ArrayList<Product?>


    private var handler: Handler? = null
    private var attempts = 0

    private val more = 20
    private val cantFirstLoad = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservables()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        fragmentView = inflater.inflate(R.layout.fragment_product_list, container, false)
        sharedElementReturnTransition =
            TransitionInflater.from(mContext).inflateTransition(android.R.transition.move)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initListener()
    }

    override fun init() {
        getProducts()
        if (loadedList) {
            loadRecyclerView()
        } else {
            //mostramos "cargando"
            a_progress_loading.visibility = View.VISIBLE
            a_progress_loading.playAnimation()
        }

    }

    override fun initObservables() {

        viewModel.allProducts.observe(this, {

            if (!loadedList) {
                productsLoadeds = ArrayList()
                allProducts = ArrayList()
                it.forEach { product ->
                    allProducts.add(product)
                }
                allProducts
                handler = Handler()
                loadDataFirst()
                loadRecyclerView()
            }

            a_progress_loading.pauseAnimation()
            a_progress_loading.progress = 0f
            a_progress_loading.visibility = View.GONE


        })

    }

    override fun initListener() {}

    private fun getProducts() {
        viewModel.viewModelScope.launch { viewModel.getProducts() }
    }

    private fun loadRecyclerView() {
        Log.e("carga recycler", "infinito")
        recyclerViewProduct.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(mContext)
        recyclerViewProduct.layoutManager = mLayoutManager
        mAdapter =
            ProductsListAdapter(
                productsLoadeds,
                recyclerViewProduct, productoItemListener
            )
        //mAdapter?.setItemOnClickListener(this)
        recyclerViewProduct.adapter = mAdapter

        // Cuando el usuario pulsa el botón Atrás, la transición se realiza hacia atrás.
        postponeEnterTransition()
        recyclerViewProduct.doOnPreDraw {
            startPostponedEnterTransition()
        }

        if (productsLoadeds.isEmpty()) {
            recyclerViewProduct.visibility = View.GONE
            textViewWithoutProducts.visibility = View.VISIBLE

            //probamos nuevamente por si las dudas
            if (attempts < 2) {
                Handler().postDelayed({ getProducts() }, 560L)
                attempts++
            }

        } else {
            loadedList = true
            recyclerViewProduct.visibility = View.VISIBLE
            textViewWithoutProducts.visibility = View.GONE
        }

        mAdapter?.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                productsLoadeds.add(null)
                mAdapter?.notifyItemInserted(productsLoadeds.size - 1)
                handler?.postDelayed(Runnable {
                    //   remove progress item
                    productsLoadeds.removeAt(productsLoadeds.size - 1)
                    mAdapter?.notifyItemRemoved(productsLoadeds.size)

                    //add items one by one
                    val start: Int = productsLoadeds.size
                    var end = start + more

                    end = if (end > allProducts.size) {
                        allProducts.size - 1
                    } else {
                        start + more
                    }

                    for (i in start + 1..end) {
                        if (allProducts.size > i) {
                            productsLoadeds.add(allProducts[i])
                            mAdapter?.notifyItemInserted(productsLoadeds.size)
                        }
                    }
                    if (allProducts.size > mAdapter?.itemCount ?: 0) {
                        mAdapter?.setLoaded()
                    }
                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                }, 2000)
            }
        })
    }

    // load initial data
    private fun loadDataFirst() {
        //Cargamos los primero remitos y si hay menos que "cantFirstLoad" cargamos todos
        if (allProducts.size <= cantFirstLoad) {
            productsLoadeds.addAll(allProducts)
        } else {
            for (i in 0..cantFirstLoad) {
                productsLoadeds.add(allProducts[i])
            }
        }
    }

    private val productoItemListener = ProductsListAdapter.OnClickListener { product, textView ->
        val direction: NavDirections =
            ProductListFragmentDirections.actionProductListFragmentToDetailProductFragment(product)

        val extras = FragmentNavigatorExtras(
            textView to product.title
        )

        findNavController().navigate(direction, extras)
    }

}