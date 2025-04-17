package com.pplus.prnumberuser.apps.mobilegift.ui


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
import com.pplus.prnumberuser.apps.bol.ui.CashExchangeActivity
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.mobilegift.data.MobileCategoryAdapter
import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.MobileCategory
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMainGiftishowBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [MainGiftishowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainGiftishowFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            //            mParam1 = arguments!!.getString(ARG_PARAM1)
        }
    }


    private var _binding: FragmentMainGiftishowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainGiftishowBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var mAdapter: MobileCategoryAdapter? = null

    override fun init() {

        binding.imageMainGiftishowBack.setOnClickListener {
            activity?.onBackPressed()
        }


        //        text_mobile_gift_retention_bol.setOnClickListener {
        //            val intent = Intent(activity, PointConfigActivity::class.java)
        //            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //            startActivity(intent)
        //        }
        binding.textMainGiftishowLogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        checkSignIn()

        binding.layoutMainGiftishowBuyHistory.setOnClickListener {
            val intent = Intent(activity, GiftishowHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.textMainGiftishowRetentionPoint.setOnClickListener {
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        mAdapter = MobileCategoryAdapter()
        binding.recyclerMainGiftishow.layoutManager = GridLayoutManager(activity, 3)
        binding.recyclerMainGiftishow.adapter = mAdapter

        binding.recyclerMainGiftishow.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_3))

        mAdapter?.listener = object : MobileCategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {

                if(position < mAdapter!!.itemCount-1){
                    val intent = Intent(activity, GiftishowActivity::class.java)
                    intent.putExtra(Const.CATEGORY, mAdapter?.getItem(position))
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    signInLauncher.launch(intent)
                }else{
//                    showAlert(R.string.msg_cash_exchange_alert)
                    val intent = Intent(activity, CashExchangeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    signInLauncher.launch(intent)
                }


            }
        }

        getCategory()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)

            if (position % 3 != 0) {
                outRect.set(mItemOffset, 0, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)


            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.setColor(ResourceUtil.getColor(activity, R.color.color_e8ecf0))
            paint.setStyle(Paint.Style.FILL)
            c.drawRoundRect(mItemOffset.toFloat(), mItemOffset.toFloat(), mItemOffset.toFloat(), mItemOffset.toFloat(), 0f, 0f, paint)


        }
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
            binding.textMainGiftishowTitle.visibility = View.VISIBLE
            binding.layoutMainGiftishowPoint.visibility = View.VISIBLE
            binding.textMainGiftishowLogin.visibility = View.GONE
            setRetentionBol()
        } else {
            binding.textMainGiftishowTitle.visibility = View.GONE
            binding.layoutMainGiftishowPoint.visibility = View.GONE
            binding.textMainGiftishowLogin.visibility = View.VISIBLE
        }
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                if (!isAdded) {
                    return
                }
                binding.textMainGiftishowRetentionPoint.text = getString(R.string.format_cash_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString()))
            }
        })
        getBuyCount()
    }

    private fun getBuyCount() {
        ApiBuilder.create().giftishowBuyCount.setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                    response: NewResultResponse<Int>?) {
                if (response?.data != null) {
                    binding.textMainGiftishowBuyHistoryCount.text = getString(R.string.format_count_unit2, FormatUtil.getMoneyType(response.data.toString()))
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Int>?) {

            }
        }).build().call()
    }

    private fun getCategory() {

        ApiBuilder.create().mobileCategoryList.setCallback(object : PplusCallback<NewResultResponse<MobileCategory>> {

            override fun onResponse(call: Call<NewResultResponse<MobileCategory>>?,
                                    response: NewResultResponse<MobileCategory>?) {
                if (!isAdded) {
                    return
                }

                if (response?.datas != null) {
                    mAdapter!!.setDataList(response.datas)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<MobileCategory>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<MobileCategory>?) {

            }
        }).build().call()
    }

    override fun getPID(): String {
        return "Main_Point shop"
    }

    companion object {

        fun newInstance(): MainGiftishowFragment {

            val fragment = MainGiftishowFragment()
            val args = Bundle()
            //            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

} // Required empty public constructor
