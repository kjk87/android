package com.pplus.prnumberuser.apps.page.ui

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.page.data.ZoomablePageImagePagerAdapter
import com.pplus.prnumberuser.core.network.model.dto.PageImage
import com.pplus.prnumberuser.databinding.ActivityIntroduceImageDetailBinding

class IntroduceImageDetailActivity : BaseActivity() {
//    private var mGalleyViewPageAdapter: ZoomablePageAdapter? = null
    private var mGalleyViewPageAdapter: ZoomablePageImagePagerAdapter? = null
    private var mSelectedPosition = 0
//    private var mImageList: List<Attachment>? = null
    private var mImageList: List<PageImage>? = null
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityIntroduceImageDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityIntroduceImageDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        mSelectedPosition = intent.getIntExtra(Const.POSITION, 0)
        mImageList = intent.getParcelableArrayListExtra(Const.DATA)
        binding.imageIntroduceImageDetailClose.setOnClickListener { finish() }


        if (mImageList != null && mImageList!!.isNotEmpty()) {
            binding.textIntroduceImageDetailPaging.text = "${mSelectedPosition + 1}/${mImageList!!.size}"

            mGalleyViewPageAdapter = ZoomablePageImagePagerAdapter(this, mImageList)
            mGalleyViewPageAdapter!!.setOnSingleTapListener { handleOnImageView(null) }
            binding.pagerIntroduceImageDetail.adapter = mGalleyViewPageAdapter
            binding.pagerIntroduceImageDetail.setCurrentItem(mSelectedPosition, false)
            binding.pagerIntroduceImageDetail.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    binding.textIntroduceImageDetailPaging.text = "${position + 1}/${mImageList!!.size}"
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
    }

    fun handleOnImageView(view: View?) {
        if (binding.layoutIntroduceImageTop.visibility == View.VISIBLE) {
            binding.layoutIntroduceImageTop.visibility = View.GONE
        } else {
            binding.layoutIntroduceImageTop.visibility = View.VISIBLE
        }
    }
}