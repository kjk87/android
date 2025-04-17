package com.pplus.prnumberbiz.apps.signup.ui


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.AccessToken
import com.facebook.FacebookException
import com.facebook.GraphResponse
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.pple.pplus.utils.part.logs.LogUtil

import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.network.model.dto.User
import com.pplus.prnumberbiz.core.sns.facebook.FaceBookUtil
import com.pplus.prnumberbiz.core.sns.google.GoogleUtil
import kotlinx.android.synthetic.main.fragment_join_select_account.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [JoinSelectAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class JoinSelectAccountFragment : BaseFragment<JoinActivity>() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_join_select_account
    }

    override fun initializeView(container: View?) {

    }

    override fun init() {
        val params = User()
        text_join_select_pplus.setOnClickListener {
            params.accountType = SnsTypeCode.pplus.name
            parentActivity.join(params)
        }

        image_join_select_facebook.setOnClickListener {
            FaceBookUtil.registerCallback(object : FaceBookUtil.FaceBookCallbackListener {

                override fun onSuccess(loginResult: com.facebook.login.LoginResult) {
                    //로그인 성공후 프로필 정보 요청
                    FaceBookUtil.requestProfile(loginResult.accessToken)
                }

                override fun onCancel() {

                    hideProgress()
                }

                override fun onError(exception: FacebookException) {

                    hideProgress()
                }

                override fun onCompleted(`object`: JSONObject, response: GraphResponse) {

                    hideProgress()
                    //프로필 정보 요청 성공
                    val id = `object`.optString("id")
                    val token = AccessToken.getCurrentAccessToken().token

                    LogUtil.e("id", "" + id)
                    LogUtil.e("token", "" + token)
                    params.loginId = id
                    params.password = token
                    params.accountType = SnsTypeCode.facebook.name
                    parentActivity.join(params)
                }
            })
            FaceBookUtil.logIn(activity)
            showProgress("")
        }

        image_join_select_google.setOnClickListener {
            showProgress("")
            GoogleUtil.init(activity) { result ->
                hideProgress()
                if (result.isSuccess) {

                    val acct = result.signInAccount
                    val id = acct!!.id
                    val token = acct.idToken

                    LogUtil.e("id", "" + id!!)
                    LogUtil.e("token", "" + token!!)
                    params.loginId = id
                    params.password = token
                    params.accountType = SnsTypeCode.google.name
                    parentActivity.join(params)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FaceBookUtil.onActivityResult(requestCode, resultCode, data)
        GoogleUtil.onActivityResult(requestCode, resultCode, data)
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                JoinSelectAccountFragment().apply {
                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
