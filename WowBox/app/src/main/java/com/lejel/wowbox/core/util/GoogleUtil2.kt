package com.lejel.wowbox.core.util

import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.FragmentActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.lejel.wowbox.R
import com.pplus.utils.part.logs.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.text.Charsets.UTF_8

/**
 * Created by 김종경 on 2016-06-30.
 */
object GoogleUtil2 {
    var mListener : GoogleSignListener? = null

    /**
     * 초기화, listener등록 후 sign in 시도
     */
    fun signIn(activity: FragmentActivity, listener: GoogleSignListener) {
        mListener = listener

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(activity.getString(R.string.web_client_id))
            .setAutoSelectEnabled(true)
//            .setNonce(<nonce string to use when generating a Google ID token>)
        .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(activity)
        val job = Job()
        CoroutineScope(Dispatchers.Main + job).launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = activity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                LogUtil.e("GoogleUtil", e.toString())
                listener.onFailure()
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential

        when (credential) {

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        LogUtil.e("GOO", credential.data.toString())

                        val idToken = googleIdTokenCredential.idToken
                        val segments = idToken.split(".")

                        val payloadAsByteArray: ByteArray = android.util.Base64.decode(segments[1], android.util.Base64.NO_WRAP)
                        val payloadInJson = JSONObject(String(payloadAsByteArray, UTF_8))
                        val id = payloadInJson.getString("sub")
                        val email = googleIdTokenCredential.id

                        mListener?.handleSignInResult(id, email)


                    } catch (e: GoogleIdTokenParsingException) {
                        LogUtil.e("GoogleUtil", "Received an invalid google id token response"+e.toString())
                        mListener?.onFailure()
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    LogUtil.e("GoogleUtil", "Unexpected type of credential")
                    mListener?.onFailure()
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                LogUtil.e("GoogleUtil", "Unexpected type of credential")
                mListener?.onFailure()
            }
        }
    }

    /**
     * GoogleSignInResult 리스너
     */
    interface GoogleSignListener {
        fun handleSignInResult(id:String, email:String)

        fun onFailure()
    }
}