package com.root37.buflexz.apps.lottery.data

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Lottery
import com.root37.buflexz.core.network.model.dto.LotteryWin
import com.root37.buflexz.core.network.model.dto.LotteryWinCondition
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.HeaderLottoWinnerBinding
import com.root37.buflexz.databinding.ItemLotteryWinStatusBinding
import com.root37.buflexz.databinding.ItemLotteryWinnerBinding
import com.root37.buflexz.databinding.ItemLottoBinding
import retrofit2.Call


/**
 * Created by imac on 2018. 1. 8..
 */
class LotteryWinHeaderAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    var mLotteryWinCondition: LotteryWinCondition? = null
    var mLottery: Lottery? = null
    var mDataList: MutableList<LotteryWin>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun clickMyResult()
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): LotteryWin {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LotteryWin>? {

        return mDataList
    }

    fun add(data: LotteryWin) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LotteryWin>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LotteryWin) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<LotteryWin>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHeader(val binding: HeaderLottoWinnerBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    class ViewHolder(val binding: ItemLotteryWinnerBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
        }
    }

    override fun getItemCount(): Int {
        if (mLotteryWinCondition == null || mLottery == null) {
            return mDataList!!.size
        } else {
            return mDataList!!.size + 1
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {

            if (mLotteryWinCondition == null || mLottery == null) {
                return
            }

            if (LoginInfoManager.getInstance().isMember()) {
                holder.binding.layoutLottoWinnerMy.visibility = View.VISIBLE
                holder.binding.textLottoWinnerMyResult.text = holder.itemView.context.getString(R.string.msg_not_win)
                holder.binding.textLottoWinnerMyResult.setBackgroundResource(R.drawable.bg_303030_radius_55)
                holder.binding.textLottoWinnerMyResult.setOnClickListener { }
                ApiBuilder.create().getMyLotteryWinCount(mLottery!!.lotteryRound!!).setCallback(object : PplusCallback<NewResultResponse<Int>> {
                    override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                        if (response?.result != null && response.result!! > 0) {
                            holder.binding.textLottoWinnerMyResult.text = holder.itemView.context.getString(R.string.msg_confirm_result)
                            holder.binding.textLottoWinnerMyResult.setBackgroundResource(R.drawable.bg_48b778_radius_27)
                            holder.binding.textLottoWinnerMyResult.setOnClickListener {
                                listener?.clickMyResult()
                            }
                        }
                    }

                    override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {

                    }
                }).build().call()


                Glide.with(holder.itemView.context).load(LoginInfoManager.getInstance().member!!.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(holder.binding.imageLottoWinnerMyProfile)
                holder.binding.textLottoWinnerMyNickname.text = LoginInfoManager.getInstance().member!!.nickname
                Glide.with(holder.itemView.context).load(Uri.parse("file:///android_asset/flags/${LoginInfoManager.getInstance().member!!.nation!!.uppercase()}.png")).into(holder.binding.imageLottoWinnerMyFlag)

                val nation = NationManager.getInstance().nationMap!![LoginInfoManager.getInstance().member!!.nation]
                if (nation!!.code == "KR") {
                    holder.binding.textLottoWinnerMyNation.text = nation.name
                } else {
                    holder.binding.textLottoWinnerMyNation.text = nation.nameEn
                }

            } else {
                holder.binding.layoutLottoWinnerMy.visibility = View.GONE
            }

            holder.binding.layoutHeaderLottoWinnerStatus.removeAllViews()
            var lottoStatusBinding = ItemLotteryWinStatusBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
            lottoStatusBinding.textLotteryWinStatusGrade.text = holder.itemView.context.getString(R.string.word_grade_1)
            lottoStatusBinding.textLotteryWinStatusCount.text = holder.itemView.context.getString(R.string.format_count_unit4, mLotteryWinCondition!!.firstGrade.toString())
            lottoStatusBinding.layoutLotteryWinStatus.setBackgroundColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_2d2d2d))
            when (mLottery!!.firstType) {
                "point" -> {
                    lottoStatusBinding.textLotteryWinStatusTotalPrize.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(mLottery!!.firstMoney.toString()))
                    lottoStatusBinding.textLotteryWinStatusPrize.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(mLotteryWinCondition!!.firstMoney.toString()))
                }

                "lotto" -> {
                    lottoStatusBinding.textLotteryWinStatusTotalPrize.text = holder.itemView.context.getString(R.string.word_lotto_ticket_en)
                    lottoStatusBinding.textLotteryWinStatusPrize.text = holder.itemView.context.getString(R.string.format_count_unit6, FormatUtil.getMoneyType(mLotteryWinCondition!!.firstMoney.toString()))
                }
            }
            holder.binding.layoutHeaderLottoWinnerStatus.addView(lottoStatusBinding.root)

            lottoStatusBinding = ItemLotteryWinStatusBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
            lottoStatusBinding.textLotteryWinStatusGrade.text = holder.itemView.context.getString(R.string.word_grade_2)
            lottoStatusBinding.textLotteryWinStatusCount.text = holder.itemView.context.getString(R.string.format_count_unit4, mLotteryWinCondition!!.secondGrade.toString())
            when (mLottery!!.secondType) {
                "point" -> {
                    lottoStatusBinding.textLotteryWinStatusTotalPrize.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(mLottery!!.secondMoney.toString()))
                    lottoStatusBinding.textLotteryWinStatusPrize.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(mLotteryWinCondition!!.secondMoney.toString()))
                }

                "lotto" -> {
                    lottoStatusBinding.textLotteryWinStatusTotalPrize.text = holder.itemView.context.getString(R.string.word_lotto_ticket_en)
                    lottoStatusBinding.textLotteryWinStatusPrize.text = holder.itemView.context.getString(R.string.format_count_unit6, FormatUtil.getMoneyType(mLotteryWinCondition!!.secondMoney.toString()))
                }
            }
            holder.binding.layoutHeaderLottoWinnerStatus.addView(lottoStatusBinding.root)

            lottoStatusBinding = ItemLotteryWinStatusBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
            lottoStatusBinding.textLotteryWinStatusGrade.text = holder.itemView.context.getString(R.string.word_grade_3)
            lottoStatusBinding.textLotteryWinStatusCount.text = holder.itemView.context.getString(R.string.format_count_unit4, mLotteryWinCondition!!.thirdGrade.toString())
            lottoStatusBinding.layoutLotteryWinStatus.setBackgroundColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_2d2d2d))
            when (mLottery!!.thirdType) {
                "point" -> {
                    lottoStatusBinding.textLotteryWinStatusTotalPrize.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(mLottery!!.thirdMoney.toString()))
                    lottoStatusBinding.textLotteryWinStatusPrize.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(mLotteryWinCondition!!.thirdMoney.toString()))
                }

                "lotto" -> {
                    lottoStatusBinding.textLotteryWinStatusTotalPrize.text = holder.itemView.context.getString(R.string.word_lotto_ticket_en)
                    lottoStatusBinding.textLotteryWinStatusPrize.text = holder.itemView.context.getString(R.string.format_count_unit6, FormatUtil.getMoneyType(mLotteryWinCondition!!.thirdMoney.toString()))
                }
            }
            holder.binding.layoutHeaderLottoWinnerStatus.addView(lottoStatusBinding.root)

            lottoStatusBinding = ItemLotteryWinStatusBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
            lottoStatusBinding.textLotteryWinStatusGrade.text = holder.itemView.context.getString(R.string.format_grade_other, "4")
            lottoStatusBinding.textLotteryWinStatusCount.text = holder.itemView.context.getString(R.string.format_count_unit4, mLotteryWinCondition!!.forthGrade.toString())
            when (mLottery!!.forthType) {
                "point" -> {
                    lottoStatusBinding.textLotteryWinStatusTotalPrize.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(mLottery!!.forthMoney.toString()))
                    lottoStatusBinding.textLotteryWinStatusPrize.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(mLotteryWinCondition!!.forthMoney.toString()))
                }

                "lotto" -> {
                    lottoStatusBinding.textLotteryWinStatusTotalPrize.text = holder.itemView.context.getString(R.string.word_lotto_ticket_en)
                    lottoStatusBinding.textLotteryWinStatusPrize.text = holder.itemView.context.getString(R.string.format_count_unit6, FormatUtil.getMoneyType(mLotteryWinCondition!!.forthMoney.toString()))
                }
            }
            holder.binding.layoutHeaderLottoWinnerStatus.addView(lottoStatusBinding.root)

            lottoStatusBinding = ItemLotteryWinStatusBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
            lottoStatusBinding.textLotteryWinStatusGrade.text = holder.itemView.context.getString(R.string.format_grade_other, "5")
            lottoStatusBinding.textLotteryWinStatusCount.text = holder.itemView.context.getString(R.string.format_count_unit4, mLotteryWinCondition!!.fifthGrade.toString())
            lottoStatusBinding.layoutLotteryWinStatus.setBackgroundColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_2d2d2d))
            when (mLottery!!.fifthType) {
                "point" -> {
                    lottoStatusBinding.textLotteryWinStatusTotalPrize.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(mLottery!!.fifthMoney.toString()))
                    lottoStatusBinding.textLotteryWinStatusPrize.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(mLotteryWinCondition!!.fifthMoney.toString()))
                }

                "lotto" -> {
                    lottoStatusBinding.textLotteryWinStatusTotalPrize.text = holder.itemView.context.getString(R.string.word_lotto_ticket_en)
                    lottoStatusBinding.textLotteryWinStatusPrize.text = holder.itemView.context.getString(R.string.format_count_unit6, FormatUtil.getMoneyType(mLotteryWinCondition!!.fifthMoney.toString()))
                }
            }
            holder.binding.layoutHeaderLottoWinnerStatus.addView(lottoStatusBinding.root)


        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)

            when(item.grade){
                1->{
                    holder.binding.textLotteryWinnerGrade.text = holder.itemView.context.getString(R.string.word_grade_1)
                }
                2->{
                    holder.binding.textLotteryWinnerGrade.text = holder.itemView.context.getString(R.string.word_grade_2)
                }
                3->{
                    holder.binding.textLotteryWinnerGrade.text = holder.itemView.context.getString(R.string.word_grade_3)
                }
                else->{
                    holder.binding.textLotteryWinnerGrade.text = holder.itemView.context.getString(R.string.format_grade_other, item.grade.toString())
                }
            }

            holder.binding.textLotteryWinnerNickname.text = item.member?.nickname
            Glide.with(holder.itemView.context).load(Const.CDN_URL + "profile/${item.member!!.userKey}/index.html").apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(PplusCommonUtil.getDefaultProfile(position)).error(PplusCommonUtil.getDefaultProfile(position))).into(holder.binding.imageLotteryWinnerProfile)
            Glide.with(holder.itemView.context).load(Uri.parse("file:///android_asset/flags/${item.member!!.nation!!.uppercase()}.png")).into(holder.binding.imageLotteryWinnerFlag)

            val nation = NationManager.getInstance().nationMap!![item.member!!.nation]
            if (nation!!.code == "KR") {

            } else {

            }

            when (item.giftType) {
                "point" -> {
                    holder.binding.textLotteryWinnerPrize.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(item.money.toString()))
                }

                "lotto" -> {
                    holder.binding.textLotteryWinnerPrize.text = holder.itemView.context.getString(R.string.format_count_unit6, FormatUtil.getMoneyType(item.money.toString()))
                }
            }

            holder.binding.layoutLotteryWinnerNumber.removeAllViews()

            if (item.no1 != null) {
                val numberList = arrayListOf<Int>()
                numberList.add(item.no1!!)
                numberList.add(item.no2!!)
                numberList.add(item.no3!!)
                numberList.add(item.no4!!)
                numberList.add(item.no5!!)
                numberList.add(item.no6!!)

                val isWinList = arrayListOf<Boolean>()
                isWinList.add(item.winNo1!!)
                isWinList.add(item.winNo2!!)
                isWinList.add(item.winNo3!!)
                isWinList.add(item.winNo4!!)
                isWinList.add(item.winNo5!!)
                isWinList.add(item.winNo6!!)

                for (j in 0 until numberList.size) {
                    val lottoNumber = numberList[j]
                    val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
                    lottoBinding.textLottoNumber.text = lottoNumber.toString()

                    lottoBinding.textLottoNumber.layoutParams.width = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_90)
                    lottoBinding.textLottoNumber.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_90)

                    if (lottoNumber in 1..10) {
                        lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ffc046)
                    } else if (lottoNumber in 11..20) {
                        lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_457eef)
                    } else if (lottoNumber in 21..30) {
                        lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ff4e4e)
                    } else if (lottoNumber in 31..40) {
                        lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ad7aff)
                    } else {
                        lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_5ecb69)
                    }

                    val isAccord = isWinList[j]

                    if (item.grade == 2 && !isAccord) {
                        lottoBinding.textLottoNumber.isSelected = item.winAdd != null && item.winAdd!!
                    } else {
                        lottoBinding.textLottoNumber.isSelected = isAccord
                    }

                    holder.binding.layoutLotteryWinnerNumber.addView(lottoBinding.root)
                    if (j < numberList.size - 1) {
                        (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_6)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            val binding = HeaderLottoWinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHeader(binding)
        } else if (viewType == TYPE_ITEM) {
            val binding = ItemLotteryWinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

}