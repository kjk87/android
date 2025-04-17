package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.pple.pplus.utils.part.apps.permission.Permission
import com.pple.pplus.utils.part.apps.permission.PermissionListener
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.PRNumberBizApplication
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.PPlusPermission
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.CategoryInfoManager
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.pages.ui.PhotoTakerActivity
import com.pplus.prnumberbiz.apps.pages.ui.SearchAddressActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.*
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.upload.DefaultUpload
import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_seller_apply.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class SellerApplyActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_seller_apply
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        edit_seller_apply_phone.setSingleLine()
        edit_seller_apply_account_holder_name.setSingleLine()
        edit_seller_apply_account_number.setSingleLine()
        edit_seller_apply_mail_id.setSingleLine()
        edit_seller_apply_detail_address.setSingleLine()

        text_seller_apply_bank_name.setOnClickListener {
            val intent = Intent(this, BankSelectActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, Const.REQ_BANK_SELECT)
        }

        text_seller_apply_reselect.setOnClickListener {
            selectImage()
        }

        text_seller_apply_attach.setOnClickListener {
            selectImage()
        }
        text_seller_apply_mail_domain.setOnClickListener {
            showEmailPopup()
        }

        text_seller_apply_view_biz_reg.setOnClickListener {
            val intent = Intent(this, BizRegMethodActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        text_seller_apply_view_terms.setOnClickListener {
            val intent = Intent(this, SaleGoodsTermsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        text_seller_apply_category1.setOnClickListener {
            showCategory(CategoryInfoManager.CATEGORY_TYPE.LEVEL1)
        }
        text_seller_apply_category2.setOnClickListener {
            showCategory(CategoryInfoManager.CATEGORY_TYPE.LEVEL2)
        }

        text_seller_apply_address.setOnClickListener {
            intent = Intent(this, SearchAddressActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SEARCH)
        }

        text_seller_apply.setOnClickListener {

            if (StringUtils.isEmpty(licenseImage)) {
                showAlert(getString(R.string.msg_reg_store_image))
                return@setOnClickListener
            }

            val bankName = text_seller_apply_bank_name.text.toString().trim()

            if (StringUtils.isEmpty(bankName)) {
                showAlert(getString(R.string.msg_input_bank_name))
                return@setOnClickListener
            }

            val depositor = edit_seller_apply_account_holder_name.text.toString().trim()

            if (StringUtils.isEmpty(depositor)) {
                showAlert(getString(R.string.msg_input_account_name))
                return@setOnClickListener
            }

            val accountNumber = edit_seller_apply_account_number.text.toString().trim()

            if (StringUtils.isEmpty(accountNumber)) {
                showAlert(getString(R.string.msg_input_account_number))
                return@setOnClickListener
            }

            val phone = edit_seller_apply_phone.text.toString().trim()

            if (StringUtils.isEmpty(phone)) {
                showAlert(getString(R.string.msg_input_store_phone))
                return@setOnClickListener
            }

            val address1 = text_seller_apply_address.text.toString().trim()
            val address2 = edit_seller_apply_detail_address.text.toString().trim()

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

            val emailId = edit_seller_apply_mail_id.text.toString().trim()

            if (StringUtils.isEmpty(emailId)) {
                showAlert(getString(R.string.msg_input_email))
                return@setOnClickListener
            }

            var email = ""
            if (mIsDirect) {
                email = emailId
            } else {
                val domain = text_seller_apply_mail_domain.text.toString()
                if (StringUtils.isEmpty(domain)) {
                    showAlert(getString(R.string.msg_select_domain))
                    return@setOnClickListener
                }
                email = "$emailId@$domain"
            }

            if (!FormatUtil.isEmailAddress(email)) {
                showAlert(R.string.msg_valid_email)
                return@setOnClickListener
            }

            val cat1 = text_seller_apply_category1.text.toString().trim()
            if (StringUtils.isEmpty(cat1)) {
                showAlert(R.string.msg_select_category)
                return@setOnClickListener
            }

            val cat2 = text_seller_apply_category2.text.toString().trim()
            if (StringUtils.isEmpty(cat2)) {
                showAlert(R.string.msg_select_category)
                return@setOnClickListener
            }

            val category = categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2]
            if (category == null) {
                showAlert(R.string.msg_select_category)
                return@setOnClickListener
            }

            if (!check_seller_apply_terms.isChecked) {
                showAlert(getString(R.string.msg_agree_terms))
                return@setOnClickListener
            }

            val page = LoginInfoManager.getInstance().user.page!!
            page.licenseImage = licenseImage
            page.bank = bankName
            page.bankAccount = accountNumber
            page.depositor = depositor
            page.phone = phone
            page.address = address
            page.email = email
            if (page.status == "return") {
                page.status = EnumData.PageStatus.redemand.name
            } else {
                page.status = EnumData.PageStatus.pending.name
            }

            page.category = category
            updatePage(page)
        }
        val page = LoginInfoManager.getInstance().user.page!!
        if (page.status == "return") {//승인거절
            if (StringUtils.isNotEmpty(page.licenseImage)) {
                licenseImage = page.licenseImage!!
                text_seller_apply_attach.visibility = View.GONE
                Glide.with(this).load(page.licenseImage).into(image_seller_apply_biz_reg)
            }

            text_seller_apply_bank_name.text = page.bank
            edit_seller_apply_account_holder_name.setText(page.depositor)
            edit_seller_apply_account_number.setText(page.bankAccount)
            edit_seller_apply_phone.setText(page.phone)
            val address = page.address
            if (address != null) {

                if (StringUtils.isNotEmpty(address.roadBase)) {
                    LogUtil.e(LOG_TAG, address.roadBase)
                    text_seller_apply_address.text = address.roadBase
                    if (StringUtils.isNotEmpty(address.roadDetail)) {
                        edit_seller_apply_detail_address.setText(address.roadDetail)
                    }
                }
            }

            if (StringUtils.isNotEmpty(page.email)) {
                val emailId = page.email!!.split("@")[0]
                val emailDomain = page.email!!.split("@")[1]
                edit_seller_apply_mail_id.setText(emailId)
                text_seller_apply_mail_domain.text = emailDomain
            }

            firstCategories = CategoryInfoManager.getInstance().categoryListStore
            if (page.category != null) {
                for (category in firstCategories!!) {
                    if (category.no == page.category!!.parent!!.no) {
                        categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1] = category
                        categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] = page.category!!
                        text_seller_apply_category1.text = category.name
                        text_seller_apply_category2.text = page.category!!.name
                        break
                    }
                }
            }
        }
    }

    fun updatePage(page: Page) {
        showProgress("")
        ApiBuilder.create().updatePage(page).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                hideProgress()
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {

                hideProgress()
            }
        }).build().call()
    }

    private var firstCategories: List<Category>? = null
    private val categoryMap = HashMap<CategoryInfoManager.CATEGORY_TYPE, Category?>()

    private fun showCategory(categoryLevel: CategoryInfoManager.CATEGORY_TYPE) {

        val styleAlert = AlertBuilder.STYLE_ALERT.LIST_RADIO_BOTTOM
        val builder = AlertBuilder.Builder()
        when (categoryLevel) {
            CategoryInfoManager.CATEGORY_TYPE.LEVEL1 -> if (CategoryInfoManager.getInstance().isCalled) {


                firstCategories = CategoryInfoManager.getInstance().categoryListStore

//                if (firstCategories == null) {
//                    firstCategories = CategoryInfoManager.getInstance().categoryListStore
//                }

                builder.setTitle(getString(R.string.word_first_category))
                builder.setRightText(getString(R.string.word_select))

                builder.setBackgroundClickable(false)

                var rootIndex = 0

                for (i in firstCategories!!.indices) {
                    val messageData = AlertData.MessageData()

//                    if (categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1] == null) {
//                        if (mPage.category != null && mPage.category!!.parent != null && mPage.category!!.parent!!.no == firstCategories!![i].no) {
//                            rootIndex = i + 1
//                        }
//                    } else {
//                        if (categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1]!!.no == firstCategories!![i].no) {
//                            rootIndex = i + 1
//                        }
//                    }

                    messageData.contents = firstCategories!![i].name

                    builder.addContents(messageData)
                }
                styleAlert.setValue(rootIndex)
                builder.setStyle_alert(styleAlert)
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        if (event_alert == AlertBuilder.EVENT_ALERT.RADIO) {
                            val index = event_alert.getValue() - 1

                            if (firstCategories != null) {
                                if (categoryMap.get(CategoryInfoManager.CATEGORY_TYPE.LEVEL1) == null || firstCategories!![index].no != categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1]!!.no) {
                                    categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1] = firstCategories!![index]
                                    text_seller_apply_category2.text = ""
                                    categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] = null
                                    text_seller_apply_category1.text = firstCategories!![index].name
                                }
                            }
                        }
                    }
                })

                builder.builder().show(this@SellerApplyActivity)
            }
            CategoryInfoManager.CATEGORY_TYPE.LEVEL2 -> {

                if (!categoryMap.containsKey(CategoryInfoManager.CATEGORY_TYPE.LEVEL1)) {
                    AlertBuilder.Builder().setContents(getString(R.string.msg_select_first_category)).setRightText(getString(R.string.word_confirm)).setDefaultMaxLine(1).builder().show(this)
                    return
                }

                val category = categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL1]

                CategoryInfoManager.getInstance().categoryListCall(CategoryInfoManager.CATEGORY_TYPE.LEVEL2, category!!.type, category.no, object : CategoryInfoManager.OnCategoryResultListener {

                    override fun onResult(Level: CategoryInfoManager.CATEGORY_TYPE, categories: List<Category>) {

                        if (categories.isEmpty()) {
                            AlertBuilder.Builder().setContents(getString(R.string.msg_select_other_category)).setRightText(getString(R.string.word_confirm)).setDefaultMaxLine(2).builder().show(this@SellerApplyActivity)
                            return
                        }

                        builder.setTitle(getString(R.string.word_second_category))
                        builder.setRightText(getString(R.string.word_select))
                        builder.setBackgroundClickable(false)
                        var rootIndex = 0


                        var messageData: AlertData.MessageData

                        var no: Long? = null

                        if (categoryMap.containsKey(CategoryInfoManager.CATEGORY_TYPE.LEVEL2)) {
                            if (categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] != null)
                                no = categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2]!!.no
                        }
//                        else if (mPage.category != null) {
//                            no = mPage.category!!.no
//                        }

                        for (i in categories.indices) {

                            if (no != null && no == categories[i].no) {
                                rootIndex = i + 1
                                categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] = categories[i]
                            }

                            messageData = AlertData.MessageData()
                            messageData.contents = categories[i].name
                            builder.addContents(messageData)
                        }

                        styleAlert.setValue(rootIndex)
                        builder.setStyle_alert(styleAlert)
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                if (event_alert == AlertBuilder.EVENT_ALERT.RADIO) {
                                    val index = event_alert.getValue() - 1
                                    text_seller_apply_category2.text = categories[index].name
                                    categoryMap[CategoryInfoManager.CATEGORY_TYPE.LEVEL2] = categories[index]
                                }
                            }
                        })

                        builder.builder().show(this@SellerApplyActivity)
                    }

                    override fun onFailed(message: String) {

                    }
                })
            }
        }

    }

    var mIsDirect = false

    private fun showEmailPopup() {

        val mailAddress = resources.getStringArray(R.array.mail_address)
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_select))
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
        builder.setContents(*mailAddress)
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {

                    AlertBuilder.EVENT_ALERT.LIST -> if (event_alert == AlertBuilder.EVENT_ALERT.LIST) {
                        val value = event_alert.getValue()
                        LogUtil.e(LOG_TAG, "value = {}", event_alert.getValue())

                        if (event_alert.getValue() == mailAddress.size) {
                            // 직접 입력
                            mIsDirect = true
                            text_seller_apply_mail_alpha.visibility = View.GONE
                            text_seller_apply_mail_domain.visibility = View.GONE
                            edit_seller_apply_mail_id.setHint(R.string.hint_input_email)
                        } else {
                            // 이메일 선택 완료
                            text_seller_apply_mail_alpha.visibility = View.VISIBLE
                            text_seller_apply_mail_domain.visibility = View.VISIBLE
                            edit_seller_apply_mail_id.setHint(R.string.hint_input_not_alpha)
                            text_seller_apply_mail_domain.text = mailAddress[event_alert.getValue() - 1]
                            mIsDirect = false
                        }
                    }
                }
            }
        }).builder().show(this)

    }

    var mPicMode = ""

    fun selectImage() {

        val pPlusPermission = PPlusPermission(this)
        pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE)
        pPlusPermission.setPermissionListener(object : PermissionListener {

            override fun onPermissionGranted() {

                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_biz_reg))
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
                builder.setContents(getString(R.string.word_camera), getString(R.string.word_gallery));
                builder.setLeftText(getString(R.string.word_cancel)).setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                        if (event_alert == AlertBuilder.EVENT_ALERT.LEFT) {
                            return
                        }
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.LIST -> {
                                when (event_alert.value) {
                                    1 -> {
                                        val pPlusPermission = PPlusPermission(this@SellerApplyActivity)
                                        pPlusPermission.addPermission(Permission.PERMISSION_KEY.CAMERA)
                                        pPlusPermission.setPermissionListener(object : PermissionListener {

                                            override fun onPermissionGranted() {
                                                val intent = Intent(this@SellerApplyActivity, PhotoTakerActivity::class.java)
                                                mPicMode = "camera"
                                                intent.putExtra("mode", "camera")
                                                startActivityForResult(intent, Const.REQ_PICK_FROM_FILE)
                                            }

                                            override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {

                                            }
                                        })
                                        pPlusPermission.checkPermission()

                                    }
                                    2 -> {
                                        val intent = Intent(this@SellerApplyActivity, PhotoTakerActivity::class.java)
                                        mPicMode = "picture"
                                        intent.putExtra("mode", "picture")
                                        startActivityForResult(intent, Const.REQ_PICK_FROM_FILE)
                                    }
                                }
                            }
                        }


                    }
                }).builder().show(this@SellerApplyActivity)
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {

            }
        })
        pPlusPermission.checkPermission()

    }

    private var defaultUpload: DefaultUpload? = null
    var licenseImage = ""
    private fun uploadFile(key: String, url: String) {

        val paramsAttachment = ParamsAttachment()
        paramsAttachment.targetType = AttachmentTargetTypeCode.goods
        paramsAttachment.setFile(url)

        if (defaultUpload == null) {
            showProgress("")
            defaultUpload = DefaultUpload(object : PplusUploadListener<Attachment> {

                override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {
                    hideProgress()
                    LogUtil.e(LOG_TAG, "tag = {}, no = {}, id = {}", tag, resultResponse.data.no, resultResponse.data.id)

                    licenseImage = resultResponse.data.url
                    if (mPicMode == "camera") {
                        PplusCommonUtil.deleteFromMediaScanner(url)
                    }
                }

                override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {
                    hideProgress()
                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString())

                }

            })
        }

        defaultUpload!!.request(key, paramsAttachment)

    }

    var zipCode = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_PICK_FROM_FILE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val dataUri = data.data
                    Glide.with(this).load(dataUri).into(image_seller_apply_biz_reg)
                    LogUtil.e(LOG_TAG, "path : {}", dataUri.path)
                    text_seller_apply_attach.visibility = View.GONE

                    uploadFile(dataUri.path, dataUri.path)
                }
            }
            Const.REQ_BANK_SELECT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val bankName = data.getStringExtra(Const.DATA)
                    text_seller_apply_bank_name.text = bankName
                }
            }
            Const.REQ_SEARCH -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val juso = data.getParcelableExtra<Juso>(Const.ADDRESS)
                    text_seller_apply_address.text = juso.roadAddr
                    zipCode = juso.zipNo
                }
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_apply_service), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }

    override fun onBackPressed() {

        if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.person.name) {
            super.onBackPressed()
        } else {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_question_finish_sale_apply), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            for (activity in PRNumberBizApplication.getActivityList()) {
                                activity.finish()
                            }

                            if (!isFinishing) {
                                finish()
                            }
                        }
                    }
                }
            }).builder().show(this)
        }

    }
}
