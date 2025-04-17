package com.root37.buflexz.apps.my.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.common.ui.common.DatePickerActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Member
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.databinding.ActivityProfileConfigBinding
import retrofit2.Call

class ProfileConfigActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProfileConfigBinding

    override fun getLayoutView(): View {
        binding = ActivityProfileConfigBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mBirthday = ""
    private var mGender = ""
    private var mNickname: String? = null
    private var mIsChangedNickname = false

    override fun initializeView(savedInstanceState: Bundle?) {
        val member = LoginInfoManager.getInstance().member!!
        Glide.with(this).load(member.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_config_default).error(R.drawable.ic_profile_config_default)).into(binding.imageProfileConfigProfile)

        binding.textProfileConfigPlatformEmail.text = member.platformEmail
        binding.textProfileConfigUserKey.text = member.userKey

        Glide.with(this).load(Uri.parse("file:///android_asset/flags/${LoginInfoManager.getInstance().member!!.nation!!.uppercase()}.png")).into(binding.imageProfileConfigFlag)
        val nation = NationManager.getInstance().nationMap!![LoginInfoManager.getInstance().member!!.nation]
        if(nation!!.code == "KR"){
            binding.textProfileConfigNation.text = nation.name
        }else{
            binding.textProfileConfigNation.text = nation.nameEn
        }

        binding.layoutProfileConfigProfile.setOnClickListener {
            val intent = Intent(this, ProfileImageConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            profieImageLanncher.launch(intent)
        }

        binding.editProfileConfigNickname.setSingleLine()
        binding.editProfileConfigEmail.setSingleLine()

        binding.editProfileConfigNickname.setText(member.nickname)

        when (member.gender) {
            "male" -> {
                binding.textProfileConfigMale.isSelected = true
                binding.textProfileConfigFemale.isSelected = false
            }

            "female" -> {
                binding.textProfileConfigMale.isSelected = false
                binding.textProfileConfigFemale.isSelected = true
            }
        }

        binding.textProfileConfigMale.setOnClickListener {
            binding.textProfileConfigMale.isSelected = true
            binding.textProfileConfigFemale.isSelected = false
            mGender = "male"
        }
        binding.textProfileConfigFemale.setOnClickListener {
            binding.textProfileConfigMale.isSelected = false
            binding.textProfileConfigFemale.isSelected = true
            mGender = "female"
        }

        if (StringUtils.isNotEmpty(member.birthday)) {
            binding.textProfileConfigBirthday.text = member.birthday
        }
        binding.textProfileConfigBirthday.setOnClickListener {
            val intent = Intent(this, DatePickerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            birthdayLauncher.launch(intent)
        }

        binding.editProfileConfigEmail.setText(member.email)

        binding.textProfileConfigNicknameCheck.setOnClickListener {

            val nickname = binding.editProfileConfigNickname.text.toString().trim()
            if (StringUtils.isEmpty(nickname)) {
                showAlert(R.string.msg_input_nickname)
                return@setOnClickListener
            }

            if (member.nickname == nickname) {
                showAlert(R.string.msg_input_change_nickname)
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["nickname"] = nickname
            showProgress("")
            ApiBuilder.create().checkNickname(params).setCallback(object : PplusCallback<NewResultResponse<Boolean>> {
                override fun onResponse(call: Call<NewResultResponse<Boolean>>?, response: NewResultResponse<Boolean>?) {
                    hideProgress()
                    if (response?.result != null && response.result!!) {
                        binding.layoutProfileConfigNickname.setBackgroundResource(R.drawable.bg_border_3px_48b778_232323_radius_33)
                        binding.textProfileConfigNicknameDesc.setText(R.string.msg_usable_nickname)
                        binding.textProfileConfigNicknameDesc.setTextColor(ResourceUtil.getColor(this@ProfileConfigActivity, R.color.color_48b778))
                        mIsChangedNickname = true
                        mNickname = nickname
                    } else {
                        binding.layoutProfileConfigNickname.setBackgroundResource(R.drawable.bg_border_3px_ff5e5e_232323_radius_33)
                        binding.textProfileConfigNicknameDesc.setText(R.string.msg_duplicate_nickname)
                        binding.textProfileConfigNicknameDesc.setTextColor(ResourceUtil.getColor(this@ProfileConfigActivity, R.color.color_ff5e5e))
                        mIsChangedNickname = false
                        mNickname = ""
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<Boolean>>?, t: Throwable?, response: NewResultResponse<Boolean>?) {
                    hideProgress()
                    binding.layoutProfileConfigNickname.setBackgroundResource(R.drawable.bg_border_3px_ff5e5e_232323_radius_33)
                    binding.textProfileConfigNicknameDesc.setText(R.string.msg_duplicate_nickname)
                    binding.textProfileConfigNicknameDesc.setTextColor(ResourceUtil.getColor(this@ProfileConfigActivity, R.color.color_ff5e5e))
                    mIsChangedNickname = false
                    mNickname = ""
                }
            }).build().call()
        }

        binding.textProfileConfigComplete.setOnClickListener {
            update()
        }
    }

    private fun update() {
        var isChange = false
        val params = Member()

        if (mIsChangedNickname) {
            params.nickname = mNickname
            isChange = true
        }

        if(StringUtils.isNotEmpty(mBirthday) && LoginInfoManager.getInstance().member!!.birthday != mBirthday){
            params.birthday = mBirthday
            isChange = true
        }

        if(StringUtils.isNotEmpty(mGender) && LoginInfoManager.getInstance().member!!.gender != mGender){
            params.gender = mGender
            isChange = true
        }

        val email = binding.editProfileConfigEmail.text.toString().trim()
        if(StringUtils.isNotEmpty(email) && LoginInfoManager.getInstance().member!!.email != email){
            params.email = email
            isChange = true
        }

        if(isChange){
            showProgress("")
            ApiBuilder.create().updateMember(params).setCallback(object : PplusCallback<NewResultResponse<Member>>{
                override fun onResponse(call: Call<NewResultResponse<Member>>?, response: NewResultResponse<Member>?) {
                    hideProgress()
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Member>>?, t: Throwable?, response: NewResultResponse<Member>?) {
                    hideProgress()
                    setResult(RESULT_OK)
                    finish()
                }
            }).build().call()
        }else{
            finish()
        }
    }



    val profieImageLanncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Glide.with(this).load(LoginInfoManager.getInstance().member!!.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_config_default).error(R.drawable.ic_profile_config_default)).into(binding.imageProfileConfigProfile)
    }

    val birthdayLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val birthday = data.getStringExtra(Const.BIRTH_DAY)
                mBirthday = birthday!!
                binding.textProfileConfigBirthday.text = birthday
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_profile_config), ToolbarOption.ToolbarMenu.LEFT)
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