//package com.pplus.prnumberbiz.apps.pages.ui
//
//
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.support.v4.view.ViewPager
//import android.view.LayoutInflater
//import android.view.MotionEvent
//import android.view.View
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.LinearLayout
//import com.pple.pplus.utils.part.logs.LogUtil
//import com.pple.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberbiz.Const
//import com.pplus.prnumberbiz.R
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
//import com.pplus.prnumberbiz.apps.common.mgmt.CategoryInfoManager
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberbiz.apps.pages.data.PageConfigPagerAdapter
//import com.pplus.prnumberbiz.apps.post.ui.PostGalleryActivity
//import com.pplus.prnumberbiz.core.code.common.EnumData
//import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode
//import com.pplus.prnumberbiz.core.network.ApiBuilder
//import com.pplus.prnumberbiz.core.network.model.dto.*
//import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment
//import com.pplus.prnumberbiz.core.network.model.request.params.ParamsIntroImage
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
//import com.pplus.prnumberbiz.core.network.upload.DefaultUpload
//import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener
//import com.pplus.prnumberbiz.core.util.PplusNumberUtil
//import com.pplus.prnumberbiz.core.util.ToastUtil
//import kotlinx.android.synthetic.main.fragment_page_config.*
//import kotlinx.android.synthetic.main.item_youtube_url.view.*
//import network.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class PageConfigFragment : BaseFragment<PageConfigActivity>() {
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_page_config
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    var mPage = LoginInfoManager.getInstance().user.page!!
//    private var attachmentList: MutableList<Attachment>? = null
//    private var mImageList: MutableList<Attachment>? = null
//    var mIntroPagerAdapter: PageConfigPagerAdapter? = null
//
//    private var firstCategories: List<Category>? = null
//
//    private val categoryMap = HashMap<CategoryInfoManager.CATEGORY_TYPE, Category?>()
//
//    override fun init() {
//
//        text_page_config_title.setOnClickListener {
//            activity?.onBackPressed()
//        }
//
//        mPage = LoginInfoManager.getInstance().user.page!!
//
//        mIntroPagerAdapter = PageConfigPagerAdapter(activity)
//        pager_page_config.adapter = mIntroPagerAdapter
//
//        mIntroPagerAdapter!!.setListener(object : PageConfigPagerAdapter.OnItemClickListener {
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
//
//            override fun onItemDelete(position: Int) {
//
//                val builder = AlertBuilder.Builder()
//                builder.setTitle(getString(R.string.word_notice_alert))
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//                if (StringUtils.isNotEmpty(mPage.mainMovieUrl) && position == 0) {
//                    builder.addContents(AlertData.MessageData(getString(R.string.msg_question_delete_main_youtube), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                } else {
//                    builder.addContents(AlertData.MessageData(getString(R.string.msg_question_delete_image), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                }
//
//                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//                builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                    override fun onCancel() {
//
//                    }
//
//                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                        when (event_alert) {
//                            AlertBuilder.EVENT_ALERT.RIGHT -> {
//
//                                if (StringUtils.isNotEmpty(mPage.mainMovieUrl) && position == 0) {
//                                    mPage.mainMovieUrl = ""
//                                    LoginInfoManager.getInstance().user.page!!.mainMovieUrl = ""
//                                    LoginInfoManager.getInstance().save()
//                                    showProgress("")
//                                    ApiBuilder.create().updatePage(LoginInfoManager.getInstance().user.page).setCallback(object : PplusCallback<NewResultResponse<Page>> {
//                                        override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
//                                            hideProgress()
//                                            callIntroduceImage()
//                                        }
//
//                                        override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
//                                            hideProgress()
//                                        }
//                                    }).build().call()
//
//                                } else {
//                                    var pos = position
//
//                                    if (StringUtils.isNotEmpty(mPage.mainMovieUrl)) {
//                                        pos = position - 1
//                                    }
//                                    mImageList!!.removeAt(pos)
//
//                                    val imgList = ArrayList<ImgUrl>()
//                                    for (i in mImageList!!.indices) {
//                                        imgList.add(ImgUrl(mImageList!![i].no, Const.IMAGE_UPLOAD_MAX_COUNT - i))
//                                    }
//
//                                    val params = ParamsIntroImage()
//                                    params.no = LoginInfoManager.getInstance().user.page!!.no
//                                    params.introImageList = imgList
//                                    showProgress("")
//                                    ApiBuilder.create().updateIntroImageList(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
//
//                                        override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
//
//                                            showAlert(R.string.msg_delete_complete)
//                                            hideProgress()
//                                            callIntroduceImage()
//                                        }
//
//                                        override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {
//
//                                            hideProgress()
//                                        }
//                                    }).build().call()
//                                }
//
//                            }
//                        }
//                    }
//                }).builder().show(activity)
//
//            }
//        })
//
//        indicator_page_config.visibility = View.GONE
//        pager_page_config.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//
//                indicator_page_config.setCurrentItem(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
//
//        text_page_config_image_add.setOnClickListener {
//
//            if (mImageList!!.size < Const.IMAGE_UPLOAD_MAX_COUNT) {
//                val intent = Intent(activity, PostGalleryActivity::class.java)
//                intent.putExtra(Const.COUNT, Const.IMAGE_UPLOAD_MAX_COUNT - mImageList!!.size)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                activity?.startActivityForResult(intent, Const.REQ_GALLERY)
//            } else {
//                ToastUtil.show(activity, getString(R.string.format_msg_image_count, Const.IMAGE_UPLOAD_MAX_COUNT))
//            }
//        }
//
//
//        if(mPage.usePrnumber!!){
//            layout_page_config_prnumber.visibility = View.VISIBLE
//        }else{
//            layout_page_config_prnumber.visibility = View.GONE
//        }
//
//        if (mPage.usePrnumber!! && mPage.numberList != null && mPage.numberList!!.isNotEmpty()) {
//            text_page_config_prnumber.text = PplusNumberUtil.getOnlyNumber(mPage.numberList!![0].number)
//        }
//
//        if (mPage.agent == null) {
//            layout_page_config_agency.visibility = View.GONE
//        } else {
//            layout_page_config_agency.visibility = View.VISIBLE
//            text_page_config_agency.text = getString(R.string.format_agency, mPage.agent!!.name)
//        }
//
//        edit_page_config_name.setText(mPage.name)
//        edit_page_config_name.setSingleLine()
//
//        if (StringUtils.isNotEmpty(mPage.catchphrase)) {
//            edit_page_config_catchphrase.setText(mPage.catchphrase)
//        }
//
////        edit_page_config_catchphrase.setOnTouchListener(object : View.OnTouchListener {
////            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
////                if (edit_page_config_catchphrase.length() > 0) {
////                    v?.parent?.requestDisallowInterceptTouchEvent(true)
////                }
////
////                return false
////            }
////        })
//
//        if (StringUtils.isNotEmpty(mPage.introduction)) {
//            edit_page_config_detail_catchphrase.setText(mPage.introduction)
//        }
//
//        edit_page_config_detail_catchphrase.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                if (edit_page_config_detail_catchphrase.length() > 0) {
//                    v?.parent?.requestDisallowInterceptTouchEvent(true)
//                }
//
//                return false
//            }
//        })
//
////        edit_page_config_opening_hours.setOnTouchListener(object : View.OnTouchListener {
////            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
////                if (edit_page_config_detail_catchphrase.length() > 0) {
////                    v?.parent?.requestDisallowInterceptTouchEvent(true)
////                }
////
////                return false
////            }
////        })
////
////        edit_page_config_convenience_info.setOnTouchListener(object : View.OnTouchListener {
////            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
////                if (edit_page_config_detail_catchphrase.length() > 0) {
////                    v?.parent?.requestDisallowInterceptTouchEvent(true)
////                }
////
////                return false
////            }
////        })
//
//        text_page_config_address.setOnClickListener {
//            val intent = Intent(activity, SearchAddressActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            intent.putExtra(Const.TYPE, mPage.type)
//            activity!!.startActivityForResult(intent, Const.REQ_SEARCH)
//        }
////        text_page_config_address2.setOnClickListener {
////            val intent = Intent(activity, SearchAddressActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            intent.putExtra(Const.TYPE, mPage.type)
////            activity!!.startActivityForResult(intent, Const.REQ_SEARCH)
////        }
//
//        LogUtil.e(LOG_TAG, "roadbase : "+mPage.address.toString())
//        val address = mPage.address
//        edit_page_config_detail_address.setSingleLine()
////        edit_page_config_detail_address2.setSingleLine()
//        if (address != null) {
//
//            LogUtil.e(LOG_TAG, "roadbase : "+address.roadBase)
//
//            if (StringUtils.isNotEmpty(address.roadBase)) {
//                LogUtil.e(LOG_TAG, address.roadBase)
//                text_page_config_address.text = address.roadBase
////                text_page_config_address2.text = address.roadBase
//
//                if (StringUtils.isNotEmpty(address.roadDetail)) {
//                    edit_page_config_detail_address.setText(address.roadDetail)
////                    edit_page_config_detail_address2.setText(address.roadDetail)
//                }
//            } else if (StringUtils.isNotEmpty(address.parcelBase)) {
//                text_page_config_address.text = address.parcelBase
////                text_page_config_address2.text = address.parcelBase
//                if (StringUtils.isNotEmpty(address.parcelDetail)) {
//                    edit_page_config_detail_address.setText(address.parcelDetail)
////                    edit_page_config_detail_address2.setText(address.parcelDetail)
//                }
//            }
//        }
//
//        text_page_config_category1.setOnClickListener {
//            showCategory(CategoryInfoManager.CATEGORY_TYPE.LEVEL1)
//        }
//
//        text_page_config_category2.setOnClickListener {
//            showCategory(CategoryInfoManager.CATEGORY_TYPE.LEVEL2)
//        }
//
//        when (mPage.type) {
//            EnumData.PageTypeCode.person.name -> {
//                firstCategories = CategoryInfoManager.getInstance().categoryListPerson
//            }
//            EnumData.PageTypeCode.store.name -> {
//                firstCategories = CategoryInfoManager.getInstance().categoryListStore
//                LogUtil.e(LOG_TAG, "category size " + firstCategories!!.size)
//            }
//            EnumData.PageTypeCode.shop.name -> {
//                firstCategories = CategoryInfoManager.getInstance().categoryListShop
//            }
//        }
//
//        if (firstCategories == null) {
//            CategoryInfoManager.getInstance().categoryListCall(CategoryInfoManager.CATEGORY_TYPE.LEVEL1, mPage.type, null, object : CategoryInfoManager.OnCategoryResultListener {
//
//                override fun onResult(Level: CategoryInfoManager.CATEGORY_TYPE, categories: List<Category>) {
//
//                    if (mPage.type == EnumData.PageTypeCode.store.name) {
//
//                        CategoryInfoManager.getInstance().categoryListStore = categories
//                        CategoryInfoManager.getInstance().save()
//                        firstCategories = CategoryInfoManager.getInstance().categoryListStore
//                    } else if (mPage.type == EnumData.PageTypeCode.person.name) {
//
//                        CategoryInfoManager.getInstance().categoryListPerson = categories
//                        CategoryInfoManager.getInstance().save()
//                        firstCategories = CategoryInfoManager.getInstance().categoryListPerson
//                    } else if (mPage.type == EnumData.PageTypeCode.shop.name) {
//
//                        CategoryInfoManager.getInstance().categoryListPerson = categories
//                        CategoryInfoManager.getInstance().save()
//                        firstCategories = CategoryInfoManager.getInstance().categoryListShop
//                    }
//
////                    CategoryInfoManager.getInstance().categoryListStore = categories
////                    CategoryInfoManager.getInstance().save()
////                    firstCategories = CategoryInfoManager.getInstance().categoryListStore
//                    setCategory()
//                }
//
//                override fun onFailed(message: String) {
//
//                }
//            })
//        } else {
//            setCategory()
//        }
//
////        text_page_config_apply_category.setOnClickListener {
////            val intent = Intent(activity, ApplyCategoryActivity::class.java)
////            intent.putExtra(Const.DATA, mPage.categoryText)
////            activity!!.startActivityForResult(intent, Const.REQ_CATEGORY_APPLY)
////        }
//
//        edit_page_config_phone.setSingleLine()
//
//        if (StringUtils.isNotEmpty(mPage.phone)) {
//            edit_page_config_phone.setText(mPage.phone!!)
//        }
//
////        if (mPage.properties != null) {
////
////            val properties = mPage.properties
////
////            if (StringUtils.isNotEmpty(properties!!.openingHours)) {
////                edit_page_config_opening_hours.setText(properties.openingHours)
////            }
////
////            if (StringUtils.isNotEmpty(properties.parkingInfo)) {
////                edit_page_config_parking_info.setText(properties.parkingInfo)
////            }
////
////            if (StringUtils.isNotEmpty(properties.convenienceInfo)) {
////                edit_page_config_convenience_info.setText(properties.convenienceInfo)
////            }
////
//////            if (StringUtils.isNotEmpty(properties.kakaoId)) {
//////                edit_page_config_kakao_id.setText(properties.kakaoId)
//////            }
//////
//////            if (StringUtils.isNotEmpty(properties.email)) {
//////                edit_page_config_email_info.setText(properties.email)
//////            }
////        }
//
////        edit_page_config_opening_hours.setOnTouchListener(object : View.OnTouchListener {
////            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
////                if (edit_page_config_opening_hours.length() > 0) {
////                    v?.parent?.requestDisallowInterceptTouchEvent(true)
////                }
////
////                return false
////            }
////        })
////
////        edit_page_config_convenience_info.setOnTouchListener(object : View.OnTouchListener {
////            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
////                if (edit_page_config_convenience_info.length() > 0) {
////                    v?.parent?.requestDisallowInterceptTouchEvent(true)
////                }
////
////                return false
////            }
////        })
//
////        edit_page_config_main_youtube.setSingleLine()
////        if (StringUtils.isNotEmpty(mPage.mainMovieUrl)) {
////            edit_page_config_main_youtube.setText(mPage.mainMovieUrl)
////        }
//
//        text_page_config_youtube_add.setOnClickListener {
//            if (edit_youtube_list.size < 5) {
//                addYoutubeLayout()
//            } else {
//                showAlert(R.string.msg_add_movie_until_5)
//            }
//        }
//
////        callMovieList()
//
//        text_page_config_save2.setOnClickListener {
//            update()
//        }
//        text_page_config_save.setOnClickListener {
//            update()
//        }
//        edit_page_config_catchphrase.setSingleLine()
//        callIntroduceImage()
//    }
//
//    private fun callIntroduceImage() {
//
//        val params = HashMap<String, String>()
//        params["no"] = "" + mPage.no!!
//        showProgress("")
//        ApiBuilder.create().getIntroImageAll(params).setCallback(object : PplusCallback<NewResultResponse<Attachment>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Attachment>>, response: NewResultResponse<Attachment>) {
//                hideProgress()
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
//                if (attachmentList!!.size > 1) {
//
//                    indicator_page_config.removeAllViews()
//                    indicator_page_config.visibility = View.VISIBLE
//                    indicator_page_config.build(LinearLayout.HORIZONTAL, attachmentList!!.size)
//                } else {
//                    indicator_page_config.visibility = View.GONE
//                }
//
//                mIntroPagerAdapter!!.dataList = attachmentList
//                pager_page_config.currentItem = 0
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Attachment>>, t: Throwable, response: NewResultResponse<Attachment>) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    fun update() {
//
//        val name = edit_page_config_name.text.toString().trim()
//        if (StringUtils.isEmpty(name)) {
//            showAlert(getString(R.string.msg_input_page_name))
//            return
//        }
//
//        mPage.name = name
//
//        val catchphrase = edit_page_config_catchphrase.text.toString().trim()
//        if (StringUtils.isEmpty(catchphrase)) {
//            showAlert(R.string.msg_input_catchphrase)
//            return
//        }
//
//        if (catchphrase.length < 10) {
//            showAlert(getString(R.string.for_catchphrase) + " " + getString(R.string.format_msg_input_over, 10))
//            return
//        }
//
//        mPage.catchphrase = catchphrase
//
//        val detailCatchphrase = edit_page_config_detail_catchphrase.text.toString().trim()
//        if (StringUtils.isEmpty(detailCatchphrase)) {
//            showAlert(R.string.msg_input_detail_catchphrase)
//            return
//        }
//
//        if (detailCatchphrase.length < 20) {
//            showAlert(getString(R.string.for_detail_catchphrase) + " " + getString(R.string.format_msg_input_over, 20))
//            return
//        }
//
//        mPage.introduction = detailCatchphrase
//
//        var address: Address? = null
//
//        var address1 = text_page_config_address.text.toString().trim()
//
//
//        if (StringUtils.isEmpty(address1)) {
//            showAlert(getString(R.string.msg_input_address))
//            return
//        }
//
//        if (StringUtils.isNotEmpty(address1)) {
//            address = Address()
//            address.roadBase = address1
//            if (StringUtils.isNotEmpty(zipCode)) {
//                address.zipCode = zipCode
//            }
//
//            var address2 = edit_page_config_detail_address.text.toString().trim()
//
//            if (StringUtils.isNotEmpty(address2)) {
//                address.roadDetail = address2
//            }
//        }
//
//        if (address != null) {
//            mPage.address = address
//        }
//
//        val cat1 = text_page_config_category1.text.toString().trim()
//        if (StringUtils.isEmpty(cat1)) {
//            ToastUtil.show(activity, getString(R.string.msg_select_category))
//            return
//        }
//
//        val cat2 = text_page_config_category2.text.toString().trim()
//        if (StringUtils.isEmpty(cat2)) {
//            ToastUtil.show(activity, getString(R.string.msg_select_category))
//            return
//        }
//
//        val category = categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2]
//        if (category == null) {
//            ToastUtil.show(activity, getString(R.string.msg_select_category))
//            return
//        }
//
//        mPage.category = category
//
//        val phone = edit_page_config_phone.text.toString().trim()
//
//        if (StringUtils.isEmpty(phone)) {
//            showAlert(R.string.msg_input_store_phone)
//            return
//        }
//
//        mPage.phone = phone
//
////        val openingHours = edit_page_config_opening_hours.text.toString().trim()
////        val parkingInfo = edit_page_config_parking_info.text.toString().trim()
////        val convenienceInfo = edit_page_config_convenience_info.text.toString().trim()
////        val kakaoId = edit_page_config_kakao_id.text.toString().trim()
////        val email = edit_page_config_email_info.text.toString().trim()
////        if (mPage.properties == null) {
////            mPage.properties = PageProperties()
////        }
////        mPage.properties!!.openingHours = openingHours
////        mPage.properties!!.parkingInfo = parkingInfo
////        mPage.properties!!.convenienceInfo = convenienceInfo
////        mPage.properties!!.kakaoId = kakaoId
////        mPage.properties!!.email = email
////
////        val mainVideoUrl = edit_page_config_main_youtube.text.toString().trim()
////        if (StringUtils.isNotEmpty(mainVideoUrl)) {
////            if (PplusCommonUtil.getYoutubeVideoId(mainVideoUrl) == null) {
////                showAlert(R.string.msg_input_correct_youtube)
////                return
////            }
////        }
////
////        mPage.mainMovieUrl = mainVideoUrl
//
//        showProgress("")
//        ApiBuilder.create().updatePage(mPage).setCallback(object : PplusCallback<NewResultResponse<Page>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                LoginInfoManager.getInstance().user.page = response.data
//                LoginInfoManager.getInstance().save()
//
//                showAlert(R.string.msg_saved)
//                activity?.setResult(Activity.RESULT_OK)
//                activity?.finish()
//
////                val introMovieList = ArrayList<ImgUrl>()
////
////                if (edit_youtube_list.size == 1) {
////                    edit_youtube_list[0].text.toString().trim()
////                }
////
////                for (i in edit_youtube_list.indices) {
////                    val addVideoUrl = edit_youtube_list[i].text.toString().trim()
////                    if (StringUtils.isNotEmpty(addVideoUrl)) {
////                        if (PplusCommonUtil.getYoutubeVideoId(addVideoUrl) == null) {
////                            showAlert(R.string.msg_input_correct_youtube)
////                            return
////                        }
////
////                        val videoUrl = ImgUrl(addVideoUrl, 5 - i)
////                        introMovieList.add(videoUrl)
////                    }
////                }
////
////                val paramsIntroMovie = ParamsIntroMovie()
////                paramsIntroMovie.no = mPage.no
////                paramsIntroMovie.introMovieList = introMovieList
////                showProgress("")
////                ApiBuilder.create().updateIntroMovieList(paramsIntroMovie).setCallback(object : PplusCallback<NewResultResponse<Page>> {
////
////                    override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
////                        hideProgress()
////                        if (!isAdded) {
////                            return
////                        }
////                        showAlert(R.string.msg_saved)
////                        activity?.setResult(Activity.RESULT_OK)
////                        activity?.finish()
////                    }
////
////                    override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {
////
////                        hideProgress()
////                    }
////                }).build().call()
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {
//
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    var edit_youtube_list = ArrayList<EditText>()
//    var view_youtube_delete_list = ArrayList<View>()
//
//    private fun addYoutubeLayout() {
//        val parent = LayoutInflater.from(activity).inflate(R.layout.item_youtube_url, LinearLayout(activity))
//        parent.edit_add_youtube.setSingleLine()
//        val image_delete = parent.findViewById<ImageView>(R.id.image_add_youtube_delete)
//        image_delete.setOnClickListener {
//            view_youtube_delete_list.remove(image_delete)
//            edit_youtube_list.remove(parent.edit_add_youtube)
//            layout_page_config_add_youtube.removeView(parent)
//            if (view_youtube_delete_list.size == 1) {
//                view_youtube_delete_list[0].visibility = View.GONE
//            }
//        }
//
//        view_youtube_delete_list[0].visibility = View.VISIBLE
//        layout_page_config_add_youtube.addView(parent)
//        view_youtube_delete_list.add(image_delete)
//        edit_youtube_list.add(parent.edit_add_youtube)
//        parent.edit_add_youtube.requestFocus()
//    }
//
//    private fun callMovieList() {
//
//        val params = HashMap<String, String>()
//        params["no"] = "" + mPage.no!!
//        ApiBuilder.create().getIntroMovieAll(params).setCallback(object : PplusCallback<NewResultResponse<ImgUrl>> {
//
//            override fun onResponse(call: Call<NewResultResponse<ImgUrl>>, response: NewResultResponse<ImgUrl>) {
//
//                edit_youtube_list = ArrayList<EditText>()
//                view_youtube_delete_list = ArrayList<View>()
//                val youtubeList = response.datas
//                if (youtubeList == null || youtubeList.size == 0) {
//                    val parent = LayoutInflater.from(activity).inflate(R.layout.item_youtube_url, LinearLayout(activity))
//                    parent.edit_add_youtube.setSingleLine()
//                    val image_delete = parent.findViewById<ImageView>(R.id.image_add_youtube_delete)
//                    image_delete.visibility = View.GONE
//                    image_delete.setOnClickListener {
//                        view_youtube_delete_list.remove(image_delete)
//                        edit_youtube_list.remove(parent.edit_add_youtube)
//                        layout_page_config_add_youtube.removeView(parent)
//                        if (view_youtube_delete_list.size == 1) {
//                            view_youtube_delete_list[0].visibility = View.GONE
//                        }
//                    }
//                    layout_page_config_add_youtube.addView(parent)
//                    view_youtube_delete_list.add(image_delete)
//                    edit_youtube_list.add(parent.edit_add_youtube)
//                } else {
//                    for (i in youtubeList.indices) {
//                        if (StringUtils.isNotEmpty(youtubeList[i].url)) {
//                            val parent = LayoutInflater.from(activity).inflate(R.layout.item_youtube_url, LinearLayout(activity))
//                            parent.edit_add_youtube.setSingleLine()
//                            parent.edit_add_youtube.setText(youtubeList[i].url)
//                            val image_delete = parent.findViewById<ImageView>(R.id.image_add_youtube_delete)
//                            if (youtubeList.size == 1) {
//                                image_delete.visibility = View.GONE
//                            }
//                            image_delete.setOnClickListener {
//                                view_youtube_delete_list.remove(image_delete)
//                                edit_youtube_list.remove(parent.edit_add_youtube)
//                                layout_page_config_add_youtube.removeView(parent)
//                                if (view_youtube_delete_list.size == 1) {
//                                    view_youtube_delete_list[0].visibility = View.GONE
//                                }
//                            }
//                            layout_page_config_add_youtube.addView(parent)
//                            view_youtube_delete_list.add(image_delete)
//                            edit_youtube_list.add(parent.edit_add_youtube)
//                        }
//
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<ImgUrl>>, t: Throwable, response: NewResultResponse<ImgUrl>) {
//
//            }
//        }).build().call()
//    }
//
//    private fun setCategory() {
//
//        if (mPage.category != null) {
//            LogUtil.e(LOG_TAG, "parent no : {}", mPage.category!!.parent!!.no)
//            for (category in firstCategories!!) {
//                if (category.no == mPage.category!!.parent!!.no) {
//                    categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1] = category
//                    categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] = mPage.category!!
//                    text_page_config_category1.text = category.name
//                    text_page_config_category2.text = mPage.category!!.name
//                    break
//                }
//            }
//        }
//    }
//
//    private fun showCategory(categoryLevel: CategoryInfoManager.CATEGORY_TYPE) {
//
//        val builder = AlertBuilder.Builder()
//        val styleAlert = AlertBuilder.STYLE_ALERT.LIST_RADIO_BOTTOM
//        when (categoryLevel) {
//            CategoryInfoManager.CATEGORY_TYPE.LEVEL1 -> if (CategoryInfoManager.getInstance().isCalled) {
//
//                if (firstCategories == null) {
//                    firstCategories = CategoryInfoManager.getInstance().categoryListStore
//                }
//
//                builder.setTitle(getString(R.string.word_first_category))
//                builder.setRightText(getString(R.string.word_select))
//
//                builder.setBackgroundClickable(false)
//
//                var rootIndex = 0
//
//                for (i in firstCategories!!.indices) {
//                    val messageData = AlertData.MessageData()
//
//                    if (categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1] == null) {
//                        if (mPage.category != null && mPage.category!!.parent != null && mPage.category!!.parent!!.no == firstCategories!!.get(i).no) {
//                            rootIndex = i + 1
//                        }
//                    } else {
//                        if (categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1]!!.no == firstCategories!![i].no) {
//                            rootIndex = i + 1
//                        }
//                    }
//
//                    messageData.contents = firstCategories!![i].name
//
//                    builder.addContents(messageData)
//                }
//                styleAlert.setValue(rootIndex)
//                builder.setStyle_alert(styleAlert)
//                builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                    override fun onCancel() {
//
//                    }
//
//                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                        if (event_alert == AlertBuilder.EVENT_ALERT.RADIO) {
//                            val index = event_alert.getValue() - 1
//
//                            if (firstCategories != null && firstCategories!![index] != null) {
//                                if (categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1] == null || firstCategories!![index].no != categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1]!!.no) {
//                                    categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1] = firstCategories!![index]
//                                    text_page_config_category1.text = ""
//                                    categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] = null
//                                    text_page_config_category1.text = firstCategories!![index].name
//                                }
//                            }
//                        }
//                    }
//                })
//
//                builder.builder().show(activity)
//            }
//            CategoryInfoManager.CATEGORY_TYPE.LEVEL2 -> {
//
//                if (!categoryMap.containsKey(CategoryInfoManager.CATEGORY_TYPE.LEVEL1)) {
//                    AlertBuilder.Builder().setContents(getString(R.string.msg_select_first_category)).setRightText(getString(R.string.word_confirm)).setDefaultMaxLine(1).builder().show(activity)
//                    return
//                }
//
//                val category = categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1]
//
//                CategoryInfoManager.getInstance().categoryListCall(CategoryInfoManager.CATEGORY_TYPE.LEVEL2, category!!.type, category.no, object : CategoryInfoManager.OnCategoryResultListener {
//
//                    override fun onResult(Level: CategoryInfoManager.CATEGORY_TYPE, categories: List<Category>) {
//
//                        if (categories.isEmpty()) {
//                            AlertBuilder.Builder().setContents(getString(R.string.msg_select_other_category)).setRightText(getString(R.string.word_confirm)).setDefaultMaxLine(2).builder().show(activity)
//                            return
//                        }
//
//                        builder.setTitle(getString(R.string.word_second_category))
//                        builder.setRightText(getString(R.string.word_select))
//                        builder.setBackgroundClickable(false)
//                        var rootIndex = 0
//
//
//                        var messageData: AlertData.MessageData
//
//                        var no: Long? = null
//
//                        if (categoryMap.containsKey(CategoryInfoManager.CATEGORY_TYPE.LEVEL2)) {
//                            if (categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] != null)
//                                no = categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2]!!.no
//                        } else if (mPage.category != null) {
//                            no = mPage.category!!.no
//                        }
//
//                        for (i in categories.indices) {
//
//                            if (no != null && no == categories[i].no) {
//                                rootIndex = i + 1
//                                categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] = categories[i]
//                            }
//
//                            messageData = AlertData.MessageData()
//                            messageData.contents = categories[i].name
//                            builder.addContents(messageData)
//                        }
//
//                        styleAlert.setValue(rootIndex)
//                        builder.setStyle_alert(styleAlert)
//                        builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                            override fun onCancel() {
//
//                            }
//
//                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                                if (event_alert == AlertBuilder.EVENT_ALERT.RADIO) {
//                                    val index = event_alert.getValue() - 1
//                                    text_page_config_category2.text = categories[index].name
//                                    categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] = categories[index]
//                                }
//                            }
//                        })
//
//                        builder.builder().show(activity)
//                    }
//
//                    override fun onFailed(message: String) {
//
//                    }
//                })
//            }
//        }
//
//    }
//
//    var zipCode: String? = null
//
//    private var defaultUpload: DefaultUpload? = null
//    private var imgList: ArrayList<ImgUrl>? = null
//
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
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_CATEGORY_APPLY -> if (resultCode == Activity.RESULT_OK) {
//                if (data != null) {
//                    val category = data.getStringExtra(Const.DATA)
//                    if (StringUtils.isNotEmpty(category)) {
//                        mPage.categoryText = category
//                    }
//                }
//            }
//            Const.REQ_SEARCH -> if (resultCode == Activity.RESULT_OK) {
//                if (data != null) {
//                    val juso = data.getParcelableExtra<Juso>(Const.ADDRESS)
//                    text_page_config_address.text = juso.roadAddr
////                    text_page_config_address2.text = juso.roadAddr
//                    zipCode = juso.zipNo
//                    callLatLon(juso.roadAddrPart1)
//                }
//            }
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
//            Const.REQ_INTRODUCE_IMAGE -> {
//                callIntroduceImage()
//            }
//        }
//    }
//
//    private fun callLatLon(address: String) {
//
//        showProgress("")
//
//        val params = HashMap<String, String>()
//        params["address"] = address
//
//        ApiBuilder.create().getCoordByAddress(params).setCallback(object : PplusCallback<NewResultResponse<Coord>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Coord>>, response: NewResultResponse<Coord>) {
//
//                hideProgress()
//                mPage.latitude = response.data.y
//                mPage.longitude = response.data.x
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Coord>>, t: Throwable, response: NewResultResponse<Coord>) {
//
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun getPID(): String {
//        return "Main_menu_edit information"
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            //            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                PageConfigFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(ARG_PARAM1, param1)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
