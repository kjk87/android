//package com.pplus.prnumberuser.apps.my.ui
//
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.graphics.Typeface
//import android.os.Bundle
//import android.view.View
//import android.widget.TextView
//import androidx.annotation.DimenRes
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.CountryConfigManager
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.my.data.RankingAdapter
//import com.pplus.prnumberuser.apps.recommend.ui.InviteActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.User
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.fragment_ranking.*
//import kotlinx.android.synthetic.main.header_ranking.*
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [RankingFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class RankingFragment : BaseFragment<BaseActivity>() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        rankType = arguments?.getString(Const.TYPE)!!
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_ranking
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    var durationType = EnumData.DurationType.week
//    var rankType = EnumData.RankType.recommend.name
//    var startWeek: String? = null
//    var endWeek: String? = null
//    var startDay: String? = null
//    var lastDay: String? = null
//
//    var mAdapter: RankingAdapter? = null
//
//    override fun init() {
//
//        val cal = Calendar.getInstance()
//        val thisMonth = SimpleDateFormat("yyyy-MM").format(cal.time)
//
//        startDay = "$thisMonth-01 00:00:00"
//        lastDay = "$thisMonth-${cal.getActualMaximum(Calendar.DAY_OF_MONTH)} 23:59:59"
//
//        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//            cal.add(Calendar.DAY_OF_WEEK, -1)
//        }
//
//        val df = SimpleDateFormat("yyyy-MM-dd")
//        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
//        startWeek = "${df.format(cal.time)} 00:00:00"
//
//        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
//        cal.add(Calendar.DAY_OF_WEEK, 1)
//        endWeek = "${df.format(cal.time)} 23:59:59"
//
//        layout_ranking_sort_recommend.setOnClickListener {
//            if (!text_ranking_sort_recommend.isSelected) {
//                setSelect(text_ranking_sort_recommend, text_ranking_sort_reward)
//                setSelect(text_ranking_weekly, text_ranking_monthly, text_ranking_accumulate)
//                text_ranking_title.setText(R.string.word_recommend_top10)
//                rankType = EnumData.RankType.recommend.name
//                durationType = EnumData.DurationType.week
//                getRanking()
//            }
//
//        }
//
//        layout_ranking_sort_reward.setOnClickListener {
//            if (!text_ranking_sort_reward.isSelected) {
//                setSelect(text_ranking_sort_reward, text_ranking_sort_recommend)
//                setSelect(text_ranking_weekly, text_ranking_monthly, text_ranking_accumulate)
//
//                text_ranking_title.setText(R.string.word_reward_top10)
//                rankType = EnumData.RankType.reward.name
//                durationType = EnumData.DurationType.week
//                getRanking()
//            }
//
//        }
//
//        text_ranking_weekly.setOnClickListener {
//            if (!text_ranking_weekly.isSelected) {
//                durationType = EnumData.DurationType.week
//                setSelect(text_ranking_weekly, text_ranking_monthly, text_ranking_accumulate)
//                getRanking()
//            }
//        }
//
//        text_ranking_monthly.setOnClickListener {
//            if (!text_ranking_monthly.isSelected) {
//                durationType = EnumData.DurationType.month
//                setSelect(text_ranking_monthly, text_ranking_weekly, text_ranking_accumulate)
//                getRanking()
//            }
//        }
//
//        text_ranking_accumulate.setOnClickListener {
//            if (!text_ranking_accumulate.isSelected) {
//                durationType = EnumData.DurationType.total
//                setSelect(text_ranking_accumulate, text_ranking_weekly, text_ranking_monthly)
//                getRanking()
//            }
//        }
//
//        text_my_ranking_invite.setOnClickListener {
//            if(!PplusCommonUtil.loginCheck(requireActivity(), null)){
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, InviteActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
////            share()
//        }
//
//        text_ranking_more.setOnClickListener {
//            if(!PplusCommonUtil.loginCheck(requireActivity(), null)){
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, InviteActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
////        text_my_ranking_recommend_history.setOnClickListener {
////            val intent = Intent(activity, RecommendHistoryActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
////        }
//
//        setSelect(text_ranking_sort_reward, text_ranking_sort_recommend)
//        setSelect(text_ranking_weekly, text_ranking_monthly, text_ranking_accumulate)
//
//        text_ranking_title.setText(R.string.word_reward_top10)
//        if (StringUtils.isEmpty(rankType)) {
//            rankType = EnumData.RankType.reward.name
//        }
//
//        durationType = EnumData.DurationType.week
//
//        mAdapter = RankingAdapter(requireActivity(), rankType)
//        recycler_ranking.layoutManager = LinearLayoutManager(activity)
//        recycler_ranking.adapter = mAdapter
//        recycler_ranking.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_225))
//
//        getRanking()
//    }
//
//    fun share() {
//        val recommendPoint = FormatUtil.getMoneyType(CountryConfigManager.getInstance().config.properties!!.recommendBol.toString())
//        val recommendKey = LoginInfoManager.getInstance().user.recommendKey
//        val text = "${getString(R.string.format_invite_description, recommendKey)}\n${getString(R.string.format_msg_invite_url, LoginInfoManager.getInstance().user.recommendKey)}"
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "text/plain"
//        intent.putExtra(Intent.EXTRA_TEXT, text)
//        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
//        startActivity(chooserIntent)
//    }
//
//    private fun setSelect(view1: TextView, view2: TextView) {
//
//        view1.typeface = Typeface.DEFAULT_BOLD
//        view2.typeface = Typeface.DEFAULT
//        view1.isSelected = true
//        view2.isSelected = false
//    }
//
//    private fun setSelect(view1: TextView, view2: TextView, view3: TextView) {
//        view1.typeface = Typeface.DEFAULT_BOLD
//        view2.typeface = Typeface.DEFAULT
//        view3.typeface = Typeface.DEFAULT
//        view1.isSelected = true
//        view2.isSelected = false
//        view3.isSelected = false
//    }
//
////    private fun getMyInviteRanking() {
////        val params = HashMap<String, String>()
////
////        when (durationType) {
////            EnumData.DurationType.week -> {
////                params["start"] = startWeek!!
////                params["end"] = endWeek!!
////            }
////
////            EnumData.DurationType.month -> {
////                params["start"] = startDay!!
////                params["end"] = lastDay!!
////            }
////        }
////
////        showProgress("")
////        ApiBuilder.create().myInviteRanking.setCallback(object : PplusCallback<NewResultResponse<User>> {
////            override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
////                hideProgress()
////                if (!isAdded) {
////                    return
////                }
////                if (response!!.data != null) {
////                    layout_my_ranking.visibility = View.VISIBLE
////                    setMyRanking(response.data)
////                } else {
////                    layout_my_ranking.visibility = View.GONE
////                }
////                getInviteRanking()
////            }
////
////            override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
////
////                hideProgress()
////                if (!isAdded) {
////                    return
////                }
////                layout_my_ranking.visibility = View.GONE
////                getInviteRanking()
////            }
////        }).build().call()
////    }
//
////    private fun getMyRewardRanking() {
////        val params = HashMap<String, String>()
////
////        when (durationType) {
////            EnumData.DurationType.week -> {
////                params["start"] = startWeek!!
////                params["end"] = endWeek!!
////            }
////
////            EnumData.DurationType.month -> {
////                params["start"] = startDay!!
////                params["end"] = lastDay!!
////            }
////        }
////
////        showProgress("")
////        ApiBuilder.create().myRewardRanking.setCallback(object : PplusCallback<NewResultResponse<User>> {
////            override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
////                if (!isAdded) {
////                    return
////                }
////
////                hideProgress()
////                if (response!!.data != null) {
////                    layout_my_ranking.visibility = View.VISIBLE
////                    setMyRanking(response.data)
////                } else {
////                    layout_my_ranking.visibility = View.GONE
////                }
////                getRewardRanking()
////            }
////
////            override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
////
////                hideProgress()
////                if (!isAdded) {
////                    return
////                }
////
////
////                layout_my_ranking.visibility = View.GONE
////                getRewardRanking()
////            }
////        }).build().call()
////    }
//
////    private fun setMyRanking(item: User) {
////        text_my_ranking_name.text = item.nickname!!
////
////        if (item.profileImage != null && StringUtils.isNotEmpty(item.profileImage!!.url)) {
////            Glide.with(this).load(item.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_my_ranking_profile)
////        } else {
////            image_my_ranking_profile.setImageResource(R.drawable.prnumber_default_img)
////        }
////
////        if (item.ranking == 1L) {
////            image_my_ranking.visibility = View.VISIBLE
////            text_my_ranking.visibility = View.GONE
////            image_my_ranking.setImageResource(R.drawable.ic_ranking_1)
////        } else if (item.ranking == 2L) {
////            image_my_ranking.visibility = View.VISIBLE
////            text_my_ranking.visibility = View.GONE
////            image_my_ranking.setImageResource(R.drawable.ic_ranking_2)
////        } else if (item.ranking == 3L) {
////            image_my_ranking.visibility = View.VISIBLE
////            text_my_ranking.visibility = View.GONE
////            image_my_ranking.setImageResource(R.drawable.ic_ranking_3)
////        } else {
////            image_my_ranking.visibility = View.GONE
////            text_my_ranking.visibility = View.VISIBLE
////            if (item.ranking!! > 999) {
////                text_my_ranking.text = "-"
////            } else {
////                text_my_ranking.text = item.ranking.toString()
////            }
////
////        }
////
////        when (rankType) {
////            EnumData.RankType.recommend.name -> {
////                text_my_ranking_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ranking_recommend_me, 0, 0, 0)
////                text_my_ranking_info.text = getString(R.string.format_count_unit2, FormatUtil.getMoneyType(item.rankingCount.toString()))
////            }
////            EnumData.RankType.reward.name -> {
////                text_my_ranking_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ranking_point, 0, 0, 0)
////                text_my_ranking_info.text = FormatUtil.getMoneyType(item.rankingCount.toString())
////            }
////        }
////    }
//
//    private fun getRanking() {
//        if (!isAdded) {
//            return
//        }
//        text_ranking_not_exist.visibility = View.GONE
//        mAdapter!!.clear()
//        mAdapter!!.rankType = rankType
//        when (rankType) {
//            EnumData.RankType.recommend.name -> {
////                getMyInviteRanking()
//                getInviteRanking()
//
//            }
//            EnumData.RankType.reward.name -> {
////                getMyRewardRanking()
//                getRewardRanking()
//
//            }
//        }
//    }
//
//
//    private fun getRewardRanking() {
//        val params = HashMap<String, String>()
//
//        when (durationType) {
//            EnumData.DurationType.week -> {
//                params["type"] = "week"
//            }
//        }
//
//        showProgress("")
//        ApiBuilder.create().getRewardRanking(params).setCallback(object : PplusCallback<NewResultResponse<User>> {
//            override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
//
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//                if (response!!.datas.size == 0) {
//                    text_ranking_not_exist.visibility = View.VISIBLE
//                    text_ranking_not_exist.setText(R.string.msg_not_exist_reward_ranking)
//                }
//                mAdapter!!.addAll(response!!.datas)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun getInviteRanking() {
//        val params = HashMap<String, String>()
//
//        when (durationType) {
//            EnumData.DurationType.week -> {
//                params["type"] = "week"
//            }
//        }
//
//        showProgress("")
//        ApiBuilder.create().getInviteRanking(params).setCallback(object : PplusCallback<NewResultResponse<User>> {
//            override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
//
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                if (response!!.datas.size == 0) {
//                    text_ranking_not_exist.visibility = View.VISIBLE
//                    text_ranking_not_exist.setText(R.string.msg_not_exist_recommend_ranking)
//                }
//                mAdapter!!.addAll(response.datas)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
//                outRect.bottom = mLastOffset
//            }
//
//        }
//    }
//
//    override fun getPID(): String {
//        return "Main_point_ranking"
//    }
//
//    companion object {
//
//        fun newInstance(rankType: String): RankingFragment {
//
//            val fragment = RankingFragment()
//            val args = Bundle()
//            args.putString(Const.TYPE, rankType)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
