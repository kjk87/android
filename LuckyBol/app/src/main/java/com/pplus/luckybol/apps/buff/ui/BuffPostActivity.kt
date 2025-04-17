package com.pplus.luckybol.apps.buff.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.buff.data.BuffPostAdapter
import com.pplus.luckybol.apps.common.component.RecyclerScaleScrollListener
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Buff
import com.pplus.luckybol.core.network.model.dto.BuffPost
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityBuffPostBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class BuffPostActivity : BaseActivity() {

    private lateinit var binding: ActivityBuffPostBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffPostBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mBuff: Buff
    private lateinit var mAdapter: BuffPostAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mIsLast = false
    private var mLockListView = false
    private var mPaging = 0

    override fun initializeView(savedInstanceState: Bundle?) {

        mBuff = intent.getParcelableExtra(Const.DATA)!!

        binding.imageBuffPostBack.setOnClickListener {
            val intent = Intent(this, BuffActivity::class.java)
            intent.putExtra(Const.DATA, mBuff)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            overridePendingTransition(R.anim.fix, R.anim.view_down)
        }

        mAdapter = BuffPostAdapter()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerBuffPost.layoutManager = mLayoutManager
        binding.recyclerBuffPost.adapter = mAdapter
        binding.recyclerBuffPost.addOnScrollListener(RecyclerScaleScrollListener(binding.layoutBuffPostFloatingReg))
        binding.recyclerBuffPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        binding.layoutBuffPostFloatingReg.setOnClickListener {
            val intent = Intent(this, BuffPostRegActivity::class.java)
            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        binding.layoutBuffPostPublicSetting.setOnClickListener {
            val intent = Intent(this, BuffPostHiddenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            reloadLauncher.launch(intent)
        }

        setRetentionBol()

        binding.layoutBuffPostLoading.visibility = View.VISIBLE
        mPaging = 0
        listCall(mPaging)
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
                        binding.layoutBuffPostLoading.visibility = View.GONE
                        val totalCount = response.data!!.totalElements!!
                        if (totalCount > 0) {
                            binding.layoutBuffPostNotExist.visibility = View.GONE
                        } else {
                            binding.layoutBuffPostNotExist.visibility = View.VISIBLE
                        }
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

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textBuffPostRetentionPoint.text = FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.point.toString())
                val buffPostPublic = LoginInfoManager.getInstance().user.buffPostPublic
                if(buffPostPublic == null || buffPostPublic){
                    binding.imageBuffPostPublicSetting.setImageResource(R.drawable.ic_buff_post_public)
                }else{
                    binding.imageBuffPostPublicSetting.setImageResource(R.drawable.ic_buff_post_hidden)
                }
            }
        })
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 0
        listCall(mPaging)
    }

    val reloadLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setRetentionBol()
    }
}