package com.lejel.wowbox.apps.attendance.ui

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
import com.lejel.wowbox.databinding.ActivityAlertAttendanceCompleteBinding
import com.yy.mobile.rollingtextview.CharOrder
import com.yy.mobile.rollingtextview.RollingTextView
import com.yy.mobile.rollingtextview.strategy.Strategy

class AlertAttendanceCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertAttendanceCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertAttendanceCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val amount = intent.getStringExtra(Const.AMOUNT)!!
        val type = intent.getStringExtra(Const.TYPE)
        val day = intent.getIntExtra(Const.DAY, 0)

        binding.textAlertAttendanceDay.text = getString(R.string.format_attendance_complete, day.toString())
        binding.textAlertAttendanceCompleteConfirm.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
        val handler = Handler(Looper.myLooper()!!)

        binding.layoutAlertAttendanceCompleteAmount.removeAllViews()
        binding.imageAlertAttendanceCompleteType.visibility = View.VISIBLE
        when(type){
            "point"->{
                binding.layoutAlertAttendanceComplete1.setBackgroundResource(R.drawable.bg_alert_point1)
                binding.layoutAlertAttendanceComplete2.setBackgroundResource(R.drawable.bg_alert_point2)
                binding.imageAlertAttendanceCompleteType.setImageResource(R.drawable.ic_alert_point)
                binding.textAlertAttendanceType.text = getString(R.string.word_save_point)

                val textView = RollingTextView(this)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_140pt).toFloat())
                textView.textColor = ResourceUtil.getColor(this, R.color.white)
                textView.addCharOrder(CharOrder.Number)
                textView.animationDuration = 2000L
                textView.charStrategy = Strategy.CarryBitAnimation()
                textView.animationInterpolator = AccelerateDecelerateInterpolator()
                textView.typeface = Typeface.create(textView.typeface, Typeface.BOLD)
                textView.setText("0")
                binding.layoutAlertAttendanceCompleteAmount.addView(textView)
                handler.postDelayed({
                    textView.setText(amount)
                }, 500)

            }
            "cash"->{
                binding.layoutAlertAttendanceComplete1.setBackgroundResource(R.drawable.bg_alert_buff1)
                binding.layoutAlertAttendanceComplete2.setBackgroundResource(R.drawable.bg_alert_buff2)
                binding.imageAlertAttendanceCompleteType.visibility = View.GONE
                binding.textAlertAttendanceType.text = getString(R.string.word_save_rp)

                val textView = RollingTextView(this)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_140pt).toFloat())
                textView.textColor = ResourceUtil.getColor(this, R.color.white)
                textView.addCharOrder(CharOrder.Number)
                textView.animationDuration = 2000L
                textView.charStrategy = Strategy.CarryBitAnimation()
                textView.animationInterpolator = AccelerateDecelerateInterpolator()
                textView.typeface = Typeface.create(textView.typeface, Typeface.BOLD)
                textView.setText("0")
                binding.layoutAlertAttendanceCompleteAmount.addView(textView)
                handler.postDelayed({
                    textView.setText(amount)
                }, 500)
            }
            "buff"->{

                binding.layoutAlertAttendanceComplete1.setBackgroundResource(R.drawable.bg_alert_buff1)
                binding.layoutAlertAttendanceComplete2.setBackgroundResource(R.drawable.bg_alert_buff2)
                binding.imageAlertAttendanceCompleteType.setImageResource(R.drawable.ic_alert_buff)
                binding.textAlertAttendanceType.text = getString(R.string.word_save_buff)

                for (i in amount.indices) {
                    LogUtil.e(LOG_TAG, amount[i].toString())
                    if(amount[i].toString() == "."){
                        val textView = TextView(this)
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_140pt).toFloat())
                        textView.setTextColor(ResourceUtil.getColor(this, R.color.white))
                        textView.setTypeface(textView.typeface, Typeface.BOLD)
                        textView.text = "."
                        binding.layoutAlertAttendanceCompleteAmount.addView(textView)
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
                        binding.layoutAlertAttendanceCompleteAmount.addView(textView)

                        handler.postDelayed({
                            textView.setText(amount[i].toString())
                        }, 500)
                    }

                }
            }
        }


    }
}
