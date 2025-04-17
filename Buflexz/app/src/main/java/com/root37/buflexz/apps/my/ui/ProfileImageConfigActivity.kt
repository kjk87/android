package com.root37.buflexz.apps.my.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.gallery.ui.OneGalleryActivity
import com.root37.buflexz.apps.my.data.MemberProfileImageAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.MemberProfileImage
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.network.upload.PplusUploadListener
import com.root37.buflexz.core.network.upload.S3Upload
import com.root37.buflexz.databinding.ActivityProfileConfigBinding
import com.root37.buflexz.databinding.ActivityProfileImageConfigBinding
import retrofit2.Call
import java.io.File

class ProfileImageConfigActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProfileImageConfigBinding

    override fun getLayoutView(): View {
        binding = ActivityProfileImageConfigBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mProfile: String? = null
    private var mAdapter:MemberProfileImageAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        val member = LoginInfoManager.getInstance().member!!
        mProfile = member.profile
        Glide.with(this).load(mProfile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_image_default).error(R.drawable.ic_profile_image_default)).into(binding.imageProfileImageConfigProfile)

        binding.recyclerProfileImageConfig.layoutManager = GridLayoutManager(this, 4)
        binding.recyclerProfileImageConfig.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.width_44))
        mAdapter = MemberProfileImageAdapter()
        binding.recyclerProfileImageConfig.adapter = mAdapter

        mAdapter!!.listener = object : MemberProfileImageAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                if(position == 0){
                    mProfile = ""
                    binding.imageProfileImageConfigProfile.setImageResource(R.drawable.ic_profile_image_default)
                }else{
                    mProfile = mAdapter!!.getItem(position).image
                    Glide.with(this@ProfileImageConfigActivity).load(mProfile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_image_default).error(R.drawable.ic_profile_image_default)).into(binding.imageProfileImageConfigProfile)
                }
            }
        }

        binding.textProfileImageConfigGallery.setOnClickListener {
            val intent = Intent(this, OneGalleryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            galleryLauncher.launch(intent)
        }

        binding.textProfileImageConfigComplete.setOnClickListener {
            val params = HashMap<String, String>()

            if(mProfile == LoginInfoManager.getInstance().member!!.profile){
                setResult(RESULT_OK)
                finish()

            }else{
                if(mProfile == null){
                    mProfile = ""
                }
                params["profile"] = mProfile!!
                showProgress("")
                ApiBuilder.create().updateProfile(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                        hideProgress()
                        LoginInfoManager.getInstance().member!!.profile = mProfile
                        LoginInfoManager.getInstance().save()
                        showAlert(R.string.msg_complete_profile_image)
                        setResult(RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                        hideProgress()
                    }
                }).build().call()
            }

        }

        getProfileImage()
    }

    private fun getProfileImage(){
        showProgress("")
        ApiBuilder.create().getMemberProfileImageList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<MemberProfileImage>>>{
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<MemberProfileImage>>>?, response: NewResultResponse<ListResultResponse<MemberProfileImage>>?) {
                hideProgress()
                mAdapter!!.clear()
                if(response?.result != null && response.result!!.list != null){
                    mAdapter!!.add(MemberProfileImage())
                    mAdapter!!.addAll(response.result!!.list!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<MemberProfileImage>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<MemberProfileImage>>?) {

            }
        }).build().call()
    }

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null) {
                val selectImage = data.data
                uploadFile(selectImage!!.path!!, "profile")
            }
        }
    }

    private var s3Upload: S3Upload? = null
    private fun uploadFile(url: String, type: String) {
        if (s3Upload == null) {
            s3Upload = S3Upload(object : PplusUploadListener<String> {

                override fun onResult(tag: String, response: NewResultResponse<String>) {
                    LogUtil.e(LOG_TAG, "tag = {}, url = {}", tag, response.result)
                    hideProgress()
                    if (response.result != null) {
                        mProfile = response.result
                        Glide.with(this@ProfileImageConfigActivity).load(mProfile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_config_default).error(R.drawable.ic_profile_config_default)).into(binding.imageProfileImageConfigProfile)
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
        params["targetType"] = type
        params["file"] = File(url)
        s3Upload!!.request("profile", params)
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)

            if (position % 4 != 3) {
                outRect.set(0, 0, mItemOffset, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_profile_image_setting), ToolbarOption.ToolbarMenu.LEFT)
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