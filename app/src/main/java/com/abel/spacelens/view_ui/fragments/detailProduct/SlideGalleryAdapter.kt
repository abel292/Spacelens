package com.abel.spacelens.view_ui.fragments.detailProduct

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.abel.spacelens.R
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlin.collections.ArrayList

class SlideGalleryAdapter(private val context: Context) :
    SliderViewAdapter<SlideGalleryAdapter.SliderAdapterVH>() {
    private var mSliderItems: MutableList<String> = ArrayList()
    private lateinit var onClickListenerPhoto: OnClickListenerPhoto

    fun renewItems(sliderItems: MutableList<String>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun setListener(onClickListenerPhoto: OnClickListenerPhoto) {
        this.onClickListenerPhoto = onClickListenerPhoto
    }
    fun getItems():ArrayList<String>{
        val photos= ArrayList<String>()
        photos.addAll(mSliderItems)
        return photos
    }
    fun addItem(sliderItem: String) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem = mSliderItems[position]

        Glide.with(context)
            .load(sliderItem)
            .fitCenter()
            .into(viewHolder.imageViewBackground)

        viewHolder.imageViewBackground.setOnClickListener {
            onClickListenerPhoto.onClickFullScreen(position)
        }
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    inner class SliderAdapterVH(
        itemView: View,
    ) : ViewHolder(itemView) {
        var imageViewBackground: ImageView = itemView.findViewById(R.id.imageViewSlide)
    }
}