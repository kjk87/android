package com.pplus.luckybol.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.alert.AlertGoogleReviewActivity
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.EventWin
import com.pplus.luckybol.core.network.model.dto.EventWinJpa
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityEventWinImpressionBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class EventWinImpressionActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_mypage_prizewinner review"
    }

    private lateinit var binding: ActivityEventWinImpressionBinding

    override fun getLayoutView(): View {
        binding = ActivityEventWinImpressionBinding.inflate(layoutInflater)
        return binding.root
    }

    var mEventWin: EventWin? = null
    var mEventWinJpa: EventWinJpa? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mEventWin = intent.getParcelableExtra(Const.EVENT_WIN)

        if(mEventWin == null){
            mEventWinJpa = intent.getParcelableExtra(Const.EVENT_WIN_JPA)
        }

        binding.textEventWinImpressionDescription.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_event_win_impression_description2))

        binding.editEventWinImpressionContents.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {

                textRightTop!!.text = getString(R.string.format_count_per, editable.length, 50)
            }
        })

        binding.textEventWinImpressionWrite.setOnClickListener {
            val impression = binding.editEventWinImpressionContents.text.toString()
            if (StringUtils.isEmpty(impression)) {
                showAlert(R.string.msg_input_win_impression)
                return@setOnClickListener
            }

            if (impression.trim().length < 5) {
                showAlert(R.string.msg_input_win_impression)
                return@setOnClickListener
            }
            write(impression.trim())
        }

        if(mEventWin != null){
            if(StringUtils.isNotEmpty(mEventWin!!.impression)){
                binding.editEventWinImpressionContents.setText(mEventWin!!.impression)
            }
        }else if(mEventWinJpa != null){
            if(StringUtils.isNotEmpty(mEventWinJpa!!.impression)){
                binding.editEventWinImpressionContents.setText(mEventWinJpa!!.impression)
            }
        }

    }

    private fun write(impression: String) {

        val params = HashMap<String, String>()
        if(mEventWin != null){
            params["event.no"] = mEventWin!!.event!!.no.toString()
            params["winNo"] = mEventWin!!.winNo.toString()
            params["id"] = mEventWin!!.id.toString()
        }else if(mEventWinJpa != null){
            params["event.no"] = mEventWinJpa!!.eventSeqNo.toString()
            params["winNo"] = mEventWinJpa!!.seqNo.toString()
            params["id"] = mEventWinJpa!!.id.toString()
        }

        params["impression"] = impression
        showProgress("")
        ApiBuilder.create().writeImpression(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()

                if (!PreferenceUtil.getDefaultPreference(this@EventWinImpressionActivity).get(Const.GOOGEL_REVIEW, false)) {
                    val intent = Intent(this@EventWinImpressionActivity, AlertGoogleReviewActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }

                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if (response?.resultCode == 801) {
                    showAlert(R.string.msg_retry_after)
                }
            }
        }).build().call()
    }

    private var textRightTop: TextView? = null

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_win_impression), ToolbarOption.ToolbarMenu.LEFT)
        textRightTop = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
        textRightTop!!.text = getString(R.string.format_count_per, 0, 50)
        textRightTop!!.isClickable = true
        textRightTop!!.gravity = Gravity.CENTER
        textRightTop!!.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
        textRightTop!!.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_fc5c57))
        textRightTop!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_45pt).toFloat())
        textRightTop!!.setSingleLine()
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
        return toolbarOption
    }

    override fun onBackPressed() {
        showAlert(R.string.msg_write_winner_description2)
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> {
                    showAlert(R.string.msg_write_winner_description2)
                }
                else -> {}
            }
        }

    }

}
