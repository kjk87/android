package com.lejel.wowbox.apps.join

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Member
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityJoinCompleteBinding
import com.lejel.wowbox.databinding.ActivityJoinNicknameBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class JoinNicknameActivity : BaseActivity() {

    private lateinit var binding: ActivityJoinNicknameBinding

    override fun getLayoutView(): View {
        binding = ActivityJoinNicknameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mMember: Member? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mMember = PplusCommonUtil.getParcelableExtra(intent, Const.MEMBER, Member::class.java)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        binding.editJoinNickname.setSingleLine()

        binding.textJoinNicknameReg.setOnClickListener {

            val nickname = binding.editJoinNickname.text.toString().trim()

            if (nickname.length <= 2) {
                return@setOnClickListener
            }

            join(nickname)
        }

        binding.editJoinNickname.addTextChangedListener {
            if (it!!.length > 2) {
                binding.textJoinNicknameReg.setBackgroundResource(R.drawable.gradient_ea5506_f47916_radius_22)
                binding.textJoinNicknameReg.isClickable = true
            } else {
                binding.textJoinNicknameReg.setBackgroundResource(R.drawable.bg_4d000000_radius_22)
                binding.textJoinNicknameReg.isClickable = false
            }
        }

        binding.textJoinNicknameReg.setBackgroundResource(R.drawable.bg_4d000000_radius_22)
        binding.textJoinNicknameReg.isClickable = false


    }

    private fun join(nickname: String) {
        mMember!!.nickname = nickname

        showProgress("")
        ApiBuilder.create().join(mMember!!).setCallback(object : PplusCallback<NewResultResponse<Member>> {
            override fun onResponse(call: Call<NewResultResponse<Member>>?, response: NewResultResponse<Member>?) {
                hideProgress()
                if (response?.result != null) {
                    setEvent(FirebaseAnalytics.Event.SIGN_UP)
                    val userKey = response.result!!.userKey
                    val intent = Intent(this@JoinNicknameActivity, JoinCompleteActivity::class.java)
                    intent.putExtra(Const.USER_KEY, userKey)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    joinCompleteLauncher.launch(intent)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Member>>?, t: Throwable?, response: NewResultResponse<Member>?) {
                hideProgress()
                if (response?.code == 587) {
                    showAlert(R.string.msg_agree_terms)
                } else if (response?.code == 504) {
                    showAlert(R.string.msg_duplicate_nickname)
                } else if (response?.code == 588) {
                    showAlert(R.string.msg_wrong_recommend_key)
                } else if (response?.code == 589) {
                    showAlert(R.string.msg_can_not_use_cursing)
                } else {
                    showAlert(R.string.msg_failed_join)
                }
            }
        }).build().call()
    }

    val joinCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            setResult(RESULT_OK)
            finish()
        }
    }
}