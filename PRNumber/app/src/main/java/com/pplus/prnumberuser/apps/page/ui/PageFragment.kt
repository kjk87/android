package com.pplus.prnumberuser.apps.page.ui


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.URLUtil
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.plus.ui.PlusInfoActivity
import com.pplus.prnumberuser.apps.product.ui.ProductShipDetailActivity
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.code.common.SnsTypeCode
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.PplusNumberUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.FragmentPageBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.info.OsUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap

class PageFragment : BaseFragment<PageActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPage = it.getParcelable(Const.PAGE)!!
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //    var mPageImagePagerAdapter: PageImagePagerAdapter? = null
    private lateinit var mPage: Page


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


        binding.imagePageBack.setOnClickListener {
            activity?.finish()
        }

        getPage()

    }

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

                binding.imagePageFacebook.visibility = View.GONE
                binding.imagePageTwitter.visibility = View.GONE
                binding.imagePageNaver.visibility = View.GONE
                binding.imagePageInstagram.visibility = View.GONE
                binding.imagePageHomepage.visibility = View.GONE
                binding.imagePageYoutube.visibility = View.GONE
                binding.imagePageKakaochannel.visibility = View.GONE

                val snsList = response.datas

                var existSns = false

                if (snsList != null && snsList.isNotEmpty()) {
                    for (sns in snsList) {
                        if (StringUtils.isNotEmpty(sns.url)) {
                            when (sns.type) {

                                SnsTypeCode.twitter.name -> {
                                    binding.imagePageTwitter.visibility = View.VISIBLE
                                    binding.imagePageTwitter.tag = sns
                                    binding.imagePageTwitter.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.facebook.name -> {
                                    binding.imagePageFacebook.visibility = View.VISIBLE
                                    binding.imagePageFacebook.tag = sns
                                    binding.imagePageFacebook.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.naver.name -> {
                                    binding.imagePageNaver.visibility = View.VISIBLE
                                    binding.imagePageNaver.tag = sns
                                    binding.imagePageNaver.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.instagram.name -> {
                                    binding.imagePageInstagram.visibility = View.VISIBLE
                                    binding.imagePageInstagram.tag = sns
                                    binding.imagePageInstagram.setOnClickListener(onSnsClickListener)
                                }
//                                SnsTypeCode.kakaoStory.name -> {
//                                    image_page_kakao.visibility = View.VISIBLE
//                                    image_page_kakao.tag = sns
//                                    image_page_kakao.setOnClickListener(onSnsClickListener)
//                                }
//                                SnsTypeCode.blog.name -> {
//                                    image_page_blog.visibility = View.VISIBLE
//                                    image_page_blog.isSelected = true
//                                    image_page_blog.tag = sns
//                                    image_page_blog.setOnClickListener(onSnsClickListener)
//                                }
                                SnsTypeCode.homepage.name -> {
                                    binding.imagePageHomepage.visibility = View.VISIBLE
                                    binding.imagePageHomepage.tag = sns
                                    binding.imagePageHomepage.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.youtube.name -> {
                                    binding.imagePageYoutube.visibility = View.VISIBLE
                                    binding.imagePageYoutube.tag = sns
                                    binding.imagePageYoutube.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.kakaoChannel.name -> {
                                    binding.imagePageKakaochannel.visibility = View.VISIBLE
                                    binding.imagePageKakaochannel.tag = sns
                                    binding.imagePageKakaochannel.setOnClickListener(onSnsClickListener)
                                }
                            }
                        }
                    }
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

    private fun snsEvent(sns: Sns) {
        // SNS 페이지 이동
        if (StringUtils.isNotEmpty(sns.url)) {
            // 계정으로 이동
            startActivity(PplusCommonUtil.getOpenSnsIntent(requireActivity(), SnsTypeCode.valueOf(sns.type), sns.url, sns.isLinkage))
        }
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

                if (mPage.virtualPage!!) {
                    binding.layoutPageInfo.visibility = View.GONE
                    binding.layoutPageNews.visibility = View.GONE
                    binding.layoutPageProduct.visibility = View.GONE
                    binding.layoutPageReview.visibility = View.GONE
                    binding.imagePagePlus.visibility = View.GONE

                    binding.imagePageNaver.visibility = View.VISIBLE
                    binding.imagePageNaver.setOnClickListener {
                        var url = mPage.homepageLink!!
                        if (!URLUtil.isValidUrl(url)) {
                            url = "http://$url"
                        }

                        PplusCommonUtil.openChromeWebView(activity!!, url)
                    }
                } else {

                    binding.layoutPageInfo.visibility = View.VISIBLE
                    binding.layoutPageNews.visibility = View.VISIBLE
                    binding.layoutPageProduct.visibility = View.VISIBLE
                    binding.layoutPageReview.visibility = View.VISIBLE

                    binding.textPageInfo.setOnClickListener {
                        val intent = Intent(activity, StoreInfoActivity::class.java)
                        intent.putExtra(Const.PAGE, mPage)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        activity?.startActivityForResult(intent, Const.REQ_DETAIL)
                    }

                    binding.layoutPageNews.setOnClickListener {
//                        val intent = Intent(activity, StoreNewsActivity::class.java)
//                        intent.putExtra(Const.PAGE, mPage)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        activity?.startActivityForResult(intent, Const.REQ_DETAIL)
                    }

                    binding.layoutPageProduct.setOnClickListener {
                        val intent = Intent(activity, StoreProductActivity::class.java)
                        intent.putExtra(Const.PAGE, mPage)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        activity?.startActivityForResult(intent, Const.REQ_DETAIL)
                    }

                    binding.layoutPageReview.setOnClickListener {
                        val intent = Intent(activity, StoreProductReviewActivity::class.java)
                        intent.putExtra(Const.PAGE, mPage)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        activity?.startActivityForResult(intent, Const.REQ_DETAIL)
                    }

                    getPlus()

                    getNewsCount()
                    getProductCount()
                    getProductReviewCount()
                    getSnsLink()
                    getActiveEvent()

                    if(mPage.mainProductPriceSeqNo != null){
                        getMainProductPrice()
                    }else{
                        binding.layoutPageMainProduct.visibility = View.INVISIBLE
                    }
                }

//                if(isFirst && LoginInfoManager.getInstance().isMember && getParentActivity().mKey == Const.PLUS_INFO && StringUtils.isNotEmpty(mPage.plusInfo)){
//                    val intent = Intent(activity, PlusInfoActivity::class.java)
//                    intent.putExtra(Const.DATA, mPage)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    plusLauncher.launch(intent)
//                }

            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable?, response: NewResultResponse<Page>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getMainProductPrice(){
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        ApiBuilder.create().getMainProductPrice(params).setCallback(object : PplusCallback<NewResultResponse<ProductPrice>> {
            override fun onResponse(
                call: Call<NewResultResponse<ProductPrice>>?,
                response: NewResultResponse<ProductPrice>?
            ) {
                if (response?.data != null) {
                    binding.layoutPageMainProduct.visibility = View.VISIBLE
                    val productPrice = response.data
                    if(productPrice.product!!.imageList != null && productPrice.product!!.imageList!!.isNotEmpty()){
                        Glide.with(activity!!).load(productPrice.product!!.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imagePageMainProduct)
                    }
                    if (productPrice.discountRatio != null && productPrice.discountRatio!!.toInt() > 0) {
                        binding.textPageMainProductDiscount.text = getString(R.string.format_percent3, productPrice.discountRatio!!.toInt().toString())
                        binding.textPageMainProductDiscount.visibility = View.VISIBLE
                    } else {
                        binding.textPageMainProductDiscount.visibility = View.GONE
                    }

                    binding.layoutPageMainProduct.setOnClickListener {
                        val intent = Intent(activity, ProductShipDetailActivity::class.java)
                        intent.putExtra(Const.DATA, productPrice)
                        startActivity(intent)
                    }

                }else{
                    binding.layoutPageMainProduct.visibility = View.INVISIBLE
                }

            }

            override fun onFailure(
                call: Call<NewResultResponse<ProductPrice>>?,
                t: Throwable?,
                response: NewResultResponse<ProductPrice>?
            ) {
                binding.layoutPageMainProduct.visibility = View.INVISIBLE
            }
        }).build().call()
    }

    var mPlus: Plus? = null

    private fun getPlus() {

        binding.imagePagePlus.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), plusLauncher)) {
                return@setOnClickListener
            }

            if (StringUtils.isNotEmpty(mPage.plusInfo)) {
                val intent = Intent(activity, PlusInfoActivity::class.java)
                intent.putExtra(Const.DATA, mPage)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                plusLauncher.launch(intent)
                return@setOnClickListener
            }

            if (!mPage.plus!!) {
                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_notice_alert))
                builder.addContents(AlertData.MessageData(getString(R.string.format_msg_question_plus, mPage.name), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                val params = Plus()
                                params.no = mPage.no
                                showProgress("")
                                ApiBuilder.create().insertPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {

                                    override fun onResponse(call: Call<NewResultResponse<Plus>>, response: NewResultResponse<Plus>) {

                                        hideProgress()
                                        ToastUtil.show(activity, getString(R.string.msg_plus_ing))
                                        mPage.plus = true
                                        binding.imagePagePlus.isSelected = mPage.plus!!
                                    }

                                    override fun onFailure(call: Call<NewResultResponse<Plus>>, t: Throwable, response: NewResultResponse<Plus>) {

                                        hideProgress()
                                    }
                                }).build().call()
                            }
                        }

                    }
                }).builder().show(activity)
            }else{
                val params = HashMap<String, String>()
                params["pageSeqNo"] = mPage.no.toString()
                showProgress("")
                ApiBuilder.create().getOnlyPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
                    override fun onResponse(call: Call<NewResultResponse<Plus>>?, response: NewResultResponse<Plus>?) {
                        hideProgress()
                        if (response?.data != null) {
                            mPlus = response.data

                            if (mPlus!!.plusGiftReceived != null && mPlus!!.plusGiftReceived!!) {
                                ToastUtil.showAlert(activity, getString(R.string.format_can_not_plus_released, mPage.name))
                                return
                            }

                            val builder = AlertBuilder.Builder()
                            builder.setTitle(getString(R.string.word_notice_alert))
                            builder.addContents(AlertData.MessageData(getString(R.string.format_msg_question_cancel_plus, mPage.name), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                            builder.setOnAlertResultListener(object : OnAlertResultListener {

                                override fun onCancel() {

                                }

                                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                    when (event_alert) {
                                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                                            val params = java.util.HashMap<String, String>()
                                            params["no"] = "" + mPage.no!!
                                            showProgress("")
                                            ApiBuilder.create().deletePlusByPage(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                                                override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                                                    hideProgress()
                                                    mPage.plus = false
                                                    binding.imagePagePlus.isSelected = mPage.plus!!
                                                }

                                                override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                                                    hideProgress()
                                                }
                                            }).build().call()
                                        }
                                    }
                                }
                            }).builder().show(activity)
                        }
                    }

                    override fun onFailure(call: Call<NewResultResponse<Plus>>?, t: Throwable?, response: NewResultResponse<Plus>?) {
                        hideProgress()
                    }
                }).build().call()
            }
        }
    }

    private fun getActiveEvent() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        ApiBuilder.create().getActiveEventByPageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
                if (response?.data != null) {
                    binding.layoutPageActiveEvent.visibility = View.VISIBLE
                    binding.layoutPageActiveEvent.setOnClickListener {
                        val intent = Intent(activity, PageEventDetailActivity::class.java)
                        intent.putExtra(Const.DATA, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        activity?.startActivityForResult(intent, Const.REQ_DETAIL)
                    }
                } else {
                    binding.layoutPageActiveEvent.visibility = View.GONE
                }

                getEventCount()
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {
                binding.layoutPageActiveEvent.visibility = View.GONE
                getEventCount()
            }
        }).build().call()
    }

    private fun getEventCount() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        ApiBuilder.create().getEventCountByPageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                if (response?.data != null && response.data > 0) {
                    binding.layoutPageEvent.visibility = View.VISIBLE
                    binding.layoutPageEvent.setOnClickListener {
                        val intent = Intent(activity, PageEventActivity::class.java)
                        intent.putExtra(Const.PAGE, mPage)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        activity?.startActivityForResult(intent, Const.REQ_DETAIL)
                    }
                } else {
                    binding.layoutPageEvent.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                binding.layoutPageEvent.visibility = View.GONE
            }
        }).build().call()
    }

    private fun getProductCount() {

        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        params["pageSeqNo"] = mPage.no.toString()
        if (mPage.storeType == "online") {
            params["salesType"] = EnumData.SalesType.SHIPPING.type.toString()
        } else {
            params["salesType"] = EnumData.SalesType.TICKET.type.toString()
        }
        showProgress("")
        ApiBuilder.create().getCountByPageSeqNoOnlyNormal(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    binding.textPageProduct.text = getString(R.string.format_goods, FormatUtil.getMoneyType(response.data.toString()))
                } else {
                    binding.textPageProduct.text = getString(R.string.format_goods, "0")
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
            }
        }).build().call()
    }

    private fun getNewsCount() {

        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        showProgress("")
        ApiBuilder.create().getNewsCountByPageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    binding.textPageNews.text = getString(R.string.format_news, FormatUtil.getMoneyType(response.data.toString()))
                } else {
                    binding.textPageNews.text = getString(R.string.format_news, "0")
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
            }
        }).build().call()
    }

    private fun getProductReviewCount() {

        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        showProgress("")
        ApiBuilder.create().getCountProductReviewByPageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    binding.textPageReview.text = getString(R.string.format_review, FormatUtil.getMoneyType(response.data.toString()))
                } else {
                    binding.textPageReview.text = getString(R.string.format_review, "0")
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
            }
        }).build().call()
    }

    private fun setData() {

        binding.textPageName.text = mPage.name


        if (mPage.numberList != null && mPage.numberList!!.isNotEmpty()) {
            binding.textPageNumber.text = PplusNumberUtil.getOnlyNumber(mPage.numberList!![0].virtualNumber)
        }

        if (StringUtils.isNotEmpty(mPage.backgroundImageUrl)) {
            Glide.with(requireActivity()).load(mPage.backgroundImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imagePageBackground)
        } else {
            if (mPage.categoryMajorSeqNo != null) {
                val params = HashMap<String, String>()
                params["seqNo"] = mPage.categoryMajorSeqNo.toString()
                ApiBuilder.create().getCategoryMajorOnly(params).setCallback(object : PplusCallback<NewResultResponse<CategoryMajor>> {
                    override fun onResponse(call: Call<NewResultResponse<CategoryMajor>>?, response: NewResultResponse<CategoryMajor>?) {
                        if (response?.data != null) {
                            Glide.with(activity!!).load(response.data.backgroundImage).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imagePageBackground)
                        }
                    }

                    override fun onFailure(call: Call<NewResultResponse<CategoryMajor>>?, t: Throwable?, response: NewResultResponse<CategoryMajor>?) {

                    }
                }).build().call()
            } else {
                ApiBuilder.create().defaultImageList.setCallback(object : PplusCallback<NewResultResponse<Attachment>> {
                    override fun onResponse(call: Call<NewResultResponse<Attachment>>?, response: NewResultResponse<Attachment>?) {
                        if (response?.datas != null && response.datas.isNotEmpty()) {
                            Glide.with(activity!!).load(response.datas[0].url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imagePageBackground)
                        } else {
                            binding.imagePageBackground.setImageResource(R.drawable.prnumber_default_img)
                        }

                    }

                    override fun onFailure(call: Call<NewResultResponse<Attachment>>?, t: Throwable?, response: NewResultResponse<Attachment>?) {

                    }
                }).build().call()
            }
        }

        binding.imagePagePlus.isSelected = mPage.plus!!

        binding.textPageCatchphrase.text = mPage.catchphrase


        if (StringUtils.isNotEmpty(mPage.phone)) {
            binding.textPageCall.visibility = View.VISIBLE
            binding.textPageCall.setOnClickListener {
                if (StringUtils.isNotEmpty(mPage.phone)) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage.phone!!))
                    startActivity(intent)
                }
            }
        } else {
            binding.textPageCall.visibility = View.GONE
        }

        binding.imagePageShare.setOnClickListener {
            var url = ""
            if(Const.API_URL.startsWith("https://api")){
                url = getString(R.string.format_page_url, mPage.no.toString())
            }else{
                url = getString(R.string.format_stage_page_url, mPage.no.toString())
            }
            val text = "${mPage.name}\n${url}"
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
            startActivity(chooserIntent)
        }

    }

    val plusLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getPage()
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance(page: Page) =
                PageFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Const.PAGE, page)
                    }
                }
    }
}
