package com.pplus.prnumberbiz.apps.main.ui


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const

import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberbiz.apps.main.data.MyPageHeaderAdapter
import com.pplus.prnumberbiz.apps.pages.data.IntroduceImagePagerAdapter
import com.pplus.prnumberbiz.apps.pages.ui.*
import com.pplus.prnumberbiz.apps.post.ui.PostGalleryActivity
import com.pplus.prnumberbiz.apps.setting.ui.AlarmContainerActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.*
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsIntroImage
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.network.upload.DefaultUpload
import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.PplusNumberUtil
import kotlinx.android.synthetic.main.fragment_my_page.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.ArrayList
import java.util.HashMap

class MyPageFragment : BaseFragment<BaseActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_my_page
    }

    override fun initializeView(container: View?) {

    }

    var mIntroPagerAdapter: IntroduceImagePagerAdapter? = null
    private lateinit var mPage: Page
    private var attachmentList: MutableList<Attachment>? = null
    private var mImageList: MutableList<Attachment>? = null

    private var mPaging: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: MyPageHeaderAdapter? = null

    override fun init() {

        appbar_home.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (!isAdded) {
                    return
                }
                if (verticalOffset <= -collapsing_home.height + toolbar_home.height) {
                    //toolbar is collapsed here
                    //write your code here
                    text_home_page_name2.visibility = View.VISIBLE
                } else {
                    text_home_page_name2.visibility = View.GONE
                }
            }
        })



        mIntroPagerAdapter = IntroduceImagePagerAdapter(activity)
        pager_home_introduce_image.adapter = mIntroPagerAdapter

        mIntroPagerAdapter!!.setListener(object : IntroduceImagePagerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (StringUtils.isNotEmpty(mIntroPagerAdapter!!.getData(position).targetType) && mIntroPagerAdapter!!.getData(position).targetType == "youtube") {

                } else {

                    var pos = position

                    if (StringUtils.isNotEmpty(mPage.mainMovieUrl)) {
                        pos = position - 1
                    }

                    val intent = Intent(activity, IntroduceImageDetailActivity::class.java)
                    intent.putExtra(Const.DATA, mImageList as ArrayList<Attachment>)
                    intent.putExtra(Const.POSITION, pos)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    activity!!.startActivityForResult(intent, Const.REQ_INTRODUCE_IMAGE)
                }
            }
        })

        indicator_home_introduce_image.visibility = View.GONE
        pager_home_introduce_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                indicator_home_introduce_image.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

//        image_home_setting.setOnClickListener {
//            val intent = Intent(activity, SettingActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        image_page_back.setOnClickListener {
            activity?.finish()
        }

        layout_home_alarm.setOnClickListener {
            val intent = Intent(activity, AlarmContainerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_ALARM)
        }

        image_my_page_edit.setOnClickListener {
            val intent = Intent(activity, PageConfigActivity::class.java)
            intent.putExtra(Const.FIRST, false)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            activity!!.startActivityForResult(intent, Const.REQ_SET_PAGE)
        }

        image_home_edit_image.setOnClickListener {
            if (mImageList == null) {
                return@setOnClickListener
            }
            if (mImageList!!.size == 0) {

                val intent = Intent(activity, PostGalleryActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivityForResult(intent, Const.REQ_GALLERY)

//                val intent = Intent(activity, GalleryActivity::class.java)
//                intent.putParcelableArrayListExtra(Const.DATA, mIntroduceAdapter.getDataList() as ArrayList<Attachment>)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                activity!!.startActivityForResult(intent, Const.REQ_INTRODUCE_IMAGE)
            } else {
                var contents: Array<String>? = null
                val builder = AlertBuilder.Builder()
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)

                if (mImageList!!.size < Const.IMAGE_UPLOAD_MAX_COUNT) {
                    contents = arrayOf(getString(R.string.word_add), getString(R.string.word_delete))
                } else {
                    contents = arrayOf(getString(R.string.word_delete))
                }

                builder.setContents(*contents)
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.LIST -> when (event_alert.getValue()) {
                                1 -> if (mImageList!!.size < Const.IMAGE_UPLOAD_MAX_COUNT) {
                                    val intent = Intent(activity, PostGalleryActivity::class.java)
                                    intent.putExtra(Const.COUNT, Const.IMAGE_UPLOAD_MAX_COUNT - mImageList!!.size)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivityForResult(intent, Const.REQ_GALLERY)
                                } else {
                                    val intent = Intent(activity, IntroImageDeleteActivity::class.java)
                                    intent.putParcelableArrayListExtra(Const.DATA, mImageList as ArrayList<Attachment>)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    activity!!.startActivityForResult(intent, Const.REQ_INTRODUCE_IMAGE)
                                }
                                2 -> {
                                    val intent = Intent(activity, IntroImageDeleteActivity::class.java)
                                    intent.putParcelableArrayListExtra(Const.DATA, mImageList as ArrayList<Attachment>)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    activity!!.startActivityForResult(intent, Const.REQ_INTRODUCE_IMAGE)
                                }
                            }
                        }
                    }
                }).builder().show(activity)
            }
        }

        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recycler_home.layoutManager = mLayoutManager
        mAdapter = MyPageHeaderAdapter(activity!!)
        recycler_home.adapter = mAdapter
        recycler_home.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_180))
        mAdapter!!.setOnItemClickListener(object : MyPageHeaderAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {

//                val intent = Intent(activity, PostDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                intent.putExtra(Const.POSITION, position)
//                activity!!.startActivityForResult(intent, Const.REQ_DETAIL)
            }
        })

        recycler_home.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

//        recycler_home.addOnScrollListener(RecyclerScaleScrollListener(layout_home_write))

//        layout_home_write.setOnClickListener {
//                        val intent = Intent(activity, PostWriteActivity::class.java)
//            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//            activity!!.startActivityForResult(intent, Const.REQ_POST)
//        }

        text_my_page_share.setOnClickListener {
            share()
        }

//        bmb_home.piecePlaceEnum = PiecePlaceEnum.Custom
//        bmb_home.buttonPlaceEnum = ButtonPlaceEnum.Custom
//        bmb_home.orderEnum = OrderEnum.DEFAULT
//        bmb_home.buttonPlaceAlignmentEnum = ButtonPlaceAlignmentEnum.Center
//
//        bmb_home.clearBuilders()
//
//        bmb_home.addBuilder(HamButton.Builder()
//                .normalColor(ResourceUtil.getColor(activity, R.color.color_f2cb3f))
//                .normalImageRes(R.drawable.ic_floating_page)
//                .normalTextRes(R.string.word_edit_page)
//                .subNormalTextRes(R.string.msg_edit_page_description)
//                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                .pieceColorRes(R.color.white)
//                .listener {
//                    val intent = Intent(activity, PageConfigActivity::class.java)
//                    intent.putExtra(Const.FIRST, false)
//                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    activity!!.startActivityForResult(intent, Const.REQ_SET_PAGE)
//                })
//
//        bmb_home.addBuilder(HamButton.Builder()
//                .normalColor(ResourceUtil.getColor(activity, R.color.color_c2aaff))
//                .normalImageRes(R.drawable.ic_floating_share)
//                .normalTextRes(R.string.msg_share)
//                .subNormalTextRes(R.string.msg_share_description)
//                .pieceColorRes(R.color.white)
//                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                .listener {
//                    share()
//                })
//
//        bmb_home.addBuilder(HamButton.Builder()
//                .normalColor(ResourceUtil.getColor(activity, R.color.color_47bcc6))
//                .normalImageRes(R.drawable.ic_floating_sns)
//                .normalTextRes(R.string.word_sns_sync)
//                .subNormalTextRes(R.string.msg_sns_sync_description)
//                .pieceColorRes(R.color.white)
//                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                .listener {
//                    val intent = Intent(activity, SnsSyncActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    activity?.startActivityForResult(intent, Const.REQ_SYNC_SNS)
//                })
//
//        bmb_home.addBuilder(HamButton.Builder()
//                .normalColor(ResourceUtil.getColor(activity, R.color.color_ffa7b3))
//                .normalImageRes(R.drawable.ic_floating_network)
//                .normalTextRes(R.string.word_secret_mode)
//                .subNormalTextRes(R.string.msg_setting_secret_mode)
//                .pieceColorRes(R.color.white)
//                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                .listener {
//                    val intent = Intent(activity, SecretModeActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                })
//
//        bmb_home.addBuilder(HamButton.Builder()
//                .normalColor(ResourceUtil.getColor(activity, R.color.color_90aee2))
//                .normalImageRes(R.drawable.ic_floating_keyword)
//                .normalTextRes(R.string.word_config_keyword)
//                .subNormalTextRes(R.string.word_config_keyword)
//                .pieceColorRes(R.color.white)
//                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                .listener {
//                    val intent = Intent(activity, KeywordConfigActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                })
//
//        bmb_home.addBuilder(HamButton.Builder()
//                .normalColor(ResourceUtil.getColor(activity, R.color.color_579ffb))
//                .normalImageRes(R.drawable.ic_floating_setting)
//                .normalTextRes(R.string.word_setting)
//                .subNormalTextRes(R.string.msg_setting_description)
//                .pieceColorRes(R.color.white)
//                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                .listener {
//                    val intent = Intent(activity, SettingActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                })

//        bmb_home.addBuilder(HamButton.Builder()
//                .normalColor(ResourceUtil.getColor(activity, R.color.color_ffa4dc))
//                .normalImageRes(R.drawable.ic_floating_number)
//                .normalTextRes(R.string.word_verification_number_config)
//                .subNormalTextRes(R.string.msg_verification_number_config_description)
//                .pieceColorRes(android.R.color.transparent)
//                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                .listener {
//                    val intent = Intent(activity, VerificationNumberConfigActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                })

//        setButtonPlace()
//        setPiecePlace()

        setData()
//        checkAlarmCount()
    }

//    private fun setButtonPlace() {
//        val builder = (bmb_home.builders[0] as HamButton.Builder)
//        val h = builder.buttonHeight
//        val w = builder.buttonWidth
//
//        val vm = bmb_home.buttonVerticalMargin
//        val vm_0_5 = vm / 2
//        val h_0_5 = h / 2
//
//        val halfButtonNumber = bmb_home.builders.size / 2
//
//        if (bmb_home.builders.size % 2 == 0) run {
//            for (i in halfButtonNumber - 1 downTo 0)
//                bmb_home.customButtonPlacePositions.add(PointF(0f, -h_0_5 - vm_0_5 - i * (h + vm)))
//            for (i in 0 until halfButtonNumber)
//                bmb_home.customButtonPlacePositions.add(PointF(0f, +h_0_5 + vm_0_5 + i * (h + vm)))
//        } else {
//            for (i in halfButtonNumber - 1 downTo 0)
//                bmb_home.customButtonPlacePositions.add(PointF(0f, -h - vm - i * (h + vm)))
//            bmb_home.customButtonPlacePositions.add(PointF(0f, 0f))
//            for (i in 0 until halfButtonNumber)
//                bmb_home.customButtonPlacePositions.add(PointF(0f, +h + vm + i * (h + vm)))
//        }
//    }
//
//    private fun setPiecePlace() {
//        val h = bmb_home.hamHeight
//        val w = bmb_home.hamWidth
//
//        val pn = bmb_home.builders.size
//        val pn_0_5 = pn / 2
//        val h_0_5 = h / 2
//        val vm = bmb_home.pieceVerticalMargin
//        val vm_0_5 = vm / 2
//
//        if (pn % 2 == 0) {
//            for (i in pn_0_5 - 1 downTo 0)
//                bmb_home.customPiecePlacePositions.add(PointF(0f, -h_0_5 - vm_0_5 - i * (h + vm)))
//            for (i in 0 until pn_0_5)
//                bmb_home.customPiecePlacePositions.add(PointF(0f, +h_0_5 + vm_0_5 + i * (h + vm)))
//        } else {
//            for (i in pn_0_5 - 1 downTo 0)
//                bmb_home.customPiecePlacePositions.add(PointF(0f, -h - vm - i * (h + vm)))
//            bmb_home.customPiecePlacePositions.add(PointF(0f, 0f))
//            for (i in 0 until pn_0_5)
//                bmb_home.customPiecePlacePositions.add(PointF(0f, +h + vm + i * (h + vm)))
//        }
//    }

    private fun getSnsLink() {

        val params = HashMap<String, String>()
        params["no"] = "" + mPage.no!!

        ApiBuilder.create().getSnsLinkAll(params).setCallback(object : PplusCallback<NewResultResponse<Sns>> {

            override fun onResponse(call: Call<NewResultResponse<Sns>>, response: NewResultResponse<Sns>) {

                if (!isAdded) {
                    return
                }

                image_page_facebook.visibility = View.GONE
                image_page_twitter.visibility = View.GONE
                image_page_kakao.visibility = View.GONE
                image_page_instagram.visibility = View.GONE

                val snsList = response.datas

                if (snsList != null && !snsList.isEmpty()) {
                    for (sns in snsList) {
                        if (StringUtils.isNotEmpty(sns.url)) {
                            when (SnsTypeCode.valueOf(sns.type)) {

                                SnsTypeCode.twitter -> {
                                    image_page_twitter.visibility = View.VISIBLE
                                    image_page_twitter.tag = sns
                                    image_page_twitter.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.facebook -> {
                                    image_page_facebook.visibility = View.VISIBLE
                                    image_page_facebook.tag = sns
                                    image_page_facebook.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.instagram -> {
                                    image_page_instagram.visibility = View.VISIBLE
                                    image_page_instagram.tag = sns
                                    image_page_instagram.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.kakao -> {
                                    image_page_kakao.visibility = View.VISIBLE
                                    image_page_kakao.tag = sns
                                    image_page_kakao.setOnClickListener(onSnsClickListener)
                                }
//                                SnsTypeCode.blog -> {
//                                    holder.text_blog.isSelected = true
//                                    holder.text_blog.tag = sns
//                                    holder.text_blog.setOnClickListener(onSnsClickListener)
//                                }
//                                SnsTypeCode.homepage -> {
//                                    holder.text_homepage.isSelected = true
//                                    holder.text_homepage.tag = sns
//                                    holder.text_homepage.setOnClickListener(onSnsClickListener)
//                                }
                            }
                        }
                    }
                }

//                mAdapter!!.setSNS(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Sns>>, t: Throwable, response: NewResultResponse<Sns>) {

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
            startActivity(PplusCommonUtil.getOpenSnsIntent(activity!!, SnsTypeCode.valueOf(sns.type), sns.url, sns.isLinkage))
        }
    }

    private fun setData() {
        mPage = LoginInfoManager.getInstance().user.page!!
        mAdapter!!.setPage(mPage)

        text_home_page_name2.text = mPage.name
        if (mPage.numberList != null && mPage.numberList!!.isNotEmpty()) {
            text_home_number.text = PplusNumberUtil.getPrNumberFormat(mPage.numberList!![0].number)
        }

        getPlusCount()
        getSnsLink()
        callIntroduceImage()

        mPaging = 0
        listCall(mPaging)
    }

    private fun getPlusCount() {
        val params = HashMap<String, String>()
        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!

        ApiBuilder.create().getFanGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {

            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {
                if (!isAdded) {
                    return
                }
                for (group in response.datas) {
                    if (group.isDefaultGroup) {
                        text_home_plus.text = PplusCommonUtil.fromHtml(getString(R.string.html_plus_count, FormatUtil.getMoneyType(group.count.toString())))
                        break
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {

            }
        }).build().call()
    }

    fun checkAlarmCount() {
        if (LoginInfoManager.getInstance().user.properties == null || LoginInfoManager.getInstance().user.properties!!.newMsgCount == 0) {
            text_home_alarm_count!!.visibility = View.INVISIBLE
        } else {
            text_home_alarm_count!!.visibility = View.VISIBLE
            if (LoginInfoManager.getInstance().user.properties != null && LoginInfoManager.getInstance().user.properties!!.newMsgCount > 99) {
                text_home_alarm_count!!.text = "99+"
            } else {
                text_home_alarm_count!!.text = LoginInfoManager.getInstance().user.properties!!.newMsgCount.toString()
            }
        }
    }

    private fun callIntroduceImage() {

        val params = HashMap<String, String>()
        params["no"] = "" + mPage.no!!
        ApiBuilder.create().getIntroImageAll(params).setCallback(object : PplusCallback<NewResultResponse<Attachment>> {

            override fun onResponse(call: Call<NewResultResponse<Attachment>>, response: NewResultResponse<Attachment>) {

                if (!isAdded) {
                    return
                }

                attachmentList = ArrayList<Attachment>()
                if (StringUtils.isNotEmpty(mPage.mainMovieUrl)) {
                    val mainYoutube = Attachment()
                    mainYoutube.targetType = "youtube"
                    mainYoutube.url = mPage.mainMovieUrl
                    attachmentList!!.add(mainYoutube)
                }
                mImageList = response.datas
                attachmentList!!.addAll(response.datas)

                callMovieList()
            }

            override fun onFailure(call: Call<NewResultResponse<Attachment>>, t: Throwable, response: NewResultResponse<Attachment>) {

            }
        }).build().call()
    }

    private fun callMovieList() {

        val params = HashMap<String, String>()
        params["no"] = "" + mPage.no!!
        ApiBuilder.create().getIntroMovieAll(params).setCallback(object : PplusCallback<NewResultResponse<ImgUrl>> {

            override fun onResponse(call: Call<NewResultResponse<ImgUrl>>, response: NewResultResponse<ImgUrl>) {

                if (!isAdded) {
                    return
                }

                val youtubeList = response.datas
                for (i in youtubeList.indices) {
                    val addMovieList = Attachment()
                    addMovieList.targetType = "youtube"
                    addMovieList.url = youtubeList[i].url
                    attachmentList!!.add(addMovieList)
                }

                if (attachmentList!!.size > 1) {

                    indicator_home_introduce_image.removeAllViews()
                    indicator_home_introduce_image.visibility = View.VISIBLE
                    indicator_home_introduce_image.build(LinearLayout.HORIZONTAL, attachmentList!!.size)
                } else {
                    indicator_home_introduce_image.visibility = View.GONE
                }

                mIntroPagerAdapter!!.dataList = attachmentList
                pager_home_introduce_image.currentItem = 0
            }

            override fun onFailure(call: Call<NewResultResponse<ImgUrl>>, t: Throwable, response: NewResultResponse<ImgUrl>) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["pageSeqNo"] = mPage.no.toString()
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        params["page"] = page.toString()

        showProgress("")
        ApiBuilder.create().getGoodsReview(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<GoodsReview>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<GoodsReview>>>?, response: NewResultResponse<SubResultResponse<GoodsReview>>?) {
                if (!isAdded) {
                    return
                }
                hideProgress()

                if (response != null) {

                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<GoodsReview>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<GoodsReview>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_SET_PAGE -> {
//                mPage = LoginInfoManager.getInstance().user.page!!
                setData()
            }
            Const.REQ_GALLERY -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val dataList = data.getParcelableArrayListExtra<Uri>(Const.CROPPED_IMAGE)

                    imgList = ArrayList()
                    if (mImageList != null && mImageList!!.size > 0) {
                        for (i in mImageList!!.indices) {
                            val priority = Const.IMAGE_UPLOAD_MAX_COUNT - i
                            imgList!!.add(ImgUrl(mImageList!![i].no, priority))
                        }
                    }

                    val maxCount = Const.IMAGE_UPLOAD_MAX_COUNT - mImageList!!.size
                    for (i in 0 until dataList.size) {
                        val priority = maxCount - i
                        upload(dataList.size, priority, dataList[i].path, AttachmentTargetTypeCode.pageIntro)
                    }
                }
            }
            Const.REQ_INTRODUCE_IMAGE -> {
                callIntroduceImage()
            }
            Const.REQ_ALARM -> {
//                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//                    override fun reload() {
//                        checkAlarmCount()
//                    }
//                })
            }
            Const.REQ_LOCATION_CODE -> {
                mAdapter!!.notifyItemChanged(0)
            }
            Const.REQ_SYNC_SNS -> {
                getSnsLink()
            }
        }
    }

    private var defaultUpload: DefaultUpload? = null
    private var imgList: ArrayList<ImgUrl>? = null

    private fun upload(size: Int, priority: Int, filepath: String, type: AttachmentTargetTypeCode) {
        val attachment = ParamsAttachment()
        attachment.targetType = type
        attachment.setFile(filepath)
        attachment.targetNo = LoginInfoManager.getInstance().user.page!!.no

        defaultUpload = DefaultUpload(object : PplusUploadListener<Attachment> {

            override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {

                hideProgress()
                val url = resultResponse.data.url
                val no = resultResponse.data.no


                imgList!!.add(ImgUrl(no, priority))
                if (imgList!!.size == size + mImageList!!.size) {
                    val params = ParamsIntroImage()
                    params.no = LoginInfoManager.getInstance().user.page!!.no
                    params.introImageList = imgList

                    ApiBuilder.create().updateIntroImageList(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

                        override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                            showAlert(R.string.msg_add_complete)
                            hideProgress()
                            callIntroduceImage()
                        }

                        override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {

                            hideProgress()
                        }
                    }).build().call()
                }
            }

            override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {

                LogUtil.e(LOG_TAG, "onFailure")
                hideProgress()
            }
        })

        showProgress("")
        defaultUpload!!.request(filepath, attachment)
    }

    private fun share() {

        val shareText = LoginInfoManager.getInstance().user.page!!.catchphrase + "\n" + getString(R.string.format_msg_page_url, "index.php?no=" + LoginInfoManager.getInstance().user.page!!.no!!)

        val intent = Intent(Intent.ACTION_SEND)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareText)
        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share_page))
        startActivity(chooserIntent)
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                MyPageFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
