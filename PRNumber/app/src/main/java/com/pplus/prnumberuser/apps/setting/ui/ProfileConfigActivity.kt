package com.pplus.prnumberuser.apps.setting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.igaworks.v2.core.AdBrixRm
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.main.ui.OneGalleryActivity
import com.pplus.prnumberuser.apps.my.ui.MemberAddressSaveActivity
import com.pplus.prnumberuser.apps.my.ui.MyFavoriteActivity
import com.pplus.prnumberuser.apps.my.ui.SelectActiveAreaActivity
import com.pplus.prnumberuser.core.code.custom.AttachmentTargetTypeCode
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.request.params.ParamsAttachment
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.upload.DefaultUpload
import com.pplus.prnumberuser.core.network.upload.PplusUploadListener
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityProfileConfigBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class ProfileConfigActivity : BaseActivity(), ImplToolbar {

    private var mIsCheckNickName = false
    private var mIsEditing: Boolean = false
    private var mNickName: String? = null
    private var mUser: User? = null
    private var mGenderType: String? = null
    private var mIsSignUp: Boolean? = false

    private var defaultUpload: DefaultUpload? = null

    override fun getPID(): String {
        return "Main_mypage_profile"
    }
    private lateinit var binding: ActivityProfileConfigBinding

    override fun getLayoutView(): View {
        binding = ActivityProfileConfigBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        mIsSignUp = intent.getBooleanExtra(Const.SIGN_UP, false)

        if (mIsSignUp!!) {

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Sign_profile_information")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)

            val eventAttr = AdBrixRm.AttrModel()
            eventAttr.setAttrs("screen_name", "Sign_profile_information")
            AdBrixRm.event("screen_view", eventAttr)

            val logger = AppEventsLogger.newLogger(this)
            logger.logEvent("screen_view", bundle)
        } else {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Main_mypage_profile")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)

            val eventAttr = AdBrixRm.AttrModel()
            eventAttr.setAttrs("screen_name", "Main_mypage_profile")
            AdBrixRm.event("screen_view", eventAttr)

            val logger = AppEventsLogger.newLogger(this)
            logger.logEvent("screen_name", bundle)
        }

        mIsEditing = false
        binding.editProfileConfigNickname.setSingleLine()
        mUser = LoginInfoManager.getInstance().user

        if (mUser!!.profileImage != null && StringUtils.isNotEmpty(mUser!!.profileImage!!.url)) {
            Glide.with(this).load(mUser!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(binding.imageProfileConfig)
        }

        if (StringUtils.isNotEmpty(mUser!!.nickname)) {
            binding.editProfileConfigNickname.setText(mUser!!.nickname)
        }

        binding.textProfileConfigNicknameDoubleCheck.setOnClickListener {
            val nickname = binding.editProfileConfigNickname.text.toString().trim()
            if (StringUtils.isEmpty(nickname)) {
                showAlert(R.string.msg_input_nickName)
                return@setOnClickListener
            }

            if (nickname.length < 2) {
                showAlert(getString(R.string.msg_input_nickname_over2))
                return@setOnClickListener
            }

            mIsEditing = true
            val params = HashMap<String, String>()
            params["nickname"] = nickname
            params["appType"] = Const.APP_TYPE
            showProgress("")
            ApiBuilder.create().existsUser(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                    hideProgress()
                    mIsCheckNickName = false
                    showAlert(getString(R.string.msg_duplicate_nickname))
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                    hideProgress()
                    mIsCheckNickName = true
                    showAlert(getString(R.string.msg_enable_use))
                    mNickName = binding.editProfileConfigNickname.text.toString().trim()
                }
            }).build().call()
        }

//        text_profile_config_married.setOnClickListener {
//            select(text_profile_config_married, text_profile_config_not_married)
//            mIsMarried = true
//            mIsEditing = true
//        }
//        text_profile_config_not_married.setOnClickListener {
//            select(text_profile_config_not_married, text_profile_config_married)
//            mIsMarried = false
//            mIsEditing = true
//        }


//        text_exist_child = findViewById(R.id.text_profile_config_exist_child)
//        text_not_exist_child = findViewById(R.id.text_profile_config_not_exist_child)
//        text_exist_child!!.setOnClickListener(this)
//        text_not_exist_child!!.setOnClickListener(this)


//        if (mUser!!.married != null) {
//            mIsMarried = mUser!!.married
//            if (mUser!!.married!!) {
//                select(text_profile_config_married, text_profile_config_not_married)
//            } else {
//                select(text_profile_config_not_married, text_profile_config_married)
//            }
//        }

//        if (mUser!!.hasChild != null) {
//            mHasChild = mUser!!.hasChild
//            if (mUser!!.hasChild!!) {
//                select(text_exist_child!!, text_not_exist_child!!)
//            } else {
//                select(text_not_exist_child!!, text_exist_child!!)
//            }
//        }

        binding.layoutProfileConfigImage.setOnClickListener {
            goGallery()
        }


        binding.layoutProfileConfigMyFavorite.setOnClickListener {
            val intent = Intent(this, MyFavoriteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            myFavoriteLauncher.launch(intent)
        }

        if (mUser!!.activeArea1Value == null || StringUtils.isEmpty(mUser!!.activeArea1Name)) {
            binding.imageProfileConfigActiveArea1NotExist.visibility = View.VISIBLE
            binding.textProfileConfigActiveArea1.visibility = View.GONE
        } else {
            binding.imageProfileConfigActiveArea1NotExist.visibility = View.GONE
            binding.textProfileConfigActiveArea1.visibility = View.VISIBLE
            binding.textProfileConfigActiveArea1.text = mUser!!.activeArea1Name
        }

        if (mUser!!.activeArea2Value == null || StringUtils.isEmpty(mUser!!.activeArea2Name)) {
            binding.imageProfileConfigActiveArea2NotExist.visibility = View.VISIBLE
            binding.textProfileConfigActiveArea2.visibility = View.GONE
        } else {
            binding.imageProfileConfigActiveArea2NotExist.visibility = View.GONE
            binding.textProfileConfigActiveArea2.visibility = View.VISIBLE
            binding.textProfileConfigActiveArea2.text = mUser!!.activeArea2Name
        }

        binding.layoutProfileConfigActiveArea1.setOnClickListener {
            val intent = Intent(this, SelectActiveAreaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            area1Launcher.launch(intent)
        }

        binding.layoutProfileConfigActiveArea2.setOnClickListener {
            val intent = Intent(this, SelectActiveAreaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            area2Launcher.launch(intent)
        }

        binding.textProfileConfigConfirm.setOnClickListener {

            val nickname = binding.editProfileConfigNickname.text.toString().trim()
            if (mIsCheckNickName) {
                if (nickname != mNickName) {
                    showAlert(R.string.msg_check_duplication_nickName)
                    return@setOnClickListener
                }
                if (mIsCheckNickName) {
                    mUser!!.nickname = mNickName
                }

                updateNickname(mNickName!!)
            } else {
                if (LoginInfoManager.getInstance().user.nickname != nickname) {
                    showAlert(R.string.msg_check_duplication_nickName)
                    return@setOnClickListener
                }
            }

        }

//        getMyFavoriteCategoryList()
        getMemberAddress()
    }

    private fun getMemberAddress(){
        showProgress("")
        ApiBuilder.create().memberAddress.setCallback(object : PplusCallback<NewResultResponse<MemberAddress>> {
            override fun onResponse(call: Call<NewResultResponse<MemberAddress>>?,
                                    response: NewResultResponse<MemberAddress>?) {
                hideProgress()

                setMemberAddress(response?.data)
            }

            override fun onFailure(call: Call<NewResultResponse<MemberAddress>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<MemberAddress>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setMemberAddress(memberAddress: MemberAddress?){
        if(memberAddress == null){
            binding.textProfileConfigNotExistaddress.visibility = View.VISIBLE
            binding.layoutProfileConfigExistAddress.visibility = View.GONE
            binding.layoutProfileConfigAddress.setOnClickListener {
                val intent = Intent(this, MemberAddressSaveActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                memberAddressLauncher.launch(intent)
            }
        }else{
            binding.textProfileConfigNotExistaddress.visibility = View.GONE
            binding.layoutProfileConfigExistAddress.visibility = View.VISIBLE
            binding.layoutProfileConfigAddress.setOnClickListener {

            }
            binding.textProfileConfigAddressModify.setOnClickListener {
                val intent = Intent(this, MemberAddressSaveActivity::class.java)
                intent.putExtra(Const.DATA, memberAddress)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                memberAddressLauncher.launch(intent)
            }

            binding.textProfileConfigAddressName.text = memberAddress.name
            binding.textProfileConfigAddress.text = memberAddress.address +"\n"+memberAddress.addressDetail
        }
    }

    private fun select(view1: View, view2: View) {

        view1.isSelected = true
        view2.isSelected = false
    }

    private fun goGallery() {

        val intent = Intent(this, OneGalleryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        galleryLauncher.launch(intent)
    }

    private fun getMyFavoriteCategoryList() {

        showProgress("")
        ApiBuilder.create().myFavoriteCategoryList.setCallback(object : PplusCallback<NewResultResponse<CategoryFavorite>> {
            override fun onResponse(call: Call<NewResultResponse<CategoryFavorite>>?, response: NewResultResponse<CategoryFavorite>?) {
                hideProgress()
                if (response?.datas != null) {
                    if (response.datas.isEmpty()) {
                        binding.layoutProfileConfigMyFavoriteNotExist.visibility = View.VISIBLE
                        binding.textProfileConfigMyFavorite.visibility = View.GONE
                    } else {
                        binding.layoutProfileConfigMyFavoriteNotExist.visibility = View.GONE
                        binding.textProfileConfigMyFavorite.visibility = View.VISIBLE
                        binding.textProfileConfigMyFavorite.text = PplusCommonUtil.fromHtml(getString(R.string.html_my_favorite_tag_title, response.datas.size.toString()))
                    }
                } else {
                    binding.layoutProfileConfigMyFavoriteNotExist.visibility = View.VISIBLE
                    binding.textProfileConfigMyFavorite.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<NewResultResponse<CategoryFavorite>>?, t: Throwable?, response: NewResultResponse<CategoryFavorite>?) {
                hideProgress()
            }
        }).build().call()
    }

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val selectImage = data.data
                apiProfileUpload(selectImage!!.path)
            }
        }
    }

    val memberAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getMemberAddress()
    }

    val area1Launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val juso = data.getParcelableExtra<Juso>(Const.DATA)
                updateActiveArea1(juso!!.value!!, juso.name!!)
            }
        }
    }

    val area2Launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val juso = data.getParcelableExtra<Juso>(Const.DATA)
                updateActiveArea2(juso!!.value!!, juso.name!!)
            }
        }
    }

    val myFavoriteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getMyFavoriteCategoryList()
    }

    private fun updateNickname(nickname: String) {

        val params = java.util.HashMap<String, String>()
        params["nickname"] = nickname
        showProgress("")
        ApiBuilder.create().updateNickname(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                hideProgress()
                LoginInfoManager.getInstance().user.nickname = nickname
                LoginInfoManager.getInstance().save()
                showAlert(R.string.msg_edit_complete)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun updateActiveArea1(activeArea1Value: String, activeArea1Name: String) {
        val params = HashMap<String, String>()
        params["activeArea1Value"] = activeArea1Value
        params["activeArea1Name"] = activeArea1Name
        showProgress("")
        ApiBuilder.create().updateActiveArea1(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                binding.imageProfileConfigActiveArea1NotExist.visibility = View.GONE
                binding.textProfileConfigActiveArea1.visibility = View.VISIBLE
                binding.textProfileConfigActiveArea1.text = activeArea1Name
                LoginInfoManager.getInstance().user.activeArea1Value = activeArea1Value
                LoginInfoManager.getInstance().user.activeArea1Name = activeArea1Name
                LoginInfoManager.getInstance().save()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {

            }
        }).build().call()
    }

    private fun updateActiveArea2(activeArea2Value: String, activeArea2Name: String) {
        val params = HashMap<String, String>()
        params["activeArea2Value"] = activeArea2Value
        params["activeArea2Name"] = activeArea2Name
        showProgress("")
        ApiBuilder.create().updateActiveArea2(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                binding.imageProfileConfigActiveArea2NotExist.visibility = View.GONE
                binding.textProfileConfigActiveArea2.visibility = View.VISIBLE
                binding.textProfileConfigActiveArea2.text = activeArea2Name
                LoginInfoManager.getInstance().user.activeArea2Value = activeArea2Value
                LoginInfoManager.getInstance().user.activeArea2Name = activeArea2Name
                LoginInfoManager.getInstance().save()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {

            }
        }).build().call()
    }

    fun apiProfileUpload(filepath: String?) {

        val attachment = ParamsAttachment()
        attachment.targetType = AttachmentTargetTypeCode.memberProfile
        attachment.setFile(filepath)
        if (defaultUpload == null) {
            defaultUpload = DefaultUpload(object : PplusUploadListener<Attachment> {

                override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {

                    hideProgress()
                    val url = resultResponse.data.url
                    val no = resultResponse.data.no

                    LoginInfoManager.getInstance().user.profileImage = ImgUrl(no, url)
                    LoginInfoManager.getInstance().save()
                    val params = HashMap<String, String>()
                    params["no"] = no.toString()
                    ApiBuilder.create().updateProfileImage(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                        override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                        }

                        override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                        }
                    }).build().call()

                    Glide.with(this@ProfileConfigActivity).load(url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_bizmain_profile_default).error(R.drawable.img_bizmain_profile_default)).into(binding.imageProfileConfig)
                    PplusCommonUtil.deleteFromMediaScanner(filepath!!)
                }

                override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {

                    LogUtil.e(LOG_TAG, "onFailure")
                    hideProgress()
                    PplusCommonUtil.deleteFromMediaScanner(filepath!!)
                }
            })
        }

        showProgress("")
        defaultUpload!!.request(filepath, attachment)
    }

    override fun getToolbarOption(): ToolbarOption {

        val signUp = intent.getBooleanExtra(Const.SIGN_UP, false)
        val toolbarOption = ToolbarOption(this)

        if (signUp) {
            toolbarOption.title = getString(R.string.word_profile_info)
            val textRightTop = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
            textRightTop.text = getString(R.string.word_skip)
            textRightTop.isClickable = true
            textRightTop.gravity = Gravity.CENTER
            textRightTop.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
            textRightTop.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_579ffb))
            textRightTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_45pt).toFloat())
            textRightTop.setSingleLine()
            toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
        } else {
            toolbarOption.initializeDefaultToolbar(getString(R.string.word_profile_config), ToolbarOption.ToolbarMenu.LEFT)
        }

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }

                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        finish()
                    }
                }
            }
        }

    }

//    override fun onBackPressed() {
//
//        if (mIsEditing) {
//            val builder = AlertBuilder.Builder()
//            builder.setTitle(getString(R.string.word_notice_alert))
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_profile_back_button1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_back_button2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//            builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                    when (event_alert) {
//                        AlertBuilder.EVENT_ALERT.RIGHT -> finish()
//                    }
//                }
//            }).builder().show(this)
//        } else {
//            finish()
//        }
//    }
}
