package com.abel.spacelens.view_ui.fragments.productList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abel.spacelens.R
import com.abel.spacelens.model.products.Product
import com.abel.spacelens.utils.OnLoadMoreListener
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList


class ProductsListAdapter(
    private val list: ArrayList<Product?>,
    recyclerView: RecyclerView,
    val onClickListener: OnClickListener?,
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0

    // La cantidad mínima de elementos que debe tener debajo de su posición de desplazamiento actual antes de cargar mas
    private val visibleThreshold = 5
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    private var loading = false
    private var onLoadMoreListener: OnLoadMoreListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (list[position] != null) VIEW_ITEM else VIEW_PROG
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        vh = if (viewType == VIEW_ITEM) {
            val v: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_product, parent, false
            )
            ProductViewHolder(v)
        } else {
            val v: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_loading_recycler, parent, false
            )
            ProgressViewHolder(v)
        }
        return vh
    }

    fun setLoaded() {
        loading = false
    }

    /*fun setItemOnClickListener(onClickListener: OnListenerItemRecyclerView<String>) {
        this.onClickListener = onClickListener
    }*/

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener?) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    //
    //todo Holder


    class ProductViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textViewProductTitle: TextView = v.findViewById(R.id.textViewNameProduct)
        var imageViewProduct: ImageView = v.findViewById(R.id.imageViewProduct)
        var enabled: Boolean = true
        fun bind(
            product: Product?,
            onClickListener: OnClickListener?) {
            textViewProductTitle.text = product?.title
            textViewProductTitle.transitionName = product?.title

            Glide.with(itemView.context)
                .load(product?.attachment?.thumbnail)
                .placeholder(R.drawable.gradient_banner)
                .error(R.drawable.gradient_banner)
                .override(200, 200)
                .centerCrop()
                .into(imageViewProduct)

            itemView.setOnClickListener {
                if (product != null) {
                    onClickListener?.onClick(product, textViewProductTitle)
                }
            }
        }

    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progressBar: LottieAnimationView =
            v.findViewById<View>(R.id.progressBarAnimated) as LottieAnimationView
    }

    init {

        if (recyclerView.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = recyclerView
                .layoutManager as LinearLayoutManager?
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int, dy: Int,
                ) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = linearLayoutManager!!.itemCount
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                    if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold
                    ) {

                        if (onLoadMoreListener != null) {
                            onLoadMoreListener!!.onLoadMore()
                        }
                        loading = true
                    }
                }
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder) {
            val product = list[position]
            holder.bind(product, onClickListener)
        }
    }

    class OnClickListener(val clickListener: (Product, TextView) -> Unit) {
        fun onClick(
            product: Product,
            title: TextView,
        ) = clickListener(product, title)
    }
}