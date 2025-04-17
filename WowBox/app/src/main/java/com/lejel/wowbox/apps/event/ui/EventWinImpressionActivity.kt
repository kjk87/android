package com.lejel.wowbox.apps.event.ui

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
import androidx.activity.OnBackPressedCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.network.model.dto.EventWin
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityEventWinImpressionBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.resource.ResourceUtil
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
    var mEvent: Event? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAlert(R.string.msg_event_win_impression_description1)
            }
        })

        mEventWin = PplusCommonUtil.getParcelableExtra(intent, Const.EVENT_WIN, EventWin::class.java)
        mEvent = PplusCommonUtil.getParcelableExtra(intent, Const.EVENT, Event::class.java)

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
                setTitle(getString(R.string.word_modify_win_impression))
                binding.editEventWinImpressionContents.setText(mEventWin!!.impression)
            }else{
                setTitle(getString(R.string.word_reg_win_impression))
            }
        }
    }

    private fun write(impression: String) {

        val params = HashMap<String, String>()
        params["seqNo"] = mEventWin!!.seqNo.toString()
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
                showAlert(R.string.msg_retry_after)
            }
        }).build().call()
    }

    private var textRightTop: TextView? = null

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reg_win_impression), ToolbarOption.ToolbarMenu.LEFT)
        textRightTop = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
        textRightTop!!.text = getString(R.string.format_count_per, 0, 50)
        textRightTop!!.isClickable = true
        textRightTop!!.gravity = Gravity.CENTER
        textRightTop!!.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
        textRightTop!!.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_fc5c57))
        textRightTop!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_45pt).toFloat())
        textRightTop!!.setSingleLine()
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop!!, 0)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        showAlert(R.string.msg_event_win_impression_description1)
                    }

                    else -> {}
                }
            }
        }
    }

}
