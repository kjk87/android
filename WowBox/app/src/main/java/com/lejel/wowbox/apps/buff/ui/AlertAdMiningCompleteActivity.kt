package com.lejel.wowbox.apps.buff.ui

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.resource.ResourceUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.databinding.ActivityAlertAdMiningCompleteBinding
import com.yy.mobile.rollingtextview.CharOrder
import com.yy.mobile.rollingtextview.RollingTextView
import com.yy.mobile.rollingtextview.strategy.Strategy

class AlertAdMiningCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertAdMiningCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertAdMiningCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val amount = intent.getStringExtra(Const.AMOUNT)!!
        val type = intent.getStringExtra(Const.TYPE)
        val depth = intent.getIntExtra(Const.DEPTH, 0)

        when(depth){
            1->{
                binding.textAlertAdMiningDepth.setText(R.string.word_step1)
            }
            2->{
                binding.textAlertAdMiningDepth.setText(R.string.word_step2)
            }
            3->{
                binding.textAlertAdMiningDepth.setText(R.string.word_step3)
            }
        }
        binding.textAlertAdMiningCompleteConfirm.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
        val handler = Handler(Looper.myLooper()!!)
        binding.imageAlertAdMiningCompleteType.visibility = View.VISIBLE
        binding.layoutAlertAdMiningCompleteAmount.removeAllViews()
        when(type){
            "point"->{

                binding.layoutAlertAdMiningComplete1.setBackgroundResource(R.drawable.bg_alert_point1)
                binding.layoutAlertAdMiningComplete2.setBackgroundResource(R.drawable.bg_alert_point2)
                binding.imageAlertAdMiningCompleteType.setImageResource(R.drawable.ic_alert_point)
                binding.textAlertAdMiningType.text = getString(R.string.word_save_point)

                val textView = RollingTextView(this)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_140pt).toFloat())
                textView.textColor = ResourceUtil.getColor(this, R.color.white)
                textView.addCharOrder(CharOrder.Number)
                textView.animationDuration = 2000L
                textView.charStrategy = Strategy.CarryBitAnimation()
                textView.animationInterpolator = AccelerateDecelerateInterpolator()
                textView.typeface = Typeface.create(textView.typeface, Typeface.BOLD)
                textView.setText("0")
                binding.layoutAlertAdMiningCompleteAmount.addView(textView)
                handler.postDelayed({
                    textView.setText(amount)
                }, 500)

            }
            "cash"->{

                binding.layoutAlertAdMiningComplete1.setBackgroundResource(R.drawable.bg_alert_buff1)
                binding.layoutAlertAdMiningComplete2.setBackgroundResource(R.drawable.bg_alert_buff2)
                binding.imageAlertAdMiningCompleteType.visibility = View.GONE
                binding.textAlertAdMiningType.text = getString(R.string.word_save_rp)

                val textView = RollingTextView(this)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_140pt).toFloat())
                textView.textColor = ResourceUtil.getColor(this, R.color.white)
                textView.addCharOrder(CharOrder.Number)
                textView.animationDuration = 2000L
                textView.charStrategy = Strategy.CarryBitAnimation()
                textView.animationInterpolator = AccelerateDecelerateInterpolator()
                textView.typeface = Typeface.create(textView.typeface, Typeface.BOLD)
                textView.setText("0")
                binding.layoutAlertAdMiningCompleteAmount.addView(textView)
                handler.postDelayed({
                    textView.setText(amount)
                }, 500)
            }
            "buff"->{

                binding.layoutAlertAdMiningComplete1.setBackgroundResource(R.drawable.bg_alert_buff1)
                binding.layoutAlertAdMiningComplete2.setBackgroundResource(R.drawable.bg_alert_buff2)
                binding.imageAlertAdMiningCompleteType.setImageResource(R.drawable.ic_alert_buff)
                binding.textAlertAdMiningType.text = getString(R.string.word_save_buff)

                for (i in amount.indices) {
                    LogUtil.e(LOG_TAG, amount[i].toString())
                    if(amount[i].toString() == "."){
                        val textView = TextView(this)
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_140pt).toFloat())
                        textView.setTextColor(ResourceUtil.getColor(this, R.color.white))
                        textView.setTypeface(textView.typeface, Typeface.BOLD)
                        textView.text = "."
                        binding.layoutAlertAdMiningCompleteAmount.addView(textView)
                    }else{
                        val textView = RollingTextView(this)
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_140pt).toFloat())
                        textView.textColor = ResourceUtil.getColor(this, R.color.white)
                        textView.typeface = Typeface.create(textView.typeface, Typeface.BOLD)
                        textView.addCharOrder(CharOrder.Number)
                        textView.animationDuration = 2000L
                        textView.charStrategy = Strategy.CarryBitAnimation()
                        textView.animationInterpolator = AccelerateDecelerateInterpolator()
                        textView.setText("0")
                        binding.layoutAlertAdMiningCompleteAmount.addView(textView)

                        handler.postDelayed({
                            textView.setText(amount[i].toString())
                        }, 500)
                    }

                }
            }
            "lottery"->{

                binding.layoutAlertAdMiningComplete1.setBackgroundResource(R.drawable.bg_alert_ticket1)
                binding.layoutAlertAdMiningComplete2.setBackgroundResource(R.drawable.bg_alert_ticket2)
                binding.imageAlertAdMiningCompleteType.setImageResource(R.drawable.ic_alert_ticket)
                binding.textAlertAdMiningType.text = getString(R.string.word_save_ticket)

                val textView = RollingTextView(this)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_140pt).toFloat())
                textView.textColor = ResourceUtil.getColor(this, R.color.white)
                textView.addCharOrder(CharOrder.Number)
                textView.animationDuration = 2000L
                textView.charStrategy = Strategy.CarryBitAnimation()
                textView.animationInterpolator = AccelerateDecelerateInterpolator()
                textView.typeface = Typeface.create(textView.typeface, Typeface.BOLD)
                textView.setText("0")
                binding.layoutAlertAdMiningCompleteAmount.addView(textView)
                handler.postDelayed({
                    textView.setText(amount)
                }, 500)

            }
        }


    }


}
