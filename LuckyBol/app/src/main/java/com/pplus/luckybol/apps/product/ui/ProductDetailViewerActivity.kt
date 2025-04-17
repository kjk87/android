package com.pplus.luckybol.apps.product.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.custom.ImageViewTouchViewPager
import com.pplus.luckybol.apps.product.data.ProductZoomablePagerAdapter
import com.pplus.luckybol.core.network.model.dto.ProductImage
import com.pplus.luckybol.databinding.ActivityPhotoDetailViewerBinding

class ProductDetailViewerActivity : BaseActivity() {

    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPhotoDetailViewerBinding

    override fun getLayoutView(): View {
        binding = ActivityPhotoDetailViewerBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mViewPager: ImageViewTouchViewPager? = null
    private var mPhotoDetailViewPageAdapter: ProductZoomablePagerAdapter? = null
    private var mSelectedPosition: Int = 0
    private var imageList: ArrayList<ProductImage>? = null


    override fun initializeView(savedInstanceState: Bundle?) {
        mSelectedPosition = intent.getIntExtra(Const.POSITION, 0)

        imageList = intent.getParcelableArrayListExtra(Const.DATA)

        binding.imagePhotoDetailClose.setOnClickListener { finish() }

        binding.toolbarTitle.setSingleLine()

        if (imageList != null && imageList!!.size > 0) {
            binding.toolbarTitle.text = "${(mSelectedPosition + 1)}/${imageList!!.size}"

            mViewPager = findViewById<View>(R.id.pager) as ImageViewTouchViewPager
            mPhotoDetailViewPageAdapter = ProductZoomablePagerAdapter(this, imageList!!)
            mPhotoDetailViewPageAdapter!!.setOnSingleTapListener(object : ProductZoomablePagerAdapter.onSingleTapListener {
                override fun onSingleTap() {
                    handleOnImageView(binding.rlTop)
                }
            })

            mViewPager!!.adapter = mPhotoDetailViewPageAdapter
            mViewPager!!.setCurrentItem(mSelectedPosition, false)

            mViewPager!!.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {

                    binding.toolbarTitle.text = "${(position + 1)}/${imageList!!.size}"
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })

        }
    }

    fun handleOnImageView(view: View) {
        if (view.visibility == View.VISIBLE) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }
}
