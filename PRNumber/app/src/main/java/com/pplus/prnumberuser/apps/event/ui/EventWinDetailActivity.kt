package com.pplus.prnumberuser.apps.event.ui

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.alert.AlertGiftReviewActivity
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.goods.ui.GoodsReviewWriteActivity
import com.pplus.prnumberuser.apps.product.ui.CheckAuthCodeActivity
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.EventWin
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ActivityEventWinDetailBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class EventWinDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityEventWinDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityEventWinDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    var mEventWin: EventWin? = null
    var mPage: Page? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mEventWin = intent.getParcelableExtra(Const.DATA)

        setData()
    }

    private fun setData() {
        val params = HashMap<String, String>()
        params["seqNo"] = mEventWin!!.winNo.toString()
        params["eventSeqNo"] = mEventWin!!.event!!.no.toString()
        showProgress("")
        ApiBuilder.create().getWinBySeqNo(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {
            override fun onResponse(call: Call<NewResultResponse<EventWin>>?, response: NewResultResponse<EventWin>?) {
                hideProgress()
                if (response?.data != null) {
                    mEventWin = response.data
                    Glide.with(this@EventWinDetailActivity).load(mEventWin!!.gift!!.giftImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imageEventWinDetailGift)
                    binding.textEventWinDetailGiftName.text = mEventWin!!.gift!!.title

                    binding.textEventWinDetailGiftExpireDate.text = mEventWin!!.gift!!.expireDate

                    if (mEventWin!!.gift!!.dayType == "all") {
                        binding.textEventWinDetailGiftDays.text = getString(R.string.word_every_dayofweek)
                    } else {
                        val dayOfWeek = mEventWin!!.gift!!.days!!.replace(",", "/").replace("0", getString(R.string.word_mon)).replace("1", getString(R.string.word_tue))
                                .replace("2", getString(R.string.word_wed)).replace("3", getString(R.string.word_thu)).replace("4", getString(R.string.word_fri))
                                .replace("5", getString(R.string.word_sat)).replace("6", getString(R.string.word_sun))
                        binding.textEventWinDetailGiftDays.text = dayOfWeek
                    }

                    if (mEventWin!!.gift!!.timeType == "all") {
                        binding.textEventWinDetailGiftUseTime.text = getString(R.string.word_every_time_use)
                    } else {
                        binding.textEventWinDetailGiftUseTime.text = "${mEventWin!!.gift!!.startTime}~${mEventWin!!.gift!!.endTime}"
                    }

                    if (mEventWin!!.gift!!.reviewPoint != null && mEventWin!!.gift!!.reviewPoint!! > 0) {
                        binding.layoutEventWinDetailGiftReviewPoint.visibility = View.VISIBLE
                        binding.textEventWinDetailGiftReviewPoint.text = getString(R.string.format_cash_unit, FormatUtil.getMoneyType(mEventWin!!.gift!!.reviewPoint.toString()))
                    } else {
                        binding.layoutEventWinDetailGiftReviewPoint.visibility = View.GONE
                    }

                    binding.textEventWinDetailGiftUse.visibility = View.VISIBLE
                    when (mEventWin!!.giftStatus) {
                        EnumData.GiftStatus.wait.status -> {
                            binding.textEventWinDetailGiftUse.setBackgroundColor(ResourceUtil.getColor(this@EventWinDetailActivity, R.color.color_579ffb))
                            binding.textEventWinDetailGiftUse.text = getString(R.string.msg_use)
                            binding.textEventWinDetailGiftUse.isEnabled = true
                            binding.textEventWinDetailGiftUse.setOnClickListener {
                                if(mPage != null){
                                    val intent = Intent(this@EventWinDetailActivity, CheckAuthCodeActivity::class.java)
                                    intent.putExtra(Const.TYPE, Const.EVENT_GIFT)
                                    intent.putExtra(Const.PAGE, mPage)
                                    intent.putExtra(Const.EVENT_GIFT, mEventWin!!.gift)
                                    startActivityForResult(intent, Const.REQ_USE)
                                }
                            }
                        }
                        EnumData.GiftStatus.use.status -> {
                            binding.textEventWinDetailGiftUse.setBackgroundColor(ResourceUtil.getColor(this@EventWinDetailActivity, R.color.color_b7b7b7))
                            binding.textEventWinDetailGiftUse.text = getString(R.string.format_use_date2, mEventWin!!.useDatetime)
                            binding.textEventWinDetailGiftUse.isEnabled = false
                        }
                        EnumData.GiftStatus.reviewWrite.status -> {
                            binding.textEventWinDetailGiftUse.setBackgroundColor(ResourceUtil.getColor(this@EventWinDetailActivity, R.color.color_b7b7b7))
                            binding.textEventWinDetailGiftUse.text = getString(R.string.word_review_write_complete)
                            binding.textEventWinDetailGiftUse.isEnabled = false
                        }
                        EnumData.GiftStatus.expired.status -> {
                            binding.textEventWinDetailGiftUse.setBackgroundColor(ResourceUtil.getColor(this@EventWinDetailActivity, R.color.color_b7b7b7))
                            binding.textEventWinDetailGiftUse.text = getString(R.string.format_expire_date4, mEventWin!!.gift!!.expireDate)
                            binding.textEventWinDetailGiftUse.isEnabled = false
                        }
                        else -> {
                            binding.textEventWinDetailGiftUse.visibility = View.GONE
                        }
                    }

                    if (mEventWin!!.event!!.pageSeqNo != null) {
                        getPage(mEventWin!!.event!!.pageSeqNo!!)
                    }else{
                        binding.textEventWinDetailGiftUse.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventWin>>?, t: Throwable?, response: NewResultResponse<EventWin>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getPage(pageSeqNo: Long) {
        val params = java.util.HashMap<String, String>()
        params["no"] = pageSeqNo.toString()
        showProgress("")
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                hideProgress()
                if (response?.data != null) {
                    mPage = response.data!!

                    binding.textEventWinDetailPageName.text = mPage!!.name
                    binding.layoutEventWinDetailPageGo.setOnClickListener {
                        val location = IntArray(2)
                        it.getLocationOnScreen(location)
                        val x = location[0] + it.width / 2
                        val y = location[1] + it.height / 2
                        PplusCommonUtil.goPage(this@EventWinDetailActivity, mPage!!, x, y)
                    }

                    if (StringUtils.isNotEmpty(mPage!!.phone)) {
                        binding.layoutEventWinDetailPagePhone.visibility = View.VISIBLE
                        binding.textEventWinDetailPagePhone.text = FormatUtil.getPhoneNumber(mPage!!.phone)
                        binding.textEventWinDetailPagePhone.setOnClickListener {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage!!.phone!!))
                            startActivity(intent)
                        }

                        binding.textEventWinDetailPageCall.setOnClickListener {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage!!.phone!!))
                            startActivity(intent)
                        }
                    } else {
                        binding.layoutEventWinDetailPagePhone.visibility = View.GONE
                    }

                    if (StringUtils.isNotEmpty(mPage!!.openHours)) {
                        binding.layoutEventWinDetailPageOpenHours.visibility = View.VISIBLE
                        binding.textEventWinDetailPageOpeningHours.text = mPage!!.openHours
                    } else {
                        binding.layoutEventWinDetailPageOpenHours.visibility = View.GONE
                    }

                    if (StringUtils.isNotEmpty(mPage!!.holiday)) {
                        binding.layoutEventWinDetailPageHoliday.visibility = View.VISIBLE
                        binding.textEventWinDetailPageHoliday.text = mPage!!.holiday
                    } else {
                        binding.layoutEventWinDetailPageHoliday.visibility = View.GONE
                    }

                    if (mPage!!.isParkingAvailable!!) {

                        if (mPage!!.isValetParkingAvailable!!) {
                            binding.textEventWinDetailPageParking.text = "${getString(R.string.word_parking_enable)}\n${getString(R.string.word_valet_parking_enable)}"
                        } else {
                            binding.textEventWinDetailPageParking.text = "${getString(R.string.word_parking_enable)}\n${getString(R.string.word_valet_parking_disable)}"
                        }
                    } else {
                        binding.textEventWinDetailPageParking.text = getString(R.string.word_parking_disable)
                    }

                    if (mPage!!.address != null && StringUtils.isEmpty(mPage!!.address!!.roadBase)) {
                        binding.layoutEventWinDetailPageAddress.visibility = View.GONE
                        binding.layoutEventWinDetailPageMap.visibility = View.GONE
                        binding.layoutEventWinDetailPageMapOption.visibility = View.GONE
                    } else {
                        binding.layoutEventWinDetailPageAddress.visibility = View.VISIBLE
                        var detailAddress = ""
                        if (StringUtils.isNotEmpty(mPage!!.address!!.roadDetail)) {
                            detailAddress = mPage!!.address!!.roadDetail!!
                        }
                        binding.textEventWinDetailPageAddress.text = "${mPage!!.address!!.roadBase} $detailAddress"

                        if (mPage!!.latitude != null && mPage!!.longitude != null) {
                            binding.layoutEventWinDetailPageMap.visibility = View.VISIBLE

                            binding.mapEventWinDetailPage.onCreate(null)
                            binding.mapEventWinDetailPage.getMapAsync(object : OnMapReadyCallback {
                                override fun onMapReady(googMap: GoogleMap) {
                                    LogUtil.e("GOOGLEMAP", "onMapReady")
                                    googMap.uiSettings.isScrollGesturesEnabled = false
                                    googMap.uiSettings.isZoomGesturesEnabled = false
                                    val latLng = LatLng(mPage!!.latitude!!, mPage!!.longitude!!)
                                    googMap.addMarker(MarkerOptions().position(latLng).title(mPage!!.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)))
                                    googMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
//                            googMap.setOnMapClickListener {
//                                val intent = Intent(activity, LocationPageActivity::class.java)
//                                intent.putExtra(Const.PAGE, mPage!!)
//                                activity?.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
//                            }
                                }

                            })

                        }
                    }

                    binding.layoutEventWinDetailPageFindRoad.setOnClickListener {
                        if (existDaummapApp()) {
                            val uri = Uri.parse("daummaps://route?ep=${mPage!!.latitude},${mPage!!.longitude}&by=CAR");
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        } else {
                            val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=net.daum.android.map"))
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }

                    }

                    binding.layoutEventWinDetailPageCallTaxi.setOnClickListener {
                        val uri = Uri.parse("https://t.kakao.com/launch?type=taxi&dest_lat=${mPage!!.latitude}&dest_lng=${mPage!!.longitude}&ref=pplus")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }

                    binding.layoutEventWinDetailPageNavigation.setOnClickListener {
                        if (NaviClient.instance.isKakaoNaviInstalled(this@EventWinDetailActivity)) {
                            LogUtil.e("kakonavi", "카카오내비 앱으로 길안내 가능")
                            startActivity(
                                    NaviClient.instance.navigateIntent(
                                            Location(mPage!!.name!!, mPage!!.longitude.toString(), mPage!!.latitude.toString()),
                                            NaviOption(coordType = CoordType.WGS84)
                                    )
                            )
                        } else {
                            LogUtil.e("kakonavi", "카카오내비 미설치: 웹 길안내 사용 권장")

                            // 웹 브라우저에서 길안내
                            // 카카오내비가 설치되지 않은 곳에서 활용할 수 있습니다.
                            val uri =
                                    NaviClient.instance.navigateWebUrl(
                                            Location(mPage!!.name!!, mPage!!.longitude.toString(), mPage!!.latitude.toString()),
                                            NaviOption(coordType = CoordType.WGS84)
                                    )

                            // CustomTabs로 길안내
                            KakaoCustomTabsClient.openWithDefault(this@EventWinDetailActivity, uri)
                        }
                    }

                    binding.layoutEventWinDetailPageCopyAddress.setOnClickListener {
                        if (mPage!!.address != null && StringUtils.isNotEmpty(mPage!!.address!!.roadBase)) {
                            var detailAddress = ""
                            if (StringUtils.isNotEmpty(mPage!!.address!!.roadDetail)) {
                                detailAddress = mPage!!.address!!.roadDetail!!
                            }

                            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                            val clip = ClipData.newPlainText("address", "${mPage!!.address!!.roadBase} $detailAddress")
                            clipboard.setPrimaryClip(clip)
                            ToastUtil.show(this@EventWinDetailActivity, R.string.msg_copied_clipboard)
                        } else {
                            ToastUtil.showAlert(this@EventWinDetailActivity, getString(R.string.msg_not_exist_address))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable?, response: NewResultResponse<Page>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun existDaummapApp(): Boolean {
        val pm = packageManager

        try {
            return (pm.getPackageInfo("net.daum.android.map", PackageManager.GET_SIGNING_CERTIFICATES) != null)
        } catch (e: PackageManager.NameNotFoundException) {
            return false;
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Const.REQ_USE->{
                if(resultCode == Activity.RESULT_OK){
                    val params = HashMap<String, String>()
                    params["eventSeqNo"] = mEventWin!!.event!!.no.toString()
                    params["seqNo"] = mEventWin!!.winNo.toString()
                    params["id"] = mEventWin!!.id.toString()
                    showProgress("")
                    ApiBuilder.create().updateUseGift(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                        override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                            hideProgress()
                            if(mEventWin!!.gift!!.reviewPoint != null && mEventWin!!.gift!!.reviewPoint!! > 0){

                                val intent = Intent(this@EventWinDetailActivity, AlertGiftReviewActivity::class.java)
                                intent.putExtra(Const.POINT, mEventWin!!.gift!!.reviewPoint)
                                startActivityForResult(intent, Const.REQ_ALERT_REVIEW)
                            }else{
                                showAlert(R.string.msg_use_complete)
                            }
                            setData()
                        }

                        override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                            hideProgress()
                        }
                    }).build().call()
                }
            }
            Const.REQ_ALERT_REVIEW->{
                if(resultCode == Activity.RESULT_OK){
                    val intent = Intent(this, GoodsReviewWriteActivity::class.java)
                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                    intent.putExtra(Const.EVENT_WIN, mEventWin)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivityForResult(intent, Const.REQ_REVIEW)
                }
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_gift_info), ToolbarOption.ToolbarMenu.LEFT)

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