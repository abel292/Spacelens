package com.abel.spacelens.view_ui.fragments.detailProduct

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionInflater
import com.abel.spacelens.R
import com.abel.spacelens.google_map.geolocalizacion.GeocoderUtil
import com.abel.spacelens.model.products.Product
import com.abel.spacelens.view_ui.fragments.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.tntkhang.fullscreenimageview.library.FullScreenImageViewActivity
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_detail_product.*
import java.util.concurrent.TimeUnit


class DetailProductFragment : BaseFragment(), OnClickListenerPhoto {

    lateinit var product: Product
    lateinit var mAdapterSlide: SlideGalleryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = requireArguments()
        product = DetailProductFragmentArgs.fromBundle(args).ArgProductDetail
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
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
        populateView()
        populateViewerImage()
    }

    override fun initObservables() {
    }

    override fun initListener() {
    }

    private fun populateViewerImage() {
        mAdapterSlide = SlideGalleryAdapter(mContext!!)
        mAdapterSlide.setListener(this)

        product.gallery.forEach {
            val imageUrl = it.src
            mAdapterSlide.addItem(imageUrl)
        }
        slidesImageView.setSliderAdapter(mAdapterSlide)
        slidesImageView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        slidesImageView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        slidesImageView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        slidesImageView.indicatorSelectedColor = Color.WHITE
        slidesImageView.indicatorUnselectedColor = Color.GRAY
        slidesImageView.scrollTimeInSec = 10 //cantidad de segundos para el auto scroll
        slidesImageView.startAutoCycle()
    }

    override fun onClickFullScreen(pos: Int) {
        showFullScreen(mAdapterSlide.getItems(), pos)

    }

    private fun showFullScreen(uriString: ArrayList<String>, pos: Int) {
        val fullImageIntent = Intent(mActivity, FullScreenImageViewActivity::class.java)
        fullImageIntent.putExtra(FullScreenImageViewActivity.URI_LIST_DATA, uriString)
        fullImageIntent.putExtra(FullScreenImageViewActivity.IMAGE_FULL_SCREEN_CURRENT_POS, pos)
        startActivity(fullImageIntent)
    }

    private fun populateView() {
        val titleRemittances = product.title
        textViewTitleDetail.text = titleRemittances
        textViewTitleDetail.transitionName = titleRemittances
        textViewPrice.text = product.price.toString()
        textViewCurrency.text = product.currency
        textViewCategory.text = product.category
        textViewCondition.text = product.p_condition
        textViewPayMethod.text = product.payment_method
        textViewDescripcion.text = product.description

        val addressName = mContext?.let {
            GeocoderUtil(it).getCityName(
                LatLng(
                    product.location.latitude,
                    product.location.longitude
                )
            )
        }

        textViewUbicacion.text = addressName ?: ""
    }
}