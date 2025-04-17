package com.lejel.wowbox.apps.common.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder.*
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.builder.data.AlertData.MessageData
import com.lejel.wowbox.apps.common.component.autofit.AutofitTextView
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertBinding
import com.lejel.wowbox.databinding.ItemAlertTextLayoutBinding
import com.pplus.utils.BusProvider
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.NumberUtils
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.apps.common.builder.AlertBuilder

class AlertActivity : BaseActivity() {
    //    private AlertData alertData;
    private var fontSize = 0
    private var alertResult: AlertResult? = null

    @ColorRes
    private val alertMessageColor = R.color.color_333333
    var radioGroup: RadioGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        fontSize = resources.getDimensionPixelSize(R.dimen.textSize_50pt)
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        alertResult = AlertResult()
        alertResult!!.alertData = PplusCommonUtil.getParcelableExtra(intent, AlertBuilder.ALERT_KEYS, AlertData::class.java)
        if (alertResult!!.alertData != null) {
            setFinishOnTouchOutside(alertResult!!.alertData!!.isBackgroundClickable)
            if (alertResult!!.alertData!!.isBackgroundClickable){
                binding.topContentsLayout.setOnClickListener {
                    finish()
                }
            }

            //@tile empty message hide view..
            if (StringUtils.isEmpty(alertResult!!.alertData!!.title)) {
                binding.textAlertTitle.visibility = View.GONE
                binding.textAlertContentsLayout.setPadding(resources.getDimensionPixelSize(R.dimen.width_44), resources.getDimensionPixelSize(R.dimen.height_44), resources.getDimensionPixelSize(R.dimen.height_44), resources.getDimensionPixelSize(R.dimen.width_44))
            } else {
                binding.textAlertTitle.visibility = View.VISIBLE
                binding.textAlertTitle.text = alertResult!!.alertData!!.title
                binding.textAlertTitle.setTextColor(ResourceUtil.getColor(this, R.color.color_333333))
                binding.textAlertContentsLayout.setPadding(resources.getDimensionPixelSize(R.dimen.width_54), resources.getDimensionPixelSize(R.dimen.height_36), resources.getDimensionPixelSize(R.dimen.height_78), resources.getDimensionPixelSize(R.dimen.width_54))
            }
            val messageDatas = alertResult!!.alertData!!.contents
            var textContents: AutofitTextView? = null
            if (alertResult!!.alertData!!.style_alert == STYLE_ALERT.MESSAGE) {
                for (data in messageDatas) {
                    val previous = textContents
                    textContents = LayoutInflater.from(this@AlertActivity).inflate(R.layout.item_alert_text, null, false) as AutofitTextView
                    ResourceUtil.setGenerateViewId(textContents)
                    var params: RelativeLayout.LayoutParams? = null
                    params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    textContents.gravity = Gravity.CENTER
                    textContents.setTextColor(ResourceUtil.getColor(this, alertMessageColor))
                    if (previous != null) {
                        params.addRule(RelativeLayout.BELOW, previous.id)
                        params.setMargins(0, resources.getDimensionPixelSize(R.dimen.width_36), 0, 0)
                    }

                    //FIXME Hoon! AUTO FIT TextView size enable..
                    //                    textContents.setSizeToFit();
                    textContents.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize.toFloat())
                    textContents.maxLines = data.maxLine
                    textContents.layoutParams = params
                    when (data.message_type) {
                        MESSAGE_TYPE.HTML -> {
                            textContents.text = PplusCommonUtil.fromHtml(data.contents!!)
                        }
                        MESSAGE_TYPE.TEXT -> textContents.text = data.contents
                    }
                    binding.textAlertContentsLayout.addView(textContents, params)
                }
            } else if (alertResult!!.alertData!!.style_alert == STYLE_ALERT.LIST_CENTER || alertResult!!.alertData!!.style_alert == STYLE_ALERT.LIST_BOTTOM) {
                if (alertResult!!.alertData!!.style_alert == STYLE_ALERT.LIST_BOTTOM) {
                    val params = binding.layoutAlert.layoutParams as RelativeLayout.LayoutParams
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    params.setMargins(0, 0, 0, resources.getDimensionPixelSize(R.dimen.height_50))
                }
                val recyclerView = RecyclerView(this)
                recyclerView.layoutManager = LinearLayoutManager(this)
                val visibleCnt = 6
                if (alertResult!!.alertData!!.contents.size > visibleCnt) {
                    val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((visibleCnt + 0.5) * resources.getDimensionPixelSize(R.dimen.height_152)).toInt())
                    recyclerView.layoutParams = params
                }else{
                    val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    recyclerView.layoutParams = params
                }
                val listAdapter = ListAdapter()
                recyclerView.adapter = listAdapter
                binding.textAlertContentsLayout.setPadding(0, 0, 0, 0)
                binding.textAlertContentsLayout.addView(recyclerView)
            } else if (alertResult!!.alertData!!.style_alert == STYLE_ALERT.LIST_RADIO || alertResult!!.alertData!!.style_alert == STYLE_ALERT.LIST_RADIO_BOTTOM) {

                // 라디오 그룹 스크롤 기능 추가를 위해 레이아웃 구성합니다.
                if (alertResult!!.alertData!!.style_alert == STYLE_ALERT.LIST_RADIO_BOTTOM) {
                    val params = binding.layoutAlert.layoutParams as RelativeLayout.LayoutParams
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    params.setMargins(0, 0, 0, resources.getDimensionPixelSize(R.dimen.height_50))
                }
                val linearLayout = LinearLayout(this@AlertActivity)
                val scrollView = ScrollView(this@AlertActivity)
                scrollView.addView(linearLayout)
                radioGroup = RadioGroup(this@AlertActivity)
                linearLayout.addView(radioGroup) //                scrollView.addView(radioGroup);
                val visibleCnt = 6
                val viewGroupParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                if (alertResult!!.alertData!!.contents.size > visibleCnt) {
                    val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((visibleCnt + 0.5) * resources.getDimensionPixelSize(R.dimen.height_152)).toInt())
                    scrollView.layoutParams = params
                }

                //(RadioGroup) LayoutInflater.from(AlertActivity.this).inflate(R.layout.item_alert_radio, null, false);
                viewGroupParams.setMargins(0, resources.getDimensionPixelSize(R.dimen.height_67), 0, resources.getDimensionPixelSize(R.dimen.height_79))
                radioGroup!!.layoutParams = viewGroupParams
                val isSelcted = false
                val firstId = 0
                for (i in alertResult!!.alertData!!.contents.indices) {
                    val messageData = alertResult!!.alertData!!.contents[i]
                    val radioButton = RadioButton(this@AlertActivity)
                    radioButton.setPadding(resources.getDimensionPixelSize(R.dimen.width_18), 0, 0, 0)
                    radioButton.setButtonDrawable(R.drawable.radio_alert)
                    radioButton.compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.width_50)
                    val layoutParams = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.height_67))
                    var marginTop = 0
                    if (i != 0) {
                        marginTop = resources.getDimensionPixelSize(R.dimen.width_54)
                    }
                    layoutParams.setMargins(resources.getDimensionPixelSize(R.dimen.width_77), marginTop, 0, 0)
                    radioButton.layoutParams = layoutParams
                    radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize.toFloat())
                    radioButton.setSingleLine()
                    radioButton.setTextColor(ResourceUtil.getColor(this@AlertActivity, R.color.color_333333))
                    radioButton.text = messageData.contents

                    //                    if(NumberUtils.compare(alertData!!.getContents().length, i + 1) == 0) {
                    //                        radioButton.setBackgroundResource(R.drawable.btn_alert_list);
                    //                    } else {
                    //                        radioButton.setBackgroundResource(R.drawable.btn_alert_list_underbar);
                    //                    }

                    //                    radioButton.setBackgroundResource(R.drawable.btn_alert_list);
                    radioButton.id = i
                    radioGroup!!.addView(radioButton)
                }
                radioGroup!!.check(Math.max(alertResult!!.alertData!!.style_alert!!.value - 1, 0))
                binding.textAlertContentsLayout.setPadding(0, 0, 0, 0)
                binding.textAlertContentsLayout.addView(scrollView)
            }
            if (StringUtils.isEmpty(alertResult!!.alertData!!.leftText) && StringUtils.isEmpty(alertResult!!.alertData!!.rightText)) {
                when (alertResult!!.alertData!!.style_alert) {
                    STYLE_ALERT.LIST_CENTER, STYLE_ALERT.LIST_BOTTOM -> {
                    }
                    STYLE_ALERT.MESSAGE -> alertResult!!.alertData!!.rightText = getString(R.string.word_confirm)
                    STYLE_ALERT.LIST_RADIO, STYLE_ALERT.LIST_RADIO_BOTTOM -> alertResult!!.alertData!!.rightText = getString(R.string.word_confirm)
                    else -> {}
                }
            }
            if (StringUtils.isEmpty(alertResult!!.alertData!!.leftText) && StringUtils.isEmpty(alertResult!!.alertData!!.rightText)) {
//                binding.textAlertLine.visibility = View.GONE
//                binding.lineAlertBottom.visibility = View.GONE
                binding.textAlertLeftBtn.visibility = View.GONE
                binding.textAlertRightBtn.visibility = View.GONE
            } else if (StringUtils.isEmpty(alertResult!!.alertData!!.leftText) || StringUtils.isEmpty(alertResult!!.alertData!!.rightText)) {
//                binding.textAlertLine.visibility = View.GONE
                binding.textAlertLeftBtn.visibility = View.GONE
                if (StringUtils.isEmpty(alertResult!!.alertData!!.leftText)) {
                    binding.textAlertRightBtn.text = alertResult!!.alertData!!.rightText
                } else if (StringUtils.isEmpty(alertResult!!.alertData!!.rightText)) {
                    binding.textAlertRightBtn.text = alertResult!!.alertData!!.leftText
                }
            } else {
                binding.textAlertLeftBtn.text = alertResult!!.alertData!!.leftText
                binding.textAlertRightBtn.text = alertResult!!.alertData!!.rightText
            }

            binding.imageAlertClose.setOnClickListener {
                finish()
            }

            binding.textAlertLeftBtn.setOnClickListener {
                alertResult!!.event_alert = EVENT_ALERT.LEFT
                BusProvider.getInstance().post(alertResult)
                finish()
            }
            binding.textAlertRightBtn.setOnClickListener {
                if (alertResult!!.alertData!!.style_alert == STYLE_ALERT.LIST_RADIO || alertResult!!.alertData!!.style_alert == STYLE_ALERT.LIST_RADIO_BOTTOM) {
                    var number = -1
                    number = if (radioGroup != null) {
                        radioGroup!!.checkedRadioButtonId
                    } else {
                        0
                    }
                    val eventAlert = EVENT_ALERT.RADIO
                    if (number != -1) {
                        eventAlert.value = number + 1
                        alertResult!!.event_alert = eventAlert
                        BusProvider.getInstance().post(alertResult)
                    } else {
                        return@setOnClickListener
                    }
                } else {
                    if (StringUtils.isEmpty(alertResult!!.alertData!!.leftText) || StringUtils.isEmpty(alertResult!!.alertData!!.rightText)) {
                        alertResult!!.event_alert = EVENT_ALERT.SINGLE
                        BusProvider.getInstance().post(alertResult)
                    } else {
                        alertResult!!.event_alert = EVENT_ALERT.RIGHT
                        BusProvider.getInstance().post(alertResult)
                    }
                }
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (alertResult!!.alertData!! != null) {
                    if (!alertResult!!.alertData!!.isAutoCancel) return
                }
                finish()
            }
        })
    }

    override fun finish() {
        alertResult!!.event_alert = EVENT_ALERT.CANCEL
        BusProvider.getInstance().post(alertResult)
        super.finish()
    }

    inner class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {


        inner class ViewHolder(val binding: ItemAlertTextLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

        override fun getItemCount(): Int {
            return alertResult!!.alertData!!.contents.size
        }

        fun getItem(position: Int): MessageData {

            return alertResult!!.alertData!!.contents[position]
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            if (NumberUtils.compare(alertResult!!.alertData!!.contents.size, position + 1) == 0) {
                holder.binding.viewAlertTextLayoutBar.visibility = View.GONE
            } else {
                holder.binding.viewAlertTextLayoutBar.visibility = View.VISIBLE
            }
            holder.binding.tvAutofit.gravity = Gravity.CENTER
            holder.binding.tvAutofit.setTextColor(ResourceUtil.getColor(holder.itemView.context, alertMessageColor))

            holder.binding.tvAutofit.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize.toFloat())
            holder.binding.tvAutofit.maxLines = 1
            val data = getItem(position)
            when (data.message_type) {
                MESSAGE_TYPE.HTML -> {
                    holder.binding.tvAutofit.text = PplusCommonUtil.fromHtml(data.contents!!)
                }
                MESSAGE_TYPE.TEXT -> holder.binding.tvAutofit.text = data.contents
            }
            holder.itemView.setOnClickListener {
                val event_alert = EVENT_ALERT.LIST
                event_alert.value = position + 1
                alertResult!!.event_alert = event_alert
                BusProvider.getInstance().post(alertResult)
                finish()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemAlertTextLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
    }
}