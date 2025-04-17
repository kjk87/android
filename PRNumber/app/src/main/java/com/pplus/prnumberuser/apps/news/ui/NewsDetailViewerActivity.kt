//package com.pplus.prnumberuser.apps.news.ui
//
//import android.os.Bundle
//import android.view.View
//import androidx.viewpager.widget.ViewPager
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.custom.ImageViewTouchViewPager
//import com.pplus.prnumberuser.apps.news.data.NewsZoomablePagerAdapter
//import com.pplus.prnumberuser.core.network.model.dto.NewsImage
//import kotlinx.android.synthetic.main.activity_photo_detail_viewer.*
//
//class NewsDetailViewerActivity : BaseActivity() {
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_photo_detail_viewer
//    }
//
//    private var mViewPager: ImageViewTouchViewPager? = null
//    private var mPhotoDetailViewPageAdapter: NewsZoomablePagerAdapter? = null
//    private var mSelectedPosition: Int = 0
//    private var newsImageList: ArrayList<NewsImage>? = null
//
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mSelectedPosition = intent.getIntExtra(Const.POSITION, 0)
//
//        newsImageList = intent.getParcelableArrayListExtra(Const.DATA)
//
//        image_photo_detail_close.setOnClickListener { finish() }
//
//        toolbar_title.setSingleLine()
//
//        if (newsImageList != null && newsImageList!!.size > 0) {
//            toolbar_title.text = "${(mSelectedPosition + 1)}/${newsImageList!!.size}"
//
//            mViewPager = findViewById<View>(R.id.pager) as ImageViewTouchViewPager
//            mPhotoDetailViewPageAdapter = NewsZoomablePagerAdapter(this, newsImageList!!)
//            mPhotoDetailViewPageAdapter!!.setOnSingleTapListener(object : NewsZoomablePagerAdapter.onSingleTapListener {
//                override fun onSingleTap() {
//                    handleOnImageView(rl_top)
//                }
//            })
//
//            mViewPager!!.adapter = mPhotoDetailViewPageAdapter
//            mViewPager!!.setCurrentItem(mSelectedPosition, false)
//
//            mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                }
//
//                override fun onPageSelected(position: Int) {
//
//                    toolbar_title.text = "${(position + 1)}/${newsImageList!!.size}"
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {
//
//                }
//            })
//
//        }
//    }
//
//    fun handleOnImageView(view: View) {
//        if (view.visibility == View.VISIBLE) {
//            view.visibility = View.GONE
//        } else {
//            view.visibility = View.VISIBLE
//        }
//    }
//}
