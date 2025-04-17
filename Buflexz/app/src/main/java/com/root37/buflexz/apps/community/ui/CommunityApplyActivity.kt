package com.root37.buflexz.apps.community.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.CommunityApply
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.network.upload.PplusUploadListener
import com.root37.buflexz.core.network.upload.S3Upload
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityCommunityApplyBinding
import retrofit2.Call
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date


class CommunityApplyActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityCommunityApplyBinding

    override fun getLayoutView(): View {
        binding = ActivityCommunityApplyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mCommunityApply: CommunityApply? = null
    private var mImage: String? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.layoutCommunityApplyMoveTelegram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/BuffCoin_Official_chat")))
        }

        binding.imageCommunityApplyAuthImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            telegramPickLauncher.launch(intent)

        }

        binding.textCommunityApply.setOnClickListener {
            if (mCommunityApply == null) {
                mCommunityApply = CommunityApply()
                mCommunityApply!!.status = "pending"
            } else {
                if (mCommunityApply!!.status == "return") {
                    mCommunityApply!!.status = "redemand"
                }
            }

            mCommunityApply!!.userKey = LoginInfoManager.getInstance().member!!.userKey

            if (StringUtils.isEmpty(mImage)) {
                showAlert(R.string.msg_attach_auth_image)
                return@setOnClickListener
            }
            mCommunityApply!!.image = mImage

            showProgress("")
            ApiBuilder.create().postCommunityApply(mCommunityApply!!).setCallback(object : PplusCallback<NewResultResponse<CommunityApply>> {
                override fun onResponse(call: Call<NewResultResponse<CommunityApply>>?, response: NewResultResponse<CommunityApply>?) {
                    hideProgress()
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.msg_alert_auth_telegram_title))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_auth_telegram_desc), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.setRightText(getString(R.string.word_confirm))
                    builder.builder().show(this@CommunityApplyActivity)
                    get()
                }

                override fun onFailure(call: Call<NewResultResponse<CommunityApply>>?, t: Throwable?, response: NewResultResponse<CommunityApply>?) {

                }
            }).build().call()
        }

        get()
    }

    private fun get() {
        showProgress("")
        ApiBuilder.create().getCommunityApply().setCallback(object : PplusCallback<NewResultResponse<CommunityApply>> {
            override fun onResponse(call: Call<NewResultResponse<CommunityApply>>?, response: NewResultResponse<CommunityApply>?) {
                if (response?.result != null) {
                    mCommunityApply = response.result
                    mImage = mCommunityApply!!.image
                    Glide.with(this@CommunityApplyActivity).load(mImage).apply(RequestOptions().centerCrop()).into(binding.imageCommunityApplyAuthImage)
                    binding.textCommunityApplyAuthImageDesc.visibility = View.GONE

                    when(mCommunityApply!!.status){
                        "normal"->{
                            binding.textCommunityApply.visibility = View.GONE
                            binding.layoutCommunityApplyStatus.visibility = View.VISIBLE
                            binding.layoutCommunityApplyPending.visibility = View.GONE
                            binding.layoutCommunityApplyReturn.visibility = View.GONE
                            binding.layoutCommunityApplyNormal.visibility = View.VISIBLE
                        }
                        "pending", "redemand"->{
                            binding.textCommunityApply.visibility = View.GONE
                            binding.layoutCommunityApplyStatus.visibility = View.VISIBLE
                            binding.layoutCommunityApplyPending.visibility = View.VISIBLE
                            binding.layoutCommunityApplyReturn.visibility = View.GONE
                            binding.layoutCommunityApplyNormal.visibility = View.GONE
                        }
                        "return"->{
                            binding.textCommunityApply.visibility = View.VISIBLE
                            binding.layoutCommunityApplyStatus.visibility = View.VISIBLE
                            binding.layoutCommunityApplyPending.visibility = View.GONE
                            binding.layoutCommunityApplyReturn.visibility = View.VISIBLE
                            binding.layoutCommunityApplyNormal.visibility = View.GONE
                            binding.textCommunityApplyReturnReason.setOnClickListener {
                                val intent = Intent(this@CommunityApplyActivity, AlertCommunityApplyReturnReasonActivity::class.java)
                                intent.putExtra(Const.DATA, mCommunityApply)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
                        }
                    }

                } else {
                    binding.textCommunityApplyAuthImageDesc.visibility = View.VISIBLE
                    binding.textCommunityApply.visibility = View.VISIBLE
                    binding.layoutCommunityApplyStatus.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<CommunityApply>>?, t: Throwable?, response: NewResultResponse<CommunityApply>?) {
                hideProgress()
            }
        }).build().call()
    }

    private val telegramPickLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val dataUri = result.data!!.data
            if (dataUri != null) {
                LogUtil.e(LOG_TAG, dataUri.path)

                val extensions = contentResolver.getType(dataUri)!!.split("/".toRegex()).toTypedArray()
                val extension = extensions[extensions.size - 1]
                val names = dataUri.path!!.split("/".toRegex()).toTypedArray()
                val fileName = names[names.size - 1]
                LogUtil.e(LOG_TAG, fileName)
                val albumF = PplusCommonUtil.getAlbumDir()
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = File(albumF, "temp_" + timeStamp + "_" + fileName + "." + extension)

                try {
                    val bitmap: Bitmap
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, dataUri))
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, dataUri)
                    }
                    if (bitmap != null) {
                        val bytes = ByteArrayOutputStream()
                        if (extension == "png") {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
                        } else {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                        }

                        val bitmapData = bytes.toByteArray() //write the bytes in file //write the bytes in file
                        val fos = FileOutputStream(file)
                        fos.write(bitmapData)
                        fos.flush()
                        fos.close()
                        val outputFileUri = Uri.fromFile(file)

                        uploadFile(outputFileUri.path!!)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private var s3Upload: S3Upload? = null
    private fun uploadFile(url: String) {
        if (s3Upload == null) {
            s3Upload = S3Upload(object : PplusUploadListener<String> {

                override fun onResult(tag: String, response: NewResultResponse<String>) {
                    LogUtil.e(LOG_TAG, "tag = {}, url = {}", tag, response.result)
                    hideProgress()
                    if (response.result != null) {

                        mImage = response.result
                        Glide.with(this@CommunityApplyActivity).load(mImage).apply(RequestOptions().centerInside()).into(binding.imageCommunityApplyAuthImage)
                        binding.textCommunityApplyAuthImageDesc.visibility = View.GONE
                    }
                }

                override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {
                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString())
                    hideProgress()
                }

            })
        }
        showProgress("")

        val params = HashMap<String, Any>()
        params["file"] = File(url)
        s3Upload!!.request("telegram", params)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_auth_telegram), ToolbarOption.ToolbarMenu.LEFT)
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