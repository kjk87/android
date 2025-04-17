package com.pplus.prnumberuser.apps.page.ui


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.CountryConfigManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.page.data.PageImagePagerAdapter
import com.pplus.prnumberuser.apps.product.ui.ProductShipDetailActivity
import com.pplus.prnumberuser.apps.subscription.ui.SubscriptionDetailActivity
import com.pplus.prnumberuser.core.code.common.SnsTypeCode
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.FragmentOfflinePageBinding
import com.pplus.prnumberuser.databinding.ItemEvalBarBinding
import com.pplus.prnumberuser.databinding.ItemMainSubscriptionBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.info.OsUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.util.*

class OfflinePageFragment : BaseFragment<PageActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPage = it.getParcelable(Const.PAGE)!! //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentOfflinePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentOfflinePageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //    var mPageImagePagerAdapter: PageImagePagerAdapter? = null
    private lateinit var mPage: Page
    var mPageImagePagerAdapter: PageImagePagerAdapter? = null
    private var mPageImageList: MutableList<PageImage>? = null

    private fun revealShow(view: View, x: Int, y: Int) {

        val w = view.width
        val h = view.height

        val endRadius = (Math.max(w, h) * 1.1);

        val revealAnimator = ViewAnimationUtils.createCircularReveal(view, x, y, 0f, endRadius.toFloat())

        view.visibility = View.VISIBLE

        revealAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })
        revealAnimator.duration = 700
        revealAnimator.start()

    }

    override fun init() {
        LogUtil.e(LOG_TAG, "init")
        if (OsUtil.isLollipop()) {
            val viewTreeObserver = binding.root.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        revealShow(binding.root, getParentActivity().x, getParentActivity().y)
                        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        }

        binding.textOfflinePageBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.appbarOfflinePage.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (!isAdded) {
                    return
                }

                //                if (verticalOffset <= -collapsing_page.height + toolbar_store.height) {
                //                    //toolbar is collapsed here
                //                    //write your code here
                //                    text_store_name2.visibility = View.VISIBLE
                ////                    layout_page_plus.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_140)
                //                } else {
                //                    text_store_name2.visibility = View.GONE
                ////                    layout_page_plus.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_230)
                //                }
            }
        })

        mPageImagePagerAdapter = PageImagePagerAdapter(requireActivity())
        binding.pagerOfflinePageIntroduceImage.adapter = mPageImagePagerAdapter

        binding.pagerOfflinePageIntroduceImage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                binding.indicatorOfflinePageIntroduceImage.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        mPageImagePagerAdapter!!.setListener(object : PageImagePagerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, IntroduceImageDetailActivity::class.java)
                intent.putExtra(Const.DATA, mPageImagePagerAdapter!!.getDataList() as ArrayList<PageImage>)
                intent.putExtra(Const.POSITION, position)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        })

        binding.textOfflinePageSubscriptionMore.setOnClickListener {
            val intent = Intent(activity, StoreSubscriptionProductActivity::class.java)
            intent.putExtra(Const.PAGE, mPage)
            intent.putExtra(Const.TYPE, Const.SUBSCRIPTION)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textOfflinePageMoneyProductMore.setOnClickListener {
            val intent = Intent(activity, StoreSubscriptionProductActivity::class.java)
            intent.putExtra(Const.PAGE, mPage)
            intent.putExtra(Const.TYPE, Const.PREPAYMENT)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textOfflinePageProductMore.setOnClickListener {
            val intent = Intent(activity, StoreProductActivity::class.java)
            intent.putExtra(Const.PAGE, mPage)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textOfflinePageReviewMore.setOnClickListener {
            val intent = Intent(activity, StoreProductReviewActivity::class.java)
            intent.putExtra(Const.PAGE, mPage)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        setData()
        getBusinessLicense()
        getPageImageList()
        getSnsLink()
        getLastSubscription()
        getLastMoneyProduct()
        getMainProductPrice()
        getAvgEval()
    }

    private fun getPage() {

        val params = HashMap<String, String>()
        params["no"] = mPage.no.toString()
        showProgress("")
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                hideProgress()

                if (!isAdded) {
                    return
                }

                if (response.data != null) {
                    mPage = response.data!!
                    setData()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable?, response: NewResultResponse<Page>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getLastSubscription() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        ApiBuilder.create().getLastSubscriptionTypeByPageSeqNoOnlyNormal(params).setCallback(object : PplusCallback<NewResultResponse<ProductPrice>> {
            override fun onResponse(call: Call<NewResultResponse<ProductPrice>>?, response: NewResultResponse<ProductPrice>?) {
                if (response?.data != null) {
                    binding.layoutOfflinePageSubscription.visibility = View.VISIBLE
                    setSubscription(response.data, binding.itemOfflinePageSubscription)
                } else {
                    binding.layoutOfflinePageSubscription.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ProductPrice>>?, t: Throwable?, response: NewResultResponse<ProductPrice>?) {

            }
        }).build().call()
    }

    private fun getLastMoneyProduct() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        ApiBuilder.create().getLastMoneyTypeByPageSeqNoOnlyNormal(params).setCallback(object : PplusCallback<NewResultResponse<ProductPrice>> {
            override fun onResponse(call: Call<NewResultResponse<ProductPrice>>?, response: NewResultResponse<ProductPrice>?) {
                if (response?.data != null) {
                    binding.layoutOfflinePageMoneyProduct.visibility = View.VISIBLE
                    setSubscription(response.data, binding.itemOfflinePageMoneyProduct)
                } else {
                    binding.layoutOfflinePageMoneyProduct.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ProductPrice>>?, t: Throwable?, response: NewResultResponse<ProductPrice>?) {

            }
        }).build().call()
    }

    private fun setSubscription(item: ProductPrice, subscriptionBinding: ItemMainSubscriptionBinding) {

        subscriptionBinding.layoutMainSubscription.setOnClickListener {
            val intent = Intent(activity, SubscriptionDetailActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        if (item.isSubscription != null && item.isSubscription!!) {
            subscriptionBinding.layoutMainSubscriptionEnd.setBackgroundResource(R.drawable.bg_4694fb_right_radius_30)
            subscriptionBinding.textMainSubscriptionDiscountRatio.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.white))
            subscriptionBinding.textMainSubscriptionDesc.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.color_999999))
            subscriptionBinding.textMainSubscriptionDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_subscription_count, item.times.toString()))
        } else {
            subscriptionBinding.layoutMainSubscriptionEnd.setBackgroundResource(R.drawable.bg_ffcf5c_right_radius_30)
            subscriptionBinding.textMainSubscriptionDiscountRatio.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.color_4a3606))
            subscriptionBinding.textMainSubscriptionDesc.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.color_4a3606))
            subscriptionBinding.textMainSubscriptionDesc.text = getString(R.string.word_remain_money_manage_type)
        }

        subscriptionBinding.textMainSubscriptionName.text = item.product!!.name

        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                subscriptionBinding.textMainSubscriptionOriginPrice.visibility = View.GONE
            } else {
                subscriptionBinding.textMainSubscriptionOriginPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                subscriptionBinding.textMainSubscriptionOriginPrice.visibility = View.VISIBLE
            }

        } else {
            subscriptionBinding.textMainSubscriptionOriginPrice.visibility = View.GONE
        }

        //        holder.text_discount.visibility = View.GONE
        if (item.discountRatio != null && item.discountRatio!!.toInt() > 0) {
            subscriptionBinding.textMainSubscriptionDiscountRatio.visibility = View.VISIBLE
            subscriptionBinding.textMainSubscriptionDiscountRatio.text = PplusCommonUtil.fromHtml(getString(R.string.html_percent_unit2, item.discountRatio!!.toInt().toString()))
        } else {
            subscriptionBinding.textMainSubscriptionDiscountRatio.visibility = View.GONE
        }
        subscriptionBinding.textMainSubscriptionSalePrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))
    }

    private fun getMainProductPrice() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        ApiBuilder.create().getMainProductPrice(params).setCallback(object : PplusCallback<NewResultResponse<ProductPrice>> {
            override fun onResponse(call: Call<NewResultResponse<ProductPrice>>?, response: NewResultResponse<ProductPrice>?) {
                if (response?.data != null) {
                    binding.layoutOfflinePageMainProduct.visibility = View.VISIBLE
                    setMainProduct(response.data)
                } else {
                    binding.layoutOfflinePageMainProduct.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<NewResultResponse<ProductPrice>>?, t: Throwable?, response: NewResultResponse<ProductPrice>?) {
                binding.layoutOfflinePageMainProduct.visibility = View.GONE
            }
        }).build().call()
    }

    private fun setMainProduct(item: ProductPrice) {

        binding.itemMainTicket.layoutMainTicket.setOnClickListener {
            val intent = Intent(activity, ProductShipDetailActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        when (item.productType) {
            "lunch" -> {
                binding.itemMainTicket.textMainTicketType.text = getString(R.string.word_lunch_event)
            }
            "dinner" -> {
                binding.itemMainTicket.textMainTicketType.text = getString(R.string.word_dinner_event)
            }
            "time" -> {
                binding.itemMainTicket.textMainTicketType.text = getString(R.string.word_time_event)
            }
        }

        if (StringUtils.isNotEmpty(item.startTime) && StringUtils.isNotEmpty(item.endTime)) {
            binding.itemMainTicket.textMainTicketUseTime.text = getString(R.string.format_use_time2, item.startTime!!.substring(0, 5), item.endTime!!.substring(0, 5))
            binding.itemMainTicket.textMainTicketUseTime2.text = getString(R.string.format_use_time3, item.startTime!!.substring(0, 5), item.endTime!!.substring(0, 5))
            val purchaseWait = CountryConfigManager.getInstance().config.properties!!.purchaseWait!!
            val startMin = (item.startTime!!.split(":")[0].toInt() * 60) + item.startTime!!.split(":")[1].toInt()
            val purchaseMin = startMin - purchaseWait
            val hour = purchaseMin / 60
            val min = purchaseMin % 60
            val strHour = DateFormatUtils.formatTime(hour)
            val strMin = DateFormatUtils.formatTime(min)
            binding.itemMainTicket.textMainTicketPurchaseTime.text = getString(R.string.format_purchase_time, "00:00", "$strHour:$strMin")
        } else {
            binding.itemMainTicket.textMainTicketUseTime.text = ""
            binding.itemMainTicket.textMainTicketUseTime2.text = ""
            binding.itemMainTicket.textMainTicketPurchaseTime.text = ""
        }

        if (item.product!!.imageList != null && item.product!!.imageList!!.isNotEmpty()) {
            Glide.with(requireActivity()).load(item.product!!.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.itemMainTicket.imageMainTicketImage)
        } else {
            binding.itemMainTicket.imageMainTicketImage.setImageResource(R.drawable.prnumber_default_img)
        }

        if (item.isPoint != null && item.isPoint!! && item.point != null && item.point!! > 0) {
            binding.itemMainTicket.textMainTicketSavePoint.visibility = View.VISIBLE
            binding.itemMainTicket.textMainTicketSavePoint.text = getString(R.string.format_cash_unit2, FormatUtil.getMoneyType(item.point!!.toInt().toString()))
        } else {
            binding.itemMainTicket.textMainTicketSavePoint.visibility = View.GONE
        }

        binding.itemMainTicket.textMainTicketName.text = item.product!!.name

        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                binding.itemMainTicket.textMainTicketOriginPrice.visibility = View.GONE
            } else {
                binding.itemMainTicket.textMainTicketOriginPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                binding.itemMainTicket.textMainTicketOriginPrice.paintFlags = binding.itemMainTicket.textMainTicketOriginPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.itemMainTicket.textMainTicketOriginPrice.visibility = View.VISIBLE
            }

        } else {
            binding.itemMainTicket.textMainTicketOriginPrice.visibility = View.GONE
        }

        //        holder.text_discount.visibility = View.GONE
        if (item.discountRatio != null && item.discountRatio!!.toInt() > 0) {
            binding.itemMainTicket.textMainTicketDiscountRatio.visibility = View.VISIBLE
            binding.itemMainTicket.textMainTicketDiscountRatio.text = getString(R.string.format_percent, item.discountRatio!!.toInt().toString())
        } else {
            binding.itemMainTicket.textMainTicketDiscountRatio.visibility = View.GONE
        }
        binding.itemMainTicket.textMainTicketSalePrice.text = PplusCommonUtil.fromHtml(getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString())))

        var isSoldOut = false

        if (item.dailyCount != null && item.dailyCount!! > 0) {
            binding.itemMainTicket.layoutMainTicketRemainCount.visibility = View.VISIBLE
            var soldCount = 0
            if (item.dailySoldCount != null) {
                soldCount = item.dailySoldCount!!
            }

            val remainCount = item.dailyCount!! - soldCount

            if (remainCount > 0) {
                binding.itemMainTicket.layoutMainTicketRemainCount.visibility = View.VISIBLE
                binding.itemMainTicket.textMainTicketRemainCount.text = remainCount.toString()
            } else {
                binding.itemMainTicket.layoutMainTicketRemainCount.visibility = View.GONE
                isSoldOut = true
            }

        } else {
            binding.itemMainTicket.layoutMainTicketRemainCount.visibility = View.GONE
        }

        var isTimeOver = false
        binding.itemMainTicket.textMainTicketRemainCount.visibility = View.GONE
        if (StringUtils.isNotEmpty(item.startTime) && StringUtils.isNotEmpty(item.endTime)) {
            val startMin = (item.startTime!!.split(":")[0].toInt() * 60) + item.startTime!!.split(":")[1].toInt()
            val endMin = (item.endTime!!.split(":")[0].toInt() * 60) + item.endTime!!.split(":")[1].toInt()
            val cal = Calendar.getInstance()
            val currentMin = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
            val purchaseWait = CountryConfigManager.getInstance().config.properties!!.purchaseWait!!
            if (currentMin >= startMin - purchaseWait) {
                isTimeOver = true
            } else {
                val remainMin = startMin - purchaseWait - currentMin
                if (remainMin > 0) {
                    binding.itemMainTicket.textMainTicketRemainCount.visibility = View.VISIBLE
                    val remainMinMillis = remainMin.toLong() * 60 * 1000
                    val countTimer = object : CountDownTimer(remainMinMillis, 1000) {

                        override fun onTick(millisUntilFinished: Long) {
                            if (!isAdded) {
                                return
                            }
                            val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
                            val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                            val seconds = (millisUntilFinished / 1000).toInt() % 60

                            val strH = DateFormatUtils.formatTime(hours)
                            val strM = DateFormatUtils.formatTime(minutes)
                            val strS = DateFormatUtils.formatTime(seconds)

                            try {
                                if (hours > 0) {
                                    binding.itemMainTicket.textMainTicketRemainCount.text = "$strH:$strM:$strS"
                                } else {
                                    if (minutes > 0) {
                                        binding.itemMainTicket.textMainTicketRemainCount.text = "$strM:$strS"
                                    } else {
                                        binding.itemMainTicket.textMainTicketRemainCount.text = strS
                                    }
                                }
                            } catch (e: Exception) {
                            }
                        }

                        override fun onFinish() {
                            if (!isAdded) {
                                return
                            }
                            try {
                                isTimeOver = true
                                binding.itemMainTicket.textMainTicketSoldStatus.setText(R.string.word_time_over_en)
                                binding.itemMainTicket.layoutMainTicketRemainCount.visibility = View.GONE
                                binding.itemMainTicket.layoutMainTicketSoldOutStatus.visibility = View.VISIBLE
                            } catch (e: Exception) {

                            }
                        }
                    }
                    countTimer.start()
                }
            }
        }

        if (isSoldOut || isTimeOver) {
            if (isSoldOut) {
                binding.itemMainTicket.textMainTicketSoldStatus.setText(R.string.word_sold_out_en)
            } else if (isTimeOver) {
                binding.itemMainTicket.textMainTicketSoldStatus.setText(R.string.word_time_over_en)
            }
            binding.itemMainTicket.layoutMainTicketRemainCount.visibility = View.GONE
            binding.itemMainTicket.layoutMainTicketSoldOutStatus.visibility = View.VISIBLE
        } else {
            binding.itemMainTicket.layoutMainTicketSoldOutStatus.visibility = View.GONE
        }
    }

    private fun getAvgEval() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        ApiBuilder.create().getProductReviewCountGroupByEvalByPageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<ReviewCountEval>> {
            override fun onResponse(call: Call<NewResultResponse<ReviewCountEval>>?, response: NewResultResponse<ReviewCountEval>?) {

                if (response?.datas != null) {
                    val productReviewCountEvalList = response.datas
                    binding.layoutOfflinePageAvgEval.visibility = View.VISIBLE
                    binding.layoutOfflinePageAvgEvalBar.removeAllViews()
                    if (mPage.avgEval == null) {
                        mPage.avgEval = 0.0f
                    }
                    val eval = String.format("%.1f", mPage.avgEval)
                    binding.textOfflinePageAvgEval.text = eval
                    binding.gradeBarOfflinePageAvgEval.build(eval)
                    var totalCount = 0
                    for (productCountEval in productReviewCountEvalList) {
                        totalCount += productCountEval.count!!
                    }

                    for (productCountEval in productReviewCountEvalList) {

                        val evalBarBinding = ItemEvalBarBinding.inflate(LayoutInflater.from(requireActivity()), LinearLayout(requireActivity()), false)
                        binding.layoutOfflinePageAvgEvalBar.addView(evalBarBinding.root)
                        (evalBarBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.width_36)
                        if (productCountEval.count == 0) {
                            evalBarBinding.textEvalBarCount.visibility = View.INVISIBLE
                        } else {
                            evalBarBinding.textEvalBarCount.visibility = View.VISIBLE
                            evalBarBinding.textEvalBarCount.text = productCountEval.count.toString()
                        }

                        if (totalCount > 0) {
                            evalBarBinding.layoutEvalBarBg.weightSum = totalCount.toFloat()
                        }

                        (evalBarBinding.viewEvalBarRate.layoutParams as LinearLayout.LayoutParams).weight = (totalCount - productCountEval.count!!).toFloat()

                        evalBarBinding.textEvalBarEval.text = getString(R.string.format_eval, productCountEval.eval.toString())

                    }

                } else {
                    binding.layoutOfflinePageAvgEval.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ReviewCountEval>>?, t: Throwable?, response: NewResultResponse<ReviewCountEval>?) {
                binding.layoutOfflinePageAvgEval.visibility = View.GONE
            }
        }).build().call()
    }

    //    private fun getLastReview(){
    //        val params = HashMap<String, String>()
    //        params["pageSeqNo"] = mPage.no.toString()
    //        ApiBuilder.create().getLastProductReviewByPageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<ProductReview>> {
    //            override fun onResponse(call: Call<NewResultResponse<ProductReview>>?, response: NewResultResponse<ProductReview>?) {
    //                if (response?.data != null) {
    //                    layout_offline_page_review.visibility = View.VISIBLE
    //                    setProductReview(response.data)
    //                }else{
    //                    layout_offline_page_review.visibility = View.GONE
    //                }
    //            }
    //
    //            override fun onFailure(call: Call<NewResultResponse<ProductReview>>?, t: Throwable?, response: NewResultResponse<ProductReview>?) {
    //
    //            }
    //        }).build().call()
    //    }

    //    private fun setProductReview(item:ProductReview){
    //        if(item.member != null){
    //            holder.text_name.text = item.member!!.nickname
    //            if(item.member!!.profileAttachment != null){
    //                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.member!!.profileAttachment!!.id}")
    //                Glide.with(holder.itemView.context).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(holder.image_profile)
    //            }else{
    //                holder.image_profile.setImageResource(R.drawable.img_commerce_user_profile_default)
    //            }
    //        }
    //
    //        holder.text_contents.text = item.review
    //
    //        if (StringUtils.isNotEmpty(item.reviewReply)) {
    //            holder.layout_reply.visibility = View.VISIBLE
    //            holder.text_reply.text = item.reviewReply
    //            if(StringUtils.isNotEmpty(item.reviewReplyDate)){
    //                holder.text_reply_date.text = PplusCommonUtil.getDateFormat(item.reviewReplyDate!!)
    //            }
    //
    //        } else {
    //            holder.layout_reply.visibility = View.GONE
    //        }
    //
    //        if (item.eval != null) {
    //            val eval = String.format("%.1f", item.eval!!.toFloat())
    //            holder.grade_bar.build(eval)
    //        } else {
    //            val eval = String.format("%.1f", 0f)
    //            holder.grade_bar.build(eval)
    //        }
    //
    //        try {
    //            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
    //            val c = Calendar.getInstance()
    //            c.time = d
    //
    //            val year = c.get(Calendar.YEAR)
    //            val month = c.get(Calendar.MONTH)
    //            val day = c.get(Calendar.DAY_OF_MONTH)
    //
    //            if (mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
    //                val output = SimpleDateFormat("a HH:mm", Locale.getDefault())
    //                holder.text_regDate.text = output.format(d)
    //            } else {
    //                val output = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    //                holder.text_regDate.text = output.format(d)
    //            }
    //
    //        } catch (e: Exception) {
    //
    //        }
    //
    //        if (item.imageList != null && item.imageList!!.isNotEmpty()) {
    //            holder.layout_image.visibility = View.VISIBLE
    //            holder.pager_image.visibility = View.VISIBLE
    //            val imageAdapter = ProductReviewImagePagerAdapter(holder.itemView.context)
    //            imageAdapter.dataList = item.imageList as ArrayList<ProductReviewImage>
    //            holder.pager_image.adapter = imageAdapter
    //            holder.indicator.visibility = View.VISIBLE
    //            holder.indicator.removeAllViews()
    //            holder.indicator.build(LinearLayout.HORIZONTAL, item.imageList!!.size)
    //            holder.pager_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
    //
    //                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    //
    //                }
    //
    //                override fun onPageSelected(position: Int) {
    //
    //                    holder.indicator.setCurrentItem(position)
    //                }
    //
    //                override fun onPageScrollStateChanged(state: Int) {
    //
    //                }
    //            })
    //
    //            //            holder.pager_image.currentItem = if (viewPageStates.containsKey(position)) viewPageStates.get(position) else 0
    //            imageAdapter.setListener(object : ProductReviewImagePagerAdapter.OnItemClickListener{
    //                override fun onItemClick(position: Int) {
    //                    if (listener != null) {
    //                        listener!!.onItemClick(holder.adapterPosition - 1)
    //                    }
    //                }
    //            })
    //        } else {
    //            holder.indicator.removeAllViews()
    //            holder.indicator.visibility = View.GONE
    //            holder.pager_image.visibility = View.GONE
    //            holder.pager_image.adapter = null
    //            holder.layout_image.visibility = View.GONE
    //        }
    //    }

    private fun getSnsLink() {

        val params = HashMap<String, String>()
        params["no"] = "" + mPage.no!!
        showProgress("")
        ApiBuilder.create().getSnsLinkAll(params).setCallback(object : PplusCallback<NewResultResponse<Sns>> {

            override fun onResponse(call: Call<NewResultResponse<Sns>>, response: NewResultResponse<Sns>) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                binding.imageOfflinePageFacebook.visibility = View.GONE
                binding.imageOfflinePageTwitter.visibility = View.GONE
                binding.imageOfflinePageNaver.visibility = View.GONE
                binding.imageOfflinePageInstagram.visibility = View.GONE
                binding.imageOfflinePageHomepage.visibility = View.GONE
                binding.imageOfflinePageYoutube.visibility = View.GONE
                binding.imageOfflinePageKakaochannel.visibility = View.GONE

                val snsList = response.datas

                if (snsList != null && snsList.isNotEmpty()) {
                    binding.layoutOfflinePageSns.visibility = View.VISIBLE
                    var isEmpty = true
                    for (sns in snsList) {
                        if (StringUtils.isNotEmpty(sns.url)) {
                            when (sns.type) {
                                SnsTypeCode.twitter.name -> {
                                    isEmpty = false
                                    binding.imageOfflinePageTwitter.visibility = View.VISIBLE
                                    binding.imageOfflinePageTwitter.tag = sns
                                    binding.imageOfflinePageTwitter.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.facebook.name -> {
                                    isEmpty = false
                                    binding.imageOfflinePageFacebook.visibility = View.VISIBLE
                                    binding.imageOfflinePageFacebook.tag = sns
                                    binding.imageOfflinePageFacebook.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.naver.name -> {
                                    isEmpty = false
                                    binding.imageOfflinePageNaver.visibility = View.VISIBLE
                                    binding.imageOfflinePageNaver.tag = sns
                                    binding.imageOfflinePageNaver.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.instagram.name -> {
                                    isEmpty = false
                                    binding.imageOfflinePageInstagram.visibility = View.VISIBLE
                                    binding.imageOfflinePageInstagram.tag = sns
                                    binding.imageOfflinePageInstagram.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.homepage.name -> {
                                    isEmpty = false
                                    binding.imageOfflinePageHomepage.visibility = View.VISIBLE
                                    binding.imageOfflinePageHomepage.tag = sns
                                    binding.imageOfflinePageHomepage.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.youtube.name -> {
                                    isEmpty = false
                                    binding.imageOfflinePageYoutube.visibility = View.VISIBLE
                                    binding.imageOfflinePageYoutube.tag = sns
                                    binding.imageOfflinePageYoutube.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.kakaoChannel.name -> {
                                    isEmpty = false
                                    binding.imageOfflinePageKakaochannel.visibility = View.VISIBLE
                                    binding.imageOfflinePageKakaochannel.tag = sns
                                    binding.imageOfflinePageKakaochannel.setOnClickListener(onSnsClickListener)
                                }
                            }
                        }
                    }
                    if (isEmpty) {
                        binding.layoutOfflinePageSns.visibility = View.GONE
                    }
                } else {
                    binding.layoutOfflinePageSns.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Sns>>, t: Throwable, response: NewResultResponse<Sns>) {
                hideProgress()
            }
        }).build().call()
    }

    private val onSnsClickListener = View.OnClickListener { v ->
        val sns = v.tag as Sns
        snsEvent(sns)
    }

    private fun snsEvent(sns: Sns) { // SNS 페이지 이동
        if (StringUtils.isNotEmpty(sns.url)) { // 계정으로 이동
            startActivity(PplusCommonUtil.getOpenSnsIntent(requireActivity(), SnsTypeCode.valueOf(sns.type), sns.url, sns.isLinkage))
        }
    }

    private fun getPageImageList() {

        val params = HashMap<String, String>()
        params["no"] = mPage.no.toString()

        ApiBuilder.create().getPageImageAll(params).setCallback(object : PplusCallback<NewResultResponse<PageImage>> {
            override fun onResponse(call: Call<NewResultResponse<PageImage>>?, response: NewResultResponse<PageImage>?) {
                if (!isAdded) {
                    return
                }
                if (response?.datas != null && response.datas.isNotEmpty()) {
                    binding.collapsingOfflinePage.visibility = View.VISIBLE
                    mPageImageList = response.datas

                    if (mPageImageList!!.size > 1) {
                        binding.indicatorOfflinePageIntroduceImage.removeAllViews()
                        binding.indicatorOfflinePageIntroduceImage.visibility = View.VISIBLE
                        binding.indicatorOfflinePageIntroduceImage.build(LinearLayout.HORIZONTAL, mPageImageList!!.size)
                    } else {
                        binding.indicatorOfflinePageIntroduceImage.visibility = View.GONE
                    }

                    mPageImagePagerAdapter!!.setDataList(mPageImageList)
                } else {
                    binding.collapsingOfflinePage.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<NewResultResponse<PageImage>>?, t: Throwable?, response: NewResultResponse<PageImage>?) {

            }
        }).build().call()
    }

    private fun getBusinessLicense() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        showProgress("")
        ApiBuilder.create().getBusinessLicense(params).setCallback(object : PplusCallback<NewResultResponse<BusinessLicense>> {
            override fun onResponse(call: Call<NewResultResponse<BusinessLicense>>?, response: NewResultResponse<BusinessLicense>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response?.data != null) {
                    val businessLicense = response.data!!

                    binding.textOfflinePageCompanyName.text = businessLicense.companyName
                    binding.textOfflinePageCeo.text = businessLicense.ceo
                    binding.textOfflinePageCompanyNumber.text = businessLicense.corporateNumber
                    binding.textOfflinePageCompanyAddress.text = businessLicense.companyAddress
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BusinessLicense>>?, t: Throwable?, response: NewResultResponse<BusinessLicense>?) {
                hideProgress()
            }
        }).build().call()
    }


    private fun setData() {

        binding.textOfflinePageName.text = mPage.name


        //        if (mPage.numberList != null && mPage.numberList!!.isNotEmpty()) {
        //            text_home_number.text = PplusNumberUtil.getPrNumberFormat(mPage.numberList!![0].number)
        //        }

        //        Glide.with(activity!!).load(mPage.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(image_store_profile)


        //        text_store_catchphrase.text = mPage.catchphrase

        if (StringUtils.isNotEmpty(mPage.introduction)) {
            binding.textOfflinePageIntroduction.text = mPage.introduction
        } else {
            binding.textOfflinePageIntroduction.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(mPage.openHours)) {
            binding.layoutOfflinePageOpenHours.visibility = View.VISIBLE
            binding.textOfflinePageOpeningHours.text = mPage.openHours
        } else {
            binding.layoutOfflinePageOpenHours.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(mPage.holiday)) {
            binding.layoutOfflinePageHoliday.visibility = View.VISIBLE
            binding.textOfflinePageHoliday.text = mPage.holiday
        } else {
            binding.layoutOfflinePageHoliday.visibility = View.GONE
        }

        if (mPage.address == null || StringUtils.isEmpty(mPage.address!!.roadBase)) {
            binding.layoutOfflinePageAddress.visibility = View.GONE
            binding.layoutOfflinePageMapOption.visibility = View.GONE //            layout_offline_page_map.visibility = View.GONE
        } else {
            binding.layoutOfflinePageAddress.visibility = View.VISIBLE
            var detailAddress = ""
            if (StringUtils.isNotEmpty(mPage.address!!.roadDetail)) {
                detailAddress = mPage.address!!.roadDetail!!
            }
            binding.textOfflinePageAddress.text = "${mPage.address!!.roadBase} $detailAddress"
            binding.textOfflinePageAddress.setOnClickListener {
                val intent = Intent(activity, LocationPageActivity::class.java)
                intent.putExtra(Const.PAGE, mPage)
                startActivity(intent)
            }

            //            if (mPage.latitude != null && mPage.longitude != null) {
            //                layout_offline_page_map.visibility = View.VISIBLE
            //
            //                map_offline_page.onCreate(null)
            //                map_offline_page.getMapAsync(object : OnMapReadyCallback {
            //                    override fun onMapReady(googMap: GoogleMap) {
            //                        LogUtil.e("GOOGLEMAP", "onMapReady")
            //                        googMap.uiSettings.isScrollGesturesEnabled = false
            //                        googMap.uiSettings.isZoomGesturesEnabled = false
            //                        val latLng = LatLng(mPage.latitude!!, mPage.longitude!!)
            //                        googMap.addMarker(MarkerOptions().position(latLng).title(mPage.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)))
            //                        googMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
            //
            //                        googMap.setOnMapClickListener {
            //                            val intent = Intent(activity, LocationPageActivity::class.java)
            //                            intent.putExtra(Const.PAGE, mPage)
            //                            startActivity(intent)
            //                        }
            //                    }
            //                })
            //
            //                image_offline_page_map_full.setOnClickListener {
            //                    val intent = Intent(activity, LocationPageActivity::class.java)
            //                    intent.putExtra(Const.PAGE, mPage)
            //                    startActivity(intent)
            //                }
            //            } else {
            //                layout_offline_page_map.visibility = View.GONE
            //            }
        }

        binding.layoutOfflinePageFindRoad.setOnClickListener {
            if (existDaummapApp()) {
                val uri = Uri.parse("daummaps://route?ep=${mPage.latitude},${mPage.longitude}&by=CAR");
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } else {
                val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=net.daum.android.map"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

        }

        binding.layoutOfflinePageCallTaxi.setOnClickListener {
            val uri = Uri.parse("https://t.kakao.com/launch?type=taxi&dest_lat=${mPage.latitude}&dest_lng=${mPage.longitude}&ref=pplus")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        binding.layoutOfflinePageNavigation.setOnClickListener {
            if (NaviClient.instance.isKakaoNaviInstalled(requireActivity())) {
                LogUtil.e("kakonavi", "카카오내비 앱으로 길안내 가능")
                startActivity(NaviClient.instance.navigateIntent(Location(mPage.name!!, mPage.longitude.toString(), mPage.latitude.toString()), NaviOption(coordType = CoordType.WGS84)))
            } else {
                LogUtil.e("kakonavi", "카카오내비 미설치: 웹 길안내 사용 권장")

                // 웹 브라우저에서 길안내
                // 카카오내비가 설치되지 않은 곳에서 활용할 수 있습니다.
                val uri = NaviClient.instance.navigateWebUrl(Location(mPage.name!!, mPage.longitude.toString(), mPage.latitude.toString()), NaviOption(coordType = CoordType.WGS84))

                // CustomTabs로 길안내
                KakaoCustomTabsClient.openWithDefault(requireActivity(), uri)
            }
        }

        binding.layoutOfflinePageCopyAddress.setOnClickListener {
            if (mPage.address != null && StringUtils.isNotEmpty(mPage.address!!.roadBase)) {
                var detailAddress = ""
                if (StringUtils.isNotEmpty(mPage.address!!.roadDetail)) {
                    detailAddress = mPage.address!!.roadDetail!!
                }

                val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                val clip = ClipData.newPlainText("address", "${mPage.address!!.roadBase} $detailAddress")
                clipboard.setPrimaryClip(clip)
                ToastUtil.show(activity, R.string.msg_copied_clipboard)
            } else {
                ToastUtil.showAlert(activity, getString(R.string.msg_not_exist_address))
            }
        }

        if (StringUtils.isNotEmpty(mPage.phone)) {
            binding.layoutOfflinePagePhone.visibility = View.VISIBLE
            binding.textOfflinePagePhone.text = mPage.phone
            binding.textOfflinePagePhone.setOnClickListener {
                if (StringUtils.isNotEmpty(mPage.phone)) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage.phone!!))
                    startActivity(intent)
                }
            }
        } else {
            binding.layoutOfflinePagePhone.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(mPage.email)) {
            binding.layoutOfflinePageEmail.visibility = View.VISIBLE
            binding.textOfflinePageEmail.text = mPage.email
        } else {
            binding.layoutOfflinePageEmail.visibility = View.GONE
        }

    }

    private fun existDaummapApp(): Boolean {
        val pm = requireActivity().packageManager

        try {
            return (pm.getPackageInfo("net.daum.android.map", PackageManager.GET_SIGNING_CERTIFICATES) != null)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance(page: Page) = OfflinePageFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Const.PAGE, page)
            }
        }
    }
}
