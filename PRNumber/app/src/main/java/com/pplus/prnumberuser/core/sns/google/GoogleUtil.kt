package com.pplus.prnumberuser.core.sns.google

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.pplus.prnumberuser.R

/**
 * Created by 김종경 on 2016-06-30.
 */
object GoogleUtil {
    private const val RC_SIGN_IN = 9000
    private var mGoogleSignInClient: GoogleSignInClient? = null
    var mListener : GoogleSignListener? = null
    private fun validateServerClientID(activity: Activity) {
        val serverClientId = activity.getString(R.string.default_web_client_id)
        val suffix = ".apps.googleusercontent.com"
        if (!serverClientId.trim { it <= ' ' }.endsWith(suffix)) {
            val message = "Invalid server client ID in strings.xml, must end with $suffix"
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 초기화, listener등록 후 sign in 시도
     */
    fun init(activity: FragmentActivity, listener: GoogleSignListener?) {
        mListener = listener
        validateServerClientID(activity)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestServerAuthCode(activity.getString(R.string.default_web_client_id), false)
            .requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)

    }

    fun launch(launcher: ActivityResultLauncher<Intent>){
        val signInIntent = mGoogleSignInClient!!.signInIntent
        launcher.launch(signInIntent)
    }

    fun signOut() {
        mGoogleSignInClient?.signOut()
    }


    /**
     * GoogleSignInResult 리스너
     */
    interface GoogleSignListener {
        fun handleSignInResult(account: GoogleSignInAccount?)
    }
}