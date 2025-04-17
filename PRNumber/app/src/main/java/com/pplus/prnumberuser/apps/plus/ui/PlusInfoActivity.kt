package com.pplus.prnumberuser.apps.plus.ui

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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
import com.pplus.prnumberuser.BusProviderData
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.page.ui.LocationPageActivity
import com.pplus.prnumberuser.apps.product.ui.CheckAuthCodeActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.Plus
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ActivityPlusInfoBinding
import com.pplus.utils.BusProvider
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class PlusInfoActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityPlusInfoBinding

    override fun getLayoutView(): View {
        binding = ActivityPlusInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mPage:Page
    var mPlus:Plus? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = intent.getParcelableExtra(Const.DATA)!!

        getPage()
    }

    private fun getPage() {

        val params = HashMap<String, String>()
        params["no"] = mPage.no.toString()
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {

                if (response.data != null) {
                    mPage = response.data!!
                    setData()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable?, response: NewResultResponse<Page>) {
            }
        }).build().call()
    }

    private fun getPlus() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        showProgress("")
        ApiBuilder.create().getOnlyPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
            override fun onResponse(call: Call<NewResultResponse<Plus>>?, response: NewResultResponse<Plus>?) {
                hideProgress()
                if (response?.data != null) {
                    mPlus = response.data
                    if(mPlus!!.plusGiftReceived != null && mPlus!!.plusGiftReceived!!){
                        binding.textPlusInfoPlus.setBackgroundResource(R.drawable.border_color_579ffb_top_2px)
                        binding.textPlusInfoPlus.setTextColor(ResourceUtil.getColor(this@PlusInfoActivity, R.color.color_579ffb))
                        binding.textPlusInfoPlus.setText(getString(R.string.format_plus_gift_received_date, mPlus!!.plusGiftReceivedDatetime))
                        binding.textPlusInfoPlus.setOnClickListener {  }
                    }else{
                        binding.textPlusInfoPlus.setBackgroundColor(ResourceUtil.getColor(this@PlusInfoActivity, R.color.color_579ffb))
                        binding.textPlusInfoPlus.setTextColor(ResourceUtil.getColor(this@PlusInfoActivity, R.color.white))
                        binding.textPlusInfoPlus.setText(R.string.msg_plus_gift_receive)
                        binding.textPlusInfoPlus.setOnClickListener {
                            val intent = Intent(this@PlusInfoActivity, CheckAuthCodeActivity::class.java)
                            intent.putExtra(Const.TYPE, Const.PLUS_GIFT)
                            intent.putExtra(Const.PAGE, mPage)
                            useLauncher.launch(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Plus>>?, t: Throwable?, response: NewResultResponse<Plus>?) {
                hideProgress()
            }
        }).build().call()
    }

    val useLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            updatePlusGift()
        }
    }

    private fun setData(){

        if(mPage.plus!!){
            getPlus()
        }else{
            binding.textPlusInfoPlus.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_579ffb))
            binding.textPlusInfoPlus.setTextColor(ResourceUtil.getColor(this, R.color.white))
            binding.textPlusInfoPlus.setText(R.string.msg_plus)

            binding.textPlusInfoPlus.setOnClickListener {
                if(!PplusCommonUtil.loginCheck(this, null)){
                    return@setOnClickListener
                }
                val params = Plus()
                params.no = mPage.no
                showProgress("")
                ApiBuilder.create().insertPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {

                    override fun onResponse(call: Call<NewResultResponse<Plus>>, response: NewResultResponse<Plus>) {

                        hideProgress()
                        ToastUtil.show(this@PlusInfoActivity, getString(R.string.msg_plus_ing))
                        mPage.plus = true
                        getPlus()
                        val bus = BusProviderData()
                        bus.subData = mPage
                        bus.type = BusProviderData.BUS_MAIN
                        BusProvider.getInstance().post(bus)
                    }

                    override fun onFailure(call: Call<NewResultResponse<Plus>>, t: Throwable, response: NewResultResponse<Plus>) {

                        hideProgress()
                    }
                }).build().call()
            }

        }

        binding.textPlusInfoPageName.text = mPage.name
        Glide.with(this).load(mPage.plusImage).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_shop_profile_default).error(R.drawable.img_shop_profile_default)).into(binding.imagePlusInfo)
        binding.textPlusInfo.text = mPage.plusInfo

        if (mPage.address == null || StringUtils.isEmpty(mPage.address!!.roadBase)) {
            binding.layoutPlusInfoAddress.visibility = View.GONE
            binding.layoutPlusInfoMapOption.visibility = View.GONE
            binding.layoutPlusInfoMap.visibility = View.GONE
        } else {
            binding.layoutPlusInfoAddress.visibility = View.VISIBLE
            var detailAddress = ""
            if (StringUtils.isNotEmpty(mPage.address!!.roadDetail)) {
                detailAddress = mPage.address!!.roadDetail!!
            }
            binding.textPlusInfoAddress.text = "${mPage.address!!.roadBase} $detailAddress"

            if (mPage.latitude != null && mPage.longitude != null) {
                binding.layoutPlusInfoMap.visibility = View.VISIBLE

                binding.mapPlusInfo.onCreate(null)
                binding.mapPlusInfo.getMapAsync(object : OnMapReadyCallback {
                    override fun onMapReady(googMap: GoogleMap) {
                        LogUtil.e("GOOGLEMAP", "onMapReady")
                        googMap.uiSettings.isScrollGesturesEnabled = false
                        googMap.uiSettings.isZoomGesturesEnabled = false
                        val latLng = LatLng(mPage.latitude!!, mPage.longitude!!)
                        googMap.addMarker(MarkerOptions().position(latLng).title(mPage.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)))
                        googMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

                        googMap.setOnMapClickListener {
                            val intent = Intent(this@PlusInfoActivity, LocationPageActivity::class.java)
                            intent.putExtra(Const.PAGE, mPage)
                            startActivity(intent)
                        }
                    }
                })

                binding.imagePlusInfoMapFull.setOnClickListener {
                    val intent = Intent(this, LocationPageActivity::class.java)
                    intent.putExtra(Const.PAGE, mPage)
                    startActivity(intent)
                }
            } else {
                binding.layoutPlusInfoMap.visibility = View.GONE
            }
        }

        binding.layoutPlusInfoFindRoad.setOnClickListener {
            if (existDaummapApp()) {
                val uri = Uri.parse("daummaps://route?ep=${mPage.latitude},${mPage.longitude}&by=CAR");
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } else {
                val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=net.daum.android.map"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

        }

        binding.layoutPlusInfoCallTaxi.setOnClickListener {
            val uri = Uri.parse("https://t.kakao.com/launch?type=taxi&dest_lat=${mPage.latitude}&dest_lng=${mPage.longitude}&ref=pplus")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        binding.layoutPlusInfoNavigation.setOnClickListener {
            if (NaviClient.instance.isKakaoNaviInstalled(this)) {
                LogUtil.e("kakonavi", "카카오내비 앱으로 길안내 가능")
                startActivity(
                        NaviClient.instance.navigateIntent(
                                Location(mPage.name!!, mPage.longitude.toString(), mPage.latitude.toString()),
                                NaviOption(coordType = CoordType.WGS84)
                        )
                )
            } else {
                LogUtil.e("kakonavi", "카카오내비 미설치: 웹 길안내 사용 권장")

                // 웹 브라우저에서 길안내
                // 카카오내비가 설치되지 않은 곳에서 활용할 수 있습니다.
                val uri =
                        NaviClient.instance.navigateWebUrl(
                                Location(mPage.name!!, mPage.longitude.toString(), mPage.latitude.toString()),
                                NaviOption(coordType = CoordType.WGS84)
                        )

                // CustomTabs로 길안내
                KakaoCustomTabsClient.openWithDefault(this, uri)
            }
        }

        binding.layoutPlusInfoCopyAddress.setOnClickListener {
            if (mPage.address != null && StringUtils.isNotEmpty(mPage.address!!.roadBase)) {
                var detailAddress = ""
                if (StringUtils.isNotEmpty(mPage.address!!.roadDetail)) {
                    detailAddress = mPage.address!!.roadDetail!!
                }

                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                val clip = ClipData.newPlainText("address", "${mPage.address!!.roadBase} $detailAddress")
                clipboard.setPrimaryClip(clip)
                ToastUtil.show(this, R.string.msg_copied_clipboard)
            } else {
                showAlert(getString(R.string.msg_not_exist_address))
            }
        }
    }

    private fun existDaummapApp(): Boolean {
        try {
            return (packageManager.getPackageInfo("net.daum.android.map", PackageManager.GET_SIGNING_CERTIFICATES) != null)
        } catch (e: PackageManager.NameNotFoundException) {
            return false;
        }
    }

    private fun updatePlusGift() {
        val params = HashMap<String, String>()
        params["plusNo"] = mPlus!!.plusNo.toString()
        showProgress("")
        ApiBuilder.create().updatePlusGift(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(getString(R.string.msg_use_complete))
                getPlus()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_plus2), ToolbarOption.ToolbarMenu.LEFT)
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