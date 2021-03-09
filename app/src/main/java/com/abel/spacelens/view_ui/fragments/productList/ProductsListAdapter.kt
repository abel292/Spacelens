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
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        var textViewProductTitle: TextView = v.findViewById(R.id.textViewTitleItem)
        var textViewProductPrice: TextView = v.findViewById(R.id.textViewNamePrice)
        var textViewProductCategory: TextView = v.findViewById(R.id.textViewCategoryItem)
        var imageViewProduct: ImageView = v.findViewById(R.id.imageViewProduct)
        var floatingActionButtonLike: FloatingActionButton =
            v.findViewById(R.id.floatingActionButtonLike)
        var like: Boolean = true
        fun bind(
            product: Product?,
            onClickListener: OnClickListener?
        ) {
            textViewProductTitle.text = product?.title
            textViewProductTitle.transitionName = product?.title
            textViewProductCategory.text = product?.category
            textViewProductPrice.text = product?.price?.toString() + " " + product?.currency ?: ""

            Glide.with(itemView.context)
                .load(product?.attachment?.thumbnail)
                .placeholder(R.drawable.ic_avatar_default_product)
                .error(R.drawable.ic_avatar_default_product)
                .override(200, 200)
                .centerCrop()
                .into(imageViewProduct)

            itemView.setOnClickListener {
                if (product != null) {
                    onClickListener?.onClick(product, textViewProductTitle)
                }
            }
            when (product?.like_user) {
                true -> {
                    floatingActionButtonLike.setImageResource(R.drawable.ic_like)
                }
                false -> {
                    floatingActionButtonLike.setImageResource(R.drawable.ic_not_like)
                }
                else -> {
                    floatingActionButtonLike.setImageResource(R.drawable.ic_not_like)

                }
            }

            floatingActionButtonLike.setOnClickListener {
                if (floatingActionButtonLike.tag == "false") {
                    floatingActionButtonLike.tag = "true"
                    floatingActionButtonLike.setImageResource(R.drawable.ic_like)
                } else {
                    floatingActionButtonLike.tag = "false"
                    floatingActionButtonLike.setImageResource(R.drawable.ic_not_like)
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