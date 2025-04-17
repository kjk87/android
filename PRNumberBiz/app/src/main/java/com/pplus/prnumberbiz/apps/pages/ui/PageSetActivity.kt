package com.pplus.prnumberbiz.apps.pages.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.CategoryInfoManager
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.pages.data.PageSetImageAdapter
import com.pplus.prnumberbiz.apps.post.data.UploadStatus
import com.pplus.prnumberbiz.apps.post.ui.PostGalleryActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.common.PageOpenBoundsCode
import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.*
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsIntroImage
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.upload.DefaultUpload
import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.activity_page_set.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.HashMap
import java.util.concurrent.ConcurrentHashMap

class PageSetActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_page_set
    }

    var mImageAdapter: PageSetImageAdapter? = null
    private lateinit var mUploadDataMap: ConcurrentHashMap<String, UploadStatus>
    private lateinit var mUploadUrlMap: ConcurrentHashMap<String, String>
    var mPage = Page()

    override fun initializeView(savedInstanceState: Bundle?) {

        mPage = LoginInfoManager.getInstance().user.page!!

        if (mPage.type == EnumData.PageTypeCode.person.name) {
            text_page_set_image_title.setText(R.string.word_page_image)
            text_page_set_name_title.setText(R.string.word_my_page_store_name)
            edit_page_set_name.setHint(R.string.hint_edit_page_store_name)
            text_page_set_catchphrase_title.setText(R.string.word_page_intro)
            edit_page_set_catchphrase.setHint(R.string.hint_page_catchphrase)
            text_page_set_address_title.setText(R.string.word_page_address)
            text_page_set_address.setHint(R.string.hint_address_1)
        }else{
            text_page_set_image_title.setText(R.string.word_store_image)
            text_page_set_name_title.setText(R.string.word_my_page_name)
            edit_page_set_name.setHint(R.string.hint_edit_page_name)
            text_page_set_catchphrase_title.setText(R.string.word_store_intro)
            edit_page_set_catchphrase.setHint(R.string.hint_store_catchphrase)
            text_page_set_address_title.setText(R.string.word_store_address)

        }

        edit_page_set_name.setSingleLine()
        edit_page_set_detail_address.setSingleLine()
        edit_page_set_agency.setSingleLine()
        edit_page_set_phone.setSingleLine()

        text_page_set_agency_title.text = PplusCommonUtil.fromHtml(getString(R.string.html_agency))

        // Setup D&D feature and RecyclerView
        val dragMgr = RecyclerViewDragDropManager()

        dragMgr.setInitiateOnMove(false)
        dragMgr.setInitiateOnLongPress(true)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_page_set_image.layoutManager = layoutManager
        mImageAdapter = PageSetImageAdapter(this)

        recycler_page_set_image.adapter = dragMgr.createWrappedAdapter(mImageAdapter!!)
        dragMgr.attachRecyclerView(recycler_page_set_image)

        mImageAdapter!!.setOnItemDeleteListener(object : PageSetImageAdapter.OnItemDeleteListener {
            override fun onItemDelete(position: Int) {
                if (mImageAdapter!!.itemCount > 0) {
                    layout_page_set_add_image.visibility = View.GONE
                    recycler_page_set_image.visibility = View.VISIBLE
                } else {
                    layout_page_set_add_image.visibility = View.VISIBLE
                    recycler_page_set_image.visibility = View.GONE
                }
            }
        })

        text_page_set_add_image.setOnClickListener {
            goPostGallery()
        }
        layout_page_set_add_image.setOnClickListener {
            goPostGallery()
        }

        text_page_set_address.setOnClickListener {
            intent = Intent(this, SearchAddressActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SEARCH)
        }

//        text_page_set_category1.setOnClickListener {
//            showCategory(CategoryInfoManager.CATEGORY_TYPE.LEVEL1)
//        }
//        text_page_set_category2.setOnClickListener {
//            showCategory(CategoryInfoManager.CATEGORY_TYPE.LEVEL2)
//        }

        mUploadDataMap = ConcurrentHashMap()
        mUploadUrlMap = ConcurrentHashMap()


        text_page_set_complete.setOnClickListener {

            val name = edit_page_set_name.text.toString().trim()
            if (StringUtils.isEmpty(name)) {
                showAlert(R.string.msg_input_page_name)
                return@setOnClickListener
            }

            mPage.name = name

            val catchphrase = edit_page_set_catchphrase.text.toString().trim()
            if (StringUtils.isEmpty(catchphrase)) {
                showAlert(R.string.msg_input_catchphrase)
                return@setOnClickListener
            }

            if (catchphrase.length < 10) {
                showAlert(getString(R.string.for_catchphrase) + " " + getString(R.string.format_msg_input_over, 10))
                return@setOnClickListener
            }

            mPage.catchphrase = catchphrase

            val address1 = text_page_set_address.text.toString().trim()
            val address2 = edit_page_set_detail_address.text.toString().trim()

            if (StringUtils.isEmpty(address1)) {
                showAlert(getString(R.string.msg_input_address))
                return@setOnClickListener
            }

            if (StringUtils.isEmpty(address2)) {
                showAlert(getString(R.string.msg_input_detail_address))
                return@setOnClickListener
            }

            val address = Address()
            address.roadBase = address1
            address.roadDetail = address2
            if (StringUtils.isNotEmpty(zipCode)) {
                address.zipCode = zipCode
            }

            mPage.address = address

            val phone = edit_page_set_phone.text.toString().trim()

            if (StringUtils.isEmpty(phone)) {
                showAlert(R.string.msg_input_store_phone)
                return@setOnClickListener
            }

            mPage.phone = phone

//            val cat1 = text_page_set_category1.text.toString().trim()
//            if (StringUtils.isEmpty(cat1)) {
//                showAlert(R.string.msg_select_category)
//                return@setOnClickListener
//            }
//
//            val cat2 = text_page_set_category2.text.toString().trim()
//            if (StringUtils.isEmpty(cat2)) {
//                showAlert(R.string.msg_select_category)
//                return@setOnClickListener
//            }
//
//            val category = categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2]
//            if (category == null) {
//                showAlert(R.string.msg_select_category)
//                return@setOnClickListener
//            }
//
//            mPage.category = category

            if ((mImageAdapter!!.getDataList() == null || mImageAdapter!!.getDataList()!!.isEmpty())) {
                showAlert(R.string.msg_reg_intro_image)
                return@setOnClickListener
            }

            mPage.openBound = PageOpenBoundsCode.everybody.name

            val agency_code = edit_page_set_agency.text.toString()
            if (StringUtils.isNotEmpty(agency_code)) {

                val params = HashMap<String, String>()
                params["code"] = agency_code
                showProgress("")
                ApiBuilder.create().getAgent(params).setCallback(object : PplusCallback<NewResultResponse<Agent>> {

                    override fun onResponse(call: Call<NewResultResponse<Agent>>, response: NewResultResponse<Agent>) {

                        hideProgress()
                        if (response.data != null) {

                            if (response.data.status == EnumData.AgentStatus.active.name) {
                                mPage.agent = response.data
                                updatePage()
                            } else {
                                showAlert(R.string.msg_pending_agency_code)
                            }

                        } else {
                            showAlert(R.string.msg_invalid_agency_code)
                        }
                    }

                    override fun onFailure(call: Call<NewResultResponse<Agent>>, t: Throwable, response: NewResultResponse<Agent>) {

                        hideProgress()
                        showAlert(R.string.msg_invalid_agency_code)
                    }
                }).build().call()

            } else {
                updatePage()
            }


        }

    }

    fun updatePage() {
        showProgress("")
        ApiBuilder.create().updatePage(mPage).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                hideProgress()

                LoginInfoManager.getInstance().user.page = response.data
                LoginInfoManager.getInstance().save()
                uploadIntroImageList()

            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {

                hideProgress()
            }
        }).build().call()
    }

    fun goPostGallery() {

        if (mImageAdapter!!.itemCount < 10) {
            val intent = Intent(this, PostGalleryActivity::class.java)
            intent.putExtra(Const.COUNT, 10 - mImageAdapter!!.itemCount)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_GALLERY)
        } else {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_max_image_count), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                }
            }).builder().show(this)
        }

    }

    var zipCode = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_GALLERY -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val dataList = data.getParcelableArrayListExtra<Uri>(Const.CROPPED_IMAGE)

                    val index = mImageAdapter!!.itemCount

                    for (i in 0 until dataList.size) {
                        mImageAdapter!!.add(Attachment())
                        mUploadDataMap[(index + i).toString()] = UploadStatus.UPLOAD
                        mUploadUrlMap[(index + i).toString()] = dataList[i].path
                    }

                    showProgress("")
                    for (i in 0 until dataList.size) {
                        uploadFile((index + i).toString(), dataList[i].path)
                    }

                    layout_page_set_add_image.visibility = View.GONE
                    recycler_page_set_image.visibility = View.VISIBLE
                }
            }
            Const.REQ_SEARCH -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val juso = data.getParcelableExtra<Juso>(Const.ADDRESS)
                        text_page_set_address.text = juso.roadAddr
                        zipCode = juso.zipNo
                        callLatLon(juso.roadAddrPart1)
                    }
                }
            }
        }
    }

    private fun callLatLon(address: String) {

        showProgress("")

        val params = HashMap<String, String>()
        params["address"] = address

        ApiBuilder.create().getCoordByAddress(params).setCallback(object : PplusCallback<NewResultResponse<Coord>> {

            override fun onResponse(call: Call<NewResultResponse<Coord>>, response: NewResultResponse<Coord>) {

                hideProgress()
                mPage.latitude = response.data.y
                mPage.longitude = response.data.x
            }

            override fun onFailure(call: Call<NewResultResponse<Coord>>, t: Throwable, response: NewResultResponse<Coord>) {

                hideProgress()
            }
        }).build().call()
    }

//    private var firstCategories: List<Category>? = null
//    private val categoryMap = HashMap<CategoryInfoManager.CATEGORY_TYPE, Category?>()
//
//    private fun showCategory(categoryLevel: CategoryInfoManager.CATEGORY_TYPE) {
//
//        val styleAlert = AlertBuilder.STYLE_ALERT.LIST_RADIO_BOTTOM
//        val builder = AlertBuilder.Builder()
//        when (categoryLevel) {
//            CategoryInfoManager.CATEGORY_TYPE.LEVEL1 -> if (CategoryInfoManager.getInstance().isCalled) {
//
//
//                when (mPage.type) {
//                    EnumData.PageTypeCode.person.name -> {
//                        firstCategories = CategoryInfoManager.getInstance().categoryListPerson
//                    }
//                    EnumData.PageTypeCode.store.name -> {
//                        firstCategories = CategoryInfoManager.getInstance().categoryListStore
//                    }
//                    EnumData.PageTypeCode.shop.name -> {
//                        firstCategories = CategoryInfoManager.getInstance().categoryListShop
//                    }
//                }
//
////                if (firstCategories == null) {
////                    firstCategories = CategoryInfoManager.getInstance().categoryListStore
////                }
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
//                        if (mPage.category != null && mPage.category!!.parent != null && mPage.category!!.parent!!.no == firstCategories!![i].no) {
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
//                            if (firstCategories != null) {
//                                if (categoryMap.get(CategoryInfoManager.CATEGORY_TYPE.LEVEL1) == null || firstCategories!![index].no != categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1]!!.no) {
//                                    categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1] = firstCategories!![index]
//                                    text_page_set_category2.text = ""
//                                    categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] = null
//                                    text_page_set_category1.text = firstCategories!![index].name
//                                }
//                            }
//                        }
//                    }
//                })
//
//                builder.builder().show(this@PageSetActivity)
//            }
//            CategoryInfoManager.CATEGORY_TYPE.LEVEL2 -> {
//
//                if (!categoryMap.containsKey(CategoryInfoManager.CATEGORY_TYPE.LEVEL1)) {
//                    AlertBuilder.Builder().setContents(getString(R.string.msg_select_first_category)).setRightText(getString(R.string.word_confirm)).setDefaultMaxLine(1).builder().show(this)
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
//                            AlertBuilder.Builder().setContents(getString(R.string.msg_select_other_category)).setRightText(getString(R.string.word_confirm)).setDefaultMaxLine(2).builder().show(this@PageSetActivity)
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
//                                    text_page_set_category2.text = categories[index].name
//                                    categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] = categories[index]
//                                }
//                            }
//                        })
//
//                        builder.builder().show(this@PageSetActivity)
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

    private var defaultUpload: DefaultUpload? = null

    private fun uploadFile(key: String, url: String) {

        val paramsAttachment = ParamsAttachment()
        paramsAttachment.targetType = AttachmentTargetTypeCode.goods
        paramsAttachment.setFile(url)

        if (defaultUpload == null) {
            defaultUpload = DefaultUpload(object : PplusUploadListener<Attachment> {

                override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {

                    LogUtil.e(LOG_TAG, "tag = {}, no = {}, id = {}", tag, resultResponse.data.no, resultResponse.data.id)


                    mImageAdapter!!.insertAttach(tag, resultResponse.data)
                    mUploadDataMap[tag] = UploadStatus.SUCCESS_UPLOAD

                    if (checkFinish()) {
                        hideProgress()
                        delete()
                    }
                }

                override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {

                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString())
                    mUploadDataMap[tag] = UploadStatus.ERROR_UPLOAD
                }

            })
        }

        defaultUpload!!.request(key, paramsAttachment)

    }

    fun uploadIntroImageList() {
        val imgList = ArrayList<ImgUrl>()

        for (i in 0 until mImageAdapter!!.getDataList()!!.size) {
            imgList.add(ImgUrl(mImageAdapter!!.getItem(i).no, Const.IMAGE_UPLOAD_MAX_COUNT - i))
        }

        val params = ParamsIntroImage()
        params.no = LoginInfoManager.getInstance().user.page!!.no
        params.introImageList = imgList

        ApiBuilder.create().updateIntroImageList(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                hideProgress()
                copyAttachment(imgList[0].no)
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {

                hideProgress()
            }
        }).build().call()
    }

    private fun copyAttachment(no: Long?) {

        val params = HashMap<String, String>()
        params["no"] = "" + no!!
        showProgress("")
        ApiBuilder.create().copyAttachment(params).setCallback(object : PplusCallback<NewResultResponse<Attachment>> {

            override fun onResponse(call: Call<NewResultResponse<Attachment>>, response: NewResultResponse<Attachment>) {

                hideProgress()
                updateProfile(response.data.no)
            }

            override fun onFailure(call: Call<NewResultResponse<Attachment>>, t: Throwable, response: NewResultResponse<Attachment>) {

                hideProgress()
            }
        }).build().call()
    }

    private fun updateProfile(no: Long?) {

        val params = HashMap<String, String>()
        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!
        params["profileImage.no"] = "" + no!!
        showProgress("")
        ApiBuilder.create().updatePageProfileImage(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                hideProgress()
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                hideProgress()
            }
        }).build().call()
    }

    fun delete() {
        val iterator = mUploadUrlMap.keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            PplusCommonUtil.deleteFromMediaScanner(mUploadUrlMap[key]!!)
        }
    }

    fun checkFinish(): Boolean {
        val iterator = mUploadDataMap.keys.iterator()
        var isFinish = true
        var isError = false
        while (iterator.hasNext()) {
            val key = iterator.next()
            when (mUploadDataMap[key]) {
                UploadStatus.UPLOAD -> {
                    isFinish = false
                }
                UploadStatus.ERROR_UPLOAD -> {
                    isError = true
                }
            }
            if (!isFinish) {
                break
            }
        }

        if (isError) {
            hideProgress()
        }

        return isFinish
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
