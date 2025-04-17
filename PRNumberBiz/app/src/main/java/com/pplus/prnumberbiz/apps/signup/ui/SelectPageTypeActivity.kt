package com.pplus.prnumberbiz.apps.signup.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.View
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.signup.data.PageTypePagerAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import kotlinx.android.synthetic.main.activity_select_page_type.*

class SelectPageTypeActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_select_page_type
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val adapter = PageTypePagerAdapter(this)

        pager_select_page_type.adapter = adapter

        val buttonText = arrayOf(R.string.word_select_store, R.string.word_select_shop, R.string.word_select_person)
//        val buttonText = arrayOf(R.string.word_select_store, R.string.word_select_shop)

        pager_select_page_type.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                text_select_page_type_confirm.setText(buttonText[position])
                when (position) {
                    0 -> {
                        image_select_page_type_prev.visibility = View.GONE
                        image_select_page_type_next.visibility = View.VISIBLE
                    }
                    1 -> {
                        image_select_page_type_prev.visibility = View.VISIBLE
                        image_select_page_type_next.visibility = View.VISIBLE
                    }
                    2 -> {
                        image_select_page_type_prev.visibility = View.VISIBLE
                        image_select_page_type_next.visibility = View.GONE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        image_select_page_type_prev.setOnClickListener {
            pager_select_page_type.currentItem = pager_select_page_type.currentItem - 1
        }

        image_select_page_type_next.setOnClickListener {
            pager_select_page_type.currentItem = pager_select_page_type.currentItem + 1
        }

        text_select_page_type_confirm.setText(buttonText[0])
        image_select_page_type_prev.visibility = View.GONE
        image_select_page_type_next.visibility = View.VISIBLE

        text_select_page_type_confirm.setOnClickListener {

            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))

            when (pager_select_page_type.currentItem) {
                0 -> builder.addContents(AlertData.MessageData(getString(R.string.html_page_type, getString(R.string.word_store)), AlertBuilder.MESSAGE_TYPE.HTML, 1))
                1 -> builder.addContents(AlertData.MessageData(getString(R.string.html_page_type, getString(R.string.word_shop)), AlertBuilder.MESSAGE_TYPE.HTML, 1))
                2 -> builder.addContents(AlertData.MessageData(getString(R.string.html_page_type, getString(R.string.word_person)), AlertBuilder.MESSAGE_TYPE.HTML, 1))
            }

            builder.addContents(AlertData.MessageData(getString(R.string.word_alert_select_page_type1), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.addContents(AlertData.MessageData(getString(R.string.word_alert_select_page_type2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val data = Intent()
                            when (pager_select_page_type.currentItem) {
                                0 -> data.putExtra(Const.TYPE, EnumData.PageTypeCode.store.name)
                                1 -> data.putExtra(Const.TYPE, EnumData.PageTypeCode.shop.name)
                                2 -> data.putExtra(Const.TYPE, EnumData.PageTypeCode.person.name)
                            }
                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                    }
                }
            }).builder().show(this)


        }
    }
}
