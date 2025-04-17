package com.lejel.wowbox.apps.luckybox.ui


import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxLoadingView
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxNotOpenAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.*
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.FragmentLuckyBoxNotOpenBinding
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [LuckyBoxNotOpenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LuckyBoxNotOpenFragment : BaseFragment<LuckyBoxContainerActivity>() {

    var mAdapter: LuckyBoxNotOpenAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }

    private var _binding: FragmentLuckyBoxNotOpenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentLuckyBoxNotOpenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var mLuckyBoxLoadingView: LuckyBoxLoadingView? = null

    override fun init() {

        mAdapter = LuckyBoxNotOpenAdapter()
        mLayoutManager = LinearLayoutManager(requireActivity())

        binding.recyclerLuckyBoxNotOpen.layoutManager = mLayoutManager
//        binding.recyclerLuckyBoxNotOpen.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_200))
        binding.recyclerLuckyBoxNotOpen.adapter = mAdapter!!
        mAdapter!!.listener = object : LuckyBoxNotOpenAdapter.OnItemClickListener{

            override fun use(item: LuckyBoxPurchaseItem) {
                val params = HashMap<String, String>()
                params["itemSeqNo"] = item.seqNo.toString()
                params["userKey"] = item.userKey!!
                params["type"] = "randomBox"
                showProgress("")
                ApiBuilder.create().openLuckyBoxPurchaseItem(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                    override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                            response: NewResultResponse<Any>?) {

                        setEvent(requireActivity(), "wowbox_open")
                        hideProgress()
                        mLuckyBoxLoadingView = LuckyBoxLoadingView()
                        mLuckyBoxLoadingView!!.isCancelable = false
                        mLuckyBoxLoadingView!!.show(childFragmentManager, "")
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

            override fun cancel(position: Int) {
                val item = mAdapter!!.getItem(position)
                showProgress("")
                ApiBuilder.create().cancelLuckyBox(item.seqNo!!).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                    override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                            response: NewResultResponse<Any>?) {
                        setEvent(requireActivity(), FirebaseAnalytics.Event.REFUND)
                        hideProgress()
                        if(!isAdded){
                            return
                        }

                        val builder = AlertBuilder.Builder()


                        if(item.paymentMethod == "cash"){
                            builder.setTitle(getString(R.string.word_cancel_complete))
                            builder.addContents(AlertData.MessageData(getString(R.string.msg_lucky_box_cancel_complete_desc1), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                            builder.addContents(AlertData.MessageData(getString(R.string.msg_lucky_box_cancel_complete_desc2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        }else{
                            builder.setTitle(getString(R.string.word_req_cancel_complete))
                            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                            builder.addContents(AlertData.MessageData(getString(R.string.msg_lucky_box_cancel_req_complete_desc1), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                            builder.addContents(AlertData.MessageData(getString(R.string.msg_lucky_box_cancel_req_complete_desc2), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
                        }

                        builder.setRightText(getString(R.string.word_confirm))
                        builder.builder().show(requireActivity())
                        listCall()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                           t: Throwable?,
                                           response: NewResultResponse<Any>?) {
                        if(!isAdded){
                            return
                        }
                        hideProgress()
                        if (response?.code == 510) {
                            showAlert(R.string.msg_expired_pay_cancel)
                        }else{
                            showAlert(R.string.msg_failed_pay_cancel)
                        }

                    }
                }).build().call()
            }
        }

        listCall()
    }

    private fun confirm(luckyBoxPurchaseItemSeqNo:Long){
        ApiBuilder.create().confirmLuckyBoxPurchaseItem(luckyBoxPurchaseItemSeqNo).setCallback(object : PplusCallback<NewResultResponse<LuckyBoxPurchaseItem>>{
            override fun onResponse(call: Call<NewResultResponse<LuckyBoxPurchaseItem>>?,
                                    response: NewResultResponse<LuckyBoxPurchaseItem>?) {
                if(!isAdded){
                    return
                }
                if(response?.result != null){
                    mLuckyBoxLoadingView!!.dismiss()
                    val intent = Intent(activity, LuckyBoxResultActivity::class.java)
                    intent.putExtra(Const.DATA, response.result)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    defaultLauncher.launch(intent)
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

    private fun listCall() {
        showProgress("")
        ApiBuilder.create().getNotOpenLuckyBoxPurchaseList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyBoxPurchase>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyBoxPurchase>>>?,
                                    response: NewResultResponse<ListResultResponse<LuckyBoxPurchase>>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                if(response?.result != null){
                    if(response.result!!.list!!.isEmpty()){
                        binding.layoutLuckyBoxNotOpenNotExist.visibility = View.VISIBLE
                    }else{
                        binding.layoutLuckyBoxNotOpenNotExist.visibility = View.GONE
                    }
                    binding.textLuckyBoxNotOpenTotalCount.text = getString(R.string.format_total_count, response.result!!.list!!.size.toString())
                    mAdapter!!.setDataList(response.result!!.list as MutableList<LuckyBoxPurchase>)
                }else{
                    binding.layoutLuckyBoxNotOpenNotExist.visibility = View.VISIBLE
                    binding.textLuckyBoxNotOpenTotalCount.text = getString(R.string.format_total_count, "0")
                }

            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyBoxPurchase>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<ListResultResponse<LuckyBoxPurchase>>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (!isAdded) {
            return@registerForActivityResult
        }

        if(result.resultCode == RESULT_OK && result.data != null && result.data!!.getIntExtra(Const.TAB, 0) == 1){
            getParentActivity().open()
        }else{
            listCall()
        }
    }

    override fun getPID(): String {
        return ""
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LuckyBoxNotOpenFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable(Const.DATA, luckyBox)
                    //                        putString(ARG_PARAM2, param2)
                }
            }
    }

}// Required empty public constructor
