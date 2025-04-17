package com.pplus.luckybol.apps.buff.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.buff.data.BuffHeaderAdapter
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Buff
import com.pplus.luckybol.core.network.model.dto.BuffPost
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityBuffBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class BuffActivity : BaseActivity() {

    private lateinit var binding: ActivityBuffBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mBuff: Buff
    private lateinit var mAdapter: BuffHeaderAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mIsLast = false
    private var mLockListView = false
    private var mPaging = 0

    override fun initializeView(savedInstanceState: Bundle?) {

        mBuff = intent.getParcelableExtra(Const.DATA)!!

        binding.imageBuffBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageBuffBack2.setOnClickListener {
            onBackPressed()
        }


        binding.btnBuffGroupModify.setOnClickListener {
            val intent = Intent(this, BuffMakeActivity::class.java)
            intent.putExtra(Const.DATA, mBuff)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        binding.btnBuffChangeOwner.setOnClickListener {
            val intent = Intent(this, BuffChangeOwnerActivity::class.java)
            intent.putExtra(Const.DATA, mBuff)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        binding.btnBuffInvite.setOnClickListener {
            val intent = Intent(this, BuffInviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        binding.btnBuffForcedExit.setOnClickListener {
            val intent = Intent(this, BuffForcedExitActivity::class.java)
            intent.putExtra(Const.DATA, mBuff)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        binding.layoutBuffPublicSetting.setOnClickListener {
            val intent = Intent(this, BuffPostHiddenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        binding.btnBuffExit.setOnClickListener {
            if(mBuff.owner == LoginInfoManager.getInstance().user.no){
                showAlert(R.string.msg_can_not_exit_group_owner)
                return@setOnClickListener
            }

            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_alarm))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_exit_buff_group), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {}

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val params = HashMap<String, String>()
                            params["buffRequestSeqNo"] = mBuff.seqNo.toString()
                            showProgress("")
                            ApiBuilder.create().exitBuff(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                                        response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    showAlert(R.string.msg_exit_buff_group)
                                    setResult(RESULT_OK)
                                    finish()
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                                       t: Throwable?,
                                                       response: NewResultResponse<Any>?) {
                                    hideProgress()
                                }
                            }).build().call()
                        }
                        else -> {}
                    }
                }
            }).builder().show(this)
        }

        binding.btnBuffPostReg.setOnClickListener {
            val intent = Intent(this, BuffPostRegActivity::class.java)
            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
            intent.putExtra(Const.BUFF, mBuff)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        binding.btnBuffPostHidden.setOnClickListener {
            val intent = Intent(this, BuffPostHiddenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        binding.buffFloating.setOnFloatingActionsMenuUpdateListener(object : FloatingActionsMenu.OnFloatingActionsMenuUpdateListener{
            override fun onMenuExpanded() {
                binding.viewBuffFloatingMask.visibility = View.VISIBLE
            }

            override fun onMenuCollapsed() {
                binding.viewBuffFloatingMask.visibility = View.GONE
            }
        })
        binding.viewBuffFloatingMask.setOnClickListener{
            binding.buffFloating.collapse()
        }

        mAdapter = BuffHeaderAdapter()
        mAdapter.launcher = launcher
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerBuff.layoutManager = mLayoutManager
        binding.recyclerBuff.adapter = mAdapter
        binding.recyclerBuff.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        binding.recyclerBuff.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = mLayoutManager.findFirstVisibleItemPosition()
                if (position > 1) {
                    binding.layoutBuffTitle.visibility = View.VISIBLE
//                    binding.layoutBuffFloatingReg.visibility = View.VISIBLE
//                    binding.buffFloating.visibility = View.GONE
                } else {
                    binding.layoutBuffTitle.visibility = View.GONE
//                    binding.layoutBuffFloatingReg.visibility = View.GONE
//                    binding.buffFloating.visibility = View.VISIBLE
                }
            }
        })
        setRetentionBol()
        getBuff()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mBuff = intent?.getParcelableExtra(Const.DATA)!!
        getBuff()
    }

    private fun getBuff() {
        val params = HashMap<String, String>()
        params["buffSeqNo"] = mBuff.seqNo.toString()

        showProgress("")
        ApiBuilder.create().getBuff(params).setCallback(object : PplusCallback<NewResultResponse<Buff>> {
            override fun onResponse(call: Call<NewResultResponse<Buff>>?,
                                    response: NewResultResponse<Buff>?) {
                hideProgress()
                if (response?.data != null) {
                    mBuff = response.data!!
                    mAdapter.mBuff = mBuff

                    var isOwner = false
                    if (mBuff.owner == LoginInfoManager.getInstance().user.no) {
                        isOwner = true
                    }

                    if(isOwner){
                        binding.btnBuffGroupModify.visibility = View.VISIBLE
                        binding.btnBuffChangeOwner.visibility = View.VISIBLE
                        binding.btnBuffForcedExit.visibility = View.VISIBLE
                        binding.btnBuffExit.visibility = View.GONE
                    }else{
                        binding.btnBuffGroupModify.visibility = View.GONE
                        binding.btnBuffChangeOwner.visibility = View.GONE
                        binding.btnBuffForcedExit.visibility = View.GONE
                        binding.btnBuffExit.visibility = View.VISIBLE

                    }

                    mPaging = 0
                    listCall(mPaging)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Buff>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Buff>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        mLockListView = true
        val params = HashMap<String, String>()
        params["buffSeqNo"] = mBuff.seqNo.toString()
        params["page"] = page.toString()
        showProgress("")
        ApiBuilder.create().getBuffPostList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuffPost>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuffPost>>>?,
                                    response: NewResultResponse<SubResultResponse<BuffPost>>?) {

                hideProgress()
                if (response?.data != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mAdapter.clear()
//                        binding.layoutBuffPostLoading.visibility = View.GONE
                        val totalCount = response.data!!.totalElements!!
//                        if (totalCount > 0) {
//                            binding.layoutBuffPostNotExist.visibility = View.GONE
//                        } else {
//                            binding.layoutBuffPostNotExist.visibility = View.VISIBLE
//                        }
                    }
                    mLockListView = false

                    val dataList = response.data!!.content!!
                    mAdapter.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<BuffPost>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<BuffPost>>?) {
                hideProgress()
            }
        }).build().call()
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getBuff()
        setRetentionBol()
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textBuffRetentionPoint.text = FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.point.toString())
                val buffPostPublic = LoginInfoManager.getInstance().user.buffPostPublic
                if(buffPostPublic == null || buffPostPublic){
                    binding.imageBuffPublicSetting.setImageResource(R.drawable.ic_buff_post_public)
                }else{
                    binding.imageBuffPublicSetting.setImageResource(R.drawable.ic_buff_post_hidden)
                }
            }
        })
    }
}