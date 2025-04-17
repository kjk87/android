package com.pplus.luckybol.apps.event.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.CountryConfigManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.data.AttendanceAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.MemberAttendance
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityAttendanceBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

class AttendanceActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityAttendanceBinding

    override fun getLayoutView(): View {
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return "Home_attendance"
    }


    override fun initializeView(savedInstanceState: Bundle?) {

        val maxAttendanceDay = CountryConfigManager.getInstance().config.properties.maxAttendanceDay
        val maxAttendanceBol = CountryConfigManager.getInstance().config.properties.maxAttendanceBol
        binding.textAttendanceDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_attendance_desc, maxAttendanceDay.toString(), maxAttendanceBol.toString()))
        binding.textAttendanceCaution2.text = getString(R.string.format_attendance_caution2, maxAttendanceDay.toString(), maxAttendanceBol.toString())

        binding.recyclerAttendance.layoutManager = GridLayoutManager(this, 5)
        val adapter = AttendanceAdapter()
        binding.recyclerAttendance.adapter = adapter

        showProgress("")
        ApiBuilder.create().attendance().setCallback(object : PplusCallback<NewResultResponse<MemberAttendance>>{
            override fun onResponse(call: Call<NewResultResponse<MemberAttendance>>?,
                                    response: NewResultResponse<MemberAttendance>?) {
                hideProgress()
                if(response?.data != null){
                    for(i in 0 until maxAttendanceDay!!){
                        if(i < response.data!!.attendanceCount!!){
                            if(i == maxAttendanceDay -1){
                                adapter.add(R.drawable.ic_stamp_sel_50)
                            }else{
                                adapter.add(R.drawable.ic_stamp_sel)
                            }

                        }else{
                            if(i == maxAttendanceDay -1){
                                adapter.add(R.drawable.ic_stamp_nor_50)
                            }else{
                                adapter.add(R.drawable.ic_stamp_nor)
                            }

                        }

                    }

                    if(response.data!!.isAttendance!!){

                        val intent = Intent(this@AttendanceActivity, AlertBolSaveActivity::class.java)
                        intent.putExtra(Const.TYPE, Const.ATTENDANCE)
                        if(response.data!!.attendanceCount!! < maxAttendanceDay){
                            intent.putExtra(Const.AMOUNT, 1)
                        }else{
                            intent.putExtra(Const.AMOUNT, maxAttendanceBol)
                        }
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<MemberAttendance>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<MemberAttendance>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_attendance_save), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}