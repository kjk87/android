package com.pplus.prnumberuser.apps.event.ui

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
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.alert.AlertGoogleReviewActivity
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.EventWin
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityEventWinImpressionBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

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

    override fun initializeView(savedInstanceState: Bundle?) {
        mEventWin = intent.getParcelableExtra(Const.EVENT_WIN);

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

        if(StringUtils.isNotEmpty(mEventWin!!.impression)){
            binding.editEventWinImpressionContents.setText(mEventWin!!.impression)
        }
    }

    private fun write(impression: String) {

        val params = HashMap<String, String>()
        params["event.no"] = mEventWin!!.event!!.no.toString()
//        params["winNo"] = mEventWin!!.winNo.toString()
        params["id"] = mEventWin!!.id.toString()
        params["impression"] = impression
        showProgress("")
        ApiBuilder.create().writeImpression(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()

                if (mEventWin!!.event!!.primaryType != "page" && !PreferenceUtil.getDefaultPreference(this@EventWinImpressionActivity).get(Const.GOOGLE_REVIEW, false)) {
                    val intent = Intent(this@EventWinImpressionActivity, AlertGoogleReviewActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }

                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
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
        textRightTop!!.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_579ffb))
        textRightTop!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_45pt).toFloat())
        textRightTop!!.setSingleLine()
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
        return toolbarOption
    }

    override fun onBackPressed() {
        showAlert(R.string.msg_write_winner_description2)
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> {
                        showAlert(R.string.msg_write_winner_description2)
                    }
                }
            }
        }

    }

}
