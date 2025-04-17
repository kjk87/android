package com.pplus.prnumberbiz.apps.goods.ui

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.View
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.custom.ImageViewTouchViewPager
import com.pplus.prnumberbiz.apps.goods.data.GoodsZoomablePagerAdapter
import kotlinx.android.synthetic.main.activity_photo_detail_viewer.*

class GoodsDetailViewerActivity : BaseActivity() {

    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_photo_detail_viewer
    }

    private var mViewPager: ImageViewTouchViewPager? = null
    private var mPhotoDetailViewPageAdapter: GoodsZoomablePagerAdapter? = null
    private var mSelectedPosition: Int = 0
    private var photoList: ArrayList<String>? = null


    override fun initializeView(savedInstanceState: Bundle?) {
        mSelectedPosition = intent.getIntExtra(Const.POSITION, 0)

        photoList = intent.getStringArrayListExtra(Const.DATA)

        image_photo_detail_close.setOnClickListener { finish() }

        toolbar_title.setSingleLine()

        if (photoList != null && photoList!!.size > 0) {
            toolbar_title.text = "${(mSelectedPosition + 1)}/${photoList!!.size}"

            mViewPager = findViewById<View>(R.id.pager) as ImageViewTouchViewPager
            mPhotoDetailViewPageAdapter = GoodsZoomablePagerAdapter(this, photoList!!)
            mPhotoDetailViewPageAdapter!!.setOnSingleTapListener(object : GoodsZoomablePagerAdapter.onSingleTapListener {
                override fun onSingleTap() {
                    handleOnImageView(rl_top)
                }
            })

            mViewPager!!.adapter = mPhotoDetailViewPageAdapter
            mViewPager!!.setCurrentItem(mSelectedPosition, false)

            mViewPager!!.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {

                    toolbar_title.text = "${(position + 1)}/${photoList!!.size}"
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
