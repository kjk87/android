package com.pplus.prnumberuser.apps.card.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.card.data.CardConfigAdapter
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Card
import com.pplus.prnumberuser.core.network.model.dto.Verification
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityCardConfigBinding
import retrofit2.Call

class CardConfigActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityCardConfigBinding

    override fun getLayoutView(): View {
        binding = ActivityCardConfigBinding.inflate(layoutInflater)
        return binding.root
    }

    var mAdapter: CardConfigAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mAdapter = CardConfigAdapter()
        binding.recyclerCardConfig.adapter = mAdapter
        binding.recyclerCardConfig.layoutManager = LinearLayoutManager(this)
        binding.recyclerCardConfig.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_200))

        mAdapter!!.setOnItemClickListener(object : CardConfigAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

            }

            override fun onRefresh() {
                listCall()
            }
        })

        binding.textCardConfigReg.setOnClickListener {
            cardReg()
        }

        binding.layoutCardConfigReg.setOnClickListener {
            cardReg()
        }

        listCall()
    }

    private fun cardReg() {

        if (LoginInfoManager.getInstance().user.verification!!.media != "external") {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_me_for_service), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {}
                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val intent = Intent(this@CardConfigActivity, VerificationMeActivity::class.java)
                            intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            verificationLauncher.launch(intent)
                        }
                    }
                }
            }).builder().show(this)
        } else {
            val intent = Intent(this, CardRegActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cardRegLauncher.launch(intent)
        }

    }

    private fun listCall() {
        showProgress("")
        ApiBuilder.create().cardList.setCallback(object : PplusCallback<NewResultResponse<Card>> {
            override fun onResponse(call: Call<NewResultResponse<Card>>?, response: NewResultResponse<Card>?) {
                hideProgress()
                if (response?.datas != null && response.datas.isNotEmpty()) {
                    binding.layoutCardConfigExist.visibility = View.VISIBLE
                    binding.layoutCardConfigNotExist.visibility = View.GONE
                    binding.textCardConfigReg.visibility = View.VISIBLE
                    mAdapter!!.setDataList(response.datas)
                } else {
                    binding.layoutCardConfigExist.visibility = View.GONE
                    binding.layoutCardConfigNotExist.visibility = View.VISIBLE
                    binding.textCardConfigReg.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Card>>?, t: Throwable?, response: NewResultResponse<Card>?) {
                hideProgress()
            }
        }).build().call()
    }

    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
                outRect.bottom = mLastOffset
            }
        }
    }

    val cardRegLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (LoginInfoManager.getInstance().user.setPayPassword == null || !LoginInfoManager.getInstance().user.setPayPassword!!) {
                val intent = Intent(this@CardConfigActivity, PayPasswordSetActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                changePasswordLauncher.launch(intent)
            }
            listCall()
        }
    }

    val checkPasswordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = Intent(this@CardConfigActivity, PayPasswordSetActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            changePasswordLauncher.launch(intent)
        }
    }

    val verificationChangePasswordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data

            if (data != null) {
                val verification = data.getParcelableExtra<Verification>(Const.VERIFICATION)
                val mobileNumber = data.getStringExtra(Const.MOBILE_NUMBER)
                if (LoginInfoManager.getInstance().user.mobile != mobileNumber) {
                    showAlert(R.string.msg_incorrect_joined_mobile_number)
                } else {
                    val intent = Intent(this@CardConfigActivity, PayPasswordSetActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    changePasswordLauncher.launch(intent)
                }
            }
        }
    }

    val changePasswordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
            }
        })
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_pay_card_config), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_config))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {

                        val builder = AlertBuilder.Builder()
                        builder.setLeftText(getString(R.string.word_cancel))
                        val contentsList = arrayListOf<String>()
                        if (LoginInfoManager.getInstance().user.setPayPassword != null && LoginInfoManager.getInstance().user.setPayPassword!!) {
                            builder.setContents(getString(R.string.msg_change_pay_password), getString(R.string.msg_init_pay_password))
                            contentsList.add(getString(R.string.msg_change_pay_password))
                            contentsList.add(getString(R.string.msg_init_pay_password))
                        } else {
                            builder.setContents(getString(R.string.word_set_pay_password))
                            contentsList.add(getString(R.string.word_set_pay_password))
                        }

                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.LIST -> {
                                        when (contentsList[event_alert.value - 1]) {
                                            getString(R.string.word_set_pay_password) -> {
                                                val intent = Intent(this@CardConfigActivity, PayPasswordSetActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                changePasswordLauncher.launch(intent)
                                            }
                                            getString(R.string.msg_change_pay_password) -> {
                                                val intent = Intent(this@CardConfigActivity, PayPasswordCheckActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                checkPasswordLauncher.launch(intent)
                                            }
                                            getString(R.string.msg_init_pay_password) -> {
                                                val intent = Intent(this@CardConfigActivity, VerificationMeActivity::class.java)
                                                intent.putExtra(Const.KEY, Const.VERIFICATION)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                verificationChangePasswordLauncher.launch(intent)
                                            }
                                        }
                                    }
                                }
                            }
                        }).builder().show(this@CardConfigActivity)

                    }
                }
            }
        }
    }
}
