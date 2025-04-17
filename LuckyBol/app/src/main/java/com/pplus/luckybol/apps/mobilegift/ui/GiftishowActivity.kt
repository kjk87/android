package com.pplus.luckybol.apps.mobilegift.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.mobilegift.data.GiftishowAdapter
import com.pplus.luckybol.apps.mobilegift.data.MobileBrandAdapter
import com.pplus.luckybol.apps.point.ui.PointHistoryActivity
import com.pplus.luckybol.apps.signin.ui.SnsLoginActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Giftishow
import com.pplus.luckybol.core.network.model.dto.MobileBrand
import com.pplus.luckybol.core.network.model.dto.MobileCategory
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityGiftishowBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class GiftishowActivity : BaseActivity() {
    private lateinit var binding: ActivityGiftishowBinding

    override fun getLayoutView(): View {
        binding = ActivityGiftishowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mMobileBrandAdapter: MobileBrandAdapter? = null
    var mSelectBrand: MobileBrand? = null

    private var mAdapter: GiftishowAdapter? = null
    var mLayoutManager: LinearLayoutManager? = null
    private var mPage = 1
    private var mTotalCount = 0
    private var mLockListView = true
    private var mSort = "seqNo,desc"
    private var mPos = 0
    private var mIsLast = false

    override fun initializeView(savedInstanceState: Bundle?) {

        val mobileCategory = intent.getParcelableExtra<MobileCategory>(Const.CATEGORY)

        mMobileBrandAdapter = MobileBrandAdapter()
        binding.recyclerGiftishowBrand.adapter = mMobileBrandAdapter
        binding.recyclerGiftishowBrand.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerGiftishow.layoutManager = mLayoutManager
        mAdapter = GiftishowAdapter()
        binding.recyclerGiftishow.adapter = mAdapter
        binding.recyclerGiftishow.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var pastVisibleItems = 0
            var visibleItemCount = 0
            var totalItemCount = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })
        mAdapter!!.listener = object : GiftishowAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@GiftishowActivity, GiftishowDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
        mSort = "seqNo,desc"

        mMobileBrandAdapter!!.listener = object : MobileBrandAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, view: View) {
                mSelectBrand = mMobileBrandAdapter!!.getItem(position)
                binding.textGiftshowBrandName.text = mSelectBrand!!.name
                mPage = 0
                listCall(mPage)
            }
        }

        binding.textGiftishowBack.setOnClickListener {
            onBackPressed()
        }

        binding.textGiftishowLogin.setOnClickListener {
            val intent = Intent(this, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        checkSignIn()

        binding.textGiftishowRetentionPoint.setOnClickListener {
            val intent = Intent(this, PointHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }
        binding.textGiftishowCategoryName.text = mobileCategory!!.name

        getBrand(mobileCategory)
    }

    private fun getBrand(mobileCategory: MobileCategory) {
        val params = HashMap<String, String>()
        params["categorySeqNo"] = mobileCategory.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getMobileBrandList(params).setCallback(object : PplusCallback<NewResultResponse<MobileBrand>> {

            override fun onResponse(call: Call<NewResultResponse<MobileBrand>>?,
                                    response: NewResultResponse<MobileBrand>?) {
                hideProgress()
                if(response?.datas != null){
                    mMobileBrandAdapter!!.setDataList(response.datas!! as MutableList<MobileBrand>)
                    if(response.datas!!.isNotEmpty()){
                        mSelectBrand = response.datas!![0]
                        binding.textGiftshowBrandName.text = mSelectBrand!!.name

                        binding.layoutGiftishowLoading.visibility = View.VISIBLE

                        mPage = 0
                        listCall(mPage)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<MobileBrand>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<MobileBrand>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        if (mSelectBrand != null) {
            params["brandSeqNo"] = "" + mSelectBrand!!.seqNo
        }
        params["page"] = page.toString()
        params["sort"] = mSort
        mLockListView = true
        //        if (page != 1 || mPos == 0) {
        //            showProgress("")
        //        }
        ApiBuilder.create().getGiftishowListByBrand(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Giftishow>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Giftishow>>>?,
                                    response: NewResultResponse<SubResultResponse<Giftishow>>?) {

                binding.layoutGiftishowLoading.visibility = View.GONE
                if (response != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
//                        text_mobile_gift_total_count?.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))
                        mAdapter!!.clear()
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data!!.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Giftishow>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<Giftishow>>?) {
                mLockListView = false
                binding.layoutGiftishowLoading.visibility = View.GONE
            }

        }).build().call()
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {

                binding.textGiftishowRetentionPoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.point.toString())))
            }
        })
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            checkSignIn()
        }
    }

    private fun checkSignIn(){
        if (LoginInfoManager.getInstance().isMember) {
            binding.textGiftishowRetentionPoint.visibility = View.VISIBLE
            binding.textGiftishowCategoryName.visibility = View.VISIBLE
            binding.textGiftishowLogin.visibility = View.GONE
            setRetentionBol()
        } else {
            binding.textGiftishowRetentionPoint.visibility = View.GONE
            binding.textGiftishowCategoryName.visibility = View.GONE
            binding.textGiftishowLogin.visibility = View.VISIBLE
        }
    }
}