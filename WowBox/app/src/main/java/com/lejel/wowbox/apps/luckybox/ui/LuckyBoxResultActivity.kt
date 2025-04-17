package com.lejel.wowbox.apps.luckybox.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyBoxResultBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxLoadingView
import com.lejel.wowbox.apps.main.ui.MainActivity
import com.lejel.wowbox.core.network.model.dto.LuckyBox
import com.lejel.wowbox.core.network.model.dto.Product
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import retrofit2.Call

class LuckyBoxResultActivity : BaseActivity() {

    private lateinit var binding: ActivityLuckyBoxResultBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mLuckyBoxLoadingView:LuckyBoxLoadingView? = null
    var mLuckyBoxPurchaseItem:LuckyBoxPurchaseItem? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mLuckyBoxPurchaseItem = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBoxPurchaseItem::class.java)

        binding.textLuckyBoxResultMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textLuckyBoxResultContainer.setOnClickListener {
            val data = Intent()
            data.putExtra(Const.TAB, 1)
            setResult(RESULT_OK,data)
            finish()
        }

        setData()

    }

    private fun setData(){
        Glide.with(this).load(mLuckyBoxPurchaseItem!!.productImage).apply(RequestOptions().centerCrop()).into(binding.imageLuckyBoxResultProductImage)

        binding.textLuckyBoxResultProductName.text = mLuckyBoxPurchaseItem!!.productName
        binding.textLuckyBoxResultProductPrice.text = getString(R.string.format_origin_price, FormatUtil.getMoneyTypeFloat(mLuckyBoxPurchaseItem!!.productPrice.toString()))
        binding.layoutLuckyBoxResultProduct.setOnClickListener {
            if(mLuckyBoxPurchaseItem!!.productSeqNo != null){

                if(!packageManager.hasSystemFeature(PackageManager.FEATURE_WEBVIEW)){
                    return@setOnClickListener
                }

                val intent = Intent(this, LuckyBoxProductInfoActivity::class.java)
                val product = Product()
                product.seqNo = mLuckyBoxPurchaseItem!!.productSeqNo
                intent.putExtra(Const.DATA, product)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }

        }

        getLuckyBox()

        listCall()
    }

    private fun getLuckyBox(){
        ApiBuilder.create().getLuckyBox(mLuckyBoxPurchaseItem!!.luckyboxSeqNo!!).setCallback(object : PplusCallback<NewResultResponse<LuckyBox>>{
            override fun onResponse(call: Call<NewResultResponse<LuckyBox>>?, response: NewResultResponse<LuckyBox>?) {
                if(response?.result != null && response.result!!.provideBol != null && response.result!!.provideBol!! > 0 ){
                    binding.layoutLuckyBoxResultBonusBol.visibility = View.VISIBLE
                    binding.textLuckyBoxResultBonusBol.text = getString(R.string.format_save, FormatUtil.getMoneyType(response.result!!.provideBol.toString()))
                }else{
                    binding.layoutLuckyBoxResultBonusBol.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<LuckyBox>>?, t: Throwable?, response: NewResultResponse<LuckyBox>?) {

            }
        }).build().call()
    }

    private fun open(item : LuckyBoxPurchaseItem){
        val params = HashMap<String, String>()
        params["itemSeqNo"] = item.seqNo.toString()
        params["userKey"] = item.userKey!!
        params["type"] = "randomBox"
        showProgress("")
        ApiBuilder.create().openLuckyBoxPurchaseItem(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {
                hideProgress()
                mLuckyBoxLoadingView = LuckyBoxLoadingView()
                mLuckyBoxLoadingView!!.isCancelable = false
                mLuckyBoxLoadingView!!.show(supportFragmentManager, "")
                val handler = Handler(Looper.myLooper()!!)
                handler.postDelayed({
                    confirm(item.seqNo!!)
                }, 2000)

            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall() {
        val params = HashMap<String, String>()
        params["luckyboxPurchaseSeqNo"] = mLuckyBoxPurchaseItem!!.luckyboxPurchaseSeqNo.toString()
        showProgress("")
        ApiBuilder.create().getNotOpenLuckyBoxPurchaseItemListByLuckyBoxPurchaseSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>>?, response: NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>?) {
                hideProgress()
                if(response?.result != null && response.result!!.list != null && response.result!!.list!!.isNotEmpty()){
                    binding.textLuckyBoxResultNextBox.visibility = View.VISIBLE
                    binding.textLuckyBoxResultNextBox.text = getString(R.string.format_next_lucky_box, response.result!!.list!!.size.toString())
                    binding.textLuckyBoxResultNextBox.setOnClickListener {
                        open(response.result!!.list!![0])
                    }
                }else{
                    binding.textLuckyBoxResultNextBox.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun confirm(luckyBoxPurchaseItemSeqNo:Long){
        val params = HashMap<String, String>()
        params["luckyBoxPurchaseItemSeqNo"] = luckyBoxPurchaseItemSeqNo.toString()
        ApiBuilder.create().confirmLuckyBoxPurchaseItem(luckyBoxPurchaseItemSeqNo).setCallback(object : PplusCallback<NewResultResponse<LuckyBoxPurchaseItem>>{
            override fun onResponse(call: Call<NewResultResponse<LuckyBoxPurchaseItem>>?,
                                    response: NewResultResponse<LuckyBoxPurchaseItem>?) {
                if(response?.result != null){
                    mLuckyBoxLoadingView!!.dismiss()
                    mLuckyBoxPurchaseItem = response.result
                    setData()

                }else{
                    val handler = Handler(Looper.myLooper()!!)
                    handler.postDelayed({
                        confirm(luckyBoxPurchaseItemSeqNo)
                    }, 1000)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<LuckyBoxPurchaseItem>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<LuckyBoxPurchaseItem>?) {

            }
        }).build().call()
    }
}