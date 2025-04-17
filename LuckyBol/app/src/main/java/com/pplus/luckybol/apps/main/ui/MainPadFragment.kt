package com.pplus.luckybol.apps.main.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.common.ui.custom.dial.DialKey
import com.pplus.luckybol.apps.common.ui.custom.dial.DialPadView
import com.pplus.luckybol.apps.event.data.EventLoadingView
import com.pplus.luckybol.apps.event.ui.EventDetailActivity
import com.pplus.luckybol.apps.event.ui.EventMoveDetailActivity
import com.pplus.luckybol.apps.event.ui.EventResultActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventResult
import com.pplus.luckybol.core.network.model.dto.Page
import com.pplus.luckybol.core.network.model.dto.User
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentMainPadBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class MainPadFragment : BaseFragment<BaseActivity>() {

    private var _binding: FragmentMainPadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainPadBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var mTotalCount: Int = 0
    private var mNumberSb = StringBuilder()
    private var mPosition = 0
    var mPage: Page? = null

    override fun init() {

        binding.imageMainPadUpDown.setOnClickListener {
            if (!animating) {
                if (!binding.layoutMainNumberPad.isShown) {
                    scrollInAnimation()
                } else {
                    scrollOutAnimation()
                }
            }
        }

        binding.imageMainPadDelete.setOnClickListener {

            if (mNumberSb.isNotEmpty()) {
                mNumberSb.setLength(mNumberSb.length - 1);
                binding.textMainPadNumber.text = mNumberSb.toString()

            }

        }
        binding.imageMainPadDelete.setOnLongClickListener {
            mNumberSb = StringBuilder()
            binding.textMainPadNumber.text = mNumberSb.toString()
            false
        }


        val handler = Handler()
        handler.postDelayed({

            if (!isAdded) {
                return@postDelayed
            }

            binding.viewMainNumberPad.setOnKeyClickListener(object : DialPadView.OnKeyClickListener {
                override fun onClick(key: DialKey?, view: View?) {
                    if (!isAdded) {
                        return
                    }

                    if (!PplusCommonUtil.loginCheck(requireActivity(), null)) {
                        return
                    }

                    if (key!!.number == "#") {
                        moveNumber()
                    } else if (key.number != "*") {
                        if (mNumberSb.length < 12) {

                            mNumberSb.append(key.number)
                            binding.textMainPadNumber.text = mNumberSb.toString()
                        }
                    } else if (key.number == "*") {
                        mNumberSb = StringBuilder()
                        binding.textMainPadNumber.text = ""

//                        PplusCommonUtil.openChromeWebView(activity!!, getString(R.string.msg_number_guide_url) + "?timestamp=" + System.currentTimeMillis())
                    }
                }

                override fun onLongClick(key: DialKey?, view: View?) {

                    if (!PplusCommonUtil.loginCheck(requireActivity(), null)) {
                        return
                    }

                    if (key!!.number != "*" && key.number != "#") {
                        if (mNumberSb.length < 12) {
                            mNumberSb.append(key.number)
                            moveNumber()

                        }
                    }
                }
            })

        }, 500)

        mNumberSb = StringBuilder()
        if (StringUtils.isNotEmpty(number)) {
            mNumberSb.append(number)
            binding.textMainPadNumber.text = mNumberSb.toString()
        }

    }

    private fun moveNumber() {


        if (mNumberSb.isNotEmpty()) {

            if (mNumberSb.length < 9) {

                getEvent()
            }

//            when (mNumberSb.toString()) {
//
//                "1" -> {
//                    val intent = Intent(activity, PlusFeedActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//                "2" -> {
//                    val intent = Intent(activity, BuyHistoryActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//                "3" -> {
//                    val intent = Intent(activity, RecommendActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//                "1234" -> {
//                    val intent = Intent(activity, RankingActivity::class.java)
//                    intent.putExtra(Const.TYPE, EnumData.RankType.recommend.name)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//                "7979" -> {
//                    val intent = Intent(activity, UserFriendActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//
//                "4949" -> {
//                    val intent = Intent(activity, BuyHistoryActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//                "0000" -> {
//                    PplusCommonUtil.openChromeWebView(activity!!, getString(R.string.msg_ad_url))
//                }
//                "0002" -> {
//                    if (LoginInfoManager.getInstance().user.page == null) {
//                        val intent = Intent(activity, BizIntroduceActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivity(intent)
//                    } else {
//                        PplusCommonUtil.runOtherApp("com.pplus.prnumberbiz")
//                    }
//                }
//                "0003" -> {
//                    val intent = Intent(activity, HashTagActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//                else -> {
//                    if (mNumberSb.length < 9) {
//
//                        getEvent()
//                    } else {
//                        getPage(view)
//                    }
//                }
//            }

            mNumberSb = StringBuilder()
            binding.textMainPadNumber.text = ""

        }
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.set(0, mTopOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }

        }
    }

    private fun getEvent() {
        val params = HashMap<String, String>()
        params["search"] = mNumberSb.toString()
        showProgress("")
        ApiBuilder.create().getEventByNumber(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {

                hideProgress()

                if (!isAdded) {
                    return
                }

                if (response!!.data != null) {
                    val event = response.data!!

                    if (event.status != "active") {
                        val url = event.moveTargetString
                        if (StringUtils.isNotEmpty(url)) {
                            PplusCommonUtil.openChromeWebView(activity!!, url!!)
                        }
                        return
                    }

                    if (event.primaryType.equals(EventType.PrimaryType.insert.name)) {

//                        val plusTerms = LoginInfoManager.getInstance().user.plusTerms

//                        if (event.pageSeqNo != null) {
//                            if (plusTerms == null || !plusTerms) {
//                                val intent = Intent(activity, AlertPlusEventTermsActivity::class.java)
//                                intent.putExtra(Const.DATA, event)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                startActivity(intent)
//                            } else {
//                                joinEvent(event)
//                            }
//                        } else {
//                            joinEvent(event)
//                        }
                        joinEvent(event)
                    } else {
                        if (event.primaryType.equals(EventType.PrimaryType.move.name)) {
                            val intent = Intent(activity, EventMoveDetailActivity::class.java)
                            intent.putExtra(Const.DATA, event)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        } else {
                            if (StringUtils.isNotEmpty(event.eventLink)) {
                                val intent = Intent(activity, EventDetailActivity::class.java)
                                intent.putExtra(Const.DATA, event)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            } else {
//                                val plusTerms = LoginInfoManager.getInstance().user.plusTerms
//
//                                if (event.pageSeqNo != null) {
//                                    if (plusTerms == null || !plusTerms) {
//                                        val intent = Intent(activity, AlertPlusEventTermsActivity::class.java)
//                                        intent.putExtra(Const.DATA, event)
//                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                        startActivity(intent)
//                                    } else {
//                                        joinEvent(event)
//                                    }
//                                } else {
//                                    joinEvent(event)
//                                }
                                joinEvent(event)
                            }
                        }
                    }

                } else {
                    showAlert(R.string.msg_not_exist_pr_number)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {

                hideProgress()
                showAlert(R.string.msg_not_exist_pr_number)
            }
        }).build().call()
    }

    fun joinEvent(event: Event) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()
        showProgress("")

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?, response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response!!.data != null) {
                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        val mLoading = EventLoadingView()
                        mLoading.isCancelable = false
                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        var delayTime = 2000L
                        mLoading.isCancelable = false
                        try {
                            mLoading.show(parentFragmentManager, "")
                        } catch (e: Exception) {

                        }

                        val handler = Handler(Looper.myLooper()!!)
                        handler.postDelayed(Runnable {

                            try {
                                mLoading.dismiss()
                            } catch (e: Exception) {

                            }

                            val intent = Intent(activity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }, delayTime)

                    } else {
                        val intent = Intent(activity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?, t: Throwable?, response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response != null) {
                    PplusCommonUtil.showEventAlert(activity!!, response.resultCode, event, null)
                }
            }
        }).build().call()
    }

    var animating = false

    private fun scrollOutAnimation() {

        val scrollOutAnimator = ObjectAnimator.ofFloat(binding.layoutMainPadBottom, "translationY", 0f,
            binding.layoutMainNumberPad.height.toFloat()).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    animating = true
                }

                override fun onAnimationCancel(animation: Animator?) {
                    animating = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    animating = false
                    if (!isAdded) {
                        return
                    }

                    binding.imageMainPadUpDown.visibility = View.VISIBLE
                    binding.imageMainPadUpDown.setImageResource(R.drawable.btn_number_up)
                    binding.layoutMainNumberPad.visibility = View.GONE
                    val ani = ObjectAnimator.ofFloat(binding.layoutMainPadBottom, "translationY",
                            0f, 0f)
                    ani.start()

                }
            })
        }

        if (!animating) {
            scrollOutAnimator.start()
        }
    }

    fun scrollInAnimation() {
        val scrollInAnimator = ObjectAnimator.ofFloat(binding.layoutMainPadBottom, "translationY",
            binding.layoutMainNumberPad.height.toFloat(), 0f).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    if (!isAdded) {
                        return
                    }

                    binding.layoutMainNumberPad.visibility = View.VISIBLE
                    animating = true
                }

                override fun onAnimationCancel(animation: Animator?) {
                    animating = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    LogUtil.e(LOG_TAG, "onAnimationEnd : scroll in")

                    if (!isAdded) {
                        return
                    }
                    binding.imageMainPadUpDown.visibility = View.GONE
                    binding.imageMainPadUpDown.setImageResource(R.drawable.btn_number_down)
//                    getParentActivity().layout_main_floating.visibility = View.GONE
//                    unlockbar_main_pad?.img_center?.visibility = View.VISIBLE
//                    unlockbar_main_pad?.img_thumb?.visibility = View.VISIBLE

                    animating = false
                }
            })
        }

        if (!animating) {

            scrollInAnimator.start()
        }
    }

    override fun getPID(): String {
        return "Main_number"
    }

    var key: String? = null
    var number: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString(Const.KEY)
            number = it.getString(Const.NUMBER)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(key: String?, number: String?) =
                MainPadFragment().apply {
                    arguments = Bundle().apply {
                        putString(Const.KEY, key)
                        putString(Const.NUMBER, number)
                    }
                }
    }
}
