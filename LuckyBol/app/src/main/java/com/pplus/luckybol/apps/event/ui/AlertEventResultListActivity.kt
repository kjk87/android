package com.pplus.luckybol.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.data.EventResultAdapter
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventResultJpa
import com.pplus.luckybol.core.network.model.dto.EventWinJpa
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityAlertEventResultListBinding
import com.pplus.utils.part.logs.LogUtil

class AlertEventResultListActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertEventResultListBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertEventResultListBinding.inflate(layoutInflater)
        return binding.root
    }

    val mWinList = arrayListOf<EventWinJpa>()
    var mEvent:Event? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        val eventResultList = intent.getParcelableArrayListExtra<EventResultJpa>(Const.DATA)!!
        mEvent = intent.getParcelableExtra<Event>(Const.EVENT)

        if(eventResultList.size >= 6){
            binding.recyclerEventResultList.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_990)
        }

        binding.textAlertEventResultListTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_event_result_title, eventResultList.size.toString()))

        val adapter = EventResultAdapter()
        adapter.mEvent = mEvent
        adapter.setDataList(eventResultList)
        binding.recyclerEventResultList.layoutManager = LinearLayoutManager(this)
        binding.recyclerEventResultList.adapter = adapter

        for (eventResult in eventResultList){
            if(eventResult.win != null){
                mWinList.add(eventResult.win!!)
            }
        }

        if(mWinList.isEmpty()){
            binding.textAlertEventResultListConfirm.text = getString(R.string.word_confirm)
            binding.textAlertEventResultListConfirm.setOnClickListener {
                setResult(RESULT_OK)
                finish()
            }
        }else{
            binding.textAlertEventResultListConfirm.text = getString(R.string.msg_write_win_impression)
            binding.textAlertEventResultListConfirm.setOnClickListener {
                val eventWin = mWinList[0]
                mWinList.removeAt(0)
                moveBuyResult(eventWin)

            }
        }
    }

    private fun moveBuyResult(eventWin:EventWinJpa){
        val intent = Intent(this@AlertEventResultListActivity, EventBuyResultActivity::class.java)
        intent.putExtra(Const.EVENT_WIN, eventWin)
        intent.putExtra(Const.EVENT, mEvent)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        resultLauncher.launch(intent)
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            LogUtil.e(LOG_TAG, "winListSize : "+ mWinList.size)
            if(mWinList.isNotEmpty()){
                val eventWin = mWinList[0]
                mWinList.removeAt(0)
                moveBuyResult(eventWin)
            }else{
                setResult(RESULT_OK)
                finish()
            }
        }
    }
}
