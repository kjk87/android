package com.root37.buflexz.apps.inquire.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.gallery.ui.GalleryOnlyActivity
import com.root37.buflexz.apps.inquire.data.InquireImageAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Inquire
import com.root37.buflexz.core.network.model.dto.InquireImage
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.network.upload.PplusUploadListener
import com.root37.buflexz.core.network.upload.S3Upload
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityInquireBinding
import retrofit2.Call
import java.io.File
import java.util.Calendar
import java.util.Date
import java.util.concurrent.ConcurrentHashMap

class InquireActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityInquireBinding

    override fun getLayoutView(): View {
        binding = ActivityInquireBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mType = ""
    lateinit var mImageAdapter: InquireImageAdapter

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.textInquireTitleTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_inquire_title_title))
        binding.textInquireContentsTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_inquire_contents_title))
        binding.editInquireTitle.setSingleLine()

        binding.textInquireType.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_alert_date_title))
            builder.setContents(getString(R.string.word_general), getString(R.string.word_partnership), getString(R.string.word_error), getString(R.string.word_etc))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.value) {
                        1 -> {
                            mType = "general"
                            binding.textInquireType.text = getString(R.string.word_general)
                        }
                        2 -> {
                            mType = "partnership"
                        }
                        3 -> {
                            mType = "error"
                        }
                        4 -> {
                            mType = "etc"
                        }
                    }
                }
            }).builder().show(this)
        }

        binding.recyclerInquireAttach.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mImageAdapter = InquireImageAdapter()
        binding.recyclerInquireAttach.adapter = mImageAdapter

        binding.imageInquireAttach.setOnClickListener {
            goGallery()
        }

        binding.textInquireComplete.setOnClickListener {
            if (StringUtils.isEmpty(mType)) {
                showAlert(R.string.msg_select_inquire_type)
                return@setOnClickListener
            }

            val title = binding.editInquireTitle.text.toString().trim()
            if (StringUtils.isEmpty(title)) {
                showAlert(R.string.msg_input_title)
                return@setOnClickListener
            }

            val contents = binding.editInquireContents.text.toString().trim()
            if (StringUtils.isEmpty(contents)) {
                showAlert(R.string.msg_input_inquire_contents)
                return@setOnClickListener
            }

            if(contents.length < 10){
                showAlert(R.string.msg_input_inquire_contents_over10)
                return@setOnClickListener
            }

            val builder = AlertBuilder.Builder()
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_inquire), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.msg_inquiry))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val params = Inquire()
                            params.type = mType
                            params.title = title
                            params.contents = contents

                            if (mImageAdapter.itemCount > 0) {
                                params.imageList = mImageAdapter.mDataList
                            }

                            showProgress("")
                            ApiBuilder.create().postInquire(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    showAlert(R.string.msg_complete_inquire)
                                    setResult(RESULT_OK)
                                    finish()
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                }
                            }).build().call()
                        }
                        else -> {

                        }
                    }
                }
            }).builder().show(this)
        }

    }

    fun goGallery() {

        if (mImageAdapter.itemCount < 10) {
            val intent = Intent(this, GalleryOnlyActivity::class.java)
            intent.putExtra(Const.COUNT, 10 - mImageAdapter.itemCount)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            galleryLauncher.launch(intent)
        } else {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_max_image_count), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                }
            }).builder().show(this)
        }
    }

    private lateinit var mUploadDataMap: ConcurrentHashMap<String, String>
    private lateinit var mUploadUrlMap: ConcurrentHashMap<String, String>

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                val dataList = PplusCommonUtil.getParcelableArrayListExtra(data, Const.IMAGE_LIST, Uri::class.java)!!

                mUploadDataMap = ConcurrentHashMap()
                mUploadUrlMap = ConcurrentHashMap()

                val index = mImageAdapter.itemCount
                for (i in 0 until dataList.size) {
                    mImageAdapter.add(InquireImage(null, null, "", null))
                    mUploadDataMap[(index + i).toString()] = "upload"
                    mUploadUrlMap[(index + i).toString()] = dataList[i].path!!
                }

                showProgress("")
                for (i in 0 until dataList.size) {
                    uploadFile((index + i).toString(), dataList[i].path!!)
                }

            }
        }
    }

    private var s3Upload: S3Upload? = null
    private fun uploadFile(key: String, url: String) {
        if (s3Upload == null) {
            s3Upload = S3Upload(object : PplusUploadListener<String> {

                override fun onResult(tag: String, response: NewResultResponse<String>) {
                    LogUtil.e(LOG_TAG, "tag = {}, url = {}", tag, response.result)
                    if (response.result != null) {
                        mImageAdapter.insertAttach(tag, InquireImage(null, null, response.result, null))
                        mUploadDataMap[tag] = "success"
                        if (checkFinish()) {
                            hideProgress()
                            delete(url)
                        }
                    }
                }

                override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {
                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString())
                    mUploadDataMap[tag] = "error"
                }

            })
        }

        val params = HashMap<String, Any>()
        params["file"] = File(url)
        s3Upload!!.request(key, params)
    }

    fun checkFinish(): Boolean {
        val iterator = mUploadDataMap.keys.iterator()
        var isFinish = true
        var isError = false
        while (iterator.hasNext()) {
            val key = iterator.next()
            when (mUploadDataMap[key]) {
                "upload" -> {
                    isFinish = false
                }

                "error" -> {
                    isError = true
                }

                else -> {}
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

    fun delete(url: String) {
        val iterator = mUploadUrlMap.keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            PplusCommonUtil.deleteFromMediaScanner(url)
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_inquiry), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}