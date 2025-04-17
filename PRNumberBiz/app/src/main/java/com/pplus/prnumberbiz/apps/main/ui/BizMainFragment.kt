package com.pplus.prnumberbiz.apps.main.ui


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.core.view.GravityCompat
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.igaworks.adbrix.IgawAdbrix
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.ads.ui.AdvertiseActivity
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.goods.ui.GoodsRegActivity2
import com.pplus.prnumberbiz.apps.goods.ui.PlusGoodsDetailActivity
import com.pplus.prnumberbiz.apps.goods.ui.SellerApplyActivity
import com.pplus.prnumberbiz.apps.keyword.ui.KeywordActivity
import com.pplus.prnumberbiz.apps.main.data.BizMainHeaderAdapter
import com.pplus.prnumberbiz.apps.marketing.ui.SnsSyncActivity
import com.pplus.prnumberbiz.apps.number.ui.MakePrnumberPreActivity
import com.pplus.prnumberbiz.apps.pages.ui.AlertPageSetGuideActivity
import com.pplus.prnumberbiz.apps.pages.ui.PageEditActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.dto.Group
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_biz_main.*
import kotlinx.android.synthetic.main.fragment_biz_main.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class BizMainFragment : BaseFragment<BizMainActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_biz_main
    }

    override fun initializeView(container: View?) {

    }

    private lateinit var mPage: Page
//    private var attachmentList: MutableList<Attachment>? = null
//    private var mImageList: MutableList<Attachment>? = null
//    var mIntroPagerAdapter: IntroduceImagePagerAdapter? = null

    private var mPaging: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
    private var mLockListView = true
    private var mIsLast = false
    private var mAdapter: BizMainHeaderAdapter? = null

    override fun init() {

        appbar_biz_main.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (!isAdded) {
                    return
                }
                if (verticalOffset <= -collapsing_biz_main.height + toolbar_biz_main.height) {
                    //toolbar is collapsed here
                    //write your code here
//                    text_biz_main_page_name2.visibility = View.VISIBLE
                    layout_biz_main_reg_goods.visibility = View.VISIBLE
                } else {
//                    text_biz_main_page_name2.visibility = View.GONE
                    layout_biz_main_reg_goods.visibility = View.GONE
                }
            }
        })

//        mIntroPagerAdapter = IntroduceImagePagerAdapter(activity)
//        pager_biz_main_introduce_image.adapter = mIntroPagerAdapter
//
//        mIntroPagerAdapter!!.setListener(object : IntroduceImagePagerAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                if (StringUtils.isNotEmpty(mIntroPagerAdapter!!.getData(position).targetType) && mIntroPagerAdapter!!.getData(position).targetType == "youtube") {
//
//                } else {
//
//                    var pos = position
//
//                    if (StringUtils.isNotEmpty(mPage.mainMovieUrl)) {
//                        pos = position - 1
//                    }
//
//                    val intent = Intent(activity, IntroduceImageDetailActivity::class.java)
//                    intent.putExtra(Const.DATA, mImageList as ArrayList<Attachment>)
//                    intent.putExtra(Const.POSITION, pos)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    activity!!.startActivityForResult(intent, Const.REQ_INTRODUCE_IMAGE)
//                }
//            }
//        })

//        indicator_biz_main_introduce_image.visibility = View.GONE
//        pager_biz_main_introduce_image.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//
//                indicator_biz_main_introduce_image.setCurrentItem(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })

        image_biz_main_user_mode.setOnClickListener {
            var intent = activity!!.packageManager.getLaunchIntentForPackage("com.pplus.prnumberuser")
            if (intent != null) {
                // We found the activity now start the activity
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                // Bring user to the market or let them choose an app?
                intent = Intent(Intent.ACTION_VIEW)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.data = Uri.parse("market://details?id=com.pplus.prnumberuser")
                startActivity(intent)
            }
        }

//        image_home_setting.setOnClickListener {
//            val intent = Intent(activity, SettingActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        image_biz_main_menu.setOnClickListener {
            if (parentActivity is BizMainActivity) {
                IgawAdbrix.retention("Main_menu")

                parentActivity.drawer_layout.openDrawer(GravityCompat.START)
            }

        }

        layout_biz_main_keyword.setOnClickListener {
            val intent = Intent(activity, KeywordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
        }

//        layout_biz_main_target_marketing.setOnClickListener {
//            val intent = Intent(activity, TargetMarketingActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        layout_biz_main_plus_customer.setOnClickListener {
            val intent = Intent(activity, AdvertiseActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
        }

        layout_biz_main_store_config.setOnClickListener {
            val intent = Intent(activity, PageEditActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
        }

        layout_biz_main_reg_goods.setOnClickListener {
            val intent = Intent(activity, GoodsRegActivity2::class.java)
            intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            activity?.startActivityForResult(intent, Const.REQ_REG)
        }

//        image_biz_main_camera.setOnClickListener {
//            if (mImageList == null) {
//                return@setOnClickListener
//            }
//            if (mImageList!!.size == 0) {
//
//                val intent = Intent(activity, PostGalleryActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                activity?.startActivityForResult(intent, Const.REQ_GALLERY)
//
////                val intent = Intent(activity, GalleryActivity::class.java)
////                intent.putParcelableArrayListExtra(Const.DATA, mIntroduceAdapter.getDataList() as ArrayList<Attachment>)
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                activity!!.startActivityForResult(intent, Const.REQ_INTRODUCE_IMAGE)
//            } else {
//                var contents: Array<String>? = null
//                val builder = AlertBuilder.Builder()
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)
//
//                if (mImageList!!.size < Const.IMAGE_UPLOAD_MAX_COUNT) {
//                    contents = arrayOf(getString(R.string.word_add), getString(R.string.word_delete))
//                } else {
//                    contents = arrayOf(getString(R.string.word_delete))
//                }
//
//                builder.setContents(*contents)
//                builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                    override fun onCancel() {
//
//                    }
//
//                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                        when (event_alert) {
//                            AlertBuilder.EVENT_ALERT.LIST -> when (event_alert.getValue()) {
//                                1 -> if (mImageList!!.size < Const.IMAGE_UPLOAD_MAX_COUNT) {
//                                    val intent = Intent(activity, PostGalleryActivity::class.java)
//                                    intent.putExtra(Const.COUNT, Const.IMAGE_UPLOAD_MAX_COUNT - mImageList!!.size)
//                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                    activity?.startActivityForResult(intent, Const.REQ_GALLERY)
//                                } else {
//                                    val intent = Intent(activity, IntroImageDeleteActivity::class.java)
//                                    intent.putParcelableArrayListExtra(Const.DATA, mImageList as ArrayList<Attachment>)
//                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                    activity?.startActivityForResult(intent, Const.REQ_INTRODUCE_IMAGE)
//                                }
//                                2 -> {
//                                    val intent = Intent(activity, IntroImageDeleteActivity::class.java)
//                                    intent.putParcelableArrayListExtra(Const.DATA, mImageList as ArrayList<Attachment>)
//                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                    activity?.startActivityForResult(intent, Const.REQ_INTRODUCE_IMAGE)
//                                }
//                            }
//                        }
//                    }
//                }).builder().show(activity)
//            }
//        }


        if (checkPageSet()) {
            getPage()
        }

    }



    private fun checkPageSet(): Boolean {
        val page = LoginInfoManager.getInstance().user.page!!
        if (StringUtils.isEmpty(page.catchphrase)) {
//            if (StringUtils.isEmpty(page.catchphrase)) {
            val intent = Intent(activity, AlertPageSetGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
            return false
        }
        return true
    }

    private fun getPage() {
        val params = HashMap<String, String>()
        params["no"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        showProgress("")
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page?>> {

            override fun onResponse(call: Call<NewResultResponse<Page?>>?, response: NewResultResponse<Page?>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response!!.data != null) {

                    LoginInfoManager.getInstance().user.page = response.data!!
                    LoginInfoManager.getInstance().save()

                    if (LoginInfoManager.getInstance().user.page!!.isSeller!!) {
                        when (LoginInfoManager.getInstance().user.page!!.sellerStatus) {
                            EnumData.SellerStatus.approval.status -> {
                            }
                            EnumData.SellerStatus.approvalWait.status, EnumData.SellerStatus.secondRequest.status -> {//승인대기
                            }
                            EnumData.SellerStatus.reject.status -> {
                            }
                        }

                        if (!LoginInfoManager.getInstance().user.page!!.usePrnumber!!) {
                            val intent = Intent(activity, MakePrnumberPreActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
                        } else {
//                            getGoodsList()
                        }

                    } else {
                        val intent = Intent(activity, SellerApplyActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        activity?.startActivityForResult(intent, Const.REQ_APPLY)
                    }

                    setData()

                    parentActivity.setPageData()

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page?>>?, t: Throwable?, response: NewResultResponse<Page?>?) {
                hideProgress()
            }
        }).build().call()
    }

    fun setData() {
        mPage = LoginInfoManager.getInstance().user.page!!
//        layout_biz_main_goods_waiting.visibility = View.GONE
//        layout_biz_main_goods_deny.visibility = View.GONE
//        text_biz_main_goods_not_exist.visibility = View.GONE

        text_biz_main_page_name.text = mPage.name
//        text_biz_main_page_name2.text = mPage.name
//        if (mPage.usePrnumber!! && mPage.numberList != null && mPage.numberList!!.isNotEmpty()) {
//            text_biz_main_number.text = PplusNumberUtil.getPrNumberFormat(mPage.numberList!![0].number)
//        }

        Glide.with(this).load(mPage.backgroundImage?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_biz_main_background)
        image_biz_main_background.setOnClickListener {
            val intent = Intent(activity, BackgroundImageDetailActivity::class.java)
            intent.putExtra(Const.URL, mPage.backgroundImage?.url)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        image_biz_main_edit.setOnClickListener {
            val intent = Intent(activity, PageEditActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
        }

        image_biz_main_link.setOnClickListener {
            val intent = Intent(activity, SnsSyncActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
        }

//        callIntroduceImage()

        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recycler_biz_main.layoutManager = mLayoutManager
        mAdapter = BizMainHeaderAdapter(activity!!)
        recycler_biz_main.adapter = mAdapter

        recycler_biz_main.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : BizMainHeaderAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(activity, PlusGoodsDetailActivity::class.java)
                intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                startActivityForResult(intent, Const.REQ_DETAIL)
            }

            override fun refresh() {
                mPaging = 0
                listCall(mPaging)
            }
        })

        getPlusCount()

        mPaging = 0
        listCall(mPaging)
    }

    private fun getPlusCount(){
        val params = HashMap<String, String>()
        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!

        ApiBuilder.create().getFanGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {

            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {
                for (group in response.datas) {
                    if (group.isDefaultGroup) {
                        text_biz_main_plus_count?.text = getString(R.string.format_plus_count2, FormatUtil.getMoneyType(group.count.toString()))
                        break
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {

            }
        }).build().call()
    }

//    private fun callIntroduceImage() {
//
//        val params = HashMap<String, String>()
//        params["no"] = "" + mPage.no!!
//        ApiBuilder.create().getIntroImageAll(params).setCallback(object : PplusCallback<NewResultResponse<Attachment>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Attachment>>, response: NewResultResponse<Attachment>) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                attachmentList = ArrayList<Attachment>()
//                if (StringUtils.isNotEmpty(mPage.mainMovieUrl)) {
//                    val mainYoutube = Attachment()
//                    mainYoutube.targetType = "youtube"
//                    mainYoutube.url = mPage.mainMovieUrl
//                    attachmentList!!.add(mainYoutube)
//                }
//                mImageList = response.datas
//                attachmentList!!.addAll(response.datas)
//
//                callMovieList()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Attachment>>, t: Throwable, response: NewResultResponse<Attachment>) {
//
//            }
//        }).build().call()
//    }

//    private fun callMovieList() {
//
//        val params = HashMap<String, String>()
//        params["no"] = "" + mPage.no!!
//        ApiBuilder.create().getIntroMovieAll(params).setCallback(object : PplusCallback<NewResultResponse<ImgUrl>> {
//
//            override fun onResponse(call: Call<NewResultResponse<ImgUrl>>, response: NewResultResponse<ImgUrl>) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                val youtubeList = response.datas
//                for (i in youtubeList.indices) {
//                    val addMovieList = Attachment()
//                    addMovieList.targetType = "youtube"
//                    addMovieList.url = youtubeList[i].url
//                    attachmentList!!.add(addMovieList)
//                }
//
//                if (attachmentList!!.size > 1) {
//
//                    indicator_biz_main_introduce_image.removeAllViews()
//                    indicator_biz_main_introduce_image.visibility = View.VISIBLE
//                    indicator_biz_main_introduce_image.build(LinearLayout.HORIZONTAL, attachmentList!!.size)
//                } else {
//                    indicator_biz_main_introduce_image.visibility = View.GONE
//                }
//
//                mIntroPagerAdapter!!.dataList = attachmentList
//                pager_biz_main_introduce_image.currentItem = 0
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<ImgUrl>>, t: Throwable, response: NewResultResponse<ImgUrl>) {
//
//            }
//        }).build().call()
//    }

    private var mSortType = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc}"

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["sort"] = mSortType
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.store.name) {
            params["type"] = "0"
        } else {
            params["type"] = "1"
        }
        params["isPlus"] = "true"
        params["page"] = page.toString()

        showProgress("")
        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
                hideProgress()

                if (response != null) {
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.mTotalCount = mTotalCount
//                        text_plus_goods_count.text = getString(R.string.format_event_goods_count, FormatUtil.getMoneyType(mTotalCount.toString()))

                        mAdapter!!.clear()
//                        if (mTotalCount == 0) {
//                            layout_plus_goods_not_exist.visibility = View.VISIBLE
//                            layout_plus_goods_exist.visibility = View.GONE
//                        } else {
//                            layout_plus_goods_not_exist.visibility = View.GONE
//                            layout_plus_goods_exist.visibility = View.VISIBLE
//                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Goods>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_ALERT_GOODS -> {
                if (resultCode == RESULT_OK) {

//                    val intent = Intent(activity, MenuConfigActivity::class.java)
//                    intent.putExtra(Const.KEY, Const.REG)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)

//                    if (mPage.type == EnumData.PageTypeCode.store.name) {
//                        if (mPage.isShopOrderable!! || mPage.isDeliveryOrderable!! || mPage.isPackingOrderable!!) {
//                            val intent = Intent(activity, MenuRegActivity::class.java)
//                            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            activity?.startActivityForResult(intent, Const.REQ_REG)
//                        } else {
//                            val intent = Intent(activity, OperationInfoActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
//                        }
//                    } else {
//                        val intent = Intent(activity, MenuRegActivity::class.java)
//                        intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                        activity?.startActivityForResult(intent, Const.REQ_REG)
//                    }

                }
            }
            Const.REQ_SET_PAGE -> {
                if (checkPageSet()) {
                    getPage()
                }
            }
//            Const.REQ_GALLERY -> {
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                    val dataList = data.getParcelableArrayListExtra<Uri>(Const.CROPPED_IMAGE)
//
//                    imgList = ArrayList()
//                    if (mImageList != null && mImageList!!.size > 0) {
//                        for (i in mImageList!!.indices) {
//                            val priority = Const.IMAGE_UPLOAD_MAX_COUNT - i
//                            imgList!!.add(ImgUrl(mImageList!![i].no, priority))
//                        }
//                    }
//
//                    val maxCount = Const.IMAGE_UPLOAD_MAX_COUNT - mImageList!!.size
//                    for (i in 0 until dataList.size) {
//                        val priority = maxCount - i
//                        upload(dataList.size, priority, dataList[i].path, AttachmentTargetTypeCode.pageIntro)
//                    }
//                }
//            }
            Const.REQ_INTRODUCE_IMAGE -> {
                getPage()
            }
            Const.REQ_ALARM -> {
                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                    override fun reload() {
//                        checkAlarmCount()
                    }
                })
            }
            Const.REQ_APPLY -> {
                getPage()
            }
            Const.REQ_LOCATION_CODE -> {
//                mAdapter!!.notifyItemChanged(0)
            }
            Const.REQ_REG, Const.REQ_MODIFY, Const.REQ_DETAIL -> {
                mPaging = 0
                listCall(mPaging)
            }
//            Const.REQ_SYNC_SNS -> {
//                getSnsLink()
//            }
            Const.REQ_SALE_HISTORY -> {
                mAdapter!!.notifyDataSetChanged()
            }
        }
    }

//    private var defaultUpload: DefaultUpload? = null
//    private var imgList: ArrayList<ImgUrl>? = null

//    private fun upload(size: Int, priority: Int, filepath: String, type: AttachmentTargetTypeCode) {
//        val attachment = ParamsAttachment()
//        attachment.targetType = type
//        attachment.setFile(filepath)
//        attachment.targetNo = LoginInfoManager.getInstance().user.page!!.no
//
//        defaultUpload = DefaultUpload(object : PplusUploadListener<Attachment> {
//
//            override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {
//
//                if (!isAdded) {
//                    return
//                }
//                hideProgress()
//                val url = resultResponse.data.url
//                val no = resultResponse.data.no
//
//
//                imgList!!.add(ImgUrl(no, priority))
//                if (imgList!!.size == size + mImageList!!.size) {
//                    val params = ParamsIntroImage()
//                    params.no = LoginInfoManager.getInstance().user.page!!.no
//                    params.introImageList = imgList
//
//                    ApiBuilder.create().updateIntroImageList(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
//
//                        override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
//                            showAlert(R.string.msg_add_complete)
//                            hideProgress()
//                            callIntroduceImage()
//                        }
//
//                        override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {
//
//                            hideProgress()
//                        }
//                    }).build().call()
//                }
//            }
//
//            override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {
//
//                LogUtil.e(LOG_TAG, "onFailure")
//                hideProgress()
//            }
//        })
//
//        showProgress("")
//        defaultUpload!!.request(filepath, attachment)
//    }

    override fun getPID(): String {
        return "Main"
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                BizMainFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
