package com.lejel.wowbox.apps.attendance.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.attendance.data.AttendanceAdapter
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.wallet.ui.WalletMakeActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.AdmobUtil
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAttendanceBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Calendar

class AttendanceActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityAttendanceBinding

    override fun getLayoutView(): View {
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String {
        return ""
    }

    private lateinit var mAdapter:AttendanceAdapter

    override fun initializeView(savedInstanceState: Bundle?) {
        val layoutManager = GridLayoutManager(this, 5)
        binding.recyclerAttendance.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.width_16)))
        binding.recyclerAttendance.layoutManager = layoutManager
        mAdapter = AttendanceAdapter()
        binding.recyclerAttendance.adapter = mAdapter

        binding.textAttendance.setOnClickListener {

//            val consentInformation = UserMessagingPlatform.getConsentInformation(this)
//            if (!consentInformation.canRequestAds()) { // Create a ConsentRequestParameters object.
//                val params = ConsentRequestParameters.Builder().build()
//
//                consentInformation.requestConsentInfoUpdate(this, params, ConsentInformation.OnConsentInfoUpdateSuccessListener {
//                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(this, ConsentForm.OnConsentFormDismissedListener { loadAndShowError -> // Consent gathering failed.
//                        LogUtil.e(LOG_TAG, String.format("%s: %s", loadAndShowError?.errorCode, loadAndShowError?.message))
//
//                        // Consent has been gathered.
//                        if (consentInformation.canRequestAds()) {
//                            AdmobUtil.getInstance(this).initAdMob()
//                        }
//
//                    })
//                }, ConsentInformation.OnConsentInfoUpdateFailureListener { requestConsentError -> // Consent gathering failed.
//                    LogUtil.e(LOG_TAG, String.format("%s: %s", requestConsentError.errorCode, requestConsentError.message))
//                })
//                return@setOnClickListener
//            }

//            if (!AdmobUtil.getInstance(this).mIsLoaded) {
//                showAlert(R.string.msg_loading_ad)
//                return@setOnClickListener
//            }

            attendance()
        }

        binding.textAttendance.setBackgroundResource(R.drawable.bg_f7f7f7_radius_27)
        binding.textAttendance.setTextColor(ResourceUtil.getColor(this@AttendanceActivity, R.color.color_cdcdcd))
        binding.textAttendance.isClickable = false
        reloadSession()
    }

    private fun attendance(){
//        val member = LoginInfoManager.getInstance().member!!
//        if (member.attendanceCount == 30 && (member.isAuthEmail == null || !member.isAuthEmail!! || StringUtils.isEmpty(member.authEmail))) {
//            val builder = AlertBuilder.Builder()
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_make_wallet), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.msg_make_wallet))
//            builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                    when (event_alert) {
//                        AlertBuilder.EVENT_ALERT.RIGHT -> {
//                            val intent = Intent(this@AttendanceActivity, WalletMakeActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            defaultLauncher.launch(intent)
//                        }
//                        else -> {
//
//                        }
//                    }
//                }
//            })
//            builder.builder().show(this)
//            return
//        }
        showProgress("")
        ApiBuilder.create().attendance().setCallback(object : PplusCallback<NewResultResponse<Float>>{
            override fun onResponse(call: Call<NewResultResponse<Float>>?, response: NewResultResponse<Float>?) {
                setEvent("attendance")
                hideProgress()
                if(response?.result != null){
                    if(LoginInfoManager.getInstance().member!!.attendanceCount == null){
                        LoginInfoManager.getInstance().member!!.attendanceCount = 0
                    }
                    val amount = response.result!!
                    val intent = Intent(this@AttendanceActivity, AlertAttendanceCompleteActivity::class.java)
                    if(LoginInfoManager.getInstance().member!!.attendanceCount!! < 29){
                        intent.putExtra(Const.AMOUNT, amount.toInt().toString())
                        intent.putExtra(Const.TYPE, "point")
                    }else{
                        intent.putExtra(Const.AMOUNT, amount.toInt().toString())
                        intent.putExtra(Const.TYPE, "point")
                    }
                    intent.putExtra(Const.DAY, LoginInfoManager.getInstance().member!!.attendanceCount!!+1)
                    startActivity(intent)

                    showAdmob()
                    reloadSession()
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Float>>?, t: Throwable?, response: NewResultResponse<Float>?) {
                hideProgress()
                showAlert(R.string.msg_not_eanble_attendance_time)

            }
        }).build().call()
    }

    private fun showAdmob(){
        if (AdmobUtil.getInstance(this).mAdmobInterstitialAd != null) {
            AdmobUtil.getInstance(this).mAdmobInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdClicked() {
                    LogUtil.e(LOG_TAG, "onAdShowedFullScreenContent")
                }

                override fun onAdDismissedFullScreenContent() {
                    LogUtil.e(LOG_TAG, "onAdDismissedFullScreenContent")
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    LogUtil.e(LOG_TAG, "onAdFailedToShowFullScreenContent")
                }

                override fun onAdImpression() {
                    LogUtil.e(LOG_TAG, "onAdImpression")
                    setEvent(FirebaseAnalytics.Event.AD_IMPRESSION)
                }

                override fun onAdShowedFullScreenContent() {
                    LogUtil.e(LOG_TAG, "onAdShowedFullScreenContent")
                }
            }
            AdmobUtil.getInstance(this).mAdmobInterstitialAd!!.show(this)
            AdmobUtil.getInstance(this).mIsLoaded = false
            AdmobUtil.getInstance(this).initAdMob()
        }else{
            AdmobUtil.getInstance(this).mIsLoaded = false
            AdmobUtil.getInstance(this).initAdMob()
        }
    }

    private fun reloadSession(){
        showProgress("")
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                hideProgress()
                mAdapter.notifyDataSetChanged()
                if(StringUtils.isNotEmpty(LoginInfoManager.getInstance().member!!.attendanceDate)){
                    binding.layoutAttendanceDate.visibility = View.VISIBLE
                    val lastDate = PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(LoginInfoManager.getInstance().member!!.attendanceDate))
                    val format = SimpleDateFormat(getString(R.string.word_date_format4))
                    binding.textAttendanceDate.text = format.format(lastDate)
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_MONTH, -1)
                    if(calendar.time > lastDate){
                        binding.textAttendance.setBackgroundResource(R.drawable.bg_ea5506_radius_27)
                        binding.textAttendance.setTextColor(ResourceUtil.getColor(this@AttendanceActivity, R.color.white))
                        binding.textAttendance.isClickable = true
                        binding.textAttendance.setText(R.string.word_attendance)
                    }else{
                        binding.textAttendance.setBackgroundResource(R.drawable.bg_f7f7f7_radius_27)
                        binding.textAttendance.setTextColor(ResourceUtil.getColor(this@AttendanceActivity, R.color.color_cdcdcd))
                        binding.textAttendance.isClickable = false
                        binding.textAttendance.setText(R.string.word_attendance_complete)
                    }
                }else{
                    binding.layoutAttendanceDate.visibility = View.GONE
                    binding.textAttendance.setBackgroundResource(R.drawable.bg_ea5506_radius_27)
                    binding.textAttendance.setTextColor(ResourceUtil.getColor(this@AttendanceActivity, R.color.white))
                    binding.textAttendance.isClickable = true
                    binding.textAttendance.setText(R.string.word_attendance)
                }
            }
        })
    }

    internal inner class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {
            val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).absoluteAdapterPosition
            if (itemPosition % 5 == 4) {
                outRect.right = 0
            } else {
                outRect.right = space
            }
            outRect.bottom = space // Add top margin only for the first item to avoid double space between items
        }
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {

            }
        })
    }
    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_attendance), ToolbarOption.ToolbarMenu.LEFT)
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