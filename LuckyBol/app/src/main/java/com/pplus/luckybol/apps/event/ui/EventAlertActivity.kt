package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.databinding.ActivityEventAlertBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.util.*

class EventAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityEventAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityEventAlertBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun initializeView(savedInstanceState: Bundle?) {
        val code = intent.getIntExtra(Const.KEY, 0)
        val event = intent.getParcelableExtra<Event>(Const.DATA)
        val joinDate = intent.getStringExtra(Const.JOIN_DATE)
        val joinTerm = intent.getIntExtra(Const.JOIN_TERM, 0)
        val remainSecond = intent.getIntExtra(Const.REMAIN_SECOND, 0)

        if (event!!.primaryType == EventType.PrimaryType.number.name) {
            binding.imageEventAlert.setImageResource(R.drawable.ic_number_event_popup_character_1)
        } else {
            binding.imageEventAlert.setImageResource(R.drawable.ic_event_popup_character_1)
        }

        when (code) {
            504 -> {
                binding.textEventAlert2.visibility = View.VISIBLE
                if (event.joinType == "minute" && event.joinTerm != null && event.joinTerm!! > 0 && StringUtils.isNotEmpty(event.lastJoinDatetime)) {

                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(event.lastJoinDatetime)
                    val calendar = Calendar.getInstance()
                    calendar.time = d
                    calendar.add(Calendar.MINUTE, event.joinTerm!!)
                    val remainMillis = calendar.timeInMillis - System.currentTimeMillis()
                    val remainSecond = remainMillis / 1000
                    if (remainSecond < 60) {
                        binding.textEventAlert1.text = getString(R.string.format_joinable_remain_seconds, remainSecond.toString())
                    } else {
                        val remainMinute = remainSecond / 60
                        val second = remainSecond % 60
                        binding.textEventAlert1.text = getString(R.string.format_joinable_remain_minute, remainMinute.toString(), second.toString())
                    }
                    binding.textEventAlert2.text = getString(R.string.msg_minute_join_desc)
                    binding.textEventAlert2.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
                } else {
                    binding.textEventAlert1.text = getString(R.string.msg_already_join_event)

                    when(event.joinType){
                        "event"->{
                            if(event.primaryType != EventType.PrimaryType.goodluck.name){
                                binding.textEventAlert2.text = getString(R.string.msg_already_join_event2)
                                binding.textEventAlert2.visibility = View.VISIBLE
                            }else{
                                binding.textEventAlert2.visibility = View.GONE
                            }
                        }
                        "daily"->{
                            binding.textEventAlert2.text = getString(R.string.format_joinable_count, getString(R.string.word_one_day), event.joinLimitCount.toString())
                        }
                        "weekly"->{
                            binding.textEventAlert2.text = getString(R.string.format_joinable_count, getString(R.string.word_one_week), event.joinLimitCount.toString())
                        }
                        "monthly"->{
                            binding.textEventAlert2.text = getString(R.string.format_joinable_count, getString(R.string.word_one_month), event.joinLimitCount.toString())
                        }
                    }
                }
            }
            516 -> {

                if (StringUtils.isNotEmpty(joinDate)) {
                    binding.imageEventAlert.setImageResource(R.drawable.ic_number_event_popup_character_1)
                    binding.textEventAlert2.visibility = View.VISIBLE
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(joinDate)
                    val calendar = Calendar.getInstance()
                    calendar.time = d
                    calendar.add(Calendar.SECOND, joinTerm)

                    val remainMillis = calendar.timeInMillis - System.currentTimeMillis()
//                    val remainSecond = remainMillis / 1000
                    if (remainSecond < 60) {
                        binding.textEventAlert1.text = getString(R.string.format_joinable_remain_seconds, remainSecond.toString())
                    } else {
                        val remainMinute = remainSecond / 60
                        val second = remainSecond % 60
                        binding.textEventAlert1.text = getString(R.string.format_joinable_remain_minute, remainMinute.toString(), second.toString())
                    }
                    binding.textEventAlert2.text = getString(R.string.msg_minute_join_desc)
                    binding.textEventAlert2.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
                } else {
                    binding.textEventAlert1.text = getString(R.string.msg_can_not_join_time_event)
                    if (event.displayTimeList == null || event.displayTimeList!!.isEmpty()) {
                        binding.textEventAlert2.visibility = View.GONE
                    } else {
                        binding.textEventAlert2.visibility = View.VISIBLE

                        //                    val currentMillis = System.currentTimeMillis()
                        //                    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        //                    val currentTime = sdf.format(Date(currentMillis)).split(":")
                        //                    val currentSecond = (currentTime[0].toInt() * 60 * 60) + (currentTime[1].toInt() * 60) + currentTime[2].toInt()
                        var timeString = ""
                        var index = 0
                        for (time in event.displayTimeList!!) {
                            timeString += "${time.start.substring(0, 2)}:${time.start.substring(2)} ~ ${time.end.substring(0, 2)}:${time.end.substring(2)}"

                            index++
                            if (event.displayTimeList!!.size > index) {
                                timeString += "\n"
                            }
                        }

                        if (StringUtils.isNotEmpty(timeString)) {
                            binding.textEventAlert2.text = timeString
                        } else {
                            binding.textEventAlert2.visibility = View.GONE
                        }

                    }
                }


            }
            else -> {
                binding.textEventAlert1.text = getString(R.string.msg_closed_event)
                binding.textEventAlert2.visibility = View.GONE
            }
        }

        binding.textEventAlertConfirm.setOnClickListener {

            when (code) {
                504, 516 -> {
//                    val url = event.moveTargetString
//                    if (StringUtils.isNotEmpty(url)) {
//
//                        val moveTargetNumber = event.moveTargetNumber
//                        if (event.moveType == "inner" && moveTargetNumber != null) {
//                            when (moveTargetNumber) {
//                                5 -> {
//                                    val intent = Intent(this, ProductShipDetailActivity::class.java)
//                                    val productPrice = ProductPrice()
//                                    productPrice.seqNo = url!!.toLong()
//                                    intent.putExtra(Const.DATA, productPrice)
//                                    startActivity(intent)
//                                }
//                            }
//                        } else {
//
//                            if (url!!.contains("http://m.tammys.co.kr")) {
//                                val intent = Intent(this, EventLandingActivity::class.java)
//                                intent.putExtra(Const.NO, event!!.no)
//                                intent.putExtra(Const.URL, url)
//                                startActivity(intent)
//                            } else {
//                                PplusCommonUtil.openChromeWebView(this, url)
//                            }
//                        }
//
//                    }
                }
            }
            finish()

        }
    }


}
