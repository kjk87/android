package com.pplus.luckybol.apps.setting.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.igaworks.v2.core.AdBrixRm
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.main.ui.OneGalleryActivity
import com.pplus.luckybol.apps.my.ui.MemberAddressSaveActivity
import com.pplus.luckybol.apps.my.ui.SelectActiveAreaActivity
import com.pplus.luckybol.apps.signup.ui.VerificationMeActivity
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.code.custom.AttachmentTargetTypeCode
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.request.params.ParamsAttachment
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.upload.DefaultUpload
import com.pplus.luckybol.core.network.upload.PplusUploadListener
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ActivityProfileConfigBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap

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

//            val logger = AppEventsLogger.newLogger(this)
//            logger.logEvent("screen_view", bundle)
        } else {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Main_mypage_profile")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)

            val eventAttr = AdBrixRm.AttrModel()
            eventAttr.setAttrs("screen_name", "Main_mypage_profile")
            AdBrixRm.event("screen_view", eventAttr)

//            val logger = AppEventsLogger.newLogger(this)
//            logger.logEvent("screen_name", bundle)
        }

        mIsEditing = false
        binding.editProfileConfigNickName.setSingleLine()
        mUser = LoginInfoManager.getInstance().user

        if (mUser!!.profileImage != null && StringUtils.isNotEmpty(mUser!!.profileImage!!.url)) {
            Glide.with(this).load(mUser!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(binding.imageProfileConfig)
        }

        if (StringUtils.isNotEmpty(mUser!!.nickname)) {
            binding.editProfileConfigNickName.setText(mUser!!.nickname)
        }

        binding.textProfileConfigNickNameDoubleCheck.setOnClickListener {
            val nickname = binding.editProfileConfigNickName.text.toString().trim()
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
                    mNickName = binding.editProfileConfigNickName.text.toString().trim()
                }
            }).build().call()
        }


        binding.layoutProfileConfigImage.setOnClickListener {
            goGallery()
        }


//        layout_profile_config_my_favorite.setOnClickListener {
//            val intent = Intent(this, MyFavoriteActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_MY_FAVORITE)
//        }

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

            val nickname = binding.editProfileConfigNickName.text.toString().trim()
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

//            mIsEditing = true
//            showProgress("")
//            ApiBuilder.create().updateUser(mUser).setCallback(object : PplusCallback<NewResultResponse<User>> {
//
//                override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {
//
//                    hideProgress()
//                    LoginInfoManager.getInstance().user = mUser
//                    LoginInfoManager.getInstance().save()
//                    if (mIsSignUp!!) {
//                        showAlert(R.string.msg_save_complete)
//                    } else {
//                        showAlert(R.string.msg_edit_complete)
//                    }
//
//                    finish()
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {
//
//                    hideProgress()
//                }
//            }).build().call()
        }

        checkVerificationMe()
        getMemberAddress()
//        getMyFavoriteCategoryList()
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

    private fun checkVerificationMe(){
        if(LoginInfoManager.getInstance().user.verification!!.media != "external"){
            binding.textProfileConfigDesc.visibility = View.VISIBLE

            binding.textProfileConfigVerificationMe.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_add2, 0, 0)
            binding.textProfileConfigVerificationMe.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_39pt).toFloat())
            binding.textProfileConfigVerificationMe.typeface = Typeface.DEFAULT
            binding.textProfileConfigVerificationMe.setTextColor(ResourceUtil.getColor(this, R.color.color_8c969f))
            binding.textProfileConfigVerificationMe.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_profile_set_verification_me_desc))
            binding.layoutProfileConfigVerificationMe.setOnClickListener {
                val intent = Intent(this, VerificationMeActivity::class.java)
                intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                verificationMeLauncher.launch(intent)
            }

        }else{
            binding.textProfileConfigDesc.visibility = View.GONE
            binding.textProfileConfigVerificationMe.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_done, 0, 0)
            binding.textProfileConfigVerificationMe.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_54pt).toFloat())
            binding.textProfileConfigVerificationMe.typeface = Typeface.DEFAULT_BOLD
            binding.textProfileConfigVerificationMe.setTextColor(ResourceUtil.getColor(this, R.color.color_fc5c57))
            binding.layoutProfileConfigVerificationMe.setOnClickListener {

            }

            var gender = ""

            if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.gender)) {

                if (LoginInfoManager.getInstance().user.gender.equals(EnumData.GenderType.male.name)) {
                    gender = getString(R.string.word_male)
                } else {
                    gender = getString(R.string.word_female)
                }
            }

            var age = ""

            if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.birthday)) {
                val birth = LoginInfoManager.getInstance().user.birthday!!.substring(0, 4)
                val year = Calendar.getInstance().get(Calendar.YEAR)
                age = (year - birth.toInt()).toString()
            }
            binding.textProfileConfigVerificationMe.text = "$gender / $age${getString(R.string.word_age_unit)}"
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

    val verificationMeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data

            if (data != null) {
                val verifiedData = data.getParcelableExtra<User>(Const.DATA)!!
                if (LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE + "##", "") != verifiedData.mobile) {
                    showAlert(R.string.msg_incorrect_joined_mobile_number)
                }else{
                    val user = LoginInfoManager.getInstance().user

                    val prams = User()
                    prams.no = user.no
                    prams.name = verifiedData.name
                    prams.birthday = verifiedData.birthday
                    prams.mobile = verifiedData.mobile
                    prams.gender = verifiedData.gender
                    prams.verification = verifiedData.verification
                    prams.appType = user.appType
                    showProgress("")
                    ApiBuilder.create().updateExternal(prams).setCallback(object :
                        PplusCallback<NewResultResponse<User>> {

                        override fun onResponse(call: Call<NewResultResponse<User>>,
                                                response: NewResultResponse<User>
                        ) {
                            hideProgress()
                            showProgress("")
                            PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                                override fun reload() {
                                    hideProgress()
                                    if (LoginInfoManager.getInstance().user.verification!!.media == "external" && StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.name)) {
                                        checkVerificationMe()
                                    }
                                    showAlert(R.string.msg_verified)
                                }
                            })
                        }

                        override fun onFailure(call: Call<NewResultResponse<User>>,
                                               t: Throwable,
                                               response: NewResultResponse<User>
                        ) {
                            hideProgress()
                        }
                    }).build().call()
                }

            }
        }
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

    private fun updateGender(gender: String) {

        val params = java.util.HashMap<String, String>()
        params["gender"] = gender
        showProgress("")
        ApiBuilder.create().updateGender(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                hideProgress()
                LoginInfoManager.getInstance().user.gender = gender
                LoginInfoManager.getInstance().save()
                ToastUtil.show(this@ProfileConfigActivity, R.string.msg_edit_complete)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

            }
        }).build().call()
    }

    private fun updateBirthday(birthday: String) {

        val params = java.util.HashMap<String, String>()
        params["birthday"] = birthday
        showProgress("")
        ApiBuilder.create().updateBirthday(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                hideProgress()
                LoginInfoManager.getInstance().user.birthday = birthday
                LoginInfoManager.getInstance().save()
                ToastUtil.show(this@ProfileConfigActivity, R.string.msg_edit_complete)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

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
                    val url = resultResponse.data!!.url
                    val no = resultResponse.data!!.no

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
            textRightTop.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_fc5c57))
            textRightTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_45pt).toFloat())
            textRightTop.setSingleLine()
            toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
        } else {
            toolbarOption.initializeDefaultToolbar(getString(R.string.word_profile_config), ToolbarOption.ToolbarMenu.LEFT)
        }

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
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
