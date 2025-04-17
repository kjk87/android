package com.lejel.wowbox.core.util

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.lejel.wowbox.R

/**
 * Created by 김종경 on 2016-06-30.
 */
object GoogleUtil {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    var mListener : GoogleSignListener? = null

    /**
     * 초기화, listener등록 후 sign in 시도
     */
    fun init(activity: FragmentActivity, listener: GoogleSignListener?) {
        mListener = listener

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.web_client_id))
            .requestId()
            .requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)

    }

    fun launch(launcher: ActivityResultLauncher<Intent>){
        signOut()
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