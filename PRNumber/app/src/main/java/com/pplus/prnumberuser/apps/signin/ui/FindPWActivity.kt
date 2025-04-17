package com.pplus.prnumberuser.apps.signin.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberuser.core.network.model.dto.Verification
import com.pplus.prnumberuser.databinding.ActivityFindPwBinding

class FindPWActivity : BaseActivity(), ImplToolbar {

    private var loginId: String? = null
    private var mobileNumber: String? = null

    override fun getPID(): String? {

        return null
    }

    private lateinit var binding: ActivityFindPwBinding

    override fun getLayoutView(): View {
        binding = ActivityFindPwBinding.inflate(layoutInflater)
        return binding.root
    }

    fun setLoginId(loginId: String) {

        this.loginId = loginId
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        findStep1()

    }

    fun findStep1() {

        val fragment = FindPWStep1Fragment.newInstance()

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.find_pw_container, fragment, fragment.javaClass.simpleName)
        ft.commit()
    }

    fun verification(id: String, mobileNumber: String) {

        this.loginId = id
        this.mobileNumber = mobileNumber
        val intent = Intent(this, VerificationMeActivity::class.java)
        intent.putExtra(Const.KEY, Const.FIND_PW)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        findPwLauncher.launch(intent)

    }

    fun changePassword(verification: Verification) {

        val intent = Intent(this, ChangePWActivity::class.java)
        intent.putExtra(Const.VERIFICATION, verification)
        intent.putExtra(Const.LOGIN_ID, loginId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        changePwLauncher.launch(intent)
    }

    val findPwLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if(data != null){
                val verification = data.getParcelableExtra<Verification>(Const.VERIFICATION)
                val mobileNumber = data.getStringExtra(Const.MOBILE_NUMBER)
                if(this.mobileNumber != mobileNumber){
                    showAlert(R.string.msg_incorrect_joined_mobile_number)
                }else{
                    changePassword(verification!!)
                }
            }
        }
    }

    val changePwLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
