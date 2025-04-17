package com.pplus.prnumberbiz.apps.pages.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.marketing.ui.SnsSyncActivity
import com.pplus.prnumberbiz.apps.pages.data.KeywordAdapter
import com.pplus.prnumberbiz.apps.pages.data.ListMarginDecoration
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_page_link.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.ArrayList
import java.util.regex.Pattern

class PageLinkActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_page_link
    }

    var mPage = LoginInfoManager.getInstance().user.page!!
    var isLink = false
    var mIsEditing = false

    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = LoginInfoManager.getInstance().user.page!!

        text_page_link_title.setOnClickListener {
            onBackPressed()
        }

        if (mPage.isLink == null) {
            mPage.isLink = false
        }

        isLink = mPage.isLink!!

        edit_page_link_url.setSingleLine()
        if (StringUtils.isNotEmpty(mPage.homepageLink)) {
            edit_page_link_url.setText(mPage.homepageLink)
        }

        edit_page_link_url.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mIsEditing = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        text_page_link_save.setOnClickListener {
            val url = edit_page_link_url.text.toString().trim()
            mPage.isLink = isLink
            if (mPage.isLink!!) {
                if (StringUtils.isEmpty(url)) {
                    showAlert(R.string.msg_input_link_url)
                    return@setOnClickListener
                }
            }

            if (!StringUtils.isEmpty(url)) {
                if (!URLUtil.isValidUrl(url)) {
                    showAlert(R.string.msg_invalid_url)
                    return@setOnClickListener
                }
            }

            mPage.homepageLink = url

            showProgress("")
            ApiBuilder.create().updatePage(mPage).setCallback(object : PplusCallback<NewResultResponse<Page>> {
                override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
                    LoginInfoManager.getInstance().user.page = response!!.data
                    LoginInfoManager.getInstance().save()
                    hideProgress()
                    showAlert(R.string.msg_saved)
                    mIsEditing = false
                }

                override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
                    hideProgress()
                }
            }).build().call()
        }

        text_page_link_page.setOnClickListener {
            isLink = false
            text_page_link_page.isSelected = true
            text_page_link_url.isSelected = false
            text_page_link_url_pre_view.visibility = View.GONE
            edit_page_link_url.visibility = View.GONE
        }

        text_page_link_url.setOnClickListener {
            isLink = true
            text_page_link_page.isSelected = false
            text_page_link_url.isSelected = true
            text_page_link_url_pre_view.visibility = View.VISIBLE
            edit_page_link_url.visibility = View.VISIBLE
        }

        val imgRes = intArrayOf(R.drawable.img_link_info)
        val adapter = PageLinkPagerAdapter(this, imgRes)
        pager_page_link.adapter = adapter
        indicator_page_link.removeAllViews()
        indicator_page_link.build(LinearLayout.HORIZONTAL, imgRes.size)
        pager_page_link.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                indicator_page_link.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        text_page_link_url_pre_view.setOnClickListener {
            val url = edit_page_link_url.text.toString().trim()
            if (StringUtils.isEmpty(url)) {
                showAlert(R.string.msg_input_link_url)
                return@setOnClickListener
            }

            if (!URLUtil.isValidUrl(url)) {
                showAlert(R.string.msg_invalid_url)
                return@setOnClickListener
            }
            PplusCommonUtil.openChromeWebView(this, url)
        }

        if (!isLink) {
            text_page_link_page.isSelected = true
            text_page_link_url.isSelected = false
            text_page_link_url_pre_view.visibility = View.GONE
            edit_page_link_url.visibility = View.GONE
        } else {
            text_page_link_page.isSelected = false
            text_page_link_url.isSelected = true
            text_page_link_url_pre_view.visibility = View.VISIBLE
            edit_page_link_url.visibility = View.VISIBLE
        }
    }

    class PageLinkPagerAdapter(val mContext: Context, val mImgRes: IntArray) : androidx.viewpager.widget.PagerAdapter() {

        private val mInflater: LayoutInflater

        init {
            mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

            (container as androidx.viewpager.widget.ViewPager).removeView(`object` as View)
        }


        override fun getCount(): Int {

            return mImgRes.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val view = mInflater.inflate(R.layout.item_post_image, container, false)

            val imageView = view.findViewById(R.id.image_post) as ImageView
            imageView.setImageResource(mImgRes[position])

            container.addView(view)
            return view
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {

            return view === `object`
        }
    }
}
