package com.pplus.luckybol.apps.event.ui


import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.bol.ui.BolConfigActivity
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.data.RandomPlayHeaderGiftAdapter
import com.pplus.luckybol.apps.signin.ui.SnsLoginActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventGift
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityRandomPlayDetailBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [RandomPlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RandomPlayFragment : BaseFragment<BaseActivity>() {

    // TODO: Rename and change types of parameters


    var mEvent: Event? = null
    var mAdapter: RandomPlayHeaderGiftAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            mGroupNo = arguments!!.getInt(Const.GROUP)
//        }
    }

    private var _binding: ActivityRandomPlayDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = ActivityRandomPlayDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        binding.imageRandomPlayBack.setOnClickListener {
            activity?.onBackPressed()
        }

        mAdapter = RandomPlayHeaderGiftAdapter()



        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mAdapter!!.getItemViewType(position)) {
                    mAdapter!!.TYPE_HEADER -> 2
                    mAdapter!!.TYPE_ITEM -> 1
                    else -> 1
                }
            }
        })

        binding.recyclerRandomPlayDetail.layoutManager = gridLayoutManager
        binding.recyclerRandomPlayDetail.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_200))
        binding.recyclerRandomPlayDetail.adapter = mAdapter!!

        binding.textRandomPlayDetailJoin.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }

            if (mEvent != null && mEvent!!.rewardPlay != null && mEvent!!.rewardPlay!! > 0) {

                val intent = Intent(activity, EventBuyActivity::class.java)
                intent.putExtra(Const.EVENT, mEvent)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                cashChangeLauncher.launch(intent)
            }
        }
        binding.layoutRandomPlayLoading.visibility = View.VISIBLE

        binding.textRandomPlayRetentionBol.setOnClickListener {
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textRandomPlayLogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        if (LoginInfoManager.getInstance().isMember) {
            setRetentionBol()
            binding.textRandomPlayRetentionBol.visibility = View.VISIBLE
            binding.textRandomPlayLogin.visibility = View.GONE
        } else {
            binding.textRandomPlayRetentionBol.visibility = View.GONE
            binding.textRandomPlayLogin.visibility = View.VISIBLE
        }

        getRandomPlayEvent()
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if (!isAdded) {
                    return
                }
                binding.textRandomPlayRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_bol_unit, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
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

    private fun getRandomPlayEvent() {
        val params = HashMap<String, String>()
        params["filter"] = EventType.PrimaryType.randomluck.name
        params["pg"] = "1"
        params["appType"] = Const.APP_TYPE
        params["platform"] = "aos"
        params["sz"] = "1"
        //        showProgress("")
        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>?,
                                    response: NewResultResponse<Event>?) {
                if (!isAdded) {
                    return
                }
                binding.layoutRandomPlayLoading.visibility = View.GONE
                if (response?.datas != null && response.datas!!.isNotEmpty()) {
                    mEvent = response.datas!![0]

                    binding.textRandomPlayDetailJoin.text = getString(R.string.format_random_join, FormatUtil.getMoneyTypeFloat(mEvent!!.rewardPlay.toString()))

                    mAdapter!!.mEvent = mEvent
                    getGiftAll()
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Event>?) {
                binding.layoutRandomPlayLoading.visibility = View.GONE
            }
        }).build().call()
    }

    private fun getGiftAll() {
        val params = HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
            override fun onResponse(call: Call<NewResultResponse<EventGift>>?,
                                    response: NewResultResponse<EventGift>?) {
                hideProgress()
                if (response?.datas != null) {
                    mAdapter!!.setDataList(response.datas!! as MutableList<EventGift>)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventGift>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventGift>?) {
                hideProgress()
            }
        }).build().call()
    }

    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setRetentionBol()
        getRandomPlayEvent()
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (LoginInfoManager.getInstance().isMember) {
                setRetentionBol()
                binding.textRandomPlayRetentionBol.visibility = View.VISIBLE
                binding.textRandomPlayLogin.visibility = View.GONE
            } else {
                binding.textRandomPlayRetentionBol.visibility = View.GONE
                binding.textRandomPlayLogin.visibility = View.VISIBLE
            }
        }
    }

    override fun getPID(): String {
        return "Main_radnomevent"
    }

    companion object {

        fun newInstance(): RandomPlayFragment {

            val fragment = RandomPlayFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
