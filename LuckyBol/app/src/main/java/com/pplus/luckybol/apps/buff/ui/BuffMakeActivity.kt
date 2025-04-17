package com.pplus.luckybol.apps.buff.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.PPlusPermission
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.PhotoTakerActivity
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.custom.AttachmentTargetTypeCode
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Attachment
import com.pplus.luckybol.core.network.model.dto.Buff
import com.pplus.luckybol.core.network.model.request.params.ParamsAttachment
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.upload.PplusUploadListener
import com.pplus.luckybol.core.network.upload.S3Upload
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityBuffMakeBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.permission.Permission
import com.pplus.utils.part.apps.permission.PermissionListener
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class BuffMakeActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityBuffMakeBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffMakeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var s3Upload: S3Upload? = null
    private var mBackgroundUrl: String? = null
    private var mBuff:Buff? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mBuff = intent.getParcelableExtra(Const.DATA)
        binding.textBuffMakeBackgroundDesc.setOnClickListener {
            val pPlusPermission = PPlusPermission(this)
            pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE)
            pPlusPermission.setPermissionListener(object : PermissionListener {

                override fun onPermissionGranted() {

                    val intent = Intent(this@BuffMakeActivity, PhotoTakerActivity::class.java)
                    intent.putExtra("mode", "picture")
                    intent.putExtra("crop", true)
                    backgroundImageLauncher.launch(intent)
                }

                override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {

                }
            })
            pPlusPermission.checkPermission()
        }

        binding.imageBuffMakeImageDelete.setOnClickListener {
            mBackgroundUrl = ""
            binding.textBuffMakeBackgroundDesc.visibility = View.VISIBLE
            binding.imageBuffMakeImageDelete.visibility = View.GONE
            binding.imageBuffMakeBackground.setImageResource(0)
        }

        binding.textBuffMakeGroupMaxCount.setOnClickListener {
            binding.textBuffMakeGroupMaxCount.isSelected = true
            binding.textBuffMakeGroupSettingCount.isSelected = false
            binding.editBuffMakeGroupCount.visibility = View.GONE
            checkEnabled()
        }

        binding.textBuffMakeGroupSettingCount.setOnClickListener {
            binding.textBuffMakeGroupMaxCount.isSelected = false
            binding.textBuffMakeGroupSettingCount.isSelected = true
            binding.editBuffMakeGroupCount.visibility = View.VISIBLE
            checkEnabled()
        }

        binding.textBuffMakeConfirm.setOnClickListener {

            val buff = Buff()

            if (!StringUtils.isEmpty(mBackgroundUrl)) {
                buff.image = mBackgroundUrl
            }

            val title = binding.editBuffMakeTitle.text.toString().trim()

            if (StringUtils.isEmpty(title)) {
                showAlert(R.string.msg_input_group_title)
                return@setOnClickListener
            }

            if (title.length < 2) {
                showAlert(R.string.msg_input_group_title_over2)
                return@setOnClickListener
            }

            buff.title = title

            val catchphrase = binding.editBuffMakeCatchphrase.text.toString().trim()
            if (StringUtils.isEmpty(catchphrase)) {
                showAlert(R.string.hint_input_catchphrase)
                return@setOnClickListener
            }

            buff.info = catchphrase

            if (binding.textBuffMakeGroupMaxCount.isSelected) {
                buff.capacity = 50
            } else {
                val count = binding.editBuffMakeGroupCount.text.toString().trim()
                if (StringUtils.isEmpty(count) || count.toInt() < 0) {
                    showAlert(R.string.hint_input_group_count)
                    return@setOnClickListener
                }
                if (count.toInt() > 50) {
                    showAlert(R.string.msg_buff_group_max_count_50)
                    return@setOnClickListener
                }
                buff.capacity = count.toInt()
            }

            if(mBuff != null){
                buff.seqNo = mBuff!!.seqNo
            }

            showProgress("")
            ApiBuilder.create().buffMake(buff).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                        response: NewResultResponse<Any>?) {
                    hideProgress()
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<Any>?) {
                    hideProgress()
                }
            }).build().call()

        }

        binding.textBuffMakeGroupMaxCount.isSelected = true
        binding.textBuffMakeGroupSettingCount.isSelected = false
        binding.editBuffMakeGroupCount.visibility = View.GONE

        binding.editBuffMakeTitle.addTextChangedListener {
            checkEnabled()
        }

        binding.editBuffMakeCatchphrase.addTextChangedListener {
            checkEnabled()
        }

        binding.editBuffMakeGroupCount.addTextChangedListener {
            checkEnabled()
        }

        if(mBuff != null){
            setTitle(getString(R.string.msg_modify_buff))
            mBackgroundUrl = mBuff!!.image
            if(StringUtils.isNotEmpty(mBackgroundUrl)){
                binding.textBuffMakeBackgroundDesc.visibility = View.GONE
                binding.imageBuffMakeImageDelete.visibility = View.VISIBLE
                Glide.with(this@BuffMakeActivity).load(mBackgroundUrl).apply(RequestOptions().centerCrop()).into(binding.imageBuffMakeBackground)
            }else{
                binding.textBuffMakeBackgroundDesc.visibility = View.VISIBLE
                binding.imageBuffMakeImageDelete.visibility = View.GONE
            }

            binding.editBuffMakeTitle.setText(mBuff!!.title)
            binding.editBuffMakeCatchphrase.setText(mBuff!!.info)
            if(mBuff!!.capacity == 50){
                binding.textBuffMakeGroupMaxCount.isSelected = true
                binding.textBuffMakeGroupSettingCount.isSelected = false
                binding.editBuffMakeGroupCount.visibility = View.GONE
            }else{
                binding.textBuffMakeGroupMaxCount.isSelected = false
                binding.textBuffMakeGroupSettingCount.isSelected = true
                binding.editBuffMakeGroupCount.visibility = View.VISIBLE
                binding.editBuffMakeGroupCount.setText(mBuff!!.capacity.toString())
            }
        }

        checkEnabled()
    }

    private fun checkEnabled() {

        var enabled = true
        val title = binding.editBuffMakeTitle.text.toString().trim()

        if (StringUtils.isEmpty(title) || title.length < 2) {
            enabled = false
        }

        if (binding.textBuffMakeGroupSettingCount.isSelected) {
            val count = binding.editBuffMakeGroupCount.text.toString().trim()
            if (StringUtils.isEmpty(count) || count.toInt() < 0) {
                enabled = false
            }else if (count.toInt() > 50) {
                enabled = false
            }


        }

        if (enabled) {
            binding.textBuffMakeConfirm.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_fc5c57))
        } else {
            binding.textBuffMakeConfirm.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_c0c6cc))
        }
        binding.textBuffMakeConfirm.isEnabled = enabled
    }

    val backgroundImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val selectImage = data.data
                LogUtil.e(LOG_TAG, "file : {}", selectImage!!.path)
                uploadFile(selectImage.path!!)

            }
        }
    }

    private fun uploadFile(url: String) {

        val paramsAttachment = ParamsAttachment()
        paramsAttachment.targetType = AttachmentTargetTypeCode.postList
        paramsAttachment.setFile(url)

        if (s3Upload == null) {
            s3Upload = S3Upload(object : PplusUploadListener<Attachment> {

                override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {
                    LogUtil.e(LOG_TAG, "tag = {}, url = {}, id = {}", tag, resultResponse.data!!.url, resultResponse.data!!.id)
                    mBackgroundUrl = resultResponse.data!!.url
                    if(StringUtils.isNotEmpty(mBackgroundUrl)){
                        binding.textBuffMakeBackgroundDesc.visibility = View.GONE
                        binding.imageBuffMakeImageDelete.visibility = View.VISIBLE
                        Glide.with(this@BuffMakeActivity).load(mBackgroundUrl).apply(RequestOptions().centerCrop()).into(binding.imageBuffMakeBackground)
                    }else{
                        binding.textBuffMakeBackgroundDesc.visibility = View.VISIBLE
                        binding.imageBuffMakeImageDelete.visibility = View.GONE
                    }
                    hideProgress()
                    PplusCommonUtil.deleteFromMediaScanner(url)
                }

                override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {

                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString())
                }

            })
        }
        showProgress("")
        s3Upload!!.request("background", paramsAttachment)
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_make_buff), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}