package com.pplus.prnumberbiz.apps.keyword.ui


import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.View
import android.widget.LinearLayout
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.main.data.MainPagerAdapter
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.fragment_keyword.*

class KeywordFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            mTab = it.getString(Const.TAB)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_keyword
    }

    override fun initializeView(container: View?) {

    }

    override fun init() {
        pager_keyword.visibility = View.VISIBLE
        val pagerAdapter = MainPagerAdapter(activity!!)
        pagerAdapter.imageRes = intArrayOf(R.drawable.img_keyword_banner_1, R.drawable.img_keyword_banner_2, R.drawable.img_keyword_banner_3)
        pager_keyword.adapter = pagerAdapter

        indicator_keyword.setImageResId(R.drawable.indi_keyword)
        indicator_keyword.visibility = View.VISIBLE
        indicator_keyword.removeAllViews()
        indicator_keyword.build(LinearLayout.HORIZONTAL, pagerAdapter.count)
        pager_keyword.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (!isAdded) {
                    return
                }
                indicator_keyword.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        text_keyword_config.text = PplusCommonUtil.fromHtml(getString(R.string.html_set_keyword))

        layout_keyword_config.setOnClickListener {
            val intent = Intent(activity, KeywordConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_DETAIL)

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
        }
    }

    override fun getPID(): String {
        return "Main_search"
    }

    companion object {


        @JvmStatic
        fun newInstance() =
                KeywordFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(Const.TAB, tab.name)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
