package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.GoodsReview
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_review_reply.*
import network.common.PplusCallback
import retrofit2.Call

class ReviewReplyActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_review_reply
    }

    var mGoodsReview:GoodsReview? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mGoodsReview = intent.getParcelableExtra(Const.DATA)

        text_edit_review_reply_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_format_count_per, 0, 500))
        edit_review_reply_contents.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                text_edit_review_reply_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_format_count_per, s!!.length, 500))

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        text_review_reply_review.text = mGoodsReview!!.review
        text_review_reply_review_reg_date.text = PplusCommonUtil.getDateFormat(mGoodsReview!!.regDatetime!!)

        if(StringUtils.isNotEmpty(mGoodsReview!!.reviewReply)){
            edit_review_reply_contents.setText(mGoodsReview!!.reviewReply)
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_review_of_reply), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    val contents = edit_review_reply_contents.text.toString().trim()
                    if(StringUtils.isEmpty(contents)){
                        showAlert(R.string.msg_input_review_of_reply)
                        return@OnToolbarListener
                    }

                    mGoodsReview!!.reviewReply = contents
                    showProgress("")
                    ApiBuilder.create().putGoodsReviewReply(mGoodsReview).setCallback(object : PplusCallback<NewResultResponse<GoodsReview>>{
                        override fun onResponse(call: Call<NewResultResponse<GoodsReview>>?, response: NewResultResponse<GoodsReview>?) {
                            hideProgress()
                            showAlert(R.string.msg_reg_review_of_reply)
                            setResult(Activity.RESULT_OK)
                            finish()
                        }

                        override fun onFailure(call: Call<NewResultResponse<GoodsReview>>?, t: Throwable?, response: NewResultResponse<GoodsReview>?) {
                            hideProgress()
                        }
                    }).build().call()
                }
            }
        }

    }
}
